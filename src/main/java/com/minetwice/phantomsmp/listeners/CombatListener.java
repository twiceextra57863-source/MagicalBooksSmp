package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CombatListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public CombatListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        if (killer != null) {
            // Check if killer has a power book
            PowerBook killerBook = plugin.getGemManager().getPlayerBook(killer.getUniqueId());
            
            if (killerBook != null) {
                // Add kill
                int oldLevel = killerBook.getLevel();
                killerBook.addKill();
                
                // Check for level up
                if (killerBook.getLevel() > oldLevel) {
                    plugin.getAnimationManager().playLevelUpAnimation(killer);
                }
                
                // Save data
                plugin.getDataManager().savePlayerData(killer.getUniqueId());
                
                // Send message
                killer.sendMessage("§a+1 Kill! §7Total: §e" + killerBook.getKills());
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof Player victim) {
            
            // Check if in grace period
            if (plugin.getGraceManager().isGracePeriod()) {
                event.setCancelled(true);
                return;
            }
            
            // Apply PvP damage reduction from config
            double reduction = plugin.getConfig().getDouble("damage.pvp-reduction", 1.0);
            event.setDamage(event.getDamage() * reduction);
        }
    }
}
