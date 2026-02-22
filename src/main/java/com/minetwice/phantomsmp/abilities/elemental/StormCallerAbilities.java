package com.minetwice.phantomsmp.abilities.elemental;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StormCallerAbilities {
    
    public static BookAbility createChainLightning() {
        return new BookAbility(
            "Chain Lightning",
            "§7Strike lightning on target",
            35, 30, 25,
            Particle.ELECTRIC_SPARK,
            Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
            (player, level) -> {
                double radius = level == 1 ? 10 : level == 2 ? 12 : 15;
                
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .findFirst()
                    .ifPresent(e -> {
                        Player target = (Player) e;
                        player.getWorld().strikeLightning(target.getLocation());
                        
                        // Chain to nearby
                        target.getNearbyEntities(3, 3, 3).stream()
                            .filter(e2 -> e2 instanceof Player && !e2.equals(player) && !e2.equals(target))
                            .limit(level)
                            .forEach(e2 -> {
                                player.getWorld().strikeLightning(e2.getLocation());
                            });
                    });
            }
        );
    }
    
    public static BookAbility createStaticCharge() {
        return new BookAbility(
            "Static Charge",
            "§7Speed II for 5 seconds",
            25, 22, 20,
            Particle.ELECTRIC_SPARK,
            Sound.BLOCK_BEACON_POWER_SELECT,
            (player, level) -> {
                int duration = level == 1 ? 100 : level == 2 ? 120 : 140;
                int speedLevel = level == 1 ? 1 : level == 2 ? 1 : 2;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, speedLevel));
                
                // Electric aura
                for (int i = 0; i < 50; i++) {
                    double angle = i * Math.PI * 2 / 25;
                    double x = Math.cos(angle) * 1.5;
                    double z = Math.sin(angle) * 1.5;
                    
                    player.getWorld().spawnParticle(
                        Particle.ELECTRIC_SPARK,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.5f);
            }
        );
    }
    
    public static BookAbility createThunderDome() {
        return new BookAbility(
            "Thunder Dome",
            "§7Circle of lightning strikes",
            40, 35, 30,
            Particle.ELECTRIC_SPARK,
            Sound.ENTITY_LIGHTNING_BOLT_IMPACT,
            (player, level) -> {
                int radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                int strikes = level == 1 ? 8 : level == 2 ? 10 : 12;
                
                // Circle of lightning
                for (int i = 0; i < strikes; i++) {
                    double angle = i * (Math.PI * 2 / strikes);
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    
                    final int delay = i * 2;
                    player.getServer().getScheduler().runTaskLater(
                        com.minetwice.phantomsmp.PhantomSMP.getInstance(),
                        () -> player.getWorld().strikeLightning(
                            player.getLocation().clone().add(x, 0, z)
                        ),
                        delay
                    );
                }
                
                // Electric dome
                for (int i = 0; i < 360; i += 15) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    for (double y = 0; y <= 3; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.ELECTRIC_SPARK,
                            player.getLocation().clone().add(x, y, z),
                            0, 0, 0, 0, 0.1
                        );
                    }
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 0.8f);
            }
        );
    }
}
