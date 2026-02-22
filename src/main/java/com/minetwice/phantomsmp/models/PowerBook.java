package com.minetwice.phantomsmp.models;

import com.minetwice.phantomsmp.PhantomSMP;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
        meta.setTitle("§8§l[ §r" + getColoredName() + " §8§l]");
        meta.setAuthor("§7PhantomSMP");
        
        // Create pages
        List<BaseComponent[]> pages = new ArrayList<>();
        
        // Page 1: Basic Info
        TextComponent page1 = new TextComponent();
        page1.addExtra("§6§l" + name + "\n\n");
        page1.addExtra("§7Theme: §f" + theme + "\n");
        page1.addExtra("§7Level: §f" + level + "\n");
        page1.addExtra("§7Kills: §f" + kills + "/" + getNextTaskKills() + "\n\n");
        page1.addExtra("§8\"The power flows through you...\"");
        pages.add(new BaseComponent[]{page1});
        
        // Page 2: Ability 1
        TextComponent page2 = new TextComponent();
        page2.addExtra("§6§lAbility I\n\n");
        page2.addExtra("§e" + ability1.getName() + "\n");
        page2.addExtra("§7" + ability1.getDescription() + "\n\n");
        page2.addExtra("§7Cooldown: §f" + ability1.getCooldown(level) + "s\n");
        page2.addExtra("§7Shift + Right Click");
        pages.add(new BaseComponent[]{page2});
        
        // Page 3: Ability 2
        TextComponent page3 = new TextComponent();
        page3.addExtra("§6§lAbility II\n\n");
        page3.addExtra("§e" + ability2.getName() + "\n");
        page3.addExtra("§7" + ability2.getDescription() + "\n\n");
        page3.addExtra("§7Cooldown: §f" + ability2.getCooldown(level) + "s\n");
        page3.addExtra("§7Shift + Left Click");
        pages.add(new BaseComponent[]{page3});
        
        // Page 4: Ability 3
        TextComponent page4 = new TextComponent();
        page4.addExtra("§6§lAbility III\n\n");
        page4.addExtra("§e" + ability3.getName() + "\n");
        page4.addExtra("§7" + ability3.getDescription() + "\n\n");
        page4.addExtra("§7Cooldown: §f" + ability3.getCooldown(level) + "s\n");
        page4.addExtra("§7Shift + F");
        pages.add(new BaseComponent[]{page4});
        
        meta.spigot().setPages(pages);
        
        // Add persistent data
        meta.getPersistentDataContainer().set(
            NamespacedKey.fromString("phantomsmp:book_id", PhantomSMP.getInstance()),
            PersistentDataType.STRING, 
            id
        );
        meta.getPersistentDataContainer().set(
            NamespacedKey.fromString("phantomsmp:owner", PhantomSMP.getInstance()),
            PersistentDataType.STRING,
            ownerUUID.toString()
        );
        
        // Add item flags
        meta.setEnchantmentGlintOverride(true);
        
        book.setItemMeta(meta);
        return book;
    }
    
    private String getColoredName() {
        switch (theme.toLowerCase()) {
            case "mythic":
                return "§c" + name;
            case "ghost":
                return "§7" + name;
            case "elemental":
                return "§b" + name;
            default:
                return "§f" + name;
        }
    }
    
    private int getNextTaskKills() {
        if (level == 1) return 10;
        if (level == 2) return 25;
        return 0;
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
    public String getId() { 
        return id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public String getTheme() { 
        return theme; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public BookAbility getAbility1() { 
        return ability1; 
    }
    
    public BookAbility getAbility2() { 
        return ability2; 
    }
    
    public BookAbility getAbility3() { 
        return ability3; 
    }
    
    public int getLevel() { 
        return level; 
    }
    
    public void setLevel(int level) { 
        this.level = level; 
    }
    
    public int getKills() { 
        return kills; 
    }
    
    public void setKills(int kills) { 
        this.kills = kills; 
    }
    
    public UUID getOwnerUUID() { 
        return ownerUUID; 
    }
}
