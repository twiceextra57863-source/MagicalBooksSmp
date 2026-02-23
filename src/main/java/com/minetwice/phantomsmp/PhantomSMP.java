package com.minetwice.phantomsmp;

import com.minetwice.phantomsmp.commands.*;
import com.minetwice.phantomsmp.listeners.*;
import com.minetwice.phantomsmp.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomSMP extends JavaPlugin {
    
    private static PhantomSMP instance;
    
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
        
        // Save default config
        saveDefaultConfig();
        
        try {
            // Initialize managers
            getLogger().info("§7Initializing managers...");
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
            if (dataManager != null) {
                dataManager.loadAll();
            }
            
            getLogger().info("§a╔════════════════════════════════════╗");
            getLogger().info("§a║     PhantomSMP v" + getDescription().getVersion() + " Enabled      ║");
            getLogger().info("§a║        Author: MineTwice           ║");
            getLogger().info("§a║      Paper 1.21.4 Compatible       ║");
            getLogger().info("§a╚════════════════════════════════════╝");
            
        } catch (Exception e) {
            getLogger().severe("§cFailed to enable PhantomSMP: " + e.getMessage());
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
        
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("§cPhantomSMP disabled!");
    }
    
    private void registerCommands() {
        // Register commands only if they exist in plugin.yml
        if (getCommand("smpstart") != null)
            getCommand("smpstart").setExecutor(new SMPStartCommand(this));
        if (getCommand("smprewind") != null)
            getCommand("smprewind").setExecutor(new SMPRewindCommand(this));
        if (getCommand("books") != null)
            getCommand("books").setExecutor(new GemsCommand(this));
        if (getCommand("bookgive") != null)
            getCommand("bookgive").setExecutor(new GemGiveCommand(this));
        if (getCommand("trade") != null)
            getCommand("trade").setExecutor(new TradeCommand(this));
        if (getCommand("bookinfo") != null)
            getCommand("bookinfo").setExecutor(new DebugCommands(this));
        if (getCommand("setbooklevel") != null)
            getCommand("setbooklevel").setExecutor(new DebugCommands(this));
        if (getCommand("addkills") != null)
            getCommand("addkills").setExecutor(new DebugCommands(this));
        if (getCommand("resetcooldowns") != null)
            getCommand("resetcooldowns").setExecutor(new DebugCommands(this));
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new GemProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new TradeListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
    }
    
    public static PhantomSMP getInstance() {
        return instance;
    }
    
    // Manager Getters
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
