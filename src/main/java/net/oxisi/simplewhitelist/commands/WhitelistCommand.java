package net.oxisi.simplewhitelist.commands;

import net.oxisi.simplewhitelist.SimpleWhitelist;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WhitelistCommand implements CommandExecutor, TabCompleter {

    private final SimpleWhitelist plugin;

    public WhitelistCommand(SimpleWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length != 2) {
            return false;
        }

        String playerName = args[0];
        boolean isWhitelisted;

        if (args[1].equalsIgnoreCase("true")) {
            isWhitelisted = true;
            sender.sendMessage(playerName + " has been whitelisted!");
        } else if (args[1].equalsIgnoreCase("false")) {
            isWhitelisted = false;
            sender.sendMessage(playerName + " has been removed from the whitelist!");
        } else {
            sender.sendMessage("Invalid argument. Please use 'true' or 'false'.");
            return false;
        }

        plugin.getConfig().set("whitelisted." + playerName, isWhitelisted);
        plugin.saveConfig();

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();
            if ("true".startsWith(args[1].toLowerCase())) {
                suggestions.add("true");
            }
            if ("false".startsWith(args[1].toLowerCase())) {
                suggestions.add("false");
            }
            return suggestions;
        }
        return null;
    }
}
