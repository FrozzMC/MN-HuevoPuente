package hacky.dev.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import hacky.dev.MNHP;
import hacky.dev.handlers.PathwayHandler;

import java.util.ArrayList;
import java.util.UUID;

public class ProjectileEvents implements Listener {
    private ArrayList<UUID> thrownEggs = new ArrayList<>();
    private FileConfiguration config;

    public ProjectileEvents(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onThrow(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() != null) {
            ItemStack stack = event.getPlayer().getItemInHand();
            if (stack.getType().equals(Material.EGG) || stack.getType().equals(Material.SNOW_BALL)) {
                if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                    if (stack.hasItemMeta()) {
                        if (stack.getItemMeta().hasDisplayName()) {
                            String eggName = ChatColor.translateAlternateColorCodes('&',
                                    MNHP.getInstance().getPluginConfig().getString("items.eggName"));
                            String ballName = ChatColor.translateAlternateColorCodes('&',
                                    MNHP.getInstance().getPluginConfig().getString("items.ballName"));

                            if (stack.getItemMeta().getDisplayName().equals(eggName) ||
                                    stack.getItemMeta().getDisplayName().equals(ballName)) {
                                thrownEggs.add(event.getPlayer().getUniqueId());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Egg || event.getEntity() instanceof Snowball) {
            if (event.getEntity() instanceof Egg) {
                Egg egg = (Egg) event.getEntity();
                if (PathwayHandler.pathways.containsKey(egg)) {
                    PathwayHandler.pathways.remove(egg);
                }
            } else {
                Snowball ball = (Snowball) event.getEntity();
                if (PathwayHandler.pathways.containsKey(ball)) {
                    PathwayHandler.pathways.remove(ball);
                }
            }
        }
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Egg || event.getEntity() instanceof Snowball) {
            Player player;
            if (event.getEntity() instanceof Egg) {
                Egg egg = (Egg) event.getEntity();
                if (egg.getShooter() instanceof Player) {
                    player = (Player) egg.getShooter();
                    if (thrownEggs.contains(player.getUniqueId())) {
                        thrownEggs.remove(player.getUniqueId());
                        PathwayHandler.pathways.put(egg, Material.WOOL);
                    }
                }
            } else {
                Snowball ball = (Snowball) event.getEntity();
                if (ball.getShooter() instanceof Player) {
                    player = (Player) ball.getShooter();
                    if (thrownEggs.contains(player.getUniqueId())) {
                        thrownEggs.remove(player.getUniqueId());
                        PathwayHandler.pathways.put(ball, Material.WOOL);
                    }
                }
            }
        }
    }
}