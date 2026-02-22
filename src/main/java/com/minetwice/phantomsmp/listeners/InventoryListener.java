package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public InventoryListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        String title = event.getView().getTitle();
        
        // Handle books menu
        if (title.contains("§5§lPower Books Menu")) {
            event.setCancelled(true);
            
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
                ItemStack clicked = event.getCurrentItem();
                String bookName = clicked.getItemMeta().getDisplayName();
                
                // Find book by name (simplified - in production use PersistentDataContainer)
                PowerBook book = plugin.getGemManager().getAllPowerBooks().stream()
                    .filter(b -> ("§c" + b.getName()).equals(bookName) || 
                                 ("§7" + b.getName()).equals(bookName) || 
                                 ("§b" + b.getName()).equals(bookName))
                    .findFirst()
                    .orElse(null);
                
                if (book != null) {
                    player.getInventory().addItem(book.createBookItem());
                    player.sendMessage(MessageUtils.format("&aYou received " + book.getName()));
                    player.closeInventory();
                }
            }
        }
    }
    
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        // Prevent dragging power books into non-player inventories
        for (ItemStack item : event.getNewItems().values()) {
            if (plugin.getGemManager().isPowerBook(item)) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
