package com.minetwice.phantomsmp.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }
    
    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
        this.meta = this.item.getItemMeta();
    }
    
    public ItemBuilder setName(String name) {
        meta.displayName(MessageUtils.format(name));
        return this;
    }
    
    public ItemBuilder setLore(String... lore) {
        List<Component> loreComponents = new ArrayList<>();
        for (String line : lore) {
            loreComponents.add(MessageUtils.format(line));
        }
        meta.lore(loreComponents);
        return this;
    }
    
    public ItemBuilder addLoreLine(String line) {
        List<Component> lore = meta.lore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(MessageUtils.format(line));
        meta.lore(lore);
        return this;
    }
    
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }
    
    public ItemBuilder addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }
    
    public ItemBuilder setGlow(boolean glow) {
        if (glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }
    
    public ItemBuilder setCustomModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }
    
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
