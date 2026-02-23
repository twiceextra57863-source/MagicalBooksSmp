package com.minetwice.phantomsmp.config;

import com.minetwice.phantomsmp.PhantomSMP;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig {
    
    private final PhantomSMP plugin;
    private FileConfiguration config;
    
    // Config values
    private String databaseType;
    private String databaseHost;
    private int databasePort;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;
    private int graceDuration;
    private int level2Kills;
    private int level3Kills;
    
    public PluginConfig(PhantomSMP plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        load();
    }
    
    public void load() {
        // Database
        this.databaseType = config.getString("database.type", "sqlite");
        this.databaseHost = config.getString("database.host", "localhost");
        this.databasePort = config.getInt("database.port", 3306);
        this.databaseName = config.getString("database.database", "phantomsmp");
        this.databaseUsername = config.getString("database.username", "root");
        this.databasePassword = config.getString("database.password", "");
        
        // Grace
        this.graceDuration = config.getInt("grace.duration", 600);
        
        // Leveling
        this.level2Kills = config.getInt("leveling.level2-kills", 10);
        this.level3Kills = config.getInt("leveling.level3-kills", 25);
    }
    
    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        load();
    }
    
    // Getters
    public String getDatabaseType() { return databaseType; }
    public String getDatabaseHost() { return databaseHost; }
    public int getDatabasePort() { return databasePort; }
    public String getDatabaseName() { return databaseName; }
    public String getDatabaseUsername() { return databaseUsername; }
    public String getDatabasePassword() { return databasePassword; }
    public int getGraceDuration() { return graceDuration; }
    public int getLevel2Kills() { return level2Kills; }
    public int getLevel3Kills() { return level3Kills; }
}
