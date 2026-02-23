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
        
        // Log startup
        getLogger().info("§ePhantomSMP is starting...");
        
        try {
            // Initialize Adventure
            this.adventure = BukkitAudiences.create(this);
            
            // Save default config
            saveDefaultConfig();
            
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
            getLogger().info("§a║      Server: 1.21.4 Compatible     ║");
            getLogger().info("§a╚════════════════════════════════════╝");
            
        } catch (Exception e) {
            getLogger().severe("§cFailed to enable PhantomSMP: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("§ePhantomSMP is shutting down...");
        
        try {
            if (dataManager != null) {
                dataManager.saveAll();
                dataManager.close();
            }
            
            if (adventure != null) {
                adventure.close();
            }
            
            getServer().getScheduler().cancelTasks(this);
            
        } catch (Exception e) {
            getLogger().severe("§cError during disable: " + e.getMessage());
        }
        
        getLogger().info("§cPhantomSMP disabled!");
    }

    private void registerCommands() {
        try {
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
                
            getLogger().info("§a✓ Commands registered successfully");
        } catch (Exception e) {
            getLogger().warning("§cFailed to register some commands: " + e.getMessage());
        }
    }

    private void registerListeners() {
        try {
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
            getServer().getPluginManager().registerEvents(new GemProtectionListener(this), this);
            getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
            getServer().getPluginManager().registerEvents(new CombatListener(this), this);
            getServer().getPluginManager().registerEvents(new TradeListener(this), this);
            
            getLogger().info("§a✓ Listeners registered successfully");
        } catch (Exception e) {
            getLogger().warning("§cFailed to register some listeners: " + e.getMessage());
        }
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
