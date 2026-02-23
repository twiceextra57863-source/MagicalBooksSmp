package com.minetwice.phantomsmp.commands;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.utils.MessageUtils;
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
            player.sendMessage(MessageUtils.format("&cOr: /trade accept"));
            player.sendMessage(MessageUtils.format("&cOr: /trade reject"));
            return true;
        }
        
        // Handle accept
        if (args[0].equalsIgnoreCase("accept")) {
            plugin.getTradeManager().acceptTrade(player);
            return true;
        }
        
        // Handle reject
        if (args[0].equalsIgnoreCase("reject")) {
            plugin.getTradeManager().rejectTrade(player);
            return true;
        }
        
        // Check if in grace period
        if (plugin.getGraceManager().isGracePeriod()) {
            player.sendMessage(MessageUtils.format("&cTrading is not allowed during grace period!"));
            return true;
        }
        
        // Check if player has a book
        if (!plugin.getGemManager().hasPowerBook(player.getUniqueId())) {
            player.sendMessage(MessageUtils.format("&cYou don't have a power book to trade!"));
            return true;
        }
        
        // Get target player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessageUtils.format("&cPlayer not found!"));
            return true;
        }
        
        if (target.equals(player)) {
            player.sendMessage(MessageUtils.format("&cYou cannot trade with yourself!"));
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
