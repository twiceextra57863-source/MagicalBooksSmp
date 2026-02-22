package com.minetwice.phantomsmp.commands;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GemsCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public GemsCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.format("&cOnly players can use this command!"));
            return true;
        }
        
        if (!player.hasPermission("phantomsmp.admin")) {
            player.sendMessage(MessageUtils.format("&cYou don't have permission!"));
            return true;
        }
        
        openBooksMenu(player);
        return true;
    }
    
    private void openBooksMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§5§lPower Books Menu"));
        
        List<PowerBook> books = plugin.getGemManager().getAllPowerBooks();
        
        for (int i = 0; i < books.size() && i < 54; i++) {
            PowerBook book = books.get(i);
            ItemStack displayItem = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = displayItem.getItemMeta();
            
            meta.displayName(Component.text(getColoredName(book)));
            
            List<Component> lore = List.of(
                Component.text("§7Theme: " + getThemeColor(book.getTheme()) + book.getTheme()),
                Component.text("§7" + book.getDescription()),
                Component.text(""),
                Component.text("§eAbilities:"),
                Component.text("§7  • " + book.getAbility1().getName()),
                Component.text("§7  • " + book.getAbility2().getName()),
                Component.text("§7  • " + book.getAbility3().getName()),
                Component.text(""),
                Component.text("§aClick to receive this book!")
            );
            
            meta.lore(lore);
            displayItem.setItemMeta(meta);
            
            gui.setItem(i, displayItem);
        }
        
        player.openInventory(gui);
    }
    
    private String getColoredName(PowerBook book) {
        return switch (book.getTheme().toLowerCase()) {
            case "anime" -> "§c" + book.getName();
            case "ghost" -> "§7" + book.getName();
            case "elemental" -> "§b" + book.getName();
            default -> "§f" + book.getName();
        };
    }
    
    private String getThemeColor(String theme) {
        return switch (theme.toLowerCase()) {
            case "anime" -> "§c";
            case "ghost" -> "§7";
            case "elemental" -> "§b";
            default -> "§f";
        };
    }
}
