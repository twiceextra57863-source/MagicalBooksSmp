package com.magicalbooks.smp;

import org.bukkit.plugin.java.JavaPlugin;

public class MagicalBooksSmp extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("MagicalBooksSmp has been enabled!");
        
        // Register commands
        // getCommand("magical").setExecutor(new MagicalCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MagicalBooksSmp has been disabled!");
    }
}