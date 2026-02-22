package com.minetwice.phantomsmp.commands;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SMPRewindCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public SMPRewindCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("phantomsmp.admin")) {
            sender.sendMessage(MessageUtils.format("&cYou don't have permission!"));
            return true;
        }
        
        // Confirm argument
        if (args.length < 1 || !args[0].equalsIgnoreCase("confirm")) {
            sender.sendMessage(MessageUtils.format("&c&lWARNING: &7This will remove all power books from all players!"));
            sender.sendMessage(MessageUtils.format("&eUse &6/smprewind confirm &eto proceed."));
            return true;
        }
        
        // Remove books from all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            removePowerBooks(player);
            plugin.getGemManager().removePlayerBook(player.getUniqueId());
            player.sendMessage(MessageUtils.format("&cYour power book has been removed by an admin."));
        }
        
        // Cancel grace period if active
        plugin.getGraceManager().cancelGracePeriod();
        
        // Broadcast
        Bukkit.broadcast(MessageUtils.format("&c&lSMP REWIND &8» &7All power books have been reset by " + sender.getName()));
        
        return true;
    }
    
    private void removePowerBooks(Player player) {
        // Remove from inventory
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && plugin.getGemManager().isPowerBook(item)) {
                item.setType(Material.AIR);
            }
        }
        
        // Remove from armor/offhand
        for (ItemStack item : player.getInventory().getExtraContents()) {
            if (item != null && plugin.getGemManager().isPowerBook(item)) {
                item.setType(Material.AIR);
            }
        }
    }
}
