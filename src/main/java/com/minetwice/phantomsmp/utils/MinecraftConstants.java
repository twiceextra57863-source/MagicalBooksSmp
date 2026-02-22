package com.minetwice.phantomsmp.utils;

import org.bukkit.Particle;
import org.bukkit.potion.PotionEffectType;

public class MinecraftConstants {
    
    // Particle mappings for 1.21
    public static final Particle SPELL_MOB = Particle.valueOf("ENTITY_EFFECT");
    public static final Particle SPELL_INSTANT = Particle.valueOf("INSTANT_EFFECT");
    public static final Particle SPELL_WITCH = Particle.valueOf("WITCH");
    public static final Particle CRIT_MAGIC = Particle.valueOf("ENCHANTED_HIT");
    public static final Particle ENCHANTMENT_TABLE = Particle.valueOf("ENCHANT");
    public static final Particle SMOKE_LARGE = Particle.valueOf("LARGE_SMOKE");
    public static final Particle BLOCK_CRACK = Particle.valueOf("BLOCK");
    public static final Particle LEVITATION = Particle.valueOf("LEVITATION");
    
    // Potion effect mappings for 1.21
    public static final PotionEffectType DAMAGE_RESISTANCE = PotionEffectType.RESISTANCE;
}
