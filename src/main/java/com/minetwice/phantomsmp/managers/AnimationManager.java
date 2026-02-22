package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

public class AnimationManager {
    
    private final PhantomSMP plugin;
    
    public AnimationManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void playSwipeAnimation(Player player) {
        Location loc = player.getLocation();
        
        // Play sound
        player.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.5f);
        
        // Create swipe particles
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks > 10) {
                    cancel();
                    return;
                }
                
                double progress = ticks / 10.0;
                double angle = progress * 2 * Math.PI;
                
                // Create circular swipe
                for (int i = 0; i < 5; i++) {
                    double offset = i * 0.5;
                    double x = Math.cos(angle + offset) * 2;
                    double z = Math.sin(angle + offset) * 2;
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        loc.clone().add(x, 1, z),
                        0, 0, 0, 0, 0
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    public void playGemSwapAnimation(Player player1, Player player2) {
        // Freeze players
        Location loc1 = player1.getLocation();
        Location loc2 = player2.getLocation();
        
        player1.setWalkSpeed(0);
        player2.setWalkSpeed(0);
        
        // Play cinematic exchange
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks > 40) { // 2 seconds
                    player1.setWalkSpeed(0.2f);
                    player2.setWalkSpeed(0.2f);
                    cancel();
                    return;
                }
                
                // Create particle link between players
                Location middle = loc1.clone().add(loc2).multiply(0.5);
                
                for (double t = 0; t <= 1; t += 0.1) {
                    Location point = loc1.clone().add(loc2.clone().subtract(loc1).multiply(t));
                    player1.getWorld().spawnParticle(Particle.PORTAL, point, 0, 0, 0, 0, 0);
                }
                
                // Spiral around middle
                double radius = 2;
                double angle = ticks * 0.3;
                
                for (int i = 0; i < 3; i++) {
                    double x = radius * Math.cos(angle + (i * 2 * Math.PI / 3));
                    double z = radius * Math.sin(angle + (i * 2 * Math.PI / 3));
                    
                    player1.getWorld().spawnParticle(
                        Particle.END_ROD,
                        middle.clone().add(x, 1, z),
                        0, 0, 0, 0, 0
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        // Play sound
        player1.playSound(player1.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0f, 0.5f);
        player2.playSound(player2.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0f, 0.5f);
    }
    
    public void playLevelUpAnimation(Player player) {
        // Title
        player.showTitle(Title.title(
            Component.text("§6§lLEVEL UP!"),
            Component.text("§eYour power grows..."),
            Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
        ));
        
        // Sound
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        
        // Particles
        Location loc = player.getLocation().add(0, 1, 0);
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks > 30) {
                    cancel();
                    return;
                }
                
                double radius = ticks * 0.2;
                
                for (int i = 0; i < 8; i++) {
                    double angle = (i * 2 * Math.PI / 8) + (ticks * 0.1);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.TOTEM_OF_UNDYING,
                        loc.clone().add(x, ticks * 0.1, z),
                        0, 0, 0, 0, 0
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
