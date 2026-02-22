package com.minetwice.phantomsmp.abilities.mythic;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class SageWarriorAbilities {
    
    public static BookAbility createEnergyRush() {
        return new BookAbility(
            "Energy Rush",
            "§7Forward dash with §benergy trail",
            20, 18, 15,
            Particle.SONIC_BOOM,
            Sound.ENTITY_BREEZE_JUMP,
            (player, level) -> {
                double power = level == 1 ? 1.5 : level == 2 ? 2.0 : 2.5;
                Vector direction = player.getLocation().getDirection().setY(0.3).normalize();
                player.setVelocity(direction.multiply(power));
                
                // Energy trail
                for (int i = 0; i < 15; i++) {
                    Vector offset = direction.clone().multiply(-i * 0.5);
                    player.getWorld().spawnParticle(
                        Particle.SONIC_BOOM,
                        player.getLocation().clone().add(offset),
                        2, 0.2, 0.2, 0.2, 0.01
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 1.0f, 1.2f);
            }
        );
    }
    
    public static BookAbility createSpiralStrike() {
        return new BookAbility(
            "Spiral Strike",
            "§7Next hit deals §c+5 hearts",
            40, 35, 30,
            Particle.END_ROD,
            Sound.ENTITY_PLAYER_ATTACK_SWEEP,
            (player, level) -> {
                int damage = level == 1 ? 10 : level == 2 ? 12 : 14;
                
                player.setMetadata("spiral_charged", new org.bukkit.metadata.FixedMetadataValue(
                    PhantomSMP.getInstance(), damage));
                
                // Spiral effect
                for (int i = 0; i < 30; i++) {
                    double angle = i * Math.PI * 2 / 15;
                    double radius = 0.8;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        player.getLocation().clone().add(x, 1.2, z),
                        0, 0, 0.1, 0, 0.05
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createTranscendentForm() {
        return new BookAbility(
            "Transcendent Form",
            "§7Strength II + Speed I for §b8 seconds",
            120, 100, 80,
            Particle.TOTEM_OF_UNDYING,
            Sound.ENTITY_WITHER_SPAWN,
            (player, level) -> {
                int duration = level == 1 ? 160 : level == 2 ? 200 : 240;
                int strengthLevel = level == 1 ? 1 : level == 2 ? 1 : 2;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, strengthLevel));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 0));
                
                // Ascension effect
                for (int i = 0; i < 100; i++) {
                    double angle = i * Math.PI * 2 / 50;
                    double radius = 3;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.TOTEM_OF_UNDYING,
                        player.getLocation().clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        0, 0, 0, 0, 1
                    );
                }
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.5f);
            }
        );
    }
}
