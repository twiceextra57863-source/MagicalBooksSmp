package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.models.BookAbility;
import com.minetwice.phantomsmp.abilities.mythic.*;
import com.minetwice.phantomsmp.abilities.ghost.*;
import com.minetwice.phantomsmp.abilities.elemental.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

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
        // MYTHIC BOOKS (1-10) - Formerly Anime
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
            VoidWalkerAbilities.createSpatialShield(),
            VoidWalkerAbilities.createVoidPull(),
            VoidWalkerAbilities.createRealityBreak(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createMindShaperBook() {
        return new PowerBook(
            "mind_shaper",
            "Mind Shaper",
            "Mythic",
            "Illusion Control",
            MindShaperAbilities.createMindHaze(),
            MindShaperAbilities.createDarkFlames(),
            MindShaperAbilities.createRealityPrison(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createSageWarriorBook() {
        return new PowerBook(
            "sage_warrior",
            "Sage Warrior",
            "Mythic",
            "Energy Combat",
            SageWarriorAbilities.createEnergyRush(),
            SageWarriorAbilities.createSpiralStrike(),
            SageWarriorAbilities.createTranscendentForm(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createCursedBladeBook() {
        return new PowerBook(
            "cursed_blade",
            "Cursed Blade",
            "Mythic",
            "Dark Slashing",
            CursedBladeAbilities.createSeveringStrike(),
            CursedBladeAbilities.createRendingSlash(),
            CursedBladeAbilities.createCursedSanctuary(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createZenMasterBook() {
        return new PowerBook(
            "zen_master",
            "Zen Master",
            "Mythic",
            "Perfect Evasion",
            ZenMasterAbilities.createPerfectDodge(),
            ZenMasterAbilities.createChiBlast(),
            ZenMasterAbilities.createEnlightenedState(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createInfernalWarlordBook() {
        return new PowerBook(
            "infernal_warlord",
            "Infernal Warlord",
            "Mythic",
            "Fire Battlefield Control",
            InfernalWarlordAbilities.createDragonBreath(),
            InfernalWarlordAbilities.createDemonicArmor(),
            InfernalWarlordAbilities.createStarfall(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createBlazeDancerBook() {
        return new PowerBook(
            "blaze_dancer",
            "Blaze Dancer",
            "Mythic",
            "Fire Speed Combat",
            BlazeDancerAbilities.createFlameRush(),
            BlazeDancerAbilities.createInfernoStrike(),
            BlazeDancerAbilities.createFirestormDance(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createStormBladeBook() {
        return new PowerBook(
            "storm_blade",
            "Storm Blade",
            "Mythic",
            "Wind Combat",
            StormBladeAbilities.createWhirlwindStrike(),
            StormBladeAbilities.createWindLeap(),
            StormBladeAbilities.createTempestFury(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createDemonSlayerBook() {
        return new PowerBook(
            "demon_slayer",
            "Demon Slayer",
            "Mythic",
            "Multiple Slashes",
            DemonSlayerAbilities.createTripleStrike(),
            DemonSlayerAbilities.createDemonicForm(),
            DemonSlayerAbilities.createBladeStorm(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createPhantomWeaverBook() {
        return new PowerBook(
            "phantom_weaver",
            "Phantom Weaver",
            "Mythic",
            "Reality Bending",
            PhantomWeaverAbilities.createPerceptionBreak(),
            PhantomWeaverAbilities.createRealityShift(),
            PhantomWeaverAbilities.createPerfectDeception(),
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
            SoulReaperAbilities.createSiphonLife(),
            SoulReaperAbilities.createDeathSentence(),
            SoulReaperAbilities.createSoulHarvest(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createWailingSpiritBook() {
        return new PowerBook(
            "wailing_spirit",
            "Wailing Spirit",
            "Ghost",
            "Sonic Terror",
            WailingSpiritAbilities.createPiercingScream(),
            WailingSpiritAbilities.createTerrorAura(),
            WailingSpiritAbilities.createSoulWail(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createShadowStalkerBook() {
        return new PowerBook(
            "shadow_stalker",
            "Shadow Stalker",
            "Ghost",
            "Stealth Assassin",
            ShadowStalkerAbilities.createShadowStep(),
            ShadowStalkerAbilities.createVeilOfDarkness(),
            ShadowStalkerAbilities.createAbyssalStrike(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createDreadLordBook() {
        return new PowerBook(
            "dread_lord",
            "Dread Lord",
            "Ghost",
            "Fear Control",
            DreadLordAbilities.createGraveWave(),
            DreadLordAbilities.createSpiritChain(),
            DreadLordAbilities.createDoomProclamation(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createNightmareBook() {
        return new PowerBook(
            "nightmare",
            "Nightmare",
            "Ghost",
            "Horror Manifestation",
            NightmareAbilities.createShadowGlide(),
            NightmareAbilities.createPanicSurge(),
            NightmareAbilities.createDarkEruption(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createVoidWhispererBook() {
        return new PowerBook(
            "void_whisperer",
            "Void Whisperer",
            "Ghost",
            "Silence Magic",
            VoidWhispererAbilities.createSilentTouch(),
            VoidWhispererAbilities.createVoidStep(),
            VoidWhispererAbilities.createAbyssCall(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createInfernalWraithBook() {
        return new PowerBook(
            "infernal_wraith",
            "Infernal Wraith",
            "Ghost",
            "Hellfire Spirit",
            InfernalWraithAbilities.createHellfireGrasp(),
            InfernalWraithAbilities.createSpectralRush(),
            InfernalWraithAbilities.createNetherExplosion(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createDoomBringerBook() {
        return new PowerBook(
            "doom_bringer",
            "Doom Bringer",
            "Ghost",
            "Curse Flames",
            DoomBringerAbilities.createCursedFlame(),
            DoomBringerAbilities.createGhastlyWave(),
            DoomBringerAbilities.createSoulInferno(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createEclipseLordBook() {
        return new PowerBook(
            "eclipse_lord",
            "Eclipse Lord",
            "Ghost",
            "Darkness Master",
            EclipseLordAbilities.createShadowCloak(),
            EclipseLordAbilities.createDarkPull(),
            EclipseLordAbilities.createTotalDarkness(),
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
            FlameSovereignAbilities.createFlameRush(),
            FlameSovereignAbilities.createFireOrb(),
            FlameSovereignAbilities.createRingOfFire(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createFrostEmperorBook() {
        return new PowerBook(
            "frost_emperor",
            "Frost Emperor",
            "Elemental",
            "Ice Control",
            FrostEmperorAbilities.createIceShard(),
            FrostEmperorAbilities.createFreezingAura(),
            FrostEmperorAbilities.createPermafrost(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createStormCallerBook() {
        return new PowerBook(
            "storm_caller",
            "Storm Caller",
            "Elemental",
            "Lightning Power",
            StormCallerAbilities.createChainLightning(),
            StormCallerAbilities.createStaticCharge(),
            StormCallerAbilities.createThunderDome(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createTidalMasterBook() {
        return new PowerBook(
            "tidal_master",
            "Tidal Master",
            "Elemental",
            "Water Control",
            TidalMasterAbilities.createWaterDash(),
            TidalMasterAbilities.createCrashingWave(),
            TidalMasterAbilities.createWhirlpool(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createTerraGuardianBook() {
        return new PowerBook(
            "terra_guardian",
            "Terra Guardian",
            "Elemental",
            "Earth Power",
            TerraGuardianAbilities.createSeismicSlam(),
            TerraGuardianAbilities.createStoneAegis(),
            TerraGuardianAbilities.createPrisonOfEarth(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createLightWeaverBook() {
        return new PowerBook(
            "light_weaver",
            "Light Weaver",
            "Elemental",
            "Radiant Power",
            LightWeaverAbilities.createRadiantFlash(),
            LightWeaverAbilities.createHealingLight(),
            LightWeaverAbilities.createSolarStrike(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createGravityLordBook() {
        return new PowerBook(
            "gravity_lord",
            "Gravity Lord",
            "Elemental",
            "Gravity Manipulation",
            GravityLordAbilities.createWeightOfTheWorld(),
            GravityLordAbilities.createZeroGravity(),
            GravityLordAbilities.createSingularity(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createMagmaKingBook() {
        return new PowerBook(
            "magma_king",
            "Magma King",
            "Elemental",
            "Lava Power",
            MagmaKingAbilities.createMoltenStrike(),
            MagmaKingAbilities.createLavaSurge(),
            MagmaKingAbilities.createVolcanicHeart(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createAbyssDwellerBook() {
        return new PowerBook(
            "abyss_dweller",
            "Abyss Dweller",
            "Elemental",
            "Deep Water",
            AbyssDwellerAbilities.createPressureWave(),
            AbyssDwellerAbilities.createWaterBind(),
            AbyssDwellerAbilities.createMaelstrom(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createWindFuryBook() {
        return new PowerBook(
            "wind_fury",
            "Wind Fury",
            "Elemental",
            "Air Power",
            WindFuryAbilities.createWindDash(),
            WindFuryAbilities.createBladeOfWind(),
            WindFuryAbilities.createTornado(),
            UUID.randomUUID()
        );
    }
    
    private PowerBook createMountainHeartBook() {
        return new PowerBook(
            "mountain_heart",
            "Mountain Heart",
            "Elemental",
            "Earth's Might",
            MountainHeartAbilities.createUnyieldingWill(),
            MountainHeartAbilities.createEarthShaker(),
            MountainHeartAbilities.createWorldBreaker(),
            UUID.randomUUID()
        );
    }
    
    // Rest of the methods remain the same...
    public boolean isPowerBook(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
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
