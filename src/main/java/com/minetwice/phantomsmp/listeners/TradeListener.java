package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.TradeRequest;
import com.minetwice.phantomsmp.utils.MessageUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class TradeListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public TradeListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getTradeManager().cancelPlayerTrades(player);
    }
    
    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        
        // Check for accept/reject clicks (handled by ClickEvent, but this is backup)
        if (message.equalsIgnoreCase("accept") || message.equalsIgnoreCase("reject")) {
            TradeRequest request = plugin.getTradeManager().getPendingRequest(player);
            if (request != null) {
                event.setCancelled(true);
                
                if (message.equalsIgnoreCase("accept")) {
                    plugin.getTradeManager().acceptTrade(request);
                } else {
                    plugin.getTradeManager().rejectTrade(request);
                }
            }
        }
    }
}
