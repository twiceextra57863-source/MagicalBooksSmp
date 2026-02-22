package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParticleManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, BukkitRunnable> activeParticles = new HashMap<>();
    private double tps = 20.0;
    
    public ParticleManager(PhantomSMP plugin) {
        this.plugin = plugin;
        
        // Monitor TPS
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            tps = plugin.getServer().getTPS()[0];
        }, 100L, 100L);
    }
    
    public void spawnIdleParticles(Player player) {
        if (!plugin.getConfig().getBoolean("particles.enabled", true)) return;
        if (tps < plugin.getConfig().getDouble("particles.tps-limit", 18.0)) return;
        
        UUID playerId = player.getUniqueId();
        
        // Cancel existing particle task
        if (activeParticles.containsKey(playerId)) {
            activeParticles.get(playerId).cancel();
        }
        
        double intensity = plugin.getConfig().getDouble("particles.intensity", 1.0);
        int maxParticles = plugin.getConfig().getInt("particles.max-particles-per-tick", 100);
        
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !player.isValid()) {
                    cancel();
                    activeParticles.remove(playerId);
                    return;
                }
                
                // Check if holding power book
                if (plugin.getGemManager().isPowerBook(player.getInventory().getItemInMainHand()) ||
                    plugin.getGemManager().isPowerBook(player.getInventory().getItemInOffHand())) {
                    
                    Location loc = player.getLocation().add(0, 1, 0);
                    int count = (int) (10 * intensity);
                    
                    // Spiral particles
                    double radius = 0.5;
                    for (int i = 0; i < count && i < maxParticles; i++) {
                        double angle = (i * 2 * Math.PI / count) + (System.currentTimeMillis() * 0.01);
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            loc.clone().add(x, Math.sin(angle) * 0.1, z),
                            0, 0, 0, 0, 0
                        );
                    }
                }
            }
        };
        
        task.runTaskTimer(plugin, 0L, 2L);
        activeParticles.put(playerId, task);
    }
    
    public void spawnCinematicParticles(Location location, Particle particle, int count, double radius) {
        if (!plugin.getConfig().getBoolean("particles.enabled", true)) return;
        if (tps < plugin.getConfig().getDouble("particles.tps-limit", 18.0)) return;
        
        double intensity = plugin.getConfig().getDouble("particles.intensity", 1.0);
        int maxParticles = plugin.getConfig().getInt("particles.max-particles-per-tick", 100);
        
        int adjustedCount = (int) (count * intensity);
        if (adjustedCount > maxParticles) adjustedCount = maxParticles;
        
        // Create sphere of particles
        for (int i = 0; i < adjustedCount; i++) {
            double theta = 2 * Math.PI * Math.random();
            double phi = Math.acos(2 * Math.random() - 1);
            
            double x = radius * Math.sin(phi) * Math.cos(theta);
            double y = radius * Math.sin(phi) * Math.sin(theta);
            double z = radius * Math.cos(phi);
            
            location.getWorld().spawnParticle(
                particle,
                location.clone().add(x, y, z),
                0, 0, 0, 0, 0
            );
        }
    }
    
    public void stopParticles(Player player) {
        UUID playerId = player.getUniqueId();
        if (activeParticles.containsKey(playerId)) {
            activeParticles.get(playerId).cancel();
            activeParticles.remove(playerId);
        }
    }
}
