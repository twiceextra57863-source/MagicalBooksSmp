package com.minetwice.phantomsmp;

import com.minetwice.phantomsmp.commands.*;
import com.minetwice.phantomsmp.listeners.*;
import com.minetwice.phantomsmp.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomSMP extends JavaPlugin {
    
    private static PhantomSMP instance;
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
        
        // Save default config
        saveDefaultConfig();
        
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
        
        // Load data
        dataManager.loadAll();
        
        getLogger().info("PhantomSMP v" + getDescription().getVersion() + " enabled!");
        getLogger().info("Author: MineTwice");
    }
    
    @Override
    public void onDisable() {
        // Save all data
        if (dataManager != null) {
            dataManager.saveAll();
        }
        
        // Cancel all tasks
        getServer().getScheduler().cancelTasks(this);
        
        getLogger().info("PhantomSMP disabled!");
    }
    
    private void registerCommands() {
        getCommand("smpstart").setExecutor(new SMPStartCommand(this));
        getCommand("smprewind").setExecutor(new SMPRewindCommand(this));
        getCommand("books").setExecutor(new GemsCommand(this));
        getCommand("bookgive").setExecutor(new GemGiveCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("bookinfo").setExecutor(new DebugCommands(this));
        getCommand("setbooklevel").setExecutor(new DebugCommands(this));
        getCommand("addkills").setExecutor(new DebugCommands(this));
        getCommand("resetcooldowns").setExecutor(new DebugCommands(this));
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
