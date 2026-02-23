package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class GraceManager {
    
    private final PhantomSMP plugin;
    private boolean gracePeriod = false;
    private int graceTimeRemaining = 0;
    private BukkitRunnable countdownTask;
    
    public GraceManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startGracePeriod() {
        this.gracePeriod = true;
        this.graceTimeRemaining = plugin.getConfig().getInt("grace.duration", 600); // 10 minutes default
        
        // Disable PvP in all worlds
        for (World world : Bukkit.getWorlds()) {
            world.setPVP(false);
        }
        
        // Start countdown
        startCountdown();
        
        // Broadcast start
        Bukkit.broadcastMessage(MessageUtils.format("&a&lGrace period started! PvP is disabled for " + 
            (graceTimeRemaining / 60) + " minutes."));
    }
    
    private void startCountdown() {
        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (graceTimeRemaining <= 0) {
                    endGracePeriod();
                    cancel();
                    return;
                }
                
                // Update action bar for all players
                if (plugin.getConfig().getBoolean("grace.countdown-actionbar", true)) {
                    String timeFormatted = formatTime(graceTimeRemaining);
                    Bukkit.getOnlinePlayers().forEach(p -> 
                        p.sendActionBar(MessageUtils.format("&a&lGRACE PERIOD: &e&l" + timeFormatted))
                    );
                }
                
                graceTimeRemaining--;
            }
        };
        
        countdownTask.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void endGracePeriod() {
        this.gracePeriod = false;
        
        // Enable PvP in all worlds
        for (World world : Bukkit.getWorlds()) {
            world.setPVP(true);
        }
        
        // Announce end of grace period
        Bukkit.broadcastMessage(MessageUtils.format("&c&lGRACE PERIOD ENDED! PvP IS NOW ENABLED!"));
        Bukkit.getOnlinePlayers().forEach(p -> 
            p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f)
        );
    }
    
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    public boolean isGracePeriod() {
        return gracePeriod;
    }
    
    public int getGraceTimeRemaining() {
        return graceTimeRemaining;
    }
    
    public void cancelGracePeriod() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        gracePeriod = false;
        
        // Enable PvP
        for (World world : Bukkit.getWorlds()) {
            world.setPVP(true);
        }
    }
}
