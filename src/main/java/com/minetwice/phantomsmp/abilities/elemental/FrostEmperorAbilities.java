package com.minetwice.phantomsmp.abilities.elemental;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FrostEmperorAbilities {
    
    public static BookAbility createIceShard() {
        return new BookAbility(
            "Ice Shard",
            "§7Snowball that slows target",
            25, 22, 20,
            Particle.SNOWFLAKE,
            Sound.ENTITY_SNOWBALL_THROW,
            (player, level) -> {
                Snowball snowball = player.launchProjectile(Snowball.class);
                snowball.setCustomName("ICE_SHARD");
                
                // Store slow duration in metadata
                int slowDuration = level == 1 ? 40 : level == 2 ? 60 : 80;
                snowball.setMetadata("slow_duration", 
                    new org.bukkit.metadata.FixedMetadataValue(
                        com.minetwice.phantomsmp.PhantomSMP.getInstance(), slowDuration));
                
                player.playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.0f, 1.2f);
            }
        );
    }
    
    public static BookAbility createFreezingAura() {
        return new BookAbility(
            "Freezing Aura",
            "§7Slowness on nearby enemies",
            25, 22, 20,
            Particle.SNOWFLAKE,
            Sound.BLOCK_POWDER_SNOW_BREAK,
            (player, level) -> {
                int radius = level == 1 ? 3 : level == 2 ? 4 : 5;
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                int slowLevel = level == 1 ? 0 : level == 2 ? 0 : 1;
                
                // Freeze particles
                for (int i = 0; i < 360; i += 30) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                // Apply slow
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).addPotionEffect(
                        new PotionEffect(PotionEffectType.SLOWNESS, duration, slowLevel)));
                
                player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createPermafrost() {
        return new BookAbility(
            "Permafrost",
            "§7Slowness + Weakness AoE",
            35, 30, 25,
            Particle.SNOWFLAKE,
            Sound.ENTITY_BREEZE_WIND_BURST,
            (player, level) -> {
                int radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                // Blizzard effect
                for (int i = 0; i < 200; i++) {
                    double angle = Math.random() * Math.PI * 2;
                    double r = Math.random() * radius;
                    double x = Math.cos(angle) * r;
                    double z = Math.sin(angle) * r;
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        player.getLocation().clone().add(x, Math.random() * 3, z),
                        0, 0, 0, 0, 0.05
                    );
                }
                
                // Apply effects
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 0));
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, 0));
                    });
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 1.2f);
            }
        );
    }
}
