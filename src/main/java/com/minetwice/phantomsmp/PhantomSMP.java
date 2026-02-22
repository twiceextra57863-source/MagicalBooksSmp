package com.minetwice.phantomsmp;

import com.minetwice.phantomsmp.commands.*;
import com.minetwice.phantomsmp.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class PhantomSMP extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register Commands
        if (getCommand("smpstart") != null) getCommand("smpstart").setExecutor(new SMPStartCommand());
        if (getCommand("smprewind") != null) getCommand("smprewind").setExecutor(new SMPRewindCommand());
        if (getCommand("gems") != null) getCommand("gems").setExecutor(new GemsCommand());
        if (getCommand("gemgive") != null) getCommand("gemgive").setExecutor(new GemGiveCommand());
        if (getCommand("trade") != null) getCommand("trade").setExecutor(new TradeCommand());
        if (getCommand("debug") != null) getCommand("debug").setExecutor(new DebugCommands());

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new GemProtectionListener(), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(), this);
        getServer().getPluginManager().registerEvents(new CombatListener(), this);
        getServer().getPluginManager().registerEvents(new TradeListener(), this);

        getLogger().info("PhantomSMP has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PhantomSMP has been disabled!");
    }
}