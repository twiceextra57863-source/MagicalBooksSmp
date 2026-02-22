package com.minetwice.phantomsmp.abilities.elemental;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class TidalMasterAbilities {
    
    public static BookAbility createWaterDash() {
        return new BookAbility(
            "Water Dash",
            "§7Dolphin's Grace effect",
            25, 22, 20,
            Particle.SPLASH,
            Sound.ENTITY_PLAYER_SPLASH,
            (player, level) -> {
                int duration = level == 1 ? 100 : level == 2 ? 120 : 140;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, duration, 0));
                
                // Water trail
                Vector direction = player.getLocation().getDirection();
                for (int i = 0; i < 20; i++) {
                    Vector offset = direction.clone().multiply(-i * 0.3);
                    player.getWorld().spawnParticle(
                        Particle.SPLASH,
                        player.getLocation().clone().add(offset),
                        5, 0.3, 0.3, 0.3, 0.1
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1.0f, 1.2f);
            }
        );
    }
    
    public static BookAbility createCrashingWave() {
        return new BookAbility(
            "Crashing Wave",
            "§7Knockback wave",
            25, 22, 20,
            Particle.SPLASH,
            Sound.ENTITY_BREEZE_WIND_BURST,
            (player, level) -> {
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                double knockback = level == 1 ? 1.2 : level == 2 ? 1.5 : 1.8;
                
                // Wave effect
                for (int i = 0; i < 360; i += 20) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.SPLASH,
                        player.getLocation().clone().add(x, 1, z),
                        10, 0.3, 0.3, 0.3, 0.1
                    );
                }
                
                // Knockback
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        Vector away = e.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                        e.setVelocity(away.multiply(knockback).setY(0.3));
                    });
                
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createWhirlpool() {
        return new BookAbility(
            "Whirlpool",
            "§7Mining Fatigue + Slowness",
            35, 30, 25,
            Particle.BUBBLE_POP,
            Sound.ENTITY_DROWNED_AMBIENT,
            (player, level) -> {
                int radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                // Whirlpool effect
                for (int i = 0; i < 360; i += 10) {
                    double rad = Math.toRadians(i);
                    double r = radius * (1 - (i / 360.0));
                    double x = Math.cos(rad) * r;
                    double z = Math.sin(rad) * r;
                    
                    for (double y = 0; y <= 2; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.BUBBLE_POP,
                            player.getLocation().clone().add(x, y, z),
                            0, 0, 0, 0, 0.1
                        );
                    }
                }
                
                // Apply effects
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 0));
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, duration, 0));
                        
                        // Pull toward center
                        Vector pull = player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.3);
                        e.setVelocity(pull);
                    });
                
                player.playSound(player.getLocation(), Sound.ENTITY_DROWNED_AMBIENT, 1.0f, 0.5f);
            }
        );
    }
}
