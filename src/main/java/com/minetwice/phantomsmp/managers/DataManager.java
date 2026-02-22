package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DataManager {
    
    private final PhantomSMP plugin;
    private Connection connection;
    private final boolean useSQLite;
    private File dataFolder;
    
    public DataManager(PhantomSMP plugin) {
        this.plugin = plugin;
        this.useSQLite = plugin.getConfig().getString("database.type", "sqlite").equalsIgnoreCase("sqlite");
        this.dataFolder = plugin.getDataFolder();
        
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        try {
            if (useSQLite) {
                File dbFile = new File(dataFolder, "phantomsmp.db");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            } else {
                // MySQL connection
                String host = plugin.getConfig().getString("database.host");
                int port = plugin.getConfig().getInt("database.port");
                String database = plugin.getConfig().getString("database.database");
                String username = plugin.getConfig().getString("database.username");
                String password = plugin.getConfig().getString("database.password");
                
                connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false",
                    username, password
                );
            }
            
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void createTables() throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS player_data (
                player_uuid VARCHAR(36) PRIMARY KEY,
                player_name VARCHAR(16),
                book_id VARCHAR(50),
                book_level INT,
                kills INT,
                grace_active BOOLEAN,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }
    
    public void savePlayerData(UUID playerUUID) {
        CompletableFuture.runAsync(() -> {
            try {
                Player player = plugin.getServer().getPlayer(playerUUID);
                String playerName = player != null ? player.getName() : "Unknown";
                PowerBook book = plugin.getGemManager().getPlayerBook(playerUUID);
                
                String bookId = book != null ? book.getId() : null;
                int bookLevel = book != null ? book.getLevel() : 0;
                int kills = book != null ? book.getKills() : 0;
                boolean graceActive = plugin.getGraceManager().isGracePeriod();
                
                String upsertSQL = useSQLite ?
                    "INSERT OR REPLACE INTO player_data (player_uuid, player_name, book_id, book_level, kills, grace_active) VALUES (?, ?, ?, ?, ?, ?)" :
                    "INSERT INTO player_data (player_uuid, player_name, book_id, book_level, kills, grace_active) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE player_name=?, book_id=?, book_level=?, kills=?, grace_active=?";
                
                try (PreparedStatement stmt = connection.prepareStatement(upsertSQL)) {
                    if (useSQLite) {
                        stmt.setString(1, playerUUID.toString());
                        stmt.setString(2, playerName);
                        stmt.setString(3, bookId);
                        stmt.setInt(4, bookLevel);
                        stmt.setInt(5, kills);
                        stmt.setBoolean(6, graceActive);
                    } else {
                        stmt.setString(1, playerUUID.toString());
                        stmt.setString(2, playerName);
                        stmt.setString(3, bookId);
                        stmt.setInt(4, bookLevel);
                        stmt.setInt(5, kills);
                        stmt.setBoolean(6, graceActive);
                        stmt.setString(7, playerName);
                        stmt.setString(8, bookId);
                        stmt.setInt(9, bookLevel);
                        stmt.setInt(10, kills);
                        stmt.setBoolean(11, graceActive);
                    }
                    
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    public void loadPlayerData(UUID playerUUID) {
        CompletableFuture.runAsync(() -> {
            try {
                String selectSQL = "SELECT * FROM player_data WHERE player_uuid = ?";
                
                try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
                    stmt.setString(1, playerUUID.toString());
                    ResultSet rs = stmt.executeQuery();
                    
                    if (rs.next()) {
                        String bookId = rs.getString("book_id");
                        int bookLevel = rs.getInt("book_level");
                        int kills = rs.getInt("kills");
                        
                        if (bookId != null) {
                            PowerBook book = plugin.getGemManager().getPowerBookById(bookId);
                            if (book != null) {
                                book.setLevel(bookLevel);
                                book.setKills(kills);
                                plugin.getGemManager().setPlayerBook(playerUUID, book);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    public void saveAll() {
        plugin.getServer().getOnlinePlayers().forEach(p -> savePlayerData(p.getUniqueId()));
    }
    
    public void loadAll() {
        plugin.getServer().getOnlinePlayers().forEach(p -> loadPlayerData(p.getUniqueId()));
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
