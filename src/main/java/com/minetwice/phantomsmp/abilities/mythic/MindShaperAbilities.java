package com.minetwice.phantomsmp.abilities.mythic;

import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MindShaperAbilities {
    
    public static BookAbility createMindHaze() {
        return new BookAbility(
            "Mind Haze",
            "§7Blindness + Slowness for §b2 seconds",
            35, 30, 25,
            Particle.SPELL_MOB,
            Sound.ENTITY_VEX_CHARGE,
            (player, level) -> {
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                double radius = level == 1 ? 10 : level == 2 ? 12 : 15;
                
                // Purple haze effect
                for (int i = 0; i < 30; i++) {
                    player.getWorld().spawnParticle(
                        Particle.SPELL_MOB,
                        player.getLocation().clone().add(
                            (Math.random() - 0.5) * radius,
                            Math.random() * 3,
                            (Math.random() - 0.5) * radius
                        ),
                        0, 0.5, 0, 0.5, 1
                    );
                }
                
                // Apply effects
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0));
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 0));
                    });
                
                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.0f, 0.8f);
            }
        );
    }
    
    public static BookAbility createDarkFlames() {
        return new BookAbility(
            "Dark Flames",
            "§7Set target on fire with §8black flames",
            50, 45, 40,
            Particle.SOUL_FIRE_FLAME,
            Sound.ITEM_FLINTANDSTEEL_USE,
            (player, level) -> {
                int fireTicks = level == 1 ? 100 : level == 2 ? 120 : 160;
                Player target = getTargetPlayer(player, 20);
                
                if (target != null) {
                    target.setFireTicks(fireTicks);
                    
                    // Black flame pillar
                    for (int i = 0; i < 30; i++) {
                        double y = i * 0.2;
                        target.getWorld().spawnParticle(
                            Particle.SOUL_FIRE_FLAME,
                            target.getLocation().clone().add(0, y, 0),
                            0, 0, 0, 0, 0.1
                        );
                    }
                }
            }
        );
    }
    
    public static BookAbility createRealityPrison() {
        return new BookAbility(
            "Reality Prison",
            "§7Immobilize target for §b2 seconds",
            150, 130, 100,
            Particle.SPELL_INSTANT,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                Player target = getTargetPlayer(player, 15);
                
                if (target != null) {
                    int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                    
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 254));
                    target.setFreezeTicks(duration);
                    
                    // Prison cage effect
                    for (int i = 0; i < 8; i++) {
                        double angle = i * Math.PI / 4;
                        double x = Math.cos(angle) * 1.5;
                        double z = Math.sin(angle) * 1.5;
                        
                        for (double y = 0; y <= 3; y += 0.5) {
                            target.getWorld().spawnParticle(
                                Particle.SPELL_WITCH,
                                target.getLocation().clone().add(x, y, z),
                                0, 0, 0, 0, 1
                            );
                        }
                    }
                    
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.3f);
                }
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
