package com.minetwice.phantomsmp;

import com.minetwice.phantomsmp.commands.*;
import com.minetwice.phantomsmp.listeners.*;
import com.minetwice.phantomsmp.managers.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomSMP extends JavaPlugin {

    private static PhantomSMP instance;
    private BukkitAudiences adventure;
    
    // Managers
    private DataManager dataManager;
    private GemManager gemManager;
    private CooldownManager cooldownManager;
    private TradeManager tradeManager;
    private GraceManager graceManager;
    private ParticleManager particleManager;
    private AnimationManager animationManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize Adventure
        this.adventure = BukkitAudiences.create(this);
        
        // Save default config
        saveDefaultConfig();
        
        try {
            // Initialize managers
            this.dataManager = new DataManager(this);
            this.gemManager = new GemManager(this);
            this.cooldownManager = new CooldownManager(this);
            this.tradeManager = new TradeManager(this);
            this.graceManager = new GraceManager(this);
            this.particleManager = new ParticleManager(this);
            this.animationManager = new AnimationManager(this);
            
            // Register commands
            registerCommands();
            
            // Register listeners
            registerListeners();
            
            // Load all player data
            dataManager.loadAll();
            
            getLogger().info("§a╔════════════════════════════════════╗");
            getLogger().info("§a║     PhantomSMP v" + getDescription().getVersion() + " Enabled      ║");
            getLogger().info("§a║        Author: MineTwice           ║");
            getLogger().info("§a║     30 Power Books Loaded          ║");
            getLogger().info("§a╚════════════════════════════════════╝");
            
        } catch (Exception e) {
            getLogger().severe("Failed to enable PhantomSMP: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveAll();
            dataManager.close();
        }
        
        if (adventure != null) {
            adventure.close();
        }
        
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("§cPhantomSMP disabled!");
    }

    private void registerCommands() {
        getCommand("smpstart").setExecutor(new SMPStartCommand(this));
        getCommand("smprewind").setExecutor(new SMPRewindCommand(this));
        getCommand("books").setExecutor(new GemsCommand(this));
        getCommand("bookgive").setExecutor(new GemGiveCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new GemProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new TradeListener(this), this);
    }

    public static PhantomSMP getInstance() {
        return instance;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public GemManager getGemManager() {
        return gemManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public TradeManager getTradeManager() {
        return tradeManager;
    }

    public GraceManager getGraceManager() {
        return graceManager;
    }

    public ParticleManager getParticleManager() {
        return particleManager;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }
}
