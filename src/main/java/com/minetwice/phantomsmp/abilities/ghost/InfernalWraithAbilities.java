package com.minetwice.phantomsmp.abilities.ghost;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class InfernalWraithAbilities {
    
    public static BookAbility createHellfireGrasp() {
        return new BookAbility(
            "Hellfire Grasp",
            "§7Burn target + Slowness for §b4 seconds",
            35, 30, 25,
            Particle.SOUL_FIRE_FLAME,
            Sound.ITEM_FLINTANDSTEEL_USE,
            (player, level) -> {
                int fireTicks = level == 1 ? 80 : level == 2 ? 100 : 120;
                int slowDuration = level == 1 ? 40 : level == 2 ? 60 : 80;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .findFirst()
                    .ifPresent(e -> {
                        Player target = (Player) e;
                        
                        target.setFireTicks(fireTicks);
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, slowDuration, 0));
                        
                        // Hellfire hand effect
                        Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                        for (double t = 0; t <= 1; t += 0.05) {
                            double x = player.getLocation().getX() + direction.getX() * t * 3;
                            double y = player.getLocation().getY() + 1 + t;
                            double z = player.getLocation().getZ() + direction.getZ() * t * 3;
                            
                            player.getWorld().spawnParticle(
                                Particle.SOUL_FIRE_FLAME,
                                new org.bukkit.Location(player.getWorld(), x, y, z),
                                0, 0, 0, 0, 0.1
                            );
                        }
                        
                        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.5f);
                    });
            }
        );
    }
    
    public static BookAbility createSpectralRush() {
        return new BookAbility(
            "Spectral Rush",
            "§7Dash forward §b6 blocks",
            30, 27, 25,
            Particle.SOUL_FIRE_FLAME,
            Sound.ENTITY_BREEZE_JUMP,
            (player, level) -> {
                double power = level == 1 ? 1.2 : level == 2 ? 1.4 : 1.6;
                Vector direction = player.getLocation().getDirection().setY(0.2).normalize();
                player.setVelocity(direction.multiply(power));
                
                // Flame trail
                for (int i = 0; i < 20; i++) {
                    Vector offset = direction.clone().multiply(-i * 0.3);
                    player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        player.getLocation().clone().add(offset),
                        2, 0.2, 0.2, 0.2, 0.01
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 1.0f, 1.2f);
            }
        );
    }
    
    public static BookAbility createNetherExplosion() {
        return new BookAbility(
            "Nether Explosion",
            "§7§c5 hearts §7damage + knockback in §b6 block radius",
            180, 160, 140,
            Particle.SOUL_FIRE_FLAME,
            Sound.ENTITY_GENERIC_EXPLODE,
            (player, level) -> {
                double damage = level == 1 ? 10 : level == 2 ? 11 : 12;
                double radius = level == 1 ? 6 : level == 2 ? 7 : 8;
                
                // Nether explosion effect
                for (int i = 0; i < 5; i++) {
                    final int layer = i;
                    player.getServer().getScheduler().runTaskLater(PhantomSMP.getInstance(), () -> {
                        double layerRadius = radius * (layer + 1) / 5;
                        
                        for (int angle = 0; angle < 360; angle += 15) {
                            double rad = Math.toRadians(angle);
                            double x = Math.cos(rad) * layerRadius;
                            double z = Math.sin(rad) * layerRadius;
                            
                            player.getWorld().spawnParticle(
                                Particle.SOUL_FIRE_FLAME,
                                player.getLocation().clone().add(x, 1, z),
                                0, 0, 0, 0, 0.2
                            );
                            
                            player.getWorld().spawnParticle(
                                Particle.LAVA,
                                player.getLocation().clone().add(x, 0.5, z),
                                0, 0, 0, 0, 0.1
                            );
                        }
                    }, i * 2L);
                }
                
                // Damage
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).damage(damage, player));
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.3f);
            }
        );
    }
}
