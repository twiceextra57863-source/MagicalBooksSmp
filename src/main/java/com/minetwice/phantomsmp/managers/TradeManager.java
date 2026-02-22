package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.models.TradeRequest;
import com.minetwice.phantomsmp.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, TradeRequest> pendingRequests = new HashMap<>();
    private final Map<UUID, BukkitRunnable> requestTimeouts = new HashMap<>();
    
    public TradeManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void sendTradeRequest(Player requester, Player target) {
        // Check if target already has pending request
        if (pendingRequests.containsKey(target.getUniqueId())) {
            requester.sendMessage(MessageUtils.format("&c" + target.getName() + " already has a pending trade request!"));
            return;
        }
        
        TradeRequest request = new TradeRequest(requester, target);
        pendingRequests.put(target.getUniqueId(), request);
        
        // Send request message with clickable buttons
        Component acceptButton = Component.text("                      [§a§lACCEPT§r]                      ")
            .clickEvent(ClickEvent.runCommand("/trade accept"))
            .hoverEvent(HoverEvent.showText(Component.text("§aClick to accept trade")));
            
        Component rejectButton = Component.text("                      [§c§lREJECT§r]                      ")
            .clickEvent(ClickEvent.runCommand("/trade reject"))
            .hoverEvent(HoverEvent.showText(Component.text("§cClick to reject trade")));
        
        requester.sendMessage(MessageUtils.format("&eTrade request sent to " + target.getName()));
        
        target.sendMessage(MessageUtils.format("&e" + requester.getName() + " wants to trade power books!"));
        target.sendMessage(acceptButton);
        target.sendMessage(rejectButton);
        
        // Set timeout
        int timeoutSeconds = plugin.getConfig().getInt("trade.timeout", 30);
        BukkitRunnable timeout = new BukkitRunnable() {
            @Override
            public void run() {
                if (pendingRequests.containsKey(target.getUniqueId())) {
                    pendingRequests.remove(target.getUniqueId());
                    requester.sendMessage(MessageUtils.format("&cTrade request to " + target.getName() + " timed out."));
                    target.sendMessage(MessageUtils.format("&cTrade request from " + requester.getName() + " timed out."));
                }
                requestTimeouts.remove(target.getUniqueId());
            }
        };
        
        timeout.runTaskLater(plugin, timeoutSeconds * 20L);
        requestTimeouts.put(target.getUniqueId(), timeout);
    }
    
    public void acceptTrade(TradeRequest request) {
        Player requester = request.getRequester();
        Player target = request.getTarget();
        
        // Cancel timeout
        if (requestTimeouts.containsKey(target.getUniqueId())) {
            requestTimeouts.get(target.getUniqueId()).cancel();
            requestTimeouts.remove(target.getUniqueId());
        }
        
        // Remove pending request
        pendingRequests.remove(target.getUniqueId());
        
        // Check if both players still have books
        if (!plugin.getGemManager().hasPowerBook(requester.getUniqueId()) ||
            !plugin.getGemManager().hasPowerBook(target.getUniqueId())) {
            requester.sendMessage(MessageUtils.format("&cTrade failed: One of you no longer has a power book!"));
            target.sendMessage(MessageUtils.format("&cTrade failed: One of you no longer has a power book!"));
            return;
        }
        
        // Get books
        PowerBook requesterBook = plugin.getGemManager().getPlayerBook(requester.getUniqueId());
        PowerBook targetBook = plugin.getGemManager().getPlayerBook(target.getUniqueId());
        
        // Freeze players
        int freezeDuration = plugin.getConfig().getInt("trade.freeze-duration", 2);
        
        requester.setWalkSpeed(0);
        target.setWalkSpeed(0);
        
        // Play swap animation
        plugin.getAnimationManager().playGemSwapAnimation(requester, target);
        
        // Swap books after freeze
        new BukkitRunnable() {
            @Override
            public void run() {
                // Unfreeze
                requester.setWalkSpeed(0.2f);
                target.setWalkSpeed(0.2f);
                
                // Perform swap
                performSwap(requester, target, requesterBook, targetBook);
            }
        }.runTaskLater(plugin, freezeDuration * 20L);
    }
    
    public void rejectTrade(TradeRequest request) {
        Player requester = request.getRequester();
        Player target = request.getTarget();
        
        // Cancel timeout
        if (requestTimeouts.containsKey(target.getUniqueId())) {
            requestTimeouts.get(target.getUniqueId()).cancel();
            requestTimeouts.remove(target.getUniqueId());
        }
        
        // Remove pending request
        pendingRequests.remove(target.getUniqueId());
        
        requester.sendMessage(MessageUtils.format("&c" + target.getName() + " rejected your trade request."));
        target.sendMessage(MessageUtils.format("&aYou rejected the trade request."));
    }
    
    private void performSwap(Player requester, Player target, PowerBook requesterBook, PowerBook targetBook) {
        // Remove books from players
        removeBookFromPlayer(requester);
        removeBookFromPlayer(target);
        
        // Swap in manager
        plugin.getGemManager().setPlayerBook(requester.getUniqueId(), targetBook);
        plugin.getGemManager().setPlayerBook(target.getUniqueId(), requesterBook);
        
        // Give new books
        requester.getInventory().addItem(targetBook.createBookItem());
        target.getInventory().addItem(requesterBook.createBookItem());
        
        // Save data
        plugin.getDataManager().savePlayerData(requester.getUniqueId());
        plugin.getDataManager().savePlayerData(target.getUniqueId());
        
        // Send messages
        requester.sendMessage(MessageUtils.format("&aTrade complete! You received " + targetBook.getName()));
        target.sendMessage(MessageUtils.format("&aTrade complete! You received " + requesterBook.getName()));
        
        // Play sound
        requester.playSound(requester.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        target.playSound(target.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }
    
    private void removeBookFromPlayer(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && plugin.getGemManager().isPowerBook(item)) {
                item.setAmount(0);
            }
        }
        
        for (ItemStack item : player.getInventory().getExtraContents()) {
            if (item != null && plugin.getGemManager().isPowerBook(item)) {
                item.setAmount(0);
            }
        }
    }
    
    public TradeRequest getPendingRequest(Player player) {
        return pendingRequests.get(player.getUniqueId());
    }
    
    public void cancelPlayerTrades(Player player) {
        // Remove as requester
        pendingRequests.entrySet().removeIf(entry -> {
            if (entry.getValue().getRequester().equals(player)) {
                if (requestTimeouts.containsKey(entry.getKey())) {
                    requestTimeouts.get(entry.getKey()).cancel();
                    requestTimeouts.remove(entry.getKey());
                }
                return true;
            }
            return false;
        });
        
        // Remove as target
        if (pendingRequests.containsKey(player.getUniqueId())) {
            TradeRequest request = pendingRequests.get(player.getUniqueId());
            request.getRequester().sendMessage(MessageUtils.format("&c" + player.getName() + " left the game, trade cancelled."));
            
            if (requestTimeouts.containsKey(player.getUniqueId())) {
                requestTimeouts.get(player.getUniqueId()).cancel();
                requestTimeouts.remove(player.getUniqueId());
            }
            
            pendingRequests.remove(player.getUniqueId());
        }
    }
}
