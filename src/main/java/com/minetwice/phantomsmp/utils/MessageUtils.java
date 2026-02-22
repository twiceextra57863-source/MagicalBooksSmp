package com.minetwice.phantomsmp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessageUtils {
    
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
    
    public static Component format(String message) {
        return SERIALIZER.deserialize(message);
    }
    
    public static String stripColor(String message) {
        return message.replaceAll("§[0-9a-fk-or]", "");
    }
    
    public static Component prefix() {
        return format("&8[&5PhantomSMP&8] &7");
    }
    
    public static Component error(String message) {
        return format("&c&lERROR &8» &7" + message);
    }
    
    public static Component success(String message) {
        return format("&a&lSUCCESS &8» &7" + message);
    }
    
    public static Component info(String message) {
        return format("&e&lINFO &8» &7" + message);
    }
}
