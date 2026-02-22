package com.minetwice.phantomsmp.commands;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import com.minetwice.phantomsmp.utils.ParticleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SMPStartCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    private final Random random = new Random();
    
    public SMPStartCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.format("&cOnly players can use this command!"));
            return true;
        }
        
        if (!player.hasPermission("phantomsmp.admin")) {
            player.sendMessage(MessageUtils.format("&cYou don't have permission!"));
            return true;
        }
        
        startSMP(player);
        return true;
    }
    
    private void startSMP(Player starter) {
        // First timer: 5 seconds
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.showTitle(Title.title(
                Component.text(MessageUtils.format("&6&lTHE SMP STARTED BY &e&l" + starter.getName())).toString(), // Fixed
                Component.text(MessageUtils.format("&7Prepare yourself...")).toString(),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
            ));
        });
        
        new BukkitRunnable() {
            @Override
            public void run() {
                // Second timer: 5 seconds countdown
                startDistributionCountdown();
            }
        }.runTaskLater(plugin, 100L); // 5 seconds * 20 ticks
    }
    
    private void startDistributionCountdown() {
        new BukkitRunnable() {
            int countdown = 5;
            
            @Override
            public void run() {
                if (countdown > 0) {
                    int finalCountdown = countdown;
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendActionBar(Component.text(MessageUtils.format("&e&lGEMS DISTRIBUTED IN: &6&l" + finalCountdown)));
                        p.playSound(p.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    });
                    countdown--;
                } else {
                    distributeGems();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void distributeGems() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<PowerBook> availableBooks = new ArrayList<>(plugin.getGemManager().getAllPowerBooks());
        
        // Shuffle books for random distribution
        java.util.Collections.shuffle(availableBooks);
        
        // Distribute unique books to each player
        for (int i = 0; i < players.size() && i < availableBooks.size(); i++) {
            Player player = players.get(i);
            PowerBook book = availableBooks.get(i);
            
            // Global swipe animation
            plugin.getAnimationManager().playSwipeAnimation(player);
            
            // Give book
            player.getInventory().addItem(book.createBookItem());
            plugin.getGemManager().setPlayerBook(player.getUniqueId(), book);
            
            // Apply levitation
            player.setAllowFlight(true);
            player.setFlying(true);
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Remove levitation
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    
                    // Show awakening title - Fixed
                    player.showTitle(Title.title(
                        Component.text(MessageUtils.format("&5&lYOU HAVE AWAKENED")).toString(),
                        Component.text(MessageUtils.format("&d&l" + book.getName())).toString(),
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
                    ));
                    
                    // Play sound
                    player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
                    
                    // Particles
                    ParticleUtils.spawnAwakeningParticles(player);
                }
            }.runTaskLater(plugin, 100L); // 5 seconds later
        }
        
        // Start grace period
        plugin.getGraceManager().startGracePeriod();
    }
}
