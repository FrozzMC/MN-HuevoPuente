package hacky.dev;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import hacky.dev.commands.BridgeCommand;
import hacky.dev.events.ProjectileEvents;
import hacky.dev.handlers.PathwayHandler;

public class MNHP extends JavaPlugin {
	private static MNHP instance;
	
	private FileConfiguration config;
	private PathwayHandler pathwayHandler;
	
	@Override
	public void onLoad() {
		instance = this;
		
		config = getConfig();
		config.options().copyDefaults(true);
		saveDefaultConfig();
		
		pathwayHandler = new PathwayHandler();
	}
	
	@Override
	public void onEnable() {
		pathwayHandler.startCheckTask();
		getServer().getPluginManager().registerEvents(new ProjectileEvents(config, pathwayHandler), this);
		new BridgeCommand(this, config);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public static MNHP getInstance() {
		return instance;
	}
}
