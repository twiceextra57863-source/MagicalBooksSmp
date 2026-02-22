package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GemProtectionListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public GemProtectionListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (plugin.getGemManager().isPowerBook(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MessageUtils.format("&cYou cannot drop power books!"));
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(item -> plugin.getGemManager().isPowerBook(item));
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        ItemStack hotbar = event.getHotbarButton() != -1 ? 
            player.getInventory().getItem(event.getHotbarButton()) : null;
        
        // Check if trying to move power book to non-player inventory
        if (event.getInventory().getType() != InventoryType.PLAYER && 
            event.getInventory().getType() != InventoryType.CRAFTING) {
            
            if (plugin.getGemManager().isPowerBook(current) || 
                plugin.getGemManager().isPowerBook(cursor) ||
                plugin.getGemManager().isPowerBook(hotbar)) {
                event.setCancelled(true);
                player.sendMessage(MessageUtils.format("&cPower books cannot be stored in containers!"));
            }
        }
    }
    
    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (plugin.getGemManager().isPowerBook(event.getItem())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        // This is handled by click event, but added for extra protection
    }
    
    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        if (plugin.getGemManager().isPowerBook(event.getItem().getItemStack())) {
            Player player = event.getPlayer();
            if (plugin.getGemManager().hasPowerBook(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(MessageUtils.format("&cYou already have a power book!"));
            }
        }
    }
    
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result != null && plugin.getGemManager().isPowerBook(result)) {
            event.setResult(null);
        }
    }
    
    @EventHandler
    public void onPrepareGrindstone(PrepareGrindstoneEvent event) {
        ItemStack result = event.getResult();
        if (result != null && plugin.getGemManager().isPowerBook(result)) {
            event.setResult(null);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && plugin.getGemManager().isPowerBook(event.getItem())) {
            // Prevent using book in crafting or other interactions
            event.setCancelled(true);
        }
    }
}
