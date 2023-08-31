package hacky.dev.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import hacky.dev.handlers.PathwayHandler;
import org.bukkit.projectiles.ProjectileSource;

import java.util.*;

public class ProjectileEvents implements Listener {
	private final Set<UUID> thrownEggs;
	private final FileConfiguration config;
	private final PathwayHandler pathwayHandler;
	
	public ProjectileEvents(final FileConfiguration config, final PathwayHandler pathwayHandler) {
		thrownEggs = new HashSet<>();
		this.config = Objects.requireNonNull(config, "FileConfiguration reference cannot be null.");
		this.pathwayHandler = Objects.requireNonNull(pathwayHandler, "PathwayHandler reference cannot be null.");
	}
	
	@EventHandler
	public void onThrow(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final ItemStack item = event.getPlayer().getItemInHand();
		// Checks if the item in the player hand is null.
		if (item == null) {
			return;
		}
		final Material type = item.getType();
		// Checks if the type is an Egg or a Snow Ball.
		if ((type != Material.EGG) && (type != Material.SNOW_BALL)) {
			return;
		}
		// Checks if the action is distinct to a 'RIGHT_CLICK_AIR' type.
		if (event.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}
		final String displayName = item.getItemMeta().getDisplayName();
		// Checks the content of the item display name.
		if (displayName.equals(ChatColor.translateAlternateColorCodes('&', config.getString("items.eggName"))) ||
			displayName.equals(ChatColor.translateAlternateColorCodes('&', config.getString("items.ballName")))
		) {
			thrownEggs.add(player.getUniqueId());
		}
	}
	
	@EventHandler
	public void onHit(final ProjectileHitEvent event) {
		final List<Projectile> projectiles = pathwayHandler.projectiles();
		final Projectile projectile = event.getEntity();
		final EntityType type = projectile.getType();
		// Checks if the entity type is a 'EGG'.
		// Else, check if the type is a 'SNOWBALL'.
		if ((type != EntityType.EGG) && (type != EntityType.SNOWBALL)) {
			return;
		}
		// Checks if the projectiles list contains that reference.
		if (!projectiles.contains(projectile)) {
			return;
		}
		projectiles.remove(projectile);
	}
	
	@EventHandler
	public void onLaunch(final ProjectileLaunchEvent event) {
		final Projectile projectile = event.getEntity();
		final EntityType type = projectile.getType();
		// Checks if the entity type is a 'EGG'.
		// Else, check if the type is a 'SNOWBALL'.
		if ((type != EntityType.EGG) && (type != EntityType.SNOWBALL)) {
			return;
		}
		final ProjectileSource shooter = projectile.getShooter();
		// Checks if the shooter is a player.
		if (!(shooter instanceof Player)) {
			return;
		}
		final UUID id = ((Player) shooter).getUniqueId();
		// Checks if the set contains this id.
		if (!thrownEggs.contains(id)) {
			return;
		}
		thrownEggs.remove(id);
		pathwayHandler.projectiles().add(projectile);
	}
}