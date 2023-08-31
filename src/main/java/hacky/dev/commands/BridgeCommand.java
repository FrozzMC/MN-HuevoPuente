package hacky.dev.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import hacky.dev.handlers.PathwayHandler;

import java.util.ArrayList;
import java.util.List;

public class BridgeCommand implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;
    private final PathwayHandler pathwayHandler;

    public BridgeCommand(JavaPlugin plugin, PathwayHandler pathwayHandler) {
        this.plugin = plugin;
        this.pathwayHandler = pathwayHandler;
        plugin.getCommand("bridge").setExecutor(this);
        plugin.getCommand("bridge").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage("noPermission"));
            return false;
        }

        Player pl = (Player) sender;

        if (!pl.hasPermission("bridge.command")) {
            pl.sendMessage(getMessage("noPermission"));
            return false;
        }

        if (args.length == 0) {
            plugin.reloadConfig();
            pl.sendMessage(getMessage("configReloaded"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give":
                if (args.length < 2) {
                    pl.sendMessage(getMessage("giveUsage"));
                    return false;
                }

                switch (args[1].toLowerCase()) {
                    case "egg":
                        pl.getInventory().addItem(new ItemStack[]{this.getItem("EGG")});
                        pl.sendMessage(getMessage("bridgeEggGiven"));
                        return true;
                    case "ball":
                        pl.getInventory().addItem(new ItemStack[]{this.getItem("BALL")});
                        pl.sendMessage(getMessage("bridgeBallGiven"));
                        return true;
                    default:
                        pl.sendMessage(getMessage("giveUsage"));
                        return false;
                }
            case "reload":
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                plugin.getServer().getPluginManager().enablePlugin(plugin);
                pl.sendMessage(getMessage("pluginReloaded"));
                return true;
            default:
                pl.sendMessage(getMessage("usage"));
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();
            subCommands.add("give");
            subCommands.add("reload");
            return subCommands;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            List<String> giveSubCommands = new ArrayList<>();
            giveSubCommands.add("egg");
            giveSubCommands.add("ball");
            return giveSubCommands;
        }
        return null;
    }

    private String getMessage(String key) {
        FileConfiguration config = plugin.getConfig();
        String message = config.getString("messages." + key);

        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            return "Message not found in config";
        }
    }


    private ItemStack getItem(String type) {
        ItemStack stack = new ItemStack(this.getType(type));
        ItemMeta meta = stack.getItemMeta();
        String itemName = "";

        FileConfiguration config = plugin.getConfig();
        if (type.equals("EGG")) {
            itemName = config.getString("items.eggName");
        } else if (type.equals("BALL")) {
            itemName = config.getString("items.ballName");
        }

        if (itemName != null && !itemName.isEmpty()) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        }

        stack.setItemMeta(meta);
        return stack;
    }

    private Material getType(String type) {
        return type.equals("EGG") ? Material.EGG : Material.SNOW_BALL;
    }
}
