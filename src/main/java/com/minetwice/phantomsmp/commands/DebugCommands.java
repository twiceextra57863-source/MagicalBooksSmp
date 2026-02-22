package com.minetwice.phantomsmp.commands;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommands implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public DebugCommands(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("phantomsmp.admin")) {
            sender.sendMessage(MessageUtils.format("&cYou don't have permission!"));
            return true;
        }
        
        switch (command.getName().toLowerCase()) {
            case "bookinfo":
                return handleBookInfo(sender, args);
            case "setbooklevel":
                return handleSetLevel(sender, args);
            case "addkills":
                return handleAddKills(sender, args);
            case "resetcooldowns":
                return handleResetCooldowns(sender, args);
            default:
                return false;
        }
    }
    
    private boolean handleBookInfo(CommandSender sender, String[] args) {
        Player target;
        
        if (args.length >= 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MessageUtils.format("&cPlayer not found!"));
                return true;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        } else {
            sender.sendMessage(MessageUtils.format("&cPlease specify a player!"));
            return true;
        }
        
        PowerBook book = plugin.getGemManager().getPlayerBook(target.getUniqueId());
        
        if (book == null) {
            sender.sendMessage(MessageUtils.format("&c" + target.getName() + " doesn't have a power book!"));
            return true;
        }
        
        sender.sendMessage(MessageUtils.format("&8&m═══════════════════"));
        sender.sendMessage(MessageUtils.format("&d&l" + target.getName() + "'s Power Book"));
        sender.sendMessage(MessageUtils.format("&8&m═══════════════════"));
        sender.sendMessage(MessageUtils.format("&7Name: &f" + book.getName()));
        sender.sendMessage(MessageUtils.format("&7Theme: &f" + book.getTheme()));
        sender.sendMessage(MessageUtils.format("&7Level: &e" + book.getLevel() + " &7(" + book.getKills() + " kills)"));
        sender.sendMessage(MessageUtils.format("&7Abilities:"));
        sender.sendMessage(MessageUtils.format("  &e1. &7" + book.getAbility1().getName()));
        sender.sendMessage(MessageUtils.format("  &e2. &7" + book.getAbility2().getName()));
        sender.sendMessage(MessageUtils.format("  &e3. &7" + book.getAbility3().getName()));
        
        return true;
    }
    
    private boolean handleSetLevel(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(MessageUtils.format("&cUsage: /setbooklevel <player> <level>"));
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageUtils.format("&cPlayer not found!"));
            return true;
        }
        
        try {
            int level = Integer.parseInt(args[1]);
            if (level < 1 || level > 3) {
                sender.sendMessage(MessageUtils.format("&cLevel must be between 1 and 3!"));
                return true;
            }
            
            PowerBook book = plugin.getGemManager().getPlayerBook(target.getUniqueId());
            if (book == null) {
                sender.sendMessage(MessageUtils.format("&c" + target.getName() + " doesn't have a power book!"));
                return true;
            }
            
            book.setLevel(level);
            plugin.getDataManager().savePlayerData(target.getUniqueId());
            
            sender.sendMessage(MessageUtils.format("&aSet " + target.getName() + "'s book level to " + level));
            target.sendMessage(MessageUtils.format("&aYour book level has been set to " + level));
            
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtils.format("&cInvalid level!"));
        }
        
        return true;
    }
    
    private boolean handleAddKills(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(MessageUtils.format("&cUsage: /addkills <player> <amount>"));
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageUtils.format("&cPlayer not found!"));
            return true;
        }
        
        try {
            int amount = Integer.parseInt(args[1]);
            if (amount < 1) {
                sender.sendMessage(MessageUtils.format("&cAmount must be positive!"));
                return true;
            }
            
            PowerBook book = plugin.getGemManager().getPlayerBook(target.getUniqueId());
            if (book == null) {
                sender.sendMessage(MessageUtils.format("&c" + target.getName() + " doesn't have a power book!"));
                return true;
            }
            
            int oldLevel = book.getLevel();
            book.setKills(book.getKills() + amount);
            
            // Check for level up
            if (book.getLevel() > oldLevel) {
                plugin.getAnimationManager().playLevelUpAnimation(target);
            }
            
            plugin.getDataManager().savePlayerData(target.getUniqueId());
            
            sender.sendMessage(MessageUtils.format("&aAdded " + amount + " kills to " + target.getName()));
            target.sendMessage(MessageUtils.format("&aYou received " + amount + " bonus kills!"));
            
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtils.format("&cInvalid amount!"));
        }
        
        return true;
    }
    
    private boolean handleResetCooldowns(CommandSender sender, String[] args) {
        Player target;
        
        if (args.length >= 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MessageUtils.format("&cPlayer not found!"));
                return true;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        } else {
            sender.sendMessage(MessageUtils.format("&cPlease specify a player!"));
            return true;
        }
        
        plugin.getCooldownManager().resetPlayerCooldowns(target);
        
        sender.sendMessage(MessageUtils.format("&aReset cooldowns for " + target.getName()));
        target.sendMessage(MessageUtils.format("&aYour ability cooldowns have been reset!"));
        
        return true;
    }
}
