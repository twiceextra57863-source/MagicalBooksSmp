package com.minetwice.phantomsmp.abilities.ghost;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class DreadLordAbilities {
    
    public static BookAbility createGraveWave() {
        return new BookAbility(
            "Grave Wave",
            "§7Slowness II in §b4 block radius",
            35, 30, 25,
            Particle.SCULK_CHARGE,
            Sound.BLOCK_SCULK_SHRIEKER_SHRIEK,
            (player, level) -> {
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                // Grave wave effect
                for (int i = 0; i < 360; i += 10) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_CHARGE,
                        player.getLocation().clone().add(x, 0.5, z),
                        0, 0, 0, 0, 0.2
                    );
                }
                
                // Apply slow
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).addPotionEffect(
                        new PotionEffect(PotionEffectType.SLOWNESS, duration, 1)));
                
                player.playSound(player.getLocation(), Sound.BLOCK_SCULK_SHRIEKER_SHRIEK, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createSpiritChain() {
        return new BookAbility(
            "Spirit Chain",
            "§7Pull target §b4 blocks §7toward you",
            40, 35, 30,
            Particle.ENCHANTMENT_TABLE,
            Sound.ENTITY_ENDERMAN_TELEPORT,
            (player, level) -> {
                double pullStrength = level == 1 ? 0.5 : level == 2 ? 0.6 : 0.7;
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .findFirst()
                    .ifPresent(e -> {
                        Player target = (Player) e;
                        Vector direction = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
                        target.setVelocity(direction.multiply(pullStrength));
                        
                        // Chain effect
                        Location start = target.getLocation().clone().add(0, 1, 0);
                        Location end = player.getLocation().clone().add(0, 1, 0);
                        
                        for (double t = 0; t <= 1; t += 0.05) {
                            double x = start.getX() + (end.getX() - start.getX()) * t;
                            double y = start.getY() + (end.getY() - start.getY()) * t;
                            double z = start.getZ() + (end.getZ() - start.getZ()) * t;
                            
                            player.getWorld().spawnParticle(
                                Particle.ENCHANTMENT_TABLE,
                                new Location(player.getWorld(), x, y, z),
                                0, 0, 0, 0, 1
                            );
                        }
                    });
            }
        );
    }
    
    public static BookAbility createDoomProclamation() {
        return new BookAbility(
            "Doom Proclamation",
            "§7Target gets Wither I + §c4 hearts damage",
            150, 130, 100,
            Particle.SPELL_WITCH,
            Sound.ENTITY_WITHER_AMBIENT,
            (player, level) -> {
                double damage = level == 1 ? 8 : level == 2 ? 9 : 10;
                int witherDuration = level == 1 ? 80 : level == 2 ? 100 : 120;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .findFirst()
                    .ifPresent(e -> {
                        Player target = (Player) e;
                        
                        // Damage
                        target.damage(damage, player);
                        
                        // Wither effect
                        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, witherDuration, 0));
                        
                        // Skull circle
                        for (int i = 0; i < 360; i += 30) {
                            double rad = Math.toRadians(i);
                            double x = Math.cos(rad) * 2;
                            double z = Math.sin(rad) * 2;
                            
                            target.getWorld().spawnParticle(
                                Particle.SPELL_WITCH,
                                target.getLocation().clone().add(x, 1.5, z),
                                0, 0, 0, 0, 1
                            );
                        }
                        
                        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 0.5f);
                    });
            }
        );
    }
}
