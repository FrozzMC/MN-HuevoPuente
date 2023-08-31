package hacky.dev.commands;

import hacky.dev.MNHP;
import org.bukkit.Bukkit;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BridgeCommand implements CommandExecutor, TabCompleter {
  private final MNHP plugin;
  private final FileConfiguration config;
  
  public BridgeCommand(final MNHP plugin, final FileConfiguration config) {
    this.plugin = Objects.requireNonNull(plugin, "MNHP object reference cannot be null.");
    this.config = Objects.requireNonNull(config, "FileConfiguration reference cannot be null.");
    plugin.getCommand("bridge").setExecutor(this);
    plugin.getCommand("bridge").setTabCompleter(this);
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    // Checks if who execute the command is a player.
    if (!(sender instanceof Player)) {
      sender.sendMessage(getMessage("noPermission"));
      return false;
    }
    final Player pl = (Player) sender;
    // Checks if the player has the required permission.
    if (!pl.hasPermission("bridge.command")) {
      pl.sendMessage(getMessage("noPermission"));
      return false;
    }
    // Checks if the command doesn't have arguments.
    if (args.length == 0) {
      pl.sendMessage(getMessage("usage"));
      return false;
    }
    // Checks if the argument '0' is 'reload'.
    // Else, check if the argument '0' is 'give'.
    if (args[0].equalsIgnoreCase("reload")) {
      Bukkit.getScheduler().cancelTasks(plugin);
      plugin.reloadConfig();
      pl.sendMessage(getMessage("configReloaded"));
    } else if (args[0].equalsIgnoreCase("give")) {
      // Checks if the command has a second argument.
      if (args.length == 1) {
        return false;
      }
      pl.getInventory().addItem(getItem(args[1].toUpperCase()));
      pl.sendMessage(getMessage("bridgeGiven").replace("%type%", args[1]));
    }
    return false;
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
      giveSubCommands.add("snow_ball");
      return giveSubCommands;
    }
    return null;
  }
  
  private String getMessage(final String key) {
    final String message = config.getString(String.format("messages.%s", key));
    // Checks if that key exists in the configuration.
    if (message != null) {
      return ChatColor.translateAlternateColorCodes('&', message);
    }
    return "Message not found in config";
  }
  
  private ItemStack getItem(final String type) {
    String itemName = "";
    // Checks if the parameter value is 'EGG'.
    // Else, check if the value is 'SNOW_BALL'.
    if (type.equals("EGG")) {
      itemName = config.getString("items.eggName");
    } else if (type.equals("SNOW_BALL")) {
      itemName = config.getString("items.ballName");
    }
    final ItemStack stack = new ItemStack(Material.getMaterial(type));
    final ItemMeta meta = stack.getItemMeta();
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
    stack.setItemMeta(meta);
    return stack;
  }
}
