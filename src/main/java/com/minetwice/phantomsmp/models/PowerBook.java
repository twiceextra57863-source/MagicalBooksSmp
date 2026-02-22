package com.minetwice.phantomsmp.models;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PowerBook {
    
    private final String id;
    private final String name;
    private final String theme;
    private final String description;
    private final BookAbility ability1;
    private final BookAbility ability2;
    private final BookAbility ability3;
    private int level;
    private int kills;
    private final UUID ownerUUID;
    
    public PowerBook(String id, String name, String theme, String description, 
                     BookAbility ability1, BookAbility ability2, BookAbility ability3, 
                     UUID ownerUUID) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.description = description;
        this.ability1 = ability1;
        this.ability2 = ability2;
        this.ability3 = ability3;
        this.level = 1;
        this.kills = 0;
        this.ownerUUID = ownerUUID;
    }
    
    public ItemStack createBookItem() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        
        // Set book title and author
        meta.setTitle(Component.text("§8§l[ §r" + getColoredName() + " §8§l]"));
        meta.setAuthor(Component.text("§7PhantomSMP"));
        
        // Create pages
        List<Component> pages = new ArrayList<>();
        
        // Page 1: Basic Info
        StringBuilder page1 = new StringBuilder();
        page1.append("§6§l").append(name).append("\n\n");
        page1.append("§7Theme: §f").append(theme).append("\n");
        page1.append("§7Level: §f").append(level).append("\n");
        page1.append("§7Kills: §f").append(kills).append("/").append(getNextTaskKills()).append("\n\n");
        page1.append("§8\"The power flows through you...\"");
        pages.add(Component.text(page1.toString()));
        
        // Page 2: Ability 1
        StringBuilder page2 = new StringBuilder();
        page2.append("§6§lAbility I\n\n");
        page2.append("§e").append(ability1.getName()).append("\n");
        page2.append("§7").append(ability1.getDescription()).append("\n\n");
        page2.append("§7Cooldown: §f").append(ability1.getCooldown(level)).append("s\n");
        page2.append("§7Shift + Right Click");
        pages.add(Component.text(page2.toString()));
        
        // Page 3: Ability 2
        StringBuilder page3 = new StringBuilder();
        page3.append("§6§lAbility II\n\n");
        page3.append("§e").append(ability2.getName()).append("\n");
        page3.append("§7").append(ability2.getDescription()).append("\n\n");
        page3.append("§7Cooldown: §f").append(ability2.getCooldown(level)).append("s\n");
        page3.append("§7Shift + Left Click");
        pages.add(Component.text(page3.toString()));
        
        // Page 4: Ability 3
        StringBuilder page4 = new StringBuilder();
        page4.append("§6§lAbility III\n\n");
        page4.append("§e").append(ability3.getName()).append("\n");
        page4.append("§7").append(ability3.getDescription()).append("\n\n");
        page4.append("§7Cooldown: §f").append(ability3.getCooldown(level)).append("s\n");
        page4.append("§7Shift + F");
        pages.add(Component.text(page4.toString()));
        
        meta.pages(pages);
        
        // Add persistent data
        meta.getPersistentDataContainer().set(
            org.bukkit.NamespacedKey.fromString("phantomsmp:book_id", PhantomSMP.getInstance()),
            PersistentDataType.STRING, 
            id
        );
        meta.getPersistentDataContainer().set(
            org.bukkit.NamespacedKey.fromString("phantomsmp:owner", PhantomSMP.getInstance()),
            PersistentDataType.STRING,
            ownerUUID.toString()
        );
        
        // Add item flags
        meta.setEnchantmentGlintOverride(true);
        
        book.setItemMeta(meta);
        return book;
    }
    
    private String getColoredName() {
        return switch (theme.toLowerCase()) {
            case "anime" -> "§c" + name;
            case "ghost" -> "§7" + name;
            case "elemental" -> "§b" + name;
            default -> "§f" + name;
        };
    }
    
    private int getNextTaskKills() {
        return level == 1 ? 10 : level == 2 ? 25 : 0;
    }
    
    public void addKill() {
        this.kills++;
        if (level == 1 && kills >= 10) {
            level = 2;
        } else if (level == 2 && kills >= 25) {
            level = 3;
        }
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getTheme() { return theme; }
    public String getDescription() { return description; }
    public BookAbility getAbility1() { return ability1; }
    public BookAbility getAbility2() { return ability2; }
    public BookAbility getAbility3() { return ability3; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getKills() { return kills; }
    public void setKills(int kills) { this.kills = kills; }
    public UUID getOwnerUUID() { return ownerUUID; }
}
