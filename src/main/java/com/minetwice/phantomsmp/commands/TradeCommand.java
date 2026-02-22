package com.minetwice.phantomsmp.commands;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.TradeRequest;
import com.minetwice.phantomsmp.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TradeCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public TradeCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.format("&cOnly players can use this command!"));
            return true;
        }
        
        if (args.length < 1) {
            player.sendMessage(MessageUtils.format("&cUsage: /trade <player>"));
            return true;
        }
        
        // Check if in grace period
        if (plugin.getGraceManager().isGracePeriod() && !plugin.getConfig().getBoolean("trade.allow-during-grace", false)) {
            player.sendMessage(MessageUtils.format("&cTrading is not allowed during grace period!"));
            return true;
        }
        
        // Check if player has a book
        if (!plugin.getGemManager().hasPowerBook(player.getUniqueId())) {
            player.sendMessage(MessageUtils.format("&cYou don't have a power book to trade!"));
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || target.equals(player)) {
            player.sendMessage(MessageUtils.format("&cInvalid player!"));
            return true;
        }
        
        // Check if target has a book
        if (!plugin.getGemManager().hasPowerBook(target.getUniqueId())) {
            player.sendMessage(MessageUtils.format("&c" + target.getName() + " doesn't have a power book!"));
            return true;
        }
        
        // Send trade request
        plugin.getTradeManager().sendTradeRequest(player, target);
        
        return true;
    }
}
