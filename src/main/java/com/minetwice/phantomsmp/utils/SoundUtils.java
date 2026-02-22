package com.minetwice.phantomsmp.utils;

import org.bukkit.Location;
import org.bukkit.Sound;

public class SoundUtils {
    public static void playSound(Location loc, Sound sound, float volume, float pitch) {
        if (loc.getWorld() != null) {
            loc.getWorld().playSound(loc, sound, volume, pitch);
        }
    }
}