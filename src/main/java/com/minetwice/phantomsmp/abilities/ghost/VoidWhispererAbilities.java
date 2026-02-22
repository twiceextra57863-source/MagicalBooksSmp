package com.minetwice.phantomsmp.abilities.ghost;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VoidWhispererAbilities {
    
    public static BookAbility createSilentTouch() {
        return new BookAbility(
            "Silent Touch",
            "§7Weakness I for §b4 seconds",
            35, 30, 25,
            Particle.ENTITY_EFFECT,  // Replaced SPELL_MOB
            Sound.ENTITY_VEX_CHARGE,
            (player, level) -> {
                int duration = level == 1 ? 80 : level == 2 ? 100 : 120;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                
                // Silent particles
                for (int i = 0; i < 30; i++) {
                    player.getWorld().spawnParticle(
                        Particle.ENTITY_EFFECT,
                        player.getLocation().clone().add(
                            (Math.random() - 0.5) * radius,
                            Math.random() * 2,
                            (Math.random() - 0.5) * radius
                        ),
                        0, 0.2, 0.2, 0.2, 1
                    );
                }
                
                // Apply weakness
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).addPotionEffect(
                        new PotionEffect(PotionEffectType.WEAKNESS, duration, 0)));
                
                player.playSound(player.getLocation(), Sound.ENTITY_VEX_CHARGE, 1.0f, 0.3f);
            }
        );
    }
    
    public static BookAbility createVoidStep() {
        return new BookAbility(
            "Void Step",
            "§7Invisibility for §b3 seconds",
            40, 35, 30,
            Particle.REVERSE_PORTAL,
            Sound.ENTITY_ENDERMAN_TELEPORT,
            (player, level) -> {
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 0));
                
                // Void particles
                for (int i = 0; i < 50; i++) {
                    player.getWorld().spawnParticle(
                        Particle.REVERSE_PORTAL,
                        player.getLocation().clone().add(
                            (Math.random() - 0.5) * 2,
                            Math.random() * 2,
                            (Math.random() - 0.5) * 2
                        ),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
            }
        );
    }
    
    public static BookAbility createAbyssCall() {
        return new BookAbility(
            "Abyss Call",
            "§7§c4 hearts §7damage + Blindness in §b5 block radius",
            150, 130, 100,
            Particle.SCULK_SOUL,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                double damage = level == 1 ? 8 : level == 2 ? 9 : 10;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                int blindDuration = level == 1 ? 40 : level == 2 ? 60 : 80;
                
                // Abyssal sound wave
                for (int i = 0; i < 360; i += 10) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    for (double y = 0; y <= 2; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.SCULK_SOUL,
                            player.getLocation().clone().add(x, y, z),
                            0, 0, 0, 0, 0.1
                        );
                    }
                }
                
                // Damage and blind
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        ((Player) e).damage(damage, player);
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindDuration, 0));
                    });
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.2f);
            }
        );
    }
}
