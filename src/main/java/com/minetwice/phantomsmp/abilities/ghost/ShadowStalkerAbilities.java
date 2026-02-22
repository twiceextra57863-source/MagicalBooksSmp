package com.minetwice.phantomsmp.abilities.ghost;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ShadowStalkerAbilities {
    
    public static BookAbility createShadowStep() {
        return new BookAbility(
            "Shadow Step",
            "§7Short teleport up to §b5 blocks",
            35, 30, 25,
            Particle.PORTAL,
            Sound.ENTITY_ENDERMAN_TELEPORT,
            (player, level) -> {
                int distance = level == 1 ? 5 : level == 2 ? 7 : 10;
                Vector direction = player.getLocation().getDirection().normalize();
                Location targetLoc = player.getLocation().clone().add(direction.multiply(distance));
                
                // Check if location is safe
                if (targetLoc.getBlock().isPassable()) {
                    // Portal effect at start
                    for (int i = 0; i < 50; i++) {
                        player.getWorld().spawnParticle(
                            Particle.PORTAL,
                            player.getLocation(),
                            0, 
                            (Math.random() - 0.5) * 2,
                            Math.random() * 2,
                            (Math.random() - 0.5) * 2,
                            0.1
                        );
                    }
                    
                    // Teleport
                    player.teleport(targetLoc);
                    
                    // Portal effect at end
                    for (int i = 0; i < 50; i++) {
                        player.getWorld().spawnParticle(
                            Particle.PORTAL,
                            player.getLocation(),
                            0,
                            (Math.random() - 0.5) * 2,
                            Math.random() * 2,
                            (Math.random() - 0.5) * 2,
                            0.1
                        );
                    }
                    
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
                }
            }
        );
    }
    
    public static BookAbility createVeilOfDarkness() {
        return new BookAbility(
            "Veil of Darkness",
            "§7Invisibility for §b3 seconds",
            25, 22, 20,
            Particle.SMOKE,
            Sound.ENTITY_ILLUSIONER_MIRROR_MOVE,
            (player, level) -> {
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 0));
                
                // Smoke cloud
                for (int i = 0; i < 100; i++) {
                    player.getWorld().spawnParticle(
                        Particle.SMOKE,
                        player.getLocation().clone().add(
                            (Math.random() - 0.5) * 3,
                            Math.random() * 2,
                            (Math.random() - 0.5) * 3
                        ),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createAbyssalStrike() {
        return new BookAbility(
            "Abyssal Strike",
            "§7AoE blindness in §b5 block radius",
            25, 22, 20,
            Particle.SMOKE_LARGE,
            Sound.ENTITY_WARDEN_ATTACK_IMPACT,
            (player, level) -> {
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                
                // Dark pulse
                for (int i = 0; i < 360; i += 15) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.SMOKE_LARGE,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                // Apply blindness
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).addPotionEffect(
                        new PotionEffect(PotionEffectType.BLINDNESS, duration, 0)));
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_ATTACK_IMPACT, 1.0f, 0.5f);
            }
        );
    }
}
