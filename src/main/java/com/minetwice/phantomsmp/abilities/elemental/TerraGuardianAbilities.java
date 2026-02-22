package com.minetwice.phantomsmp.abilities.elemental;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class TerraGuardianAbilities {
    
    public static BookAbility createSeismicSlam() {
        return new BookAbility(
            "Seismic Slam",
            "§7Knockback wave",
            35, 30, 25,
            Particle.BLOCK_CRACK,
            Sound.ENTITY_GENERIC_EXPLODE,
            (player, level) -> {
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                double knockback = level == 1 ? 1.5 : level == 2 ? 1.8 : 2.0;
                
                // Ground crack effect
                for (int i = 0; i < 360; i += 15) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.BLOCK_CRACK,
                        player.getLocation().clone().add(x, 0.2, z),
                        10, 0.2, 0, 0.2, Material.STONE.createBlockData()
                    );
                }
                
                // Knockback
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        Vector away = e.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                        e.setVelocity(away.multiply(knockback).setY(0.5));
                    });
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createStoneAegis() {
        return new BookAbility(
            "Stone Aegis",
            "§7Resistance I for 6 seconds",
            25, 22, 20,
            Particle.BLOCK_CRACK,
            Sound.BLOCK_STONE_PLACE,
            (player, level) -> {
                int duration = level == 1 ? 120 : level == 2 ? 140 : 160;
                int resistanceLevel = level == 1 ? 0 : level == 2 ? 0 : 1;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, resistanceLevel));
                
                // Stone armor effect
                for (int i = 0; i < 50; i++) {
                    double angle = i * Math.PI * 2 / 25;
                    double x = Math.cos(angle) * 1.2;
                    double z = Math.sin(angle) * 1.2;
                    
                    player.getWorld().spawnParticle(
                        Particle.BLOCK_CRACK,
                        player.getLocation().clone().add(x, 1 + Math.sin(angle) * 0.3, z),
                        0, 0, 0, 0, Material.STONE.createBlockData()
                    );
                }
                
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_PLACE, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createPrisonOfEarth() {
        return new BookAbility(
            "Prison of Earth",
            "§7Surround target with temporary blocks",
            30, 27, 25,
            Particle.BLOCK_CRACK,
            Sound.BLOCK_STONE_BREAK,
            (player, level) -> {
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                
                player.getNearbyEntities(5, 5, 5).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .findFirst()
                    .ifPresent(e -> {
                        Player target = (Player) e;
                        org.bukkit.Location loc = target.getLocation();
                        
                        // Create temporary blocks
                        Material prisonMaterial = Material.STONE;
                        java.util.List<org.bukkit.block.Block> blocks = new java.util.ArrayList<>();
                        
                        for (int y = 0; y <= 2; y++) {
                            for (int x = -1; x <= 1; x++) {
                                for (int z = -1; z <= 1; z++) {
                                    if (x == 0 && y == 1 && z == 0) continue; // Leave center empty
                                    
                                    org.bukkit.block.Block block = loc.clone().add(x, y, z).getBlock();
                                    if (block.getType() == Material.AIR) {
                                        block.setType(prisonMaterial);
                                        blocks.add(block);
                                    }
                                }
                            }
                        }
                        
                        // Remove after duration
                        player.getServer().getScheduler().runTaskLater(
                            com.minetwice.phantomsmp.PhantomSMP.getInstance(),
                            () -> blocks.forEach(b -> b.setType(Material.AIR)),
                            duration
                        );
                        
                        // Effect
                        target.getWorld().spawnParticle(
                            Particle.BLOCK_CRACK,
                            target.getLocation(),
                            50, 1, 2, 1, Material.STONE.createBlockData()
                        );
                    });
            }
        );
    }
}
