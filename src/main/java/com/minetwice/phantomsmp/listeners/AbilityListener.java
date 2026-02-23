package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class AbilityListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public AbilityListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || item.getType() != Material.WRITTEN_BOOK) {
            return;
        }
        
        if (!plugin.getGemManager().isPowerBook(item)) {
            return;
        }
        
        event.setCancelled(true);
        
        if (!player.isSneaking()) {
            plugin.getParticleManager().spawnIdleParticles(player);
            return;
        }
        
        PowerBook book = plugin.getGemManager().getPlayerBook(player.getUniqueId());
        if (book == null) {
            return;
        }
        
        if (plugin.getGraceManager().isGracePeriod()) {
            player.sendMessage(MessageUtils.colorize("&cAbilities are disabled during grace period!"));
            return;
        }
        
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            activateAbility(player, book, 1);
        } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            activateAbility(player, book, 2);
        }
    }
    
    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        
        if (player.isSneaking()) {
            ItemStack mainHand = event.getMainHandItem();
            ItemStack offHand = event.getOffHandItem();
            
            if (plugin.getGemManager().isPowerBook(mainHand) || plugin.getGemManager().isPowerBook(offHand)) {
                event.setCancelled(true);
                
                PowerBook book = plugin.getGemManager().getPlayerBook(player.getUniqueId());
                if (book != null) {
                    activateAbility(player, book, 3);
                }
            }
        }
    }
    
    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        
        if (newItem != null && plugin.getGemManager().isPowerBook(newItem)) {
            PowerBook book = plugin.getGemManager().getPlayerBook(player.getUniqueId());
            if (book != null) {
                String message = "&d" + book.getName() + " &7| &eLevel " + book.getLevel() + 
                                 " &7| &cKills: " + book.getKills() + "/" + 
                                 (book.getLevel() == 1 ? 10 : (book.getLevel() == 2 ? 25 : 0));
                player.sendActionBar(MessageUtils.colorize(message));
            }
        }
    }
    
    private void activateAbility(Player player, PowerBook book, int abilityNumber) {
        String cooldownKey = player.getUniqueId().toString() + "_" + book.getId() + "_" + abilityNumber;
        
        if (plugin.getCooldownManager().isOnCooldown(cooldownKey)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(cooldownKey);
            player.sendActionBar(MessageUtils.colorize("&cAbility on cooldown: &e" + remaining + "s"));
            return;
        }
        
        BookAbility ability = null;
        switch (abilityNumber) {
            case 1:
                ability = book.getAbility1();
                break;
            case 2:
                ability = book.getAbility2();
                break;
            case 3:
                ability = book.getAbility3();
                break;
            default:
                return;
        }
        
        if (ability == null) {
            return;
        }
        
        try {
            ability.getExecutor().execute(player, book.getLevel());
            
            int cooldown = ability.getCooldown(book.getLevel());
            plugin.getCooldownManager().setCooldown(cooldownKey, cooldown);
            
            if (ability.getSound() != null) {
                player.playSound(player.getLocation(), ability.getSound(), 1.0f, 1.0f);
            }
            
            startCooldownDisplay(player, cooldownKey, cooldown);
            
        } catch (Exception e) {
            player.sendMessage(MessageUtils.colorize("&cAbility execution failed!"));
            e.printStackTrace();
        }
    }
    
    private void startCooldownDisplay(Player player, String cooldownKey, int totalCooldown) {
        new BukkitRunnable() {
            int remaining = totalCooldown;
            
            @Override
            public void run() {
                if (remaining <= 0 || !plugin.getCooldownManager().isOnCooldown(cooldownKey)) {
                    player.sendActionBar(MessageUtils.colorize("&aAbility ready!"));
                    cancel();
                    return;
                }
                
                player.sendActionBar(MessageUtils.colorize("&cCooldown: &e" + remaining + "s"));
                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
