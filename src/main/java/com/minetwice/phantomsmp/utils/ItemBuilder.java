package com.minetwice.phantomsmp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    
    private final ItemStack item;
    private final ItemMeta meta;
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
    
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }
    
    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
        this.meta = this.item.getItemMeta();
    }
    
    public ItemBuilder setName(String name) {
        if (meta != null) {
            // Convert legacy color codes to Component
            Component displayName = LEGACY_SERIALIZER.deserialize(MessageUtils.colorize(name));
            meta.displayName(displayName);
        }
        return this;
    }
    
    public ItemBuilder setLore(String... lore) {
        if (meta != null) {
            List<Component> loreComponents = new ArrayList<>();
            for (String line : lore) {
                // Convert each line to Component
                Component loreLine = LEGACY_SERIALIZER.deserialize(MessageUtils.colorize(line));
                loreComponents.add(loreLine);
            }
            meta.lore(loreComponents);
        }
        return this;
    }
    
    public ItemBuilder addLoreLine(String line) {
        if (meta != null) {
            List<Component> lore = meta.lore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            // Convert line to Component
            Component loreLine = LEGACY_SERIALIZER.deserialize(MessageUtils.colorize(line));
            lore.add(loreLine);
            meta.lore(lore);
        }
        return this;
    }
    
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (meta != null) {
            meta.setUnbreakable(unbreakable);
        }
        return this;
    }
    
    public ItemBuilder addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        if (meta != null) {
            meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        }
        return this;
    }
    
    public ItemBuilder setGlow(boolean glow) {
        if (meta != null && glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }
    
    public ItemBuilder setCustomModelData(int data) {
        if (meta != null) {
            meta.setCustomModelData(data);
        }
        return this;
    }
    
    public ItemStack build() {
        if (meta != null) {
            item.setItemMeta(meta);
        }
        return item;
    }
}
