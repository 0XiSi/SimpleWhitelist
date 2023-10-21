package net.oxisi.simplewhitelist;

import net.oxisi.simplewhitelist.commands.WhitelistCommand;
import net.oxisi.simplewhitelist.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class SimpleWhitelist extends JavaPlugin {

    private WatchService watchService;
    private Path configFile;

    @Override
    public void onEnable() {
        configFile = Paths.get(this.getDataFolder().getAbsolutePath(), "config.yml");

        if (!configFile.toFile().exists()) {
            saveDefaultConfig();
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Objects.requireNonNull(this.getCommand("whitelist")).setExecutor(new WhitelistCommand(this));

        startWatchingConfig();
    }

    private void startWatchingConfig() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            configFile.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Start a new thread to listen for file changes
        new Thread(() -> {
            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changedPath = (Path) event.context();
                    if (changedPath.endsWith("config.yml")) {
                        reloadConfig();
                        getLogger().info("Config reloaded due to file change!");
                    }
                }

                key.reset();
            }
        }).start();
    }

    @Override
    public void onDisable() {
        try {
            watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
