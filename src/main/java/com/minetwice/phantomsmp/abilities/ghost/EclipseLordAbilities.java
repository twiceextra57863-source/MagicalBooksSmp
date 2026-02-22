package com.minetwice.phantomsmp.abilities.ghost;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class EclipseLordAbilities {
    
    public static BookAbility createShadowCloak() {
        return new BookAbility(
            "Shadow Cloak",
            "§7Resistance I for §b4 seconds",
            40, 35, 30,
            Particle.SMOKE,
            Sound.ENTITY_ILLUSIONER_MIRROR_MOVE,
            (player, level) -> {
                int duration = level == 1 ? 80 : level == 2 ? 100 : 120;
                int resistanceLevel = level == 1 ? 0 : level == 2 ? 0 : 1;
                
                // FIXED: Use RESISTANCE instead of DAMAGE_RESISTANCE
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, duration, resistanceLevel));
                
                // Shadow cloak effect
                for (int i = 0; i < 50; i++) {
                    double angle = i * Math.PI * 2 / 25;
                    double x = Math.cos(angle) * 1.5;
                    double z = Math.sin(angle) * 1.5;
                    
                    player.getWorld().spawnParticle(
                        Particle.SMOKE,
                        player.getLocation().clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createDarkPull() {
        return new BookAbility(
            "Dark Pull",
            "§7Pull target §b4 blocks §7toward you",
            35, 32, 30,
            Particle.LARGE_SMOKE,  // Replaced SMOKE_LARGE
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
                        
                        // Dark tendrils
                        for (int i = 0; i < 10; i++) {
                            double t = i / 10.0;
                            Vector pos = target.getLocation().toVector().clone()
                                .add(direction.clone().multiply(t * 3));
                            
                            target.getWorld().spawnParticle(
                                Particle.LARGE_SMOKE,
                                pos.getX(), pos.getY() + 1, pos.getZ(),
                                0, 0, 0, 0, 0.1
                            );
                        }
                    });
            }
        );
    }
    
    public static BookAbility createTotalDarkness() {
        return new BookAbility(
            "Total Darkness",
            "§7Slowness II + §c4 hearts §7damage in §b6 block radius",
            150, 130, 100,
            Particle.SCULK_SOUL,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                double damage = level == 1 ? 8 : level == 2 ? 9 : 10;
                double radius = level == 1 ? 6 : level == 2 ? 7 : 8;
                int slowDuration = level == 1 ? 80 : level == 2 ? 100 : 120;
                
                // Eclipse effect
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
                
                // Inner circle
                for (int i = 0; i < 100; i++) {
                    double angle = Math.random() * Math.PI * 2;
                    double r = Math.random() * radius;
                    double x = Math.cos(angle) * r;
                    double z = Math.sin(angle) * r;
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        player.getLocation().clone().add(x, Math.random() * 2, z),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                // Damage and slow
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        ((Player) e).damage(damage, player);
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, slowDuration, 1));
                    });
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.2f);
            }
        );
    }
}
