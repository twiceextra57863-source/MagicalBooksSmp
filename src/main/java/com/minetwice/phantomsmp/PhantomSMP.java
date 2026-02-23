package com.minetwice.phantomsmp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomSMP extends JavaPlugin {
    
    private static PhantomSMP instance;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Display startup message
        getLogger().info("+=======================================+");
        getLogger().info("|     PhantomSMP v" + getDescription().getVersion() + "           |");
        getLogger().info("|     Author: MineTwice                 |");
        getLogger().info("|     Server: Paper 1.21.4              |");
        getLogger().info("|     Status: LOADED SUCCESSFULLY       |");
        getLogger().info("+=======================================+");
        
        // Check server software
        String serverName = Bukkit.getServer().getName();
        String serverVersion = Bukkit.getBukkitVersion();
        
        getLogger().info("§aServer Software: " + serverName);
        getLogger().info("§aServer Version: " + serverVersion);
        
        if (serverName.equalsIgnoreCase("Paper")) {
            getLogger().info("§a✓ Running on Paper - Optimized!");
        }
    }
    
    @Override
    public void onDisable() {
        getLogger().info("PhantomSMP has been disabled!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("phantomtest")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("§a§l╔════════════════════════════════╗");
                player.sendMessage("§a§l║    PhantomSMP is WORKING!     ║");
                player.sendMessage("§a§l╠════════════════════════════════╣");
                player.sendMessage("§f  Version: §e" + getDescription().getVersion());
                player.sendMessage("§f  Author: §eMineTwice");
                player.sendMessage("§f  Server: §ePaper 1.21.4");
                player.sendMessage("§a§l╚════════════════════════════════╝");
            } else {
                sender.sendMessage("PhantomSMP is working!");
                sender.sendMessage("Version: " + getDescription().getVersion());
                sender.sendMessage("Server: Paper 1.21.4");
            }
            return true;
        }
        return false;
    }
    
    public static PhantomSMP getInstance() {
        return instance;
    }
}
