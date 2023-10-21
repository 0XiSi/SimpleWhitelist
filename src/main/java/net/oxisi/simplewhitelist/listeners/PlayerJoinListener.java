package net.oxisi.simplewhitelist.listeners;

import net.kyori.adventure.text.Component;
import net.oxisi.simplewhitelist.SimpleWhitelist;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SimpleWhitelist plugin;

    public PlayerJoinListener(SimpleWhitelist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();

        if (!plugin.getConfig().getBoolean("whitelisted." + playerName, false)) {
            Component kickMessage = Component.text(ChatColor.RED + "You are not whitelisted on this server!");
            event.getPlayer().kick(kickMessage);
        }
    }
}