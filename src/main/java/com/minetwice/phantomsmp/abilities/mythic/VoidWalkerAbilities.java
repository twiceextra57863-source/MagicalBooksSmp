package com.minetwice.phantomsmp.abilities.mythic;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class VoidWalkerAbilities {
    
    public static BookAbility createSpatialShield() {
        return new BookAbility(
            "Spatial Shield",
            "§7Grants §bResistance II §7for 3 seconds",
            40, 35, 30,
            Particle.PORTAL,
            Sound.ENTITY_ILLUSIONER_CAST_SPELL,
            (player, level) -> {
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 1));
                
                // Spatial distortion effect
                for (int i = 0; i < 360; i += 15) {
                    double radians = Math.toRadians(i);
                    double x = Math.cos(radians) * 2;
                    double z = Math.sin(radians) * 2;
                    
                    for (double y = 0; y <= 2; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.REVERSE_PORTAL,
                            player.getLocation().clone().add(x, y, z),
                            0, 0, 0, 0, 0.1
                        );
                    }
                }
                
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
            }
        );
    }
    
    public static BookAbility createVoidPull() {
        return new BookAbility(
            "Void Pull",
            "§7Pull nearby players within §b5 blocks",
            45, 40, 35,
            Particle.ENCHANTMENT_TABLE,
            Sound.ENTITY_ENDERMAN_TELEPORT,
            (player, level) -> {
                double strength = level == 1 ? 1.0 : level == 2 ? 1.5 : 2.0;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                
                // Create vortex
                for (int i = 0; i < 360; i += 10) {
                    double radians = Math.toRadians(i);
                    double x = Math.cos(radians) * radius;
                    double z = Math.sin(radians) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.REVERSE_PORTAL,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.2
                    );
                }
                
                // Pull entities
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        Vector direction = player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(direction.multiply(strength));
                    });
            }
        );
    }
    
    public static BookAbility createRealityBreak() {
        return new BookAbility(
            "Reality Break",
            "§7Space-time rupture dealing §c6 hearts",
            120, 100, 80,
            Particle.SONIC_BOOM,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                double damage = level == 1 ? 12 : level == 2 ? 14 : 16;
                double radius = level == 1 ? 6 : level == 2 ? 7 : 8;
                
                // Reality distortion
                for (int i = 0; i < 5; i++) {
                    final int layer = i;
                    player.getServer().getScheduler().runTaskLater(PhantomSMP.getInstance(), () -> {
                        double layerRadius = radius * (layer + 1) / 5;
                        
                        for (int angle = 0; angle < 360; angle += 15) {
                            double rad = Math.toRadians(angle);
                            double x = Math.cos(rad) * layerRadius;
                            double z = Math.sin(rad) * layerRadius;
                            
                            player.getWorld().spawnParticle(
                                Particle.SONIC_BOOM,
                                player.getLocation().clone().add(x, 1, z),
                                0, 0, 0, 0, 0
                            );
                        }
                    }, i * 2L);
                }
                
                // Damage
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).damage(damage, player));
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.5f);
            }
        );
    }
}
