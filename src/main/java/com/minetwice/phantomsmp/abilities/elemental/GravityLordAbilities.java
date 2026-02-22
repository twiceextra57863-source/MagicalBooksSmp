package com.minetwice.phantomsmp.abilities.elemental;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GravityLordAbilities {
    
    public static BookAbility createWeightOfTheWorld() {
        return new BookAbility(
            "Weight of the World",
            "§7Slowness IV for 2 seconds",
            35, 30, 25,
            Particle.CRIT,
            Sound.ENTITY_IRON_GOLEM_HURT,
            (player, level) -> {
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                int slowLevel = level == 1 ? 3 : level == 2 ? 3 : 4;
                
                // Gravity field
                for (int i = 0; i < 360; i += 30) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.CRIT,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.2
                    );
                }
                
                // Apply slow
                player.getNearbyEntities(radius, radius, radius).stream()
package com.minetwice.phantomsmp.abilities.elemental;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GravityLordAbilities {
    
    public static BookAbility createWeightOfTheWorld() {
        return new BookAbility(
            "Weight of the World",
            "§7Slowness IV for 2 seconds",
            35, 30, 25,
            Particle.CRIT,
            Sound.ENTITY_IRON_GOLEM_HURT,
            (player, level) -> {
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                int slowLevel = level == 1 ? 3 : level == 2 ? 3 : 4;
                
                // Gravity field
                for (int i = 0; i < 360; i += 30) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.CRIT,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.2
                    );
                }
                
                // Apply slow
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).addPotionEffect(
                        new PotionEffect(PotionEffectType.SLOWNESS, duration, slowLevel)));
                
                player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createZeroGravity() {
        return new BookAbility(
            "Zero Gravity",
            "§7Levitation for 1 second",
            25, 22, 20,
            Particle.LEVITATION,
            Sound.ENTITY_SHULKER_SHOOT,
            (player, level) -> {
                int duration = level == 1 ? 20 : level == 2 ? 30 : 40;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                
                // Anti-gravity field
                for (int i = 0; i < 50; i++) {
                    double x = (Math.random() - 0.5) * radius;
                    double z = (Math.random() - 0.5) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.LEVITATION,
                        player.getLocation().clone().add(x, Math.random() * 3, z),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                // Apply levitation
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).addPotionEffect(
                        new PotionEffect(PotionEffectType.LEVITATION, duration, 0)));
                
                player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createSingularity() {
        return new BookAbility(
            "Singularity",
            "§7Drop enemies for fall damage",
            40, 35, 30,
            Particle.ENCHANTED_HIT,
            Sound.ENTITY_GENERIC_EXPLODE,
            (player, level) -> {
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                double liftStrength = level == 1 ? 1.5 : level == 2 ? 2.0 : 2.5;
                
                // First: lift everyone up
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        e.setVelocity(new Vector(0, liftStrength, 0));
                        
                        // Lift particles
                        for (int i = 0; i < 20; i++) {
                            e.getWorld().spawnParticle(
                                Particle.ENCHANTED_HIT,
                                e.getLocation().clone().add(0, i * 0.5, 0),
                                0, 0, 0, 0, 0.1
                            );
                        }
                    });
                
                // Then: smash them down after 1 second
                player.getServer().getScheduler().runTaskLater(
                    PhantomSMP.getInstance(),
                    () -> {
                        player.getNearbyEntities(radius, radius, radius).stream()
                            .filter(e -> e instanceof Player && !e.equals(player))
                            .forEach(e -> {
                                e.setVelocity(new Vector(0, -2.0, 0));
                                
                                // Impact prediction
                                e.getWorld().spawnParticle(
                                    Particle.EXPLOSION,
                                    e.getLocation(),
                                    10, 0.5, 0, 0.5, 0.1
                                );
                            });
                    },
                    20L
                );
                
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
            }
        );
    }
}
