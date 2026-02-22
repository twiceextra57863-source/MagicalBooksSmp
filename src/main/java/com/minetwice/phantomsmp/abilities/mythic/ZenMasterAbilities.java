package com.minetwice.phantomsmp.abilities.mythic;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ZenMasterAbilities {
    
    public static BookAbility createPerfectDodge() {
        return new BookAbility(
            "Perfect Dodge",
            "§7§b50% §7chance to ignore damage",
            60, 55, 50,
            Particle.END_ROD,
            Sound.ENTITY_PLAYER_LEVELUP,
            (player, level) -> {
                double chance = level == 1 ? 0.5 : level == 2 ? 0.6 : 0.7;
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                // Silver aura
                for (int i = 0; i < 50; i++) {
                    double angle = i * Math.PI * 2 / 25;
                    double x = Math.cos(angle) * 2;
                    double z = Math.sin(angle) * 2;
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.2
                    );
                }
                
                player.setMetadata("dodge_active", new org.bukkit.metadata.FixedMetadataValue(
                    PhantomSMP.getInstance(), true));
                player.setMetadata("dodge_chance", new org.bukkit.metadata.FixedMetadataValue(
                    PhantomSMP.getInstance(), chance));
                
                player.getServer().getScheduler().runTaskLater(PhantomSMP.getInstance(), () -> {
                    player.removeMetadata("dodge_active", PhantomSMP.getInstance());
                    player.removeMetadata("dodge_chance", PhantomSMP.getInstance());
                }, duration);
                
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
            }
        );
    }
    
    public static BookAbility createChiBlast() {
        return new BookAbility(
            "Chi Blast",
            "§7Launch enemy upward",
            45, 40, 35,
            Particle.CRIT,
            Sound.ENTITY_PLAYER_ATTACK_CRIT,
            (player, level) -> {
                double power = level == 1 ? 1.5 : level == 2 ? 2.0 : 2.5;
                Player target = getTargetPlayer(player, 5);
                
                if (target != null) {
                    target.setVelocity(new Vector(0, power, 0));
                    
                    // Impact wave
                    for (int i = 0; i < 20; i++) {
                        double angle = i * Math.PI * 2 / 20;
                        double x = Math.cos(angle) * 2;
                        double z = Math.sin(angle) * 2;
                        
                        target.getWorld().spawnParticle(
                            Particle.EXPLOSION,
                            target.getLocation().clone().add(x, 0, z),
                            0, 0, 0, 0, 0
                        );
                    }
                    
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 0.8f);
                }
            }
        );
    }
    
    public static BookAbility createEnlightenedState() {
        return new BookAbility(
            "Enlightened State",
            "§7Strength III for §b6 seconds",
            180, 160, 140,
            Particle.FLASH,
            Sound.ENTITY_WITHER_SPAWN,
            (player, level) -> {
                int duration = level == 1 ? 120 : level == 2 ? 140 : 160;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 0));
                
                // Divine aura
                for (int i = 0; i < 5; i++) {
                    final int wave = i;
                    player.getServer().getScheduler().runTaskLater(PhantomSMP.getInstance(), () -> {
                        double radius = wave * 1.5;
                        
                        for (int angle = 0; angle < 360; angle += 15) {
                            double rad = Math.toRadians(angle);
                            double x = Math.cos(rad) * radius;
                            double z = Math.sin(rad) * radius;
                            
                            player.getWorld().spawnParticle(
                                Particle.FLASH,
                                player.getLocation().clone().add(x, 1, z),
                                0, 0, 0, 0, 0
                            );
                        }
                    }, i * 5L);
                }
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.5f);
            }
        );
    }
    
    private static Player getTargetPlayer(Player player, int maxDistance) {
        return player.getWorld().getPlayers().stream()
            .filter(p -> !p.equals(player))
            .filter(p -> p.getLocation().distance(player.getLocation()) <= maxDistance)
            .min((p1, p2) -> Double.compare(
                p1.getLocation().distance(player.getLocation()),
                p2.getLocation().distance(player.getLocation())
            ))
            .orElse(null);
    }
}
