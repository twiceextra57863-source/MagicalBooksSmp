package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PlayerData;
import com.minetwice.phantomsmp.models.PowerBook;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class DataManager {

    private final PhantomSMP plugin;
    private HikariDataSource dataSource;
    private final Map<UUID, PlayerData> playerDataCache = new ConcurrentHashMap<>();
    private final DatabaseType databaseType;

    public enum DatabaseType {
        SQLITE, MYSQL
    }

    public DataManager(PhantomSMP plugin) {
        this.plugin = plugin;
        this.databaseType = DatabaseType.valueOf(
            plugin.getConfig().getString("database.type", "SQLITE").toUpperCase()
        );
        
        initializeConnectionPool();
        createTables();
        startAutoSave();
    }

    private void initializeConnectionPool() {
        HikariConfig config = new HikariConfig();
        
        if (databaseType == DatabaseType.SQLITE) {
            File dbFile = new File(plugin.getDataFolder(), "phantomsmp.db");
            config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
            config.setDriverClassName("org.sqlite.JDBC");
            config.setMaximumPoolSize(1); // SQLite only supports one connection
            config.setConnectionTestQuery("SELECT 1");
            
        } else { // MySQL
            String host = plugin.getConfig().getString("database.host", "localhost");
            int port = plugin.getConfig().getInt("database.port", 3306);
            String database = plugin.getConfig().getString("database.database", "phantomsmp");
            String username = plugin.getConfig().getString("database.username", "root");
            String password = plugin.getConfig().getString("database.password", "");
            
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database 
                + "?useSSL=false&serverTimezone=UTC");
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        }
        
        config.setPoolName("PhantomSMP-Pool");
        
        try {
            dataSource = new HikariDataSource(config);
            plugin.getLogger().log(Level.INFO, "§aDatabase connection pool initialized!");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize database connection pool!", e);
        }
    }

    private void createTables() {
        String createTableSQL = databaseType == DatabaseType.SQLITE ?
            "CREATE TABLE IF NOT EXISTS player_data (" +
            "player_uuid VARCHAR(36) PRIMARY KEY," +
            "player_name VARCHAR(16)," +
            "book_id VARCHAR(50)," +
            "book_level INT DEFAULT 1," +
            "kills INT DEFAULT 0," +
            "grace_active BOOLEAN DEFAULT 0," +
            "last_seen BIGINT," +
            "playtime BIGINT DEFAULT 0," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ");" :
            "CREATE TABLE IF NOT EXISTS player_data (" +
            "player_uuid VARCHAR(36) PRIMARY KEY," +
            "player_name VARCHAR(16)," +
            "book_id VARCHAR(50)," +
            "book_level INT DEFAULT 1," +
            "kills INT DEFAULT 0," +
            "grace_active BOOLEAN DEFAULT FALSE," +
            "last_seen BIGINT," +
            "playtime BIGINT DEFAULT 0," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        String createCooldownsTable = databaseType == DatabaseType.SQLITE ?
            "CREATE TABLE IF NOT EXISTS cooldowns (" +
            "player_uuid VARCHAR(36)," +
            "ability_key VARCHAR(100)," +
            "expiry BIGINT," +
            "PRIMARY KEY (player_uuid, ability_key)" +
            ");" :
            "CREATE TABLE IF NOT EXISTS cooldowns (" +
            "player_uuid VARCHAR(36)," +
            "ability_key VARCHAR(100)," +
            "expiry BIGINT," +
            "PRIMARY KEY (player_uuid, ability_key)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        String createStatsTable = databaseType == DatabaseType.SQLITE ?
            "CREATE TABLE IF NOT EXISTS stats (" +
            "player_uuid VARCHAR(36) PRIMARY KEY," +
            "total_kills INT DEFAULT 0," +
            "deaths INT DEFAULT 0," +
            "abilities_used INT DEFAULT 0," +
            "trade_count INT DEFAULT 0," +
            " FOREIGN KEY (player_uuid) REFERENCES player_data(player_uuid)" +
            ");" :
            "CREATE TABLE IF NOT EXISTS stats (" +
            "player_uuid VARCHAR(36) PRIMARY KEY," +
            "total_kills INT DEFAULT 0," +
            "deaths INT DEFAULT 0," +
            "abilities_used INT DEFAULT 0," +
            "trade_count INT DEFAULT 0," +
            "FOREIGN KEY (player_uuid) REFERENCES player_data(player_uuid) ON DELETE CASCADE" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            stmt.execute(createCooldownsTable);
            stmt.execute(createStatsTable);
            plugin.getLogger().log(Level.INFO, "§aDatabase tables created/verified!");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create database tables!", e);
        }
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initializeConnectionPool();
        }
        return dataSource.getConnection();
    }

    public void savePlayerData(UUID playerUUID) {
        CompletableFuture.runAsync(() -> {
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player == null) return;

            PlayerData cached = playerDataCache.get(playerUUID);
            if (cached == null) {
                cached = new PlayerData(playerUUID);
                cached.setPlayerName(player.getName());
            }

            PowerBook book = plugin.getGemManager().getPlayerBook(playerUUID);
            
            String sql = databaseType == DatabaseType.SQLITE ?
                "INSERT OR REPLACE INTO player_data " +
                "(player_uuid, player_name, book_id, book_level, kills, grace_active, last_seen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)" :
                "INSERT INTO player_data " +
                "(player_uuid, player_name, book_id, book_level, kills, grace_active, last_seen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "player_name = VALUES(player_name), " +
                "book_id = VALUES(book_id), " +
                "book_level = VALUES(book_level), " +
                "kills = VALUES(kills), " +
                "grace_active = VALUES(grace_active), " +
                "last_seen = VALUES(last_seen)";

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, playerUUID.toString());
                stmt.setString(2, player.getName());
                stmt.setString(3, book != null ? book.getId() : null);
                stmt.setInt(4, book != null ? book.getLevel() : 0);
                stmt.setInt(5, book != null ? book.getKills() : 0);
                stmt.setBoolean(6, plugin.getGraceManager().isGracePeriod());
                stmt.setLong(7, System.currentTimeMillis());
                
                stmt.executeUpdate();
                
                // Update cache
                cached.setBookId(book != null ? book.getId() : null);
                cached.setBookLevel(book != null ? book.getLevel() : 0);
                cached.setKills(book != null ? book.getKills() : 0);
                cached.setGraceActive(plugin.getGraceManager().isGracePeriod());
                cached.setLastSeen(System.currentTimeMillis());
                
                playerDataCache.put(playerUUID, cached);
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to save data for " + player.getName(), e);
            }
        });
    }

    public CompletableFuture<PlayerData> loadPlayerData(UUID playerUUID) {
        return CompletableFuture.supplyAsync(() -> {
            // Check cache first
            if (playerDataCache.containsKey(playerUUID)) {
                return playerDataCache.get(playerUUID);
            }

            String sql = "SELECT * FROM player_data WHERE player_uuid = ?";
            
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, playerUUID.toString());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    PlayerData data = new PlayerData(playerUUID);
                    data.setPlayerName(rs.getString("player_name"));
                    data.setBookId(rs.getString("book_id"));
                    data.setBookLevel(rs.getInt("book_level"));
                    data.setKills(rs.getInt("kills"));
                    data.setGraceActive(rs.getBoolean("grace_active"));
                    data.setLastSeen(rs.getLong("last_seen"));
                    data.setPlaytime(rs.getLong("playtime"));
                    
                    playerDataCache.put(playerUUID, data);
                    
                    // Load cooldowns
                    loadCooldowns(playerUUID, data);
                    
                    return data;
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to load data for " + playerUUID, e);
            }
            
            return null;
        });
    }

    private void loadCooldowns(UUID playerUUID, PlayerData data) {
        String sql = "SELECT * FROM cooldowns WHERE player_uuid = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, playerUUID.toString());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String abilityKey = rs.getString("ability_key");
                long expiry = rs.getLong("expiry");
                if (expiry > System.currentTimeMillis()) {
                    data.getCooldowns().put(abilityKey, expiry);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load cooldowns for " + playerUUID, e);
        }
    }

    public void saveCooldown(UUID playerUUID, String abilityKey, long expiry) {
        CompletableFuture.runAsync(() -> {
            String sql = databaseType == DatabaseType.SQLITE ?
                "INSERT OR REPLACE INTO cooldowns (player_uuid, ability_key, expiry) VALUES (?, ?, ?)" :
                "INSERT INTO cooldowns (player_uuid, ability_key, expiry) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE expiry = VALUES(expiry)";

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, playerUUID.toString());
                stmt.setString(2, abilityKey);
                stmt.setLong(3, expiry);
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to save cooldown for " + playerUUID, e);
            }
        });
    }

    public void saveAll() {
        plugin.getServer().getOnlinePlayers().forEach(p -> savePlayerData(p.getUniqueId()));
        plugin.getLogger().log(Level.INFO, "§aAll player data saved!");
    }

    public void loadAll() {
        plugin.getServer().getOnlinePlayers().forEach(p -> {
            loadPlayerData(p.getUniqueId()).thenAccept(data -> {
                if (data != null && data.getBookId() != null) {
                    PowerBook book = plugin.getGemManager().getPowerBookById(data.getBookId());
                    if (book != null) {
                        book.setLevel(data.getBookLevel());
                        book.setKills(data.getKills());
                        plugin.getGemManager().setPlayerBook(p.getUniqueId(), book);
                    }
                }
            });
        });
    }

    private void startAutoSave() {
        int interval = plugin.getConfig().getInt("database.auto-save", 300) * 20; // seconds to ticks
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            saveAll();
        }, interval, interval);
    }

    public void close() {
        saveAll();
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            plugin.getLogger().log(Level.INFO, "§aDatabase connection pool closed!");
        }
    }

    public PlayerData getCachedData(UUID playerUUID) {
        return playerDataCache.get(playerUUID);
    }

    public Map<UUID, PlayerData> getAllCachedData() {
        return new HashMap<>(playerDataCache);
    }
}
