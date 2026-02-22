package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.models.BookAbility;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GemManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, PowerBook> playerBooks = new ConcurrentHashMap<>();
    private final List<PowerBook> allPowerBooks = new ArrayList<>();
    private final NamespacedKey bookIdKey;
    private final NamespacedKey ownerKey;
    
    public GemManager(PhantomSMP plugin) {
        this.plugin = plugin;
        this.bookIdKey = new NamespacedKey(plugin, "phantomsmp:book_id");
        this.ownerKey = new NamespacedKey(plugin, "phantomsmp:owner");
        initializePowerBooks();
    }
    
    private void initializePowerBooks() {
        // MYTHIC BOOKS (1-10)
        allPowerBooks.add(createVoidWalkerBook());
        allPowerBooks.add(createMindShaperBook());
        allPowerBooks.add(createSageWarriorBook());
        allPowerBooks.add(createCursedBladeBook());
        allPowerBooks.add(createZenMasterBook());
        allPowerBooks.add(createInfernalWarlordBook());
        allPowerBooks.add(createBlazeDancerBook());
        allPowerBooks.add(createStormBladeBook());
        allPowerBooks.add(createDemonSlayerBook());
        allPowerBooks.add(createPhantomWeaverBook());
        
        // GHOST BOOKS (11-19)
        allPowerBooks.add(createSoulReaperBook());
        allPowerBooks.add(createWailingSpiritBook());
        allPowerBooks.add(createShadowStalkerBook());
        allPowerBooks.add(createDreadLordBook());
        allPowerBooks.add(createNightmareBook());
        allPowerBooks.add(createVoidWhispererBook());
        allPowerBooks.add(createInfernalWraithBook());
        allPowerBooks.add(createDoomBringerBook());
        allPowerBooks.add(createEclipseLordBook());
        
        // ELEMENTAL BOOKS (20-30)
        allPowerBooks.add(createFlameSovereignBook());
        allPowerBooks.add(createFrostEmperorBook());
        allPowerBooks.add(createStormCallerBook());
        allPowerBooks.add(createTidalMasterBook());
        allPowerBooks.add(createTerraGuardianBook());
        allPowerBooks.add(createLightWeaverBook());
        allPowerBooks.add(createGravityLordBook());
        allPowerBooks.add(createMagmaKingBook());
        allPowerBooks.add(createAbyssDwellerBook());
        allPowerBooks.add(createWindFuryBook());
        allPowerBooks.add(createMountainHeartBook());
    }
    
    private PowerBook createVoidWalkerBook() {
        return new PowerBook(
            "void_walker",
            "Void Walker",
            "Mythic",
            "Space/Time Manipulation",
            createSpatialShield(),
            createVoidPull(),
            createRealityBreak(),
            UUID.randomUUID()
        );
    }
    
    private BookAbility createSpatialShield() {
        return new BookAbility(
            "Spatial Shield",
            "Â§7Grants Â§bResistance II Â§7for 3 seconds",
            40, 35, 30,
            Particle.PORTAL,
            Sound.ENTITY_ILLUSIONER_CAST_SPELL,
            (player, level) -> {
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 1));
                
                // Spatial distortion effect
                for (int i = 0; i < 360; i += 15) {
                    double radians = Math.toRadians(i);
                    double x = Math.cos(radians) * 2;
                    double z = Math.sin(radians) * 2;
                    
                    for (double y = 0; y <= 2; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.REVERSE_PORTAL,
                            player.getLocation().clone().add(x, y, z),
                            0, 0, 0, 0, 0.1
                        );
                    }
                }
                
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
            }
        );
    }
    
    private BookAbility createVoidPull() {
        return new BookAbility(
            "Void Pull",
            "Â§7Pull nearby players within Â§b5 blocks",
            45, 40, 35,
            Particle.ENCHANTMENT_TABLE,
            Sound.ENTITY_ENDERMAN_TELEPORT,
            (player, level) -> {
                double strength = level == 1 ? 1.0 : level == 2 ? 1.5 : 2.0;
                double radius = level == 1 ? 5 : level == 2 ? 6 : 7;
                
                // Create vortex
                for (int i = 0; i < 360; i += 10) {
                    double radians = Math.toRadians(i);
                    double x = Math.cos(radians) * radius;
                    double z = Math.sin(radians) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.REVERSE_PORTAL,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.2
                    );
                }
                
                // Pull entities
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        Vector direction = player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(direction.multiply(strength));
                    });
            }
        );
    }
    
    private BookAbility createRealityBreak() {
        return new BookAbility(
            "Reality Break",
            "Â§7Space-time rupture dealing Â§c6 hearts",
            120, 100, 80,
            Particle.SONIC_BOOM,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                double damage = level == 1 ? 12 : level == 2 ? 14 : 16;
                double radius = level == 1 ? 6 : level == 2 ? 7 : 8;
                
                // Reality distortion
                for (int i = 0; i < 5; i++) {
                    final int layer = i;
                    player.getServer().getScheduler().runTaskLater(plugin, () -> {
                        double layerRadius = radius * (layer + 1) / 5;
                        
                        for (int angle = 0; angle < 360; angle += 15) {
                            double rad = Math.toRadians(angle);
                            double x = Math.cos(rad) * layerRadius;
                            double z = Math.sin(rad) * layerRadius;
                            
                            player.getWorld().spawnParticle(
                                Particle.SONIC_BOOM,
                                player.getLocation().clone().add(x, 1, z),
                                0, 0, 0, 0, 0
                            );
                        }
                    }, i * 2L);
                }
                
                // Damage
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).damage(damage, player));
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.5f);
            }
        );
    }
    
    private PowerBook createMindShaperBook() {
        return new PowerBook(
            "mind_shaper",
            "Mind Shaper",
            "Mythic",
            "Illusion Control",
            createMindHaze(),
            createDarkFlames(),
            createRealityPrison(),
            UUID.randomUUID()
        );
    }
    
    private BookAbility createMindHaze() {
        return new BookAbility(
            "Mind Haze",
            "Â§7Blindness + Slowness for Â§b2 seconds",
            35, 30, 25,
            Particle.SPELL_MOB,
            Sound.ENTITY_VEX_CHARGE,
            (player, level) -> {
                int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                double radius = level == 1 ? 10 : level == 2 ? 12 : 15;
                
                // Purple haze effect
                for (int i = 0; i < 30; i++) {
                    player.getWorld().spawnParticle(
                        Particle.SPELL_MOB,
                        player.getLocation().clone().add(
                            (Math.random() - 0.5) * radius,
                            Math.random() * 3,
                            (Math.random() - 0.5) * radius
                        ),
                        0, 0.5, 0, 0.5, 1
                    );
                }
                
                // Apply effects
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> {
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0));
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 0));
                    });
                
                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.0f, 0.8f);
            }
        );
    }
    
    private BookAbility createDarkFlames() {
        return new BookAbility(
            "Dark Flames",
            "Â§7Set target on fire with Â§8black flames",
            50, 45, 40,
            Particle.SOUL_FIRE_FLAME,
            Sound.ITEM_FLINTANDSTEEL_USE,
            (player, level) -> {
                int fireTicks = level == 1 ? 100 : level == 2 ? 120 : 160;
                Player target = getTargetPlayer(player, 20);
                
                if (target != null) {
                    target.setFireTicks(fireTicks);
                    
                    // Black flame pillar
                    for (int i = 0; i < 30; i++) {
                        double y = i * 0.2;
                        target.getWorld().spawnParticle(
                            Particle.SOUL_FIRE_FLAME,
                            target.getLocation().clone().add(0, y, 0),
                            0, 0, 0, 0, 0.1
                        );
                    }
                }
            }
        );
    }
    
    private BookAbility createRealityPrison() {
        return new BookAbility(
            "Reality Prison",
            "Â§7Immobilize target for Â§b2 seconds",
            150, 130, 100,
            Particle.SPELL_INSTANT,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                Player target = getTargetPlayer(player, 15);
                
                if (target != null) {
                    int duration = level == 1 ? 40 : level == 2 ? 60 : 80;
                    
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 254));
                    target.setFreezeTicks(duration);
                    
                    // Prison cage effect
                    for (int i = 0; i < 8; i++) {
                        double angle = i * Math.PI / 4;
                        double x = Math.cos(angle) * 1.5;
                        double z = Math.sin(angle) * 1.5;
                        
                        for (double y = 0; y <= 3; y += 0.5) {
                            target.getWorld().spawnParticle(
                                Particle.SPELL_WITCH,
                                target.getLocation().clone().add(x, y, z),
                                0, 0, 0, 0, 1
                            );
                        }
                    }
                    
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.3f);
                }
            }
        );
    }
    
    private PowerBook createSageWarriorBook() {
        return new PowerBook(
            "sage_warrior",
            "Sage Warrior",
            "Mythic",
            "Energy Combat",
            createEnergyRush(),
            createSpiralStrike(),
            createTranscendentForm(),
            UUID.randomUUID()
        );
    }
    
    private BookAbility createEnergyRush() {
        return new BookAbility(
            "Energy Rush",
            "Â§7Forward dash with Â§benergy trail",
            20, 18, 15,
            Particle.SONIC_BOOM,
            Sound.ENTITY_BREEZE_JUMP,
            (player, level) -> {
                double power = level == 1 ? 1.5 : level == 2 ? 2.0 : 2.5;
                Vector direction = player.getLocation().getDirection().setY(0.3).normalize();
                player.setVelocity(direction.multiply(power));
                
                // Energy trail
                for (int i = 0; i < 15; i++) {
                    Vector offset = direction.clone().multiply(-i * 0.5);
                    player.getWorld().spawnParticle(
                        Particle.SONIC_BOOM,
                        player.getLocation().clone().add(offset),
                        2, 0.2, 0.2, 0.2, 0.01
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 1.0f, 1.2f);
            }
        );
    }
    
    private BookAbility createSpiralStrike() {
        return new BookAbility(
            "Spiral Strike",
            "Â§7Next hit deals Â§c+5 hearts",
            40, 35, 30,
            Particle.END_ROD,
            Sound.ENTITY_PLAYER_ATTACK_SWEEP,
            (player, level) -> {
                int damage = level == 1 ? 10 : level == 2 ? 12 : 14;
                
                player.setMetadata("spiral_charged", new org.bukkit.metadata.FixedMetadataValue(
                    plugin, damage));
                
                // Spiral effect
                for (int i = 0; i < 30; i++) {
                    double angle = i * Math.PI * 2 / 15;
                    double radius = 0.8;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        player.getLocation().clone().add(x, 1.2, z),
                        0, 0, 0.1, 0, 0.05
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 0.8f);
            }
        );
    }
    
    private BookAbility createTranscendentForm() {
        return new BookAbility(
            "Transcendent Form",
            "Â§7Strength II + Speed I for Â§b8 seconds",
            120, 100, 80,
            Particle.TOTEM_OF_UNDYING,
            Sound.ENTITY_WITHER_SPAWN,
            (player, level) -> {
                int duration = level == 1 ? 160 : level == 2 ? 200 : 240;
                int strengthLevel = level == 1 ? 1 : level == 2 ? 1 : 2;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, strengthLevel));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 0));
                
                // Ascension effect
                for (int i = 0; i < 100; i++) {
                    double angle = i * Math.PI * 2 / 50;
                    double radius = 3;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.TOTEM_OF_UNDYING,
                        player.getLocation().clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        0, 0, 0, 0, 1
                    );
                }
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.5f);
            }
        );
    }
    
    private PowerBook createCursedBladeBook() {
        return new PowerBook(
            "cursed_blade",
            "Cursed Blade",
            "Mythic",
            "Dark Slashing",
            createSeveringStrike(),
            createRendingSlash(),
            createCursedSanctuary(),
            UUID.randomUUID()
        );
    }
    
    private BookAbility createSeveringStrike() {
        return new BookAbility(
            "Severing Strike",
            "Â§7Next attack deals Â§c+4 hearts",
            25, 22, 20,
            Particle.SWEEP_ATTACK,
            Sound.ENTITY_PLAYER_ATTACK_CRIT,
            (player, level) -> {
                int damage = level == 1 ? 8 : level == 2 ? 10 : 12;
                
                player.setMetadata("severing_charged", new org.bukkit.metadata.FixedMetadataValue(
                    plugin, damage));
                
                // Slash lines
                Vector direction = player.getLocation().getDirection();
                for (int i = 0; i < 8; i++) {
                    double offset = i * 0.5;
                    Vector pos = direction.clone().multiply(offset);
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        player.getLocation().clone().add(pos).add(0, 1, 0),
                        0, 0, 0, 0, 0
                    );
                }
                
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.8f);
            }
        );
    }
    
    private BookAbility createRendingSlash() {
        return new BookAbility(
            "Rending Slash",
            "Â§7AoE damage in Â§c4 block Â§7radius",
            60, 55, 50,
            Particle.SWEEP_ATTACK,
            Sound.ENTITY_PLAYER_ATTACK_SWEEP,
            (player, level) -> {
                double damage = level == 1 ? 8 : level == 2 ? 10 : 12;
                double radius = level == 1 ? 4 : level == 2 ? 5 : 6;
                
                // Circular slash
                for (int i = 0; i < 360; i += 15) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.CRIT_MAGIC,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0
                    );
                }
                
                // Damage
                player.getNearbyEntities(radius, radius, radius).stream()
                    .filter(e -> e instanceof Player && !e.equals(player))
                    .forEach(e -> ((Player) e).damage(damage, player));
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.5f);
            }
        );
    }
    
    private BookAbility createCursedSanctuary() {
        return new BookAbility(
            "Cursed Sanctuary",
            "Â§7Damage aura for Â§c6 seconds",
            180, 160, 140,
            Particle.SOUL,
            Sound.ENTITY_WARDEN_SONIC_BOOM,
            (player, level) -> {
                double damage = level == 1 ? 4 : level == 2 ? 5 : 6;
                double radius = level == 1 ? 6 : level == 2 ? 7 : 8;
                int duration = level == 1 ? 120 : level == 2 ? 140 : 160;
                
                // Create cursed circle
                for (int i = 0; i < 360; i += 10) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * radius;
                    double z = Math.sin(rad) * radius;
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        player.getLocation().clone().add(x, 0.5, z),
                        0, 0, 0, 0, 0.1
                    );
                }
                
                // Damage over time
                int[] taskId = new int[1];
                taskId[0] = player.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    player.getNearbyEntities(radius, radius, radius).stream()
                        .filter(e -> e instanceof Player && !e.equals(player))
                        .forEach(e -> ((Player) e).damage(damage, player));
                }, 0L, 20L);
                
                // Cancel after duration
                player.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.getServer().getScheduler().cancelTask(taskId[0]);
                }, duration);
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.3f);
            }
        );
    }
    
    private PowerBook createZenMasterBook() {
        return new PowerBook(
            "zen_master",
            "Zen Master",
            "Mythic",
            "Perfect Evasion",
            createPerfectDodge(),
            createChiBlast(),
            createEnlightenedState(),
            UUID.randomUUID()
        );
    }
    
    private BookAbility createPerfectDodge() {
        return new BookAbility(
            "Perfect Dodge",
            "Â§7Â§b50% Â§7chance to ignore damage",
            60, 55, 50,
            Particle.END_ROD,
            Sound.ENTITY_PLAYER_LEVELUP,
            (player, level) -> {
                double chance = level == 1 ? 0.5 : level == 2 ? 0.6 : 0.7;
                int duration = level == 1 ? 60 : level == 2 ? 80 : 100;
                
                // Silver aura
                for (int i = 0; i < 50; i++) {
                    double angle = i * Math.PI * 2 / 25;
                    double x = Math.cos(angle) * 2;
                    double z = Math.sin(angle) * 2;
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        player.getLocation().clone().add(x, 1, z),
                        0, 0, 0, 0, 0.2
                    );
                }
                
                player.setMetadata("dodge_active", new org.bukkit.metadata.FixedMetadataValue(
                    plugin, true));
                player.setMetadata("dodge_chance", new org.bukkit.metadata.FixedMetadataValue(
                    plugin, chance));
                
                player.getServer().getScheduler().runTaskLater(plugin, () -> {
                    player.removeMetadata("dodge_active", plugin);
                    player.removeMetadata("dodge_chance", plugin);
                }, duration);
                
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
            }
        );
    }
    
    private BookAbility createChiBlast() {
        return new BookAbility(
            "Chi Blast",
            "Â§7Launch enemy upward",
            45, 40, 35,
            Particle.CRIT,
            Sound.ENTITY_PLAYER_ATTACK_CRIT,
            (player, level) -> {
                double power = level == 1 ? 1.5 : level == 2 ? 2.0 : 2.5;
                Player target = getTargetPlayer(player, 5);
                
                if (target != null) {
                    target.setVelocity(new Vector(0, power, 0));
                    
                    // Impact wave
                    for (int i = 0; i < 20; i++) {
                        double angle = i * Math.PI * 2 / 20;
                        double x = Math.cos(angle) * 2;
                        double z = Math.sin(angle) * 2;
                        
                        target.getWorld().spawnParticle(
                            Particle.EXPLOSION,
                            target.getLocation().clone().add(x, 0, z),
                            0, 0, 0, 0, 0
                        );
                    }
                    
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 0.8f);
                }
            }
        );
    }
    
    private BookAbility createEnlightenedState() {
        return new BookAbility(
            "Enlightened State",
            "Â§7Strength III for Â§b6 seconds",
            180, 160, 140,
            Particle.FLASH,
            Sound.ENTITY_WITHER_SPAWN,
            (player, level) -> {
                int duration = level == 1 ? 120 : level == 2 ? 140 : 160;
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 0));
                
                // Divine aura
                for (int i = 0; i < 5; i++) {
                    final int wave = i;
                    player.getServer().getScheduler().runTaskLater(plugin, () -> {
                        double radius = wave * 1.5;
                        
                        for (int angle = 0; angle < 360; angle += 15) {
                            double rad = Math.toRadians(angle);
                            double x = Math.cos(rad) * radius;
                            double z = Math.sin(rad) * radius;
                            
                            player.getWorld().spawnParticle(
                                Particle.FLASH,
                                player.getLocation().clone().add(x, 1, z),
                                0, 0, 0, 0, 0
                            );
                        }
                    }, i * 5L);
                }
                
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.5f);
            }
        );
    }
    
    // Quick placeholder methods for remaining books (you can expand these)
    private PowerBook createInfernalWarlordBook() {
        return new PowerBook(
            "infernal_warlord",
            "Infernal Warlord",
            "Mythic",
            "Fire Battlefield Control",
            createPlaceholderAbility("Dragon Breath", 40),
            createPlaceholderAbility("Demonic Armor", 60),
            createPlaceholderAbility("Starfall", 120),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createBlazeDancerBook() {
        return new PowerBook(
            "blaze_dancer",
            "Blaze Dancer",
            "Mythic",
            "Fire Speed Combat",
            createPlaceholderAbility("Flame Rush", 25),
            createPlaceholderAbility("Inferno Strike", 40),
            createPlaceholderAbility("Firestorm Dance", 90),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createStormBladeBook() {
        return new PowerBook(
            "storm_blade",
            "Storm Blade",
            "Mythic",
            "Wind Combat",
            createPlaceholderAbility("Whirlwind Strike", 30),
            createPlaceholderAbility("Wind Leap", 25),
            createPlaceholderAbility("Tempest Fury", 100),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createDemonSlayerBook() {
        return new PowerBook(
            "demon_slayer",
            "Demon Slayer",
            "Mythic",
            "Multiple Slashes",
            createPlaceholderAbility("Triple Strike", 30),
            createPlaceholderAbility("Demonic Form", 50),
            createPlaceholderAbility("Blade Storm", 110),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createPhantomWeaverBook() {
        return new PowerBook(
            "phantom_weaver",
            "Phantom Weaver",
            "Mythic",
            "Reality Bending",
            createPlaceholderAbility("Perception Break", 35),
            createPlaceholderAbility("Reality Shift", 45),
            createPlaceholderAbility("Perfect Deception", 130),
            UUID.randomUUID()
        );
    }
    
    // Ghost Books
    private PowerBook createSoulReaperBook() {
        return new PowerBook(
            "soul_reaper",
            "Soul Reaper",
            "Ghost",
            "Life Drain",
            createPlaceholderAbility("Siphon Life", 25),
            createPlaceholderAbility("Death Sentence", 35),
            createPlaceholderAbility("Soul Harvest", 90),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createWailingSpiritBook() {
        return new PowerBook(
            "wailing_spirit",
            "Wailing Spirit",
            "Ghost",
            "Sonic Terror",
            createPlaceholderAbility("Piercing Scream", 30),
            createPlaceholderAbility("Terror Aura", 35),
            createPlaceholderAbility("Soul Wail", 85),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createShadowStalkerBook() {
        return new PowerBook(
            "shadow_stalker",
            "Shadow Stalker",
            "Ghost",
            "Stealth Assassin",
            createPlaceholderAbility("Shadow Step", 25),
            createPlaceholderAbility("Veil of Darkness", 30),
            createPlaceholderAbility("Abyssal Strike", 80),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createDreadLordBook() {
        return new PowerBook(
            "dread_lord",
            "Dread Lord",
            "Ghost",
            "Fear Control",
            createPlaceholderAbility("Grave Wave", 30),
            createPlaceholderAbility("Spirit Chain", 35),
            createPlaceholderAbility("Doom Proclamation", 95),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createNightmareBook() {
        return new PowerBook(
            "nightmare",
            "Nightmare",
            "Ghost",
            "Horror Manifestation",
            createPlaceholderAbility("Shadow Glide", 25),
            createPlaceholderAbility("Panic Surge", 30),
            createPlaceholderAbility("Dark Eruption", 90),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createVoidWhispererBook() {
        return new PowerBook(
            "void_whisperer",
            "Void Whisperer",
            "Ghost",
            "Silence Magic",
            createPlaceholderAbility("Silent Touch", 25),
            createPlaceholderAbility("Void Step", 30),
            createPlaceholderAbility("Abyss Call", 85),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createInfernalWraithBook() {
        return new PowerBook(
            "infernal_wraith",
            "Infernal Wraith",
            "Ghost",
            "Hellfire Spirit",
            createPlaceholderAbility("Hellfire Grasp", 30),
            createPlaceholderAbility("Spectral Rush", 25),
            createPlaceholderAbility("Nether Explosion", 95),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createDoomBringerBook() {
        return new PowerBook(
            "doom_bringer",
            "Doom Bringer",
            "Ghost",
            "Curse Flames",
            createPlaceholderAbility("Cursed Flame", 30),
            createPlaceholderAbility("Ghastly Wave", 35),
            createPlaceholderAbility("Soul Inferno", 100),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createEclipseLordBook() {
        return new PowerBook(
            "eclipse_lord",
            "Eclipse Lord",
            "Ghost",
            "Darkness Master",
            createPlaceholderAbility("Shadow Cloak", 30),
            createPlaceholderAbility("Dark Pull", 35),
            createPlaceholderAbility("Total Darkness", 90),
            UUID.randomUUID()
        );
    }
    
    // Elemental Books
    private PowerBook createFlameSovereignBook() {
        return new PowerBook(
            "flame_sovereign",
            "Flame Sovereign",
            "Elemental",
            "Fire Master",
            createPlaceholderAbility("Flame Rush", 20),
            createPlaceholderAbility("Fire Orb", 30),
            createPlaceholderAbility("Ring of Fire", 45),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createFrostEmperorBook() {
        return new PowerBook(
            "frost_emperor",
            "Frost Emperor",
            "Elemental",
            "Ice Control",
            createPlaceholderAbility("Ice Shard", 20),
            createPlaceholderAbility("Freezing Aura", 30),
            createPlaceholderAbility("Permafrost", 45),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createStormCallerBook() {
        return new PowerBook(
            "storm_caller",
            "Storm Caller",
            "Elemental",
            "Lightning Power",
            createPlaceholderAbility("Chain Lightning", 25),
            createPlaceholderAbility("Static Charge", 25),
            createPlaceholderAbility("Thunder Dome", 50),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createTidalMasterBook() {
        return new PowerBook(
            "tidal_master",
            "Tidal Master",
            "Elemental",
            "Water Control",
            createPlaceholderAbility("Water Dash", 20),
            createPlaceholderAbility("Crashing Wave", 30),
            createPlaceholderAbility("Whirlpool", 45),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createTerraGuardianBook() {
        return new PowerBook(
            "terra_guardian",
            "Terra Guardian",
            "Elemental",
            "Earth Power",
            createPlaceholderAbility("Seismic Slam", 25),
            createPlaceholderAbility("Stone Aegis", 30),
            createPlaceholderAbility("Prison of Earth", 50),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createLightWeaverBook() {
        return new PowerBook(
            "light_weaver",
            "Light Weaver",
            "Elemental",
            "Radiant Power",
            createPlaceholderAbility("Radiant Flash", 25),
            createPlaceholderAbility("Healing Light", 30),
            createPlaceholderAbility("Solar Strike", 45),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createGravityLordBook() {
        return new PowerBook(
            "gravity_lord",
            "Gravity Lord",
            "Elemental",
            "Gravity Manipulation",
            createPlaceholderAbility("Weight of the World", 25),
            createPlaceholderAbility("Zero Gravity", 25),
            createPlaceholderAbility("Singularity", 55),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createMagmaKingBook() {
        return new PowerBook(
            "magma_king",
            "Magma King",
            "Elemental",
            "Lava Power",
            createPlaceholderAbility("Molten Strike", 25),
            createPlaceholderAbility("Lava Surge", 30),
            createPlaceholderAbility("Volcanic Heart", 50),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createAbyssDwellerBook() {
        return new PowerBook(
            "abyss_dweller",
            "Abyss Dweller",
            "Elemental",
            "Deep Water",
            createPlaceholderAbility("Pressure Wave", 25),
            createPlaceholderAbility("Water Bind", 30),
            createPlaceholderAbility("Maelstrom", 50),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createWindFuryBook() {
        return new PowerBook(
            "wind_fury",
            "Wind Fury",
            "Elemental",
            "Air Power",
            createPlaceholderAbility("Wind Dash", 20),
            createPlaceholderAbility("Blade of Wind", 30),
            createPlaceholderAbility("Tornado", 45),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createMountainHeartBook() {
        return new PowerBook(
            "mountain_heart",
            "Mountain Heart",
            "Elemental",
            "Earth's Might",
            createPlaceholderAbility("Unyielding Will", 30),
            createPlaceholderAbility("Earth Shaker", 35),
            createPlaceholderAbility("World Breaker", 60),
            UUID.randomUUID()
        );
    }
    
    private BookAbility createPlaceholderAbility(String name, int cooldown) {
        return new BookAbility(
            name,
            "Â§7Ability description",
            cooldown, cooldown - 5, cooldown - 10,
            Particle.VILLAGER_HAPPY,
            Sound.ENTITY_PLAYER_LEVELUP,
            (player, level) -> {
                player.sendMessage("Â§aUsed " + name);
                player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation(), 20, 1, 1, 1, 0);
            }
        );
    }
    
    private Player getTargetPlayer(Player player, int maxDistance) {
        return player.getWorld().getPlayers().stream()
            .filter(p -> !p.equals(player))
            .filter(p -> p.getLocation().distance(player.getLocation()) <= maxDistance)
            .min((p1, p2) -> Double.compare(
                p1.getLocation().distance(player.getLocation()),
                p2.getLocation().distance(player.getLocation())
            ))
            .orElse(null);
    }
    
    public boolean isPowerBook(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        if (!(item.getItemMeta() instanceof BookMeta)) return false;
        BookMeta meta = (BookMeta) item.getItemMeta();
        return meta.getPersistentDataContainer().has(bookIdKey, PersistentDataType.STRING);
    }
    
    public PowerBook getPowerBookFromItem(ItemStack item) {
        if (!isPowerBook(item)) return null;
        BookMeta meta = (BookMeta) item.getItemMeta();
        String id = meta.getPersistentDataContainer().get(bookIdKey, PersistentDataType.STRING);
        return allPowerBooks.stream()
            .filter(b -> b.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public void setPlayerBook(UUID playerUUID, PowerBook book) {
        playerBooks.put(playerUUID, book);
        plugin.getDataManager().savePlayerData(playerUUID);
    }
    
    public PowerBook getPlayerBook(UUID playerUUID) {
        return playerBooks.get(playerUUID);
    }
    
    public boolean hasPowerBook(UUID playerUUID) {
        return playerBooks.containsKey(playerUUID);
    }
    
    public void removePlayerBook(UUID playerUUID) {
        playerBooks.remove(playerUUID);
        plugin.getDataManager().savePlayerData(playerUUID);
    }
    
    public List<PowerBook> getAllPowerBooks() {
        return allPowerBooks;
    }
    
    public PowerBook getPowerBookById(String id) {
        return allPowerBooks.stream()
            .filter(b -> b.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public NamespacedKey getBookIdKey() {
        return bookIdKey;
    }
    
    public NamespacedKey getOwnerKey() {
        return ownerKey;
    }
}
