package hacky.dev;

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
    public void onEnable() {
        instance = this;

        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();

        pathwayHandler = new PathwayHandler();
        getServer().getPluginManager().registerEvents(new ProjectileEvents(config), this);

        new BridgeCommand(this, pathwayHandler);
    }

    @Override
    public void onDisable() {
    }

    public static MNHP getInstance() {
        return instance;
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }

    public PathwayHandler getPathwayHandler() {
        return pathwayHandler;
    }
}
