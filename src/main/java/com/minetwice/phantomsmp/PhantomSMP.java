package com.minetwice.phantomsmp;

import com.minetwice.phantomsmp.commands.*;
import com.minetwice.phantomsmp.listeners.*;
import com.minetwice.phantomsmp.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

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
            
            // Load data
            dataManager.loadAll();
            
            getLogger().info("§aPhantomSMP v" + getDescription().getVersion() + " enabled successfully!");
            getLogger().info("§aAuthor: MineTwice");
            
        } catch (Exception e) {
            getLogger().severe("§cFailed to enable PhantomSMP: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDisable() {
        // Save all data
        if (dataManager != null) {
            dataManager.saveAll();
            dataManager.close();
        }
        
        // Cancel all tasks
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
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
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
