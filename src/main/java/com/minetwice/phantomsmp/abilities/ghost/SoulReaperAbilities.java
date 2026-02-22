package com.minetwice.phantomsmp.abilities.ghost;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SoulReaperAbilities {
    
    public static BookAbility createSiphonLife() {
        return new BookAbility(
            "Siphon Life",
            "§7Steal §c2 hearts §7from target and heal yourself",
            25, 22, 20,
            Particle.HEART,
            Sound.ENTITY_WITHER_HURT,
            (player, level) -> {
                double damage = level == 1 ? 4 : level == 2 ? 5 : 6;
                double heal = level == 1 ? 4 : level == 2 ? 5 : 6;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                
                // Find nearest target
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .findFirst()
                    .ifPresent(e -> {
                        Player target = (Player) e;
                        target.damage(damage, player);
                        player.setHealth(Math.min(player.getHealth() + heal, player.getMaxHealth()));
                        
                        // Life drain particles - red to green
                        for (int i = 0; i < 20; i++) {
                            double t = i / 20.0;
                            target.getWorld().spawnParticle(
                                Particle.SPELL_MOB,
                                target.getLocation().clone().add(0, 1, 0),
                                0, 1, 0, 0, 1
                            );
                            player.getWorld().spawnParticle(
                                Particle.HEART,
                                player.getLocation().clone().add(0, 1, 0),
                                0, 0, 0, 0, 1
                            );
                        }
                    });
                
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createDeathSentence() {
        return new BookAbility(
            "Death Sentence",
            "§7Target takes §c+20% damage §7for 5 seconds",
            35, 30, 25,
            Particle.SPELL_WITCH,
            Sound.ENTITY_WITHER_SHOOT,
            (player, level) -> {
                int duration = level == 1 ? 100 : level == 2 ? 120 : 140;
                double multiplier = level == 1 ? 1.2 : level == 2 ? 1.25 : 1.3;
                double radius = level == 1 ? 10 : level == 2 ? 12 : 15;
                
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .findFirst()
                    .ifPresent(e -> {
                        Player target = (Player) e;
                        
                        // Mark target
                        target.setMetadata("death_marked", new org.bukkit.metadata.FixedMetadataValue(
                            PhantomSMP.getInstance(), multiplier));
                        
                        // Skull particles around target
                        for (int i = 0; i < 360; i += 30) {
                            double rad = Math.toRadians(i);
                            double x = Math.cos(rad) * 1.5;
                            double z = Math.sin(rad) * 1.5;
                            
                            target.getWorld().spawnParticle(
                                Particle.SPELL_WITCH,
                                target.getLocation().clone().add(x, 1.5, z),
                                0, 0, 0, 0, 1
                            );
                        }
                        
                        // Remove after duration
                        player.getServer().getScheduler().runTaskLater(PhantomSMP.getInstance(), () -> {
                            target.removeMetadata("death_marked", PhantomSMP.getInstance());
                        }, duration);
                        
                        target.playSound(target.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.5f);
                    });
            }
        );
    }
    
    public static BookAbility createSoulHarvest() {
        return new BookAbility(
            "Soul Harvest",
            "§7Execute enemy if below §c8 hearts",
            40, 35, 30,
            Particle.DAMAGE_INDICATOR,
            Sound.ENTITY_WITHER_DEATH,
            (player, level) -> {
                double threshold = level == 1 ? 16 : level == 2 ? 18 : 20; // hearts * 2
                double damage = level == 1 ? 20 : level == 2 ? 25 : 30;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .map(e -> (Player) e)
                    .filter(target -> target.getHealth() <= threshold)
                    .findFirst()
                    .ifPresent(target -> {
                        target.damage(damage, player);
                        
                        // Grim reaper effect
                        for (int i = 0; i < 50; i++) {
                            double y = i * 0.2;
                            target.getWorld().spawnParticle(
                                Particle.SOUL,
                                target.getLocation().clone().add(0, y, 0),
                                0, 0, 0, 0, 0.1
                            );
                        }
                        
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);
                    });
            }
        );
    }
}
