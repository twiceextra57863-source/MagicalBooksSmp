package com.minetwice.phantomsmp.utils;

import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtils {
    public static void spawnParticle(Location loc, Particle particle, int count) {
        if (loc.getWorld() != null) {
            loc.getWorld().spawnParticle(particle, loc, count);
        }
    }
}