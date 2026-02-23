package com.minetwice.phantomsmp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class MessageUtils {
    
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
    
    /**
     * Convert & color codes to § and return a String
     */
    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    /**
     * Alias for format() - converts & color codes to §
     */
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    /**
     * Convert a colored string to an Adventure Component
     */
    public static Component toComponent(String message) {
        return LEGACY_SERIALIZER.deserialize(colorize(message));
    }
    
    /**
     * Strip all color codes from a message
     */
    public static String stripColor(String message) {
        return ChatColor.stripColor(colorize(message));
    }
    
    /**
     * Get the plugin prefix as a String
     */
    public static String prefix() {
        return format("&8[&5PhantomSMP&8] &7");
    }
    
    /**
     * Get the plugin prefix as a Component
     */
    public static Component prefixComponent() {
        return toComponent("&8[&5PhantomSMP&8] &7");
    }
    
    /**
     * Format an error message as String
     */
    public static String error(String message) {
        return format("&c&lERROR &8» &7" + message);
    }
    
    /**
     * Format an error message as Component
     */
    public static Component errorComponent(String message) {
        return toComponent("&c&lERROR &8» &7" + message);
    }
    
    /**
     * Format a success message as String
     */
    public static String success(String message) {
        return format("&a&lSUCCESS &8» &7" + message);
    }
    
    /**
     * Format a success message as Component
     */
    public static Component successComponent(String message) {
        return toComponent("&a&lSUCCESS &8» &7" + message);
    }
    
    /**
     * Format an info message as String
     */
    public static String info(String message) {
        return format("&e&lINFO &8» &7" + message);
    }
    
    /**
     * Format an info message as Component
     */
    public static Component infoComponent(String message) {
        return toComponent("&e&lINFO &8» &7" + message);
    }
}
