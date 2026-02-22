package com.minetwice.phantomsmp.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleUtils {
    
    public static void spawnAwakeningParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        // Burst effect
        player.getWorld().spawnParticle(Particle.FLASH, loc, 1, 0, 0, 0, 0);
        
        // Spiral up
        for (int i = 0; i < 360; i += 10) {
            double radians = Math.toRadians(i);
            double x = Math.cos(radians) * 2;
            double z = Math.sin(radians) * 2;
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, 0, z),
                0, 0, 0.5, 0, 0.1
            );
        }
        
        // Big explosion effect
        for (int i = 0; i < 50; i++) {
            double x = (Math.random() - 0.5) * 3;
            double y = Math.random() * 4;
            double z = (Math.random() - 0.5) * 3;
            
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                loc.clone().add(x, y, z),
                0, 0, 0, 0, 0.5
            );
        }
    }
    
    public static void spawnIdleBookParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        // Orbiting particles
        double time = System.currentTimeMillis() * 0.005;
        
        for (int i = 0; i < 4; i++) {
            double angle = time + (i * Math.PI / 2);
            double x = Math.cos(angle) * 0.8;
            double z = Math.sin(angle) * 0.8;
            
            player.getWorld().spawnParticle(
                Particle.ENCHANT,
                loc.clone().add(x, 0.5 + Math.sin(time + i) * 0.2, z),
                0, 0, 0, 0, 1
            );
        }
    }
}
