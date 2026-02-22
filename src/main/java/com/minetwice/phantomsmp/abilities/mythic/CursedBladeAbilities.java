package com.minetwice.phantomsmp.abilities.mythic;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CursedBladeAbilities {
    
    public static BookAbility createSeveringStrike() {
        return new BookAbility(
            "Severing Strike",
            "§7Next attack deals §c+4 hearts",
            25, 22, 20,
            Particle.SWEEP_ATTACK,
            Sound.ENTITY_PLAYER_ATTACK_CRIT,
            (player, level) -> {
                int damage = level == 1 ? 8 : level == 2 ? 10 : 12;
                
                player.setMetadata("severing_charged", new org.bukkit.metadata.FixedMetadataValue(
                    PhantomSMP.getInstance(), damage));
                
                // Slash lines
                Vector direction = player.getLocation().getDirection();
                for (int i = 0; i < 8; i++) {
                    double offset = i * 0.5;
                    Vector pos = direction.clone().multiply(offset);
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        player.getLocation().clone().add(pos).add(0, 1, 0),
                        0, 0, 0, 0, 0
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createRendingSlash() {
        return new BookAbility(
            "Rending Slash",
            "§7AoE damage in §c4 block §7radius",
            60, 55, 50,
            Particle.SWEEP_ATTACK,
            Sound.ENTITY_PLAYER_ATTACK_SWEEP,
            (player, level) -> {
                double damage = level == 1 ? 8 : level == 2 ? 10 : 12;
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                
                // Circular slash
                for (int i = 0; i < 360; i += 15) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.ENCHANTED_HIT,  // Replaced CRIT_MAGIC
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0
                    );
                }
                
                // Damage
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).damage(damage, player));
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createCursedSanctuary() {
        return new BookAbility(
            "Cursed Sanctuary",
            "§7Damage aura for §c6 seconds",
            180, 160, 140,
            Particle.SOUL,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                double damage = level == 1 ? 4 : level == 2 ? 5 : 6;
                double radius = level == 1 ? 6 : level == 2 ? 7 : 8;
                int duration = level == 1 ? 120 : level == 2 ? 140 : 160;
                
                // Create cursed circle
                for (int i = 0; i < 360; i += 10) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        player.getLocation().clone().add(x, 0.5, z),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                // Damage over time
                int[] taskId = new int[1];
                taskId[0] = player.getServer().getScheduler().scheduleSyncRepeatingTask(PhantomSMP.getInstance(), () -> {
                    player.getNearbyEntities(radius, radius, radius).stream()
                        .filter(e -> e instanceof Player && !e.equals(player))
                        .forEach(e -> ((Player) e).damage(damage, player));
                }, 0L, 20L);
                
                // Cancel after duration
                player.getServer().getScheduler().scheduleSyncDelayedTask(PhantomSMP.getInstance(), () -> {
                    player.getServer().getScheduler().cancelTask(taskId[0]);
                }, duration);
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.3f);
            }
        );
    }
}
