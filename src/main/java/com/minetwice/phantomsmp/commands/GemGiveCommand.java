package com.minetwice.phantomsmp.commands;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GemGiveCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public GemGiveCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("phantomsmp.admin")) {
            sender.sendMessage(MessageUtils.format("&cYou don't have permission!"));
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(MessageUtils.format("&cUsage: /bookgive <player> <book_id>"));
            sender.sendMessage(MessageUtils.format("&7Available books:"));
            plugin.getGemManager().getAllPowerBooks().forEach(book -> 
                sender.sendMessage(MessageUtils.format("&e- " + book.getId() + " &7(" + book.getName() + ")"))
            );
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageUtils.format("&cPlayer not found!"));
            return true;
        }
        
        String bookId = args[1];
        PowerBook book = plugin.getGemManager().getPowerBookById(bookId);
        
        if (book == null) {
            sender.sendMessage(MessageUtils.format("&cInvalid book ID!"));
            return true;
        }
        
        // Check if player already has a book
        if (plugin.getGemManager().hasPowerBook(target.getUniqueId())) {
            sender.sendMessage(MessageUtils.format("&c" + target.getName() + " already has a power book!"));
            return true;
        }
        
        // Give the book
        target.getInventory().addItem(book.createBookItem());
        plugin.getGemManager().setPlayerBook(target.getUniqueId(), book);
        
        sender.sendMessage(MessageUtils.format("&aGave " + book.getName() + " to " + target.getName()));
        target.sendMessage(MessageUtils.format("&aYou received " + book.getName() + " from an admin!"));
        
        return true;
    }
}
