package com.minetwice.phantomsmp.utils;

import net.md_5.bungee.api.ChatColor;

public class MessageUtils {
    
    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String stripColor(String message) {
        return ChatColor.stripColor(message);
    }
    
    public static String prefix() {
        return format("&8[&5PhantomSMP&8] &7");
    }
    
    public static String error(String message) {
        return format("&c&lERROR &8» &7" + message);
    }
    
    public static String success(String message) {
        return format("&a&lSUCCESS &8» &7" + message);
    }
    
    public static String info(String message) {
        return format("&e&lINFO &8» &7" + message);
    }
}
