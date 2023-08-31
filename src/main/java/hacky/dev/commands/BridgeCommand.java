package hacky.dev.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import hacky.dev.handlers.PathwayHandler;

public class BridgeCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final PathwayHandler pathwayHandler;

    public BridgeCommand(JavaPlugin plugin, PathwayHandler pathwayHandler) {
        this.plugin = plugin;
        this.pathwayHandler = pathwayHandler;
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage("noPermission"));
            return false;
        } else {
            Player pl = (Player) sender;
            if (!pl.hasPermission("bridge.command")) {
                pl.sendMessage(getMessage("noPermission"));
                return false;
            } else if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("egg")) {
                    pl.getInventory().addItem(new ItemStack[]{this.getItem("EGG")});
                    pl.sendMessage(getMessage("bridgeEggGiven"));
                    return true;
                } else if (args[0].equalsIgnoreCase("ball")) {
                    pl.getInventory().addItem(new ItemStack[]{this.getItem("BALL")});
                    pl.sendMessage(getMessage("bridgeBallGiven"));
                    return true;
                } else {
                    pl.sendMessage(getMessage("usage"));
                    return false;
                }
            } else {
                pl.sendMessage(getMessage("usage"));
                return false;
            }
        }
    }

    private String getMessage(String key) {
        FileConfiguration config = plugin.getConfig();
        return ChatColor.translateAlternateColorCodes('&', config.getString("messages." + key));
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