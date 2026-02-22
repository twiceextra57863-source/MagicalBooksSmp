package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public PlayerJoinListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        
        // Load player data
        plugin.getDataManager().loadPlayerData(playerUUID);
        
        // Check if SMP has started and player doesn't have a gem
        if (plugin.getGraceManager().isGracePeriod() && !plugin.getGemManager().hasPowerBook(playerUUID)) {
            giveRandomGem(player);
        }
        
        // Start idle particles if holding gem
        plugin.getParticleManager().spawnIdleParticles(player);
    }
    
    private void giveRandomGem(Player player) {
        // Get all currently assigned gems
        List<PowerBook> availableBooks = plugin.getGemManager().getAllPowerBooks().stream()
            .filter(book -> !isGemAssigned(book.getId()))
            .toList();
        
        if (!availableBooks.isEmpty()) {
            PowerBook randomBook = availableBooks.get((int) (Math.random() * availableBooks.size()));
            player.getInventory().addItem(randomBook.createBookItem());
            plugin.getGemManager().setPlayerBook(player.getUniqueId(), randomBook);
            
            player.sendMessage("§aYou received a power book: §d" + randomBook.getName());
        }
    }
    
    private boolean isGemAssigned(String bookId) {
        return plugin.getGemManager().getAllPowerBooks().stream()
            .anyMatch(book -> book.getId().equals(bookId) && 
                plugin.getGemManager().getPlayerBook(book.getOwnerUUID()) != null);
    }
}
