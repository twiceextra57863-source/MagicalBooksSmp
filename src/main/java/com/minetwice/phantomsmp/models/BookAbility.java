package com.minetwice.phantomsmp.models;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BookAbility {
    
    private final String name;
    private final String description;
    private final int baseCooldown;
    private final int level2Cooldown;
    private final int level3Cooldown;
    private final Particle particle;
    private final Sound sound;
    private final AbilityExecutor executor;
    
    public BookAbility(String name, String description, int baseCooldown, int level2Cooldown, 
                       int level3Cooldown, Particle particle, Sound sound, AbilityExecutor executor) {
        this.name = name;
        this.description = description;
        this.baseCooldown = baseCooldown;
        this.level2Cooldown = level2Cooldown;
        this.level3Cooldown = level3Cooldown;
        this.particle = particle;
        this.sound = sound;
        this.executor = executor;
    }
    
    public int getCooldown(int level) {
        return switch (level) {
            case 1 -> baseCooldown;
            case 2 -> level2Cooldown;
            case 3 -> level3Cooldown;
            default -> baseCooldown;
        };
    }
    
    public String getName() { 
        return name; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public Particle getParticle() { 
        return particle; 
    }
    
    public Sound getSound() { 
        return sound; 
    }
    
    public AbilityExecutor getExecutor() { 
        return executor; 
    }
    
    @FunctionalInterface
    public interface AbilityExecutor {
        void execute(Player player, int level);
    }
}
