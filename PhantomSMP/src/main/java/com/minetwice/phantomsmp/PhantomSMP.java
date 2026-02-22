package com.minetwice.phantomsmp;

import org.bukkit.plugin.java.JavaPlugin;

public final class PhantomSMP extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("PhantomSMP enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PhantomSMP disabled!");
    }
}