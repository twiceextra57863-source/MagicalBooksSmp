package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import com.minetwice.phantomsmp.utils.ParticleUtils;
import com.minetwice.phantomsmp.utils.SoundUtils;
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
        if (!event.hasItem()) return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || item.getType() != Material.WRITTEN_BOOK) return;
        if (!plugin.getGemManager().isPowerBook(item)) return;
        
        // Cancel default book interaction
        event.setCancelled(true);
        
        // Check if player is sneaking
        if (!player.isSneaking()) {
            // Idle particle effects when holding book
            plugin.getParticleManager().spawnIdleParticles(player);
            return;
        }
        
        // Get player's power book
        PowerBook book = plugin.getGemManager().getPlayerBook(player.getUniqueId());
        if (book == null) return;
        
        // Check if in grace period and abilities are disabled
        if (plugin.getGraceManager().isGracePeriod() && plugin.getConfig().getBoolean("grace.abilities-disabled", false)) {
            player.sendMessage(MessageUtils.format("&cAbilities are disabled during grace period!"));
            return;
        }
        
        // Handle ability activation
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Shift + Right Click = Ability 1
            activateAbility(player, book, 1);
        } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            // Shift + Left Click = Ability 2
            activateAbility(player, book, 2);
        }
    }
    
    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        
        // Check if player is sneaking and holding a power book
        if (player.isSneaking()) {
            ItemStack mainHand = event.getMainHandItem();
            ItemStack offHand = event.getOffHandItem();
            
            // Check if either hand has a power book
            if (plugin.getGemManager().isPowerBook(mainHand) || plugin.getGemManager().isPowerBook(offHand)) {
                event.setCancelled(true);
                
                // Shift + F = Ability 3
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
            // Show book info in action bar when selected
            PowerBook book = plugin.getGemManager().getPlayerBook(player.getUniqueId());
            if (book != null) {
                player.sendActionBar(MessageUtils.format(
                    "&d" + book.getName() + " &7| &eLevel " + book.getLevel() + 
                    " &7| &cKills: " + book.getKills() + "/" + (book.getLevel() == 1 ? 10 : 25)
                ));
            }
        }
    }
    
    private void activateAbility(Player player, PowerBook book, int abilityNumber) {
        // Check cooldown
        String cooldownKey = player.getUniqueId() + "_" + book.getId() + "_" + abilityNumber;
        if (plugin.getCooldownManager().isOnCooldown(cooldownKey)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(cooldownKey);
            player.sendActionBar(MessageUtils.format("&cAbility on cooldown: &e" + remaining + "s"));
            return;
        }
        
        // Get ability
        var ability = switch (abilityNumber) {
            case 1 -> book.getAbility1();
            case 2 -> book.getAbility2();
            case 3 -> book.getAbility3();
            default -> null;
        };
        
        if (ability == null) return;
        
        // Execute ability
        ability.getExecutor().execute(player, book.getLevel());
        
        // Set cooldown
        int cooldown = ability.getCooldown(book.getLevel());
        plugin.getCooldownManager().setCooldown(cooldownKey, cooldown);
        
        // Play effects
        if (ability.getSound() != null) {
            player.playSound(player.getLocation(), ability.getSound(), 1.0f, 1.0f);
        }
        
        // Start cooldown display
        startCooldownDisplay(player, cooldownKey, cooldown);
    }
    
    private void startCooldownDisplay(Player player, String cooldownKey, int totalCooldown) {
        new BukkitRunnable() {
            int remaining = totalCooldown;
            
            @Override
            public void run() {
                if (remaining <= 0 || !plugin.getCooldownManager().isOnCooldown(cooldownKey)) {
                    player.sendActionBar(MessageUtils.format("&aAbility ready!"));
                    cancel();
                    return;
                }
                
                player.sendActionBar(MessageUtils.format("&cCooldown: &e" + remaining + "s"));
                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
