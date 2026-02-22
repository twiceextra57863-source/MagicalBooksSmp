package com.minetwice.phantomsmp.config;

import com.minetwice.phantomsmp.PhantomSMP;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class PluginConfig {

    private final PhantomSMP plugin;
    private CommentedConfigurationNode rootNode;
    private final Path configPath;
    private final Map<String, Object> cachedValues = new HashMap<>();

    // Config values
    private DatabaseConfig database;
    private GraceConfig grace;
    private AbilityConfig abilities;
    private ParticleConfig particles;
    private LevelingConfig leveling;
    private DamageConfig damage;
    private TradeConfig trade;
    private ProtectionConfig protection;
    private boolean checkUpdates;
    private String language;

    public PluginConfig(PhantomSMP plugin) {
        this.plugin = plugin;
        this.configPath = plugin.getDataFolder().toPath().resolve("config.yml");
    }

    public void load() {
        try {
            // Create default config if not exists
            if (!Files.exists(configPath)) {
                plugin.saveResource("config.yml", false);
            }

            // Load configuration
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .build();

            rootNode = loader.load();

            // Load all sections
            loadDatabase();
            loadGrace();
            loadAbilities();
            loadParticles();
            loadLeveling();
            loadDamage();
            loadTrade();
            loadProtection();
            
            this.checkUpdates = rootNode.node("check-updates").getBoolean(true);
            this.language = rootNode.node("language").getString("en");

            plugin.getLogger().log(Level.INFO, "§aConfiguration loaded successfully!");

        } catch (ConfigurateException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load configuration!", e);
        }
    }

    private void loadDatabase() {
        database = new DatabaseConfig();
        database.type = rootNode.node("database", "type").getString("sqlite");
        database.host = rootNode.node("database", "host").getString("localhost");
        database.port = rootNode.node("database", "port").getInt(3306);
        database.name = rootNode.node("database", "database").getString("phantomsmp");
        database.username = rootNode.node("database", "username").getString("root");
        database.password = rootNode.node("database", "password").getString("");
        database.poolSize = rootNode.node("database", "pool-size").getInt(10);
        database.autoSave = rootNode.node("database", "auto-save").getInt(300);
    }

    private void loadGrace() {
        grace = new GraceConfig();
        grace.duration = rootNode.node("grace", "duration").getInt(600);
        grace.pvpDisabled = rootNode.node("grace", "pvp-disabled").getBoolean(true);
        grace.countdownActionbar = rootNode.node("grace", "countdown-actionbar").getBoolean(true);
        grace.abilitiesDisabled = rootNode.node("grace", "abilities-disabled").getBoolean(false);
        grace.broadcastMessages = rootNode.node("grace", "broadcast-messages").getBoolean(true);
    }

    private void loadAbilities() {
        abilities = new AbilityConfig();
        abilities.maxCooldown = rootNode.node("abilities", "max-cooldown").getInt(180);
        abilities.cooldownDisplay = rootNode.node("abilities", "cooldown-display").getBoolean(true);
        abilities.particlesOnUse = rootNode.node("abilities", "particles-on-use").getBoolean(true);
        abilities.soundsOnUse = rootNode.node("abilities", "sounds-on-use").getBoolean(true);
        abilities.globalCooldown = rootNode.node("abilities", "global-cooldown").getInt(0);
    }

    private void loadParticles() {
        particles = new ParticleConfig();
        particles.enabled = rootNode.node("particles", "enabled").getBoolean(true);
        particles.intensity = rootNode.node("particles", "intensity").getDouble(1.0);
        particles.tpsLimit = rootNode.node("particles", "tps-limit").getDouble(18.0);
        particles.maxPerTick = rootNode.node("particles", "max-particles-per-tick").getInt(100);
        particles.idleParticles = rootNode.node("particles", "idle-particles").getBoolean(true);
    }

    private void loadLeveling() {
        leveling = new LevelingConfig();
        leveling.level2Kills = rootNode.node("leveling", "level2-kills").getInt(10);
        leveling.level3Kills = rootNode.node("leveling", "level3-kills").getInt(25);
        leveling.killMessages = rootNode.node("leveling", "kill-messages").getBoolean(true);
        leveling.levelupSound = rootNode.node("leveling", "levelup-sound").getBoolean(true);
        leveling.levelupTitle = rootNode.node("leveling", "levelup-title").getBoolean(true);
        leveling.killCounter = rootNode.node("leveling", "kill-counter").getBoolean(true);
    }

    private void loadDamage() {
        damage = new DamageConfig();
        damage.pvpReduction = rootNode.node("damage", "pvp-reduction").getDouble(1.0);
        damage.pveReduction = rootNode.node("damage", "pve-reduction").getDouble(1.0);
        damage.maxAbilityDamage = rootNode.node("damage", "max-ability-damage").getInt(14);
        damage.minAbilityDamage = rootNode.node("damage", "min-ability-damage").getInt(2);
        damage.fallDamageProtection = rootNode.node("damage", "fall-damage-protection").getBoolean(true);
    }

    private void loadTrade() {
        trade = new TradeConfig();
        trade.timeout = rootNode.node("trade", "timeout").getInt(30);
        trade.freezeDuration = rootNode.node("trade", "freeze-duration").getInt(2);
        trade.allowDuringGrace = rootNode.node("trade", "allow-during-grace").getBoolean(false);
        trade.requireBothBooks = rootNode.node("trade", "require-both-books").getBoolean(true);
        trade.cooldown = rootNode.node("trade", "cooldown").getInt(60);
    }

    private void loadProtection() {
        protection = new ProtectionConfig();
        protection.preventDuplication = rootNode.node("protection", "prevent-duplication").getBoolean(true);
        protection.preventStacking = rootNode.node("protection", "prevent-stacking").getBoolean(true);
        protection.preventChestStorage = rootNode.node("protection", "prevent-chest-storage").getBoolean(true);
        protection.preventDropping = rootNode.node("protection", "prevent-dropping").getBoolean(true);
        protection.preventRename = rootNode.node("protection", "prevent-rename").getBoolean(true);
        protection.preventCrafting = rootNode.node("protection", "prevent-crafting").getBoolean(true);
        protection.preventLavaDestroy = rootNode.node("protection", "prevent-lava-destroy").getBoolean(true);
        protection.preventGrindstone = rootNode.node("protection", "prevent-grindstone").getBoolean(true);
    }

    // Configuration classes
    public static class DatabaseConfig {
        public String type;
        public String host;
        public int port;
        public String name;
        public String username;
        public String password;
        public int poolSize;
        public int autoSave;
    }

    public static class GraceConfig {
        public int duration;
        public boolean pvpDisabled;
        public boolean countdownActionbar;
        public boolean abilitiesDisabled;
        public boolean broadcastMessages;
    }

    public static class AbilityConfig {
        public int maxCooldown;
        public boolean cooldownDisplay;
        public boolean particlesOnUse;
        public boolean soundsOnUse;
        public int globalCooldown;
    }

    public static class ParticleConfig {
        public boolean enabled;
        public double intensity;
        public double tpsLimit;
        public int maxPerTick;
        public boolean idleParticles;
    }

    public static class LevelingConfig {
        public int level2Kills;
        public int level3Kills;
        public boolean killMessages;
        public boolean levelupSound;
        public boolean levelupTitle;
        public boolean killCounter;
    }

    public static class DamageConfig {
        public double pvpReduction;
        public double pveReduction;
        public int maxAbilityDamage;
        public int minAbilityDamage;
        public boolean fallDamageProtection;
    }

    public static class TradeConfig {
        public int timeout;
        public int freezeDuration;
        public boolean allowDuringGrace;
        public boolean requireBothBooks;
        public int cooldown;
    }

    public static class ProtectionConfig {
        public boolean preventDuplication;
        public boolean preventStacking;
        public boolean preventChestStorage;
        public boolean preventDropping;
        public boolean preventRename;
        public boolean preventCrafting;
        public boolean preventLavaDestroy;
        public boolean preventGrindstone;
    }

    // Getters
    public DatabaseConfig getDatabase() { return database; }
    public GraceConfig getGrace() { return grace; }
    public AbilityConfig getAbilities() { return abilities; }
    public ParticleConfig getParticles() { return particles; }
    public LevelingConfig getLeveling() { return leveling; }
    public DamageConfig getDamage() { return damage; }
    public TradeConfig getTrade() { return trade; }
    public ProtectionConfig getProtection() { return protection; }
    public boolean isCheckUpdates() { return checkUpdates; }
    public String getLanguage() { return language; }
}
