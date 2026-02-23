package com.minetwice.phantomsmp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class MessageUtils {
    
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
    
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static Component toComponent(String message) {
        return LEGACY_SERIALIZER.deserialize(colorize(message));
    }
    
    public static String stripColor(String message) {
        return ChatColor.stripColor(colorize(message));
    }
    
    public static String prefix() {
        return colorize("&8[&5PhantomSMP&8] &7");
    }
    
    public static Component prefixComponent() {
        return toComponent("&8[&5PhantomSMP&8] &7");
    }
    
    public static String error(String message) {
        return colorize("&c&lERROR &8» &7" + message);
    }
    
    public static Component errorComponent(String message) {
        return toComponent("&c&lERROR &8» &7" + message);
    }
    
    public static String success(String message) {
        return colorize("&a&lSUCCESS &8» &7" + message);
    }
    
    public static Component successComponent(String message) {
        return toComponent("&a&lSUCCESS &8» &7" + message);
    }
    
    public static String info(String message) {
        return colorize("&e&lINFO &8» &7" + message);
    }
    
    public static Component infoComponent(String message) {
        return toComponent("&e&lINFO &8» &7" + message);
    }
}
