package com.minetwice.phantomsmp.abilities.elemental;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FlameSovereignAbilities {
    
    public static BookAbility createFlameRush() {
        return new BookAbility(
            "Flame Rush",
            "§7Dash with fire trail",
            25, 22, 20,
            Particle.FLAME,
            Sound.ENTITY_BLAZE_SHOOT,
            (player, level) -> {
                double power = level == 1 ? 1.2 : level == 2 ? 1.4 : 1.6;
                Vector direction = player.getLocation().getDirection().setY(0.2).normalize();
                player.setVelocity(direction.multiply(power));
                
                // Fire trail
                for (int i = 0; i < 20; i++) {
                    Vector offset = direction.clone().multiply(-i * 0.3);
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        player.getLocation().clone().add(offset),
                        5, 0.2, 0.2, 0.2, 0.01
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.2f);
            }
        );
    }
    
    public static BookAbility createFireOrb() {
        return new BookAbility(
            "Fire Orb",
            "§7Launch a fireball",
            35, 30, 25,
            Particle.LARGE_SMOKE,
            Sound.ENTITY_BLAZE_SHOOT,
            (player, level) -> {
                Fireball fireball = player.launchProjectile(Fireball.class);
                fireball.setYield(level == 1 ? 1 : level == 2 ? 2 : 3);
                fireball.setIsIncendiary(true);
                
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createRingOfFire() {
        return new BookAbility(
            "Ring of Fire",
            "§7Create a ring of fire",
            30, 27, 25,
            Particle.LAVA,
            Sound.ITEM_FIRECHARGE_USE,
            (player, level) -> {
                int radius = level == 1 ? 3 : level == 2 ? 4 : 5;
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                // Create fire ring
                for (int i = 0; i < 360; i += 10) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        player.getLocation().clone().add(x, 0.5, z),
                        0, 0, 0, 0, 0.1
                    );
                    
                    // Set blocks on fire - Fixed: Get block from location properly
                    Location blockLoc = player.getLocation().clone().add(x, 0, z);
                    if (Math.random() < 0.3 && blockLoc.getBlock().getType() == Material.AIR) {
                        blockLoc.getBlock().setType(Material.FIRE);
                    }
                }
                
                // Damage over time task
                int[] taskId = new int[1];
                taskId[0] = player.getServer().getScheduler().scheduleSyncRepeatingTask(
                    PhantomSMP.getInstance(), 
                    () -> {
                        player.getNearbyEntities(radius, radius, radius).stream()
                            .filter(e -> e instanceof Player && !e.equals(player))
                            .forEach(e -> {
                                e.setFireTicks(20);
                            });
                    }, 0L, 20L);
                
                // Cancel after duration
                player.getServer().getScheduler().scheduleSyncDelayedTask(
                    PhantomSMP.getInstance(),
                    () -> player.getServer().getScheduler().cancelTask(taskId[0]),
                    duration
                );
                
                player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 0.5f);
            }
        );
    }
}
