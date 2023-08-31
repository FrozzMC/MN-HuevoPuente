package hacky.dev.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import hacky.dev.MNHP;

import java.util.HashMap;
import java.util.Iterator;

public class PathwayHandler {
    private final Material bridgeMaterial;
    private final byte eggData;
    private final byte snowballData;

    public static HashMap<Projectile, Material> pathways = new HashMap();

    public PathwayHandler() {
        this.bridgeMaterial = Material.WOOL;
        this.eggData = 14;
        this.snowballData = 5;

        new BukkitRunnable() {
            public void run() {
                if (!pathways.isEmpty()) {
                    Iterator<Projectile> iterator = pathways.keySet().iterator();
                    while (iterator.hasNext()) {
                        Projectile proj = iterator.next();
                        if (proj.isDead() || !proj.isValid()) {
                            iterator.remove();
                            continue;
                        }

                        if (proj instanceof Egg) {
                            handleProjectile(proj, eggData);
                        } else {
                            handleProjectile(proj, snowballData);
                        }
                    }
                }
            }
        }.runTaskTimer(MNHP.getInstance(), 0L, 1L);
    }

    private void handleProjectile(Projectile proj, byte data) {
        Player pl = (Player) proj.getShooter();
        Location loc = proj.getLocation();

        if (pl.getLocation().distance(loc) > 4.0) {
            loc = loc.clone().subtract(0.0, 2.0, 0.0);

            loc.getBlock().setType(bridgeMaterial);
            loc.getBlock().setData(data);

            pl.playSound(loc, Sound.STEP_STONE, 10.0F, 1.0F);

            loc.clone().subtract(0.0, 0.0, 1.0).getBlock().setType(bridgeMaterial);
            loc.clone().subtract(1.0, 0.0, 0.0).getBlock().setType(bridgeMaterial);
            loc.clone().add(0.0, 0.0, 1.0).getBlock().setType(bridgeMaterial);
            loc.clone().add(1.0, 0.0, 0.0).getBlock().setType(bridgeMaterial);

            loc.clone().subtract(0.0, 0.0, 1.0).getBlock().setData(data);
            loc.clone().subtract(1.0, 0.0, 0.0).getBlock().setData(data);
            loc.clone().add(0.0, 0.0, 1.0).getBlock().setData(data);
            loc.clone().add(1.0, 0.0, 0.0).getBlock().setData(data);
        }
    }

    public Material getBridgeMaterial() {
        return bridgeMaterial;
    }
}
