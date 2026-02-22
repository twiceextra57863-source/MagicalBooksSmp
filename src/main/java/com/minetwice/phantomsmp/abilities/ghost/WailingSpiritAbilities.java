package com.minetwice.phantomsmp.abilities.ghost;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class WailingSpiritAbilities {
    
    public static BookAbility createPiercingScream() {
        return new BookAbility(
            "Piercing Scream",
            "§7Knockback + Nausea effect",
            40, 35, 30,
            Particle.SONIC_BOOM,
            Sound.ENTITY_BREEZE_INHALE,
            (player, level) -> {
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                double knockback = level == 1 ? 1.0 : level == 2 ? 1.3 : 1.6;
                int nauseaDuration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                // Sonic wave effect
                for (int i = 0; i < 360; i += 15) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    for (double y = 0; y <= 2; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.SONIC_BOOM,
                            player.getLocation().clone().add(x, y, z),
                            0, 0, 0, 0, 0
                        );
                    }
                }
                
                // Affect nearby players
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        Player target = (Player) e;
                        
                        // Knockback
                        Vector away = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                        target.setVelocity(away.multiply(knockback).setY(0.3));
                        
                        // Nausea
                        target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, nauseaDuration, 0));
                        
                        // Ring around target
                        for (int j = 0; j < 360; j += 45) {
                            double rad2 = Math.toRadians(j);
                            double x2 = Math.cos(rad2) * 1.2;
                            double z2 = Math.sin(rad2) * 1.2;
                            
                            target.getWorld().spawnParticle(
                                Particle.SONIC_BOOM,
                                target.getLocation().clone().add(x2, 1, z2),
                                0, 0, 0, 0, 0
                            );
                        }
                    });
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_INHALE, 1.0f, 0.3f);
            }
        );
    }
    
    public static BookAbility createTerrorAura() {
        return new BookAbility(
            "Terror Aura",
            "§7Slowness aura around you",
            25, 22, 20,
            Particle.SCULK_SOUL,
            Sound.ENTITY_WARDEN_NEARBY_CLOSE,
            (player, level) -> {
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                int slowLevel = level == 1 ? 0 : level == 2 ? 0 : 1;
                
                // Create aura
                for (int i = 0; i < 100; i++) {
                    double angle = i * Math.PI * 2 / 50;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        player.getLocation().clone().add(x, 0.5, z),
                        0, 0, 0, 0, 0.05
                    );
                }
                
                // Apply slow
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).addPotionEffect(
                        new PotionEffect(PotionEffectType.SLOWNESS, duration, slowLevel)));
                
                player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_NEARBY_CLOSE, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createSoulWail() {
        return new BookAbility(
            "Soul Wail",
            "§7High knockback burst",
            35, 30, 25,
            Particle.SCULK_CHARGE,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                double radius = level == 1 ? 6 : level == 2 ? 7 : 8;
                double knockback = level == 1 ? 2.0 : level == 2 ? 2.5 : 3.0;
                
                // Expanding ring
                for (int i = 0; i < 5; i++) {
                    final int layer = i;
                    player.getServer().getScheduler().runTaskLater(PhantomSMP.getInstance(), () -> {
                        double layerRadius = radius * (layer + 1) / 5;
                        
                        for (int angle = 0; angle < 360; angle += 10) {
                            double rad = Math.toRadians(angle);
                            double x = Math.cos(rad) * layerRadius;
                            double z = Math.sin(rad) * layerRadius;
                            
                            player.getWorld().spawnParticle(
                                Particle.SCULK_CHARGE,
                                player.getLocation().clone().add(x, 1, z),
                                0, 0, 0, 0, 0.1
                            );
                        }
                    }, i * 2L);
                }
                
                // Knockback
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        Vector away = e.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                        e.setVelocity(away.multiply(knockback).setY(0.5));
                    });
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.3f);
            }
        );
    }
}
