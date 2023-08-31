package hacky.dev.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import hacky.dev.MNHP;

import java.util.*;

public class PathwayHandler {
	private final List<Projectile> projectiles;
	private final Material bridgeMaterial;
	
	public PathwayHandler() {
		projectiles = new ArrayList<>();
		bridgeMaterial = Material.WOOL;
	}
	
	public void startCheckTask() {
		new BukkitRunnable() {
			public void run() {
				// Checks if the list don't have any element.
				if (projectiles.isEmpty()) {
					return;
				}
				for (final Projectile projectile : projectiles) {
					// Checks if the projectile is death, or isn't valid.
					if (projectile.isDead() || !projectile.isValid()) {
						continue;
					}
					EntityType type = projectile.getType();
					// Checks if the projectile type is 'EGG' or 'SNOWBALL'.
          if ((type == EntityType.EGG) || (type == EntityType.SNOWBALL)) {
            handleProjectile(projectile);
          }
          type = null;
        }
			}
		}.runTaskTimer(MNHP.getInstance(), 0L, 1L);
	}
	
	public List<Projectile> projectiles() {
		return projectiles;
	}
	
	private void handleProjectile(final Projectile projectile) {
		Location loc = projectile.getLocation();
		// Checks if the player location distance to the projectile location is minor or equal to 4.0
		if (((Player) projectile.getShooter()).getLocation().distance(loc) > 4.0) {
      loc.subtract(0.0, 2.0, 0.0).getBlock().setType(bridgeMaterial);
      loc.subtract(0.0, 0.0, 1.0).getBlock().setType(bridgeMaterial);
      loc.subtract(1.0, 0.0, 0.0).getBlock().setType(bridgeMaterial);
    }
	}
}
