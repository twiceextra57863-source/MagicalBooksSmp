package com.minetwice.phantomsmp;
package com.minetwice.phantomsmp;

import com.minetwice.phantomsmp.commands.*;
import com.minetwice.phantomsmp.config.PluginConfig;
import com.minetwice.phantomsmp.listeners.*;
import com.minetwice.phantomsmp.managers.*;
import com.minetwice.phantomsmp.metrics.Metrics;
import com.minetwice.phantomsmp.updater.UpdateChecker;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

public final class PhantomSMP extends JavaPlugin {

    private static PhantomSMP instance;
    private BukkitAudiences adventure;
    private PluginConfig pluginConfig;
    
    // Managers
    private DataManager dataManager;
    private GemManager gemManager;
    private CooldownManager cooldownManager;
    private TradeManager tradeManager;
    private GraceManager graceManager;
    private ParticleManager particleManager;
    private AnimationManager animationManager;
    private EffectManager effectManager;
    private WorldGuardIntegration worldGuardIntegration;
    private PlaceholderAPIIntegration placeholderAPI;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize Adventure
        this.adventure = BukkitAudiences.create(this);
        
        // Load configuration
        loadConfiguration();
        
        // Initialize metrics (bStats)
        setupMetrics();
        
        try {
            // Initialize managers in correct order
            this.dataManager = new DataManager(this);
            this.gemManager = new GemManager(this);
            this.cooldownManager = new CooldownManager(this);
            this.tradeManager = new TradeManager(this);
            this.graceManager = new GraceManager(this);
            this.particleManager = new ParticleManager(this);
            this.animationManager = new AnimationManager(this);
            this.effectManager = new EffectManager(this);
            
            // Check for integrations
            setupIntegrations();
            
            // Register commands
            registerCommands();
            
            // Register listeners
            registerListeners();
            
            // Start update checker
            checkForUpdates();
            
            // Load all player data
            dataManager.loadAll();
            
            // Start particle optimization task
            startParticleOptimization();
            
            getLogger().log(Level.INFO, "§a╔════════════════════════════════════╗");
            getLogger().log(Level.INFO, "§a║     PhantomSMP v{0} Enabled      ║", getDescription().getVersion());
            getLogger().log(Level.INFO, "§a║        Author: MineTwice           ║");
            getLogger().log(Level.INFO, "§a║     30 Power Books Loaded          ║");
            getLogger().log(Level.INFO, "§a╚════════════════════════════════════╝");
            
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable PhantomSMP: " + e.getMessage());
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
        
        getLogger().log(Level.INFO, "§cPhantomSMP disabled successfully!");
    }

    private void loadConfiguration() {
        saveDefaultConfig();
        pluginConfig = new PluginConfig(this);
        pluginConfig.load();
    }

    private void setupMetrics() {
        int pluginId = 12345; // Replace with your bStats plugin ID
        Metrics metrics = new Metrics(this, pluginId);
        
        // Add custom charts
        metrics.addCustomChart(new Metrics.SingleLineChart("active_players", 
            () -> getServer().getOnlinePlayers().size()));
        metrics.addCustomChart(new Metrics.SingleLineChart("total_books", 
            () -> gemManager != null ? gemManager.getAllPowerBooks().size() : 0));
    }

    private void setupIntegrations() {
        // WorldGuard integration
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardIntegration = new WorldGuardIntegration(this);
            getLogger().log(Level.INFO, "§aWorldGuard integration enabled!");
        }
        
        // PlaceholderAPI integration
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPI = new PlaceholderAPIIntegration(this);
            placeholderAPI.register();
            getLogger().log(Level.INFO, "§aPlaceholderAPI integration enabled!");
        }
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
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    }

    private void checkForUpdates() {
        if (pluginConfig.isCheckUpdates()) {
            new UpdateChecker(this, 12345).getVersion(version -> {
                if (!getDescription().getVersion().equals(version)) {
                    getLogger().log(Level.INFO, "§eA new update is available: v{0}", version);
                }
            });
        }
    }

    private void startParticleOptimization() {
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            particleManager.optimizeParticles();
        }, 200L, 200L); // Every 10 seconds
    }

    public static PhantomSMP getInstance() {
        return instance;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
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

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public WorldGuardIntegration getWorldGuardIntegration() {
        return worldGuardIntegration;
    }

    public PlaceholderAPIIntegration getPlaceholderAPI() {
        return placeholderAPI;
    }
}
