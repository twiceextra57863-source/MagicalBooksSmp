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
    
    private GravityLordAbilities() {
        // Private constructor to prevent instantiation
    }
    
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
                
                // Gravity field effect
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
                
                // Apply slow effect to nearby players
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        Player target = (Player) e;
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, slowLevel));
                    });
                
                player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 1.0f, 0.5f);
            }
        );
    }
package com.minetwice.phantomsmp.abilities.elemental;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GravityLordAbilities {
    
    public static BookAbility createWeightOfTheWorld() {
        return new BookAbility(
            "Weight of the World",
            "Slowness IV for 2 seconds",
            35, 30, 25,
            Particle.CRIT,
            Sound.ENTITY_IRON_GOLEM_HURT,
            (player, level) -> {
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 3));
                player.sendMessage(MessageUtils.format("&7Used &bWeight of the World"));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 1.0f, 1.0f);
            }
        );
    }
    
    public static BookAbility createZeroGravity() {
        return new BookAbility(
            "Zero Gravity",
            "Levitation for 1 second",
            25, 22, 20,
            Particle.END_ROD,
            Sound.ENTITY_SHULKER_SHOOT,
            (player, level) -> {
                int duration = level == 1 ? 20 : level == 2 ? 30 : 40;
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, duration, 0));
                player.sendMessage(MessageUtils.format("&7Used &bZero Gravity"));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 1.0f, 1.0f);
            }
        );
    }
    
    public static BookAbility createSingularity() {
        return new BookAbility(
            "Singularity",
            "Powerful gravity pull",
            40, 35, 30,
            Particle.ENCHANTED_HIT,
            Sound.ENTITY_GENERIC_EXPLODE,
            (player, level) -> {
                player.sendMessage(MessageUtils.format("&7Used &bSingularity"));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation(), 10, 1, 1, 1, 0);
            }
        );
    }
}
