package com.minetwice.phantomsmp.abilities.ghost;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightmareAbilities {
    
    public static BookAbility createShadowGlide() {
        return new BookAbility(
            "Shadow Glide",
            "§7Invisibility + Speed I for §b3 seconds",
            40, 35, 30,
            Particle.SMOKE,
            Sound.ENTITY_ILLUSIONER_MIRROR_MOVE,
            (player, level) -> {
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 0));
                
                // Shadow trail
                for (int i = 0; i < 50; i++) {
                    player.getWorld().spawnParticle(
                        Particle.SMOKE,
                        player.getLocation().clone().add(
                            (Math.random() - 0.5) * 2,
                            Math.random(),
                            (Math.random() - 0.5) * 2
                        ),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.0f, 0.3f);
            }
        );
    }
    
    public static BookAbility createPanicSurge() {
        return new BookAbility(
            "Panic Surge",
            "§7Blindness + Nausea in §b5 block radius",
            45, 40, 35,
            Particle.SCULK_SOUL,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                
                // Panic wave
                for (int i = 0; i < 360; i += 10) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.2
                    );
                }
                
                // Apply effects
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0));
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, duration, 0));
                    });
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createDarkEruption() {
        return new BookAbility(
            "Dark Eruption",
            "§7§c5 hearts §7damage + knockback in §b6 block radius",
            180, 150, 120,
            Particle.EXPLOSION_EMITTER,
            Sound.ENTITY_WITHER_SPAWN,
            (player, level) -> {
                double damage = level == 1 ? 10 : level == 2 ? 11 : 12;
                double radius = level == 1 ? 6 : level == 2 ? 7 : 8;
                double knockback = level == 1 ? 1.5 : level == 2 ? 1.8 : 2.0;
                
                // Eruption effect
                for (int i = 0; i < 5; i++) {
                    final int layer = i;
                    player.getServer().getScheduler().runTaskLater(PhantomSMP.getInstance(), () -> {
                        double layerRadius = radius * (layer + 1) / 5;
                        
                        for (int angle = 0; angle < 360; angle += 15) {
                            double rad = Math.toRadians(angle);
                            double x = Math.cos(rad) * layerRadius;
                            double z = Math.sin(rad) * layerRadius;
                            
                            player.getWorld().spawnParticle(
                                Particle.SOUL,
                                player.getLocation().clone().add(x, 0.5, z),
                                0, 0, 0, 0, 0.2
                            );
                            
                            player.getWorld().spawnParticle(
                                Particle.EXPLOSION,
                                player.getLocation().clone().add(x, 1, z),
                                0, 0, 0, 0, 0
                            );
                        }
                    }, i * 3L);
                }
                
                // Damage and knockback
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        ((Player) e).damage(damage, player);
                        org.bukkit.util.Vector away = e.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                        e.setVelocity(away.multiply(knockback).setY(0.5));
                    });
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
            }
        );
    }
}
