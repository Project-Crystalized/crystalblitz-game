package cc.crystalized;

import gg.crystalized.lobby.LevelManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.FloodgateApi;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class GameManager {

    public Teams teams;
    public BossbarManager bossbar = new BossbarManager();
    public WorldBorderManager worldborder = new WorldBorderManager();
    //The list of pure shard generators, added for health and disabeling them - Mish
    public final List<PureShardGenerator> pureShardGenerators = new ArrayList<>();
    public static List<PlayerData> playerDatas = new ArrayList<>();
    public static GameTypes GameType;
    //All the stale overflow generators, pure overflow handeled in Pure generators
    private final List<CrystalOverFlowGeneration> crystalOverflowsStaleGenerators = new ArrayList<>();

    enum GameTypes {
        Custom,
        StandardSolos,
        StandardDuos,
        StandardTrios,
        StandardSquads,
    }

    public GameManager(GameTypes type) {
        Bukkit.getServer().sendMessage(text("Starting Game!"));
        for (Entity e : Bukkit.getWorld("world").getEntities()) {
            if (e instanceof Villager || e instanceof TextDisplay || e instanceof Arrow || e instanceof Item) {
                e.remove();
            }
        }

        GameType = type;
        teams = new Teams(type);
        TeamStatus.Init();
        playerDatas.clear();
        setupEntities();
        //The pure shard generators set up
        setupPureShardGenerators();
        //The set up for stale crystals oveflow generators, no need for Pure shards as that is inside pure shard generators already
        setupStaleCrystalOverflowGenerators();

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getEnderChest().setMaxStackSize(54);
            p.getEnderChest();
            p.getEnderChest().clear();
        }
        for (Player p : crystalBlitz.getInstance().getOnlinePlayers()) {
            givePlayerItems(p);
            Teams.setPlayerDisplayNames(p);
            p.setGameMode(GameMode.SURVIVAL);
            new ScoreboardManager(p);
            for (Player player : Bukkit.getOnlinePlayers()) {
                p.unlistPlayer(player);
            }

            p.getInventory().setItem(0, CrystalBlitzItems.getCBItem("wooden_sword").item);
            p.getInventory().setItem(1, CrystalBlitzItems.getCBItem("wooden_pickaxe").item);
            Location ploc = new Location(Bukkit.getWorld("world"),
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[0],
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[1],
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[2]
            );
            //So if falling not die immiditely when teleported
            p.setFallDistance(0);
            p.teleport(ploc);
            playerDatas.add(new PlayerData(p));
            new CustomPlayerNametags(p);
        }
        for (String s : teams.spectator) {
            Player p = Bukkit.getPlayer(s);
            Teams.setPlayerDisplayNames(p);
            p.teleport(new Location(Bukkit.getWorld("world"),
                    crystalBlitz.getInstance().mapdata.spectator_spawn[0],
                    crystalBlitz.getInstance().mapdata.spectator_spawn[1],
                    crystalBlitz.getInstance().mapdata.spectator_spawn[2]
            ));
            p.setGameMode(GameMode.SPECTATOR);
            new ScoreboardManager(p);
            playerDatas.add(new PlayerData(p));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                //Main game loop
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getGameMode().equals(GameMode.SURVIVAL) && p.getY() < crystalBlitz.getInstance().mapdata.DeathLimit) {
                        p.damage(40, DamageSource.builder(DamageType.OUT_OF_WORLD).build());
                    }
                    TabMenu.sendTabMenu(p);
                }

                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1);

        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getWorldBorder().isInside(p.getLocation()) && p.getGameMode().equals(GameMode.SURVIVAL)) {
                        p.damage(1, DamageSource.builder(DamageType.OUTSIDE_BORDER).build());
                    }
                }
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 15);
    }

    public static void ForceEndGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                LevelManager.giveExperience(p, 5);
                LevelManager.giveMoney(p, 20);
            } catch (NoClassDefFoundError e) {}
            p.kick();
        }

        for (Entity e : Bukkit.getWorld("world").getEntities()) {
            if (e instanceof Villager || e instanceof TextDisplay || e instanceof Arrow || e instanceof Item) {
                e.remove();
            }
        }

        Bukkit.getLogger().log(Level.INFO, "Removing player-made blocks, please wait before rejoining...");

        new BukkitRunnable() {
            int i = 0;

            public void run() {
                Set<Block> remove_set = new HashSet<>();
                for (Block block : crystalBlitz.getInstance().Blocks) {
                    Bukkit.getLogger().log(Level.INFO,
                            "Set Block " + block.getType() + " at X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ() + " to air."
                    );
                    if (Bukkit.getOnlinePlayers().size() != 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.kick(text("[!] We are still clearing blocks, please wait before rejoining..."));
                        }
                    }
                    i++;
                    block.setType(Material.AIR);
                    remove_set.add(block);
                    if (i > 10) {
                        break;
                    }
                }
                crystalBlitz.getInstance().Blocks.removeAll(remove_set);
                if (crystalBlitz.getInstance().Blocks.isEmpty()) {
                    Bukkit.getLogger().log(Level.INFO, "Removed all player-made blocks! You may rejoin to start another game");
                    for (TeamData td : Teams.team_datas) {
                        td.nexus.resetNexuses();
                    }
                    //ensures that all the pure shards generators will be propely fixed/revived at game shut down
                    //TODO probobly should make it one method
                    crystalBlitz.getInstance().gamemanager.revivePureShardGenerators();
                    crystalBlitz.getInstance().gamemanager.removePureShardHealthBars();
                    crystalBlitz.getInstance().gamemanager.cancelOverflowGenerationTasks();
                    crystalBlitz.getInstance().gamemanager = null;
                    cancel();
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1);
    }

    public void destroyAllNexuses() {
        for (TeamData td : Teams.team_datas) {
            td.nexus.destroyNexus(td.name);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, "crystalized:effect.nexus_crystal_destroyed", 50, 1);
        }
    }

    private static void setupEntities() {
        Component name = text("Shop");

        for (String team : Teams.teams) {
            if (!team.equals("spectator")) {
                Location loc = new Location(
                        Bukkit.getWorld("world"),
                        crystalBlitz.getInstance().mapdata.getShop(team)[0] + 0.5,
                        crystalBlitz.getInstance().mapdata.getShop(team)[1],
                        crystalBlitz.getInstance().mapdata.getShop(team)[2] + 0.5
                );
                Villager shop = Bukkit.getWorld("world").spawn(loc, Villager.class, entity -> {
                    entity.setGravity(true);
                    entity.setInvulnerable(true);
                    entity.setAI(false);
                    entity.setCustomNameVisible(true);
                    entity.customName(name);
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 200, false, false, false));
                });
            }
        }
    }

    //Gives players leather armor of their team's colour, do anything else seperately
    public static void givePlayerItems(Player p) {
        ShopListener.buyItem(p, CrystalBlitzItems.getCBItem("leather_armor"));
    }

    public static void StartEndGame(String winning_team, TeamData td) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, "crystalized:effect.ls_game_won", 50, 1);
            if (GameManager.GameType.equals(GameTypes.StandardSolos)) {
                Player lastPlayer = Bukkit.getPlayer(Teams.get_team_from_string(winning_team).getFirst());
                p.showTitle(Title.title(
                        lastPlayer.displayName(),
                        translatable("crystalized.game.knockoff.win").color(YELLOW),
                        Title.Times.times(Duration.ofMillis(250), Duration.ofSeconds(5), Duration.ofMillis(1000)))
                );
                p.sendMessage(lastPlayer.displayName().append(text(" ")).append(translatable("crystalized.game.knockoff.win").color(YELLOW)));
            } else {
                p.showTitle(Title.title(
                        text(td.symbol).append(translatable("crystalized.game.generic.team." + td.name).color(TextColor.color(td.color.asRGB()))).append(text(td.symbol)),
                        translatable("crystalized.game.knockoff.win").color(YELLOW),
                        Title.Times.times(Duration.ofMillis(250), Duration.ofSeconds(5), Duration.ofMillis(1000)))
                );
                p.sendMessage(
                        text(td.symbol).append(translatable("crystalized.game.generic.team." + td.name).color(TextColor.color(td.color.asRGB()))).append(text(td.symbol))
                                .append(text(" ")).append(translatable("crystalized.game.knockoff.win").color(YELLOW))
                );
            }
            if (Teams.getPlayerTeam(p).equals(td.name)) {
                p.playSound(p, "crystalized:effect.ls_game_won", 50, 1);
            } else {
                p.playSound(p, "crystalized:effect.ls_game_lost", 50, 1);
            }
        }
        CrystalBlitzDatabase.save_game(winning_team);

        new BukkitRunnable() {
            int timer = 0;
            FloodgateApi floodgateapi = FloodgateApi.getInstance();

            @Override
            public void run() {
                switch (timer) {
                    case 2:
                        Collections.sort(playerDatas, new PlayerDataComparator());
                        Collections.reverse(playerDatas);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (floodgateapi.isFloodgatePlayer(p.getUniqueId())) {
                                p.sendMessage(Component.text("-".repeat(40)).color(GOLD));
                            } else {
                                p.sendMessage(Component.text(" ".repeat(55)).color(GOLD).decoration(TextDecoration.STRIKETHROUGH,  true));
                            }
                        }
                        Bukkit.getServer().sendMessage(Component.text("")
                                .append(Component.text("\n").append(Component.translatable("crystalized.game.crystalblitz.name").color(LIGHT_PURPLE)).append(Component.text(" ").color(WHITE))) //TODO add symbol here
                                .append(Component.text("\n").append(Component.translatable("crystalized.game.generic.gameresults").color(BLUE)))
                        );
                        if (playerDatas.size() > 0) {
                            PlayerData first = playerDatas.get(0);
                            Bukkit.getServer().sendMessage(Component.text("   1st. ")
                                    .append(Component.text(first.p.getName())).color(GREEN).append(text(" ".repeat(20 - first.p.getName().length())))
                                    .append(Component.text("" + first.kills))
                            );
                        }
                        if (playerDatas.size() > 1) {
                            PlayerData second = playerDatas.get(1);
                            Bukkit.getServer().sendMessage(Component.text("   2nd. ")
                                    .append(Component.text(second.p.getName())).color(YELLOW).append(text(" ".repeat(20 - second.p.getName().length())))
                                    .append(Component.text("" + second.kills))
                            );
                        }
                        if (playerDatas.size() > 2) {
                            PlayerData third = playerDatas.get(2);
                            Bukkit.getServer().sendMessage(Component.text("   3rd. ")
                                    .append(Component.text(third.p.getName())).color(YELLOW).append(text(" ".repeat(20 - third.p.getName().length())))
                                    .append(Component.text("" + third.kills))
                            );
                        }

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (floodgateapi.isFloodgatePlayer(p.getUniqueId())) {
                                p.sendMessage(Component.text("-".repeat(40)).color(GOLD));
                            } else {
                                p.sendMessage(Component.text(" ".repeat(55)).color(GOLD).decoration(TextDecoration.STRIKETHROUGH,  true));
                            }
                        }
                        break;
                    case 12, 13, 14:
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player, "minecraft:block.note_block.hat", SoundCategory.MASTER,50, 1); //TODO placeholder sound
                        }
                        break;
                    case 15:
                        ForceEndGame();
                        cancel();
                        break;
                }
                timer++;
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 20,20);
    }

    public static PlayerData getPlayerData(Player p) {
        for (PlayerData pd : playerDatas) {
            if (pd.p == p) {
                return pd;
            }
        }

        return null;
    }

    //Methods for Pure shard generators

    //The set up method for pure shard generators
    private void setupPureShardGenerators() {
        //Gets the world assuming it is titled world
        World world = Bukkit.getWorld("world");
        //If null nothing happens
        if (world == null) {
            return;
        }
        //Scans loaded chunks once when the game starts. Searching for the generators blocks and setting them
        for (Chunk chunk : world.getLoadedChunks()) {

            //Multiplied by 16 as chunck.getX/Z is a chunk cordinate and needs to converted
            int startX = chunk.getX() * 16;
            int startZ = chunk.getZ() * 16;
            //Goes through x an z of the chunck and the world height
            for (int x = startX; x < startX + 16; x++) {
                for (int z = startZ; z < startZ + 16; z++) {
                    for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
                        //gets the block at the location
                        Block block = world.getBlockAt(x, y, z);
                        //Skips non pure shard generator block
                        if (!PureShardGenerator.isPureGeneratorSourceBlock(block)) {
                            continue;
                        }
                        //Ensure that the generator is not created for the upper block, only lower blocks
                        if (PureShardGenerator.isPureGeneratorSourceBlock(block.getRelative(BlockFace.DOWN))) {
                            continue;
                        }
                        //Creates the new pure shard generator based on the bottom block of the generator
                        pureShardGenerators.add(new PureShardGenerator(block));
                    }
                }
            }
        }
    }
    //This is to get the pure shard generator based on location
    public PureShardGenerator getPureShardGenerator(Location loc) {
        //Goes through the generators and if it is a source block location returns the generator
        //If not found returns null
        for (PureShardGenerator generator : pureShardGenerators) {
            if (generator.isSourceBlock(loc)) {
                return generator;
            }
        }

        return null;
    }
    //This gets the generator from the spikes locations
    public PureShardGenerator getPureShardGeneratorFromSpike(Location loc) {
        //Goes through the generators and checks it is owns the spike bases on location, returns the generator if found
        for (PureShardGenerator generator : pureShardGenerators) {
            if (generator.ownsSpike(loc)) {
                return generator;
            }
        }

        return null;
    }
    //This methods revies all the generators for gen upgrade/game end/shut down
    public void revivePureShardGenerators() {
        //goes through all pure shards generators and revies each one
        for (PureShardGenerator generator : pureShardGenerators) {
            generator.revive();
        }
    }
    //Removes health bar for all generators on unexpected server shut down
    public void removePureShardHealthBars() {
        for (PureShardGenerator generator : pureShardGenerators) {
            generator.removeHealthBar();
        }
    }
    //cancels all overflow generators.
    public void cancelOverflowGenerationTasks() {
        for (PureShardGenerator generator : pureShardGenerators) {
            generator.cancelOverflowGenerationTask();
        }
        //Cancels all stale oveflow generators
        for (CrystalOverFlowGeneration overFlowStaleGeneration : crystalOverflowsStaleGenerators) {
            overFlowStaleGeneration.cancelOverflowGeneration();
        }
        crystalOverflowsStaleGenerators.clear();
    }
    //This method sets up all stale overflow generators
    private void setupStaleCrystalOverflowGenerators() {
        new BukkitRunnable() {
            @Override
            public void run() {
                //Goes through team data, and gets the stale shard location
                for (TeamData team : Teams.team_datas) {
                    Location staleLocation = crystalBlitz.getInstance().mapdata.getStaleShardLoc(team.name);
                    //Slightly added the height to account for the gen upgrade which makes it higher, to not overcomplicate with chaning location for generating
                    Location overflowLocation = staleLocation.clone().add(0.5, 2.2, 0.5);
                    //Starts the stale oveflow generation and null pure generator as it is not a pure generator.
                    crystalOverflowsStaleGenerators.add(new CrystalOverFlowGeneration(overflowLocation, OverflowGeneratorType.STALE, null));
                }
            } //small delay added during debuging.
        }.runTaskLater(crystalBlitz.getInstance(), 1);
    }
}

class TabMenu {

    static Component StatsPlayerList = text("");
    static Component alive = text("[Alive] ").color(WHITE);
    static Component dead = text("[Dead] ").color(WHITE);
    static Component eliminated = text("[Eliminated] ").color(WHITE);

    private static void addToStatsString(Component s) {
        StatsPlayerList = StatsPlayerList.append(s);
    }

    public static void sendTabMenu(Player p) {
        StatsPlayerList = text("");
        Teams t = crystalBlitz.getInstance().gamemanager.teams;

        p.sendPlayerListHeader(
                text("\n")
                        .append(text("Crystalized: Crystal Blitz").color(LIGHT_PURPLE))
                        .append(text("\n"))
        );


        addToStatsString(text("---------------------------------------------------\n").color(GRAY));
        for (TeamData td : t.team_datas) {
            List<String> team = t.get_team_from_string(td.name); //probably unsafe, im just shooting in the dark to see if this works
            if (!team.isEmpty()) {
                addToStatsString(text("\n").append(text(td.symbol)).append(translatable("crystalized.game.generic.team." + td.name).color(TextColor.color(td.color.asRGB()))).append(text("\n")));
                for (String s : team) {
                    Player player = Bukkit.getPlayer(s);
                    PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(player);
                    if (pd.isEliminated) {
                        addToStatsString(eliminated);
                    } else if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                        addToStatsString(dead);
                    } else {
                        addToStatsString(alive);
                    }
                    addToStatsString(text("").append(pd.cachedRankIcon_large).append(text(" ")).append(player.displayName()).append(text(" \uE101: " + pd.kills)).append(text(" \uE101(N): " + pd.nexus_kills)).append(text(" \uE103: " + pd.deaths)).append(text("\n")));
                }
            }
        }
        addToStatsString(text("\n---------------------------------------------------\n\n").color(GRAY)
                .append(text("Crystal Blitz Version: " + crystalBlitz.getInstance().getDescription().getVersion())).color(DARK_GRAY).append(text("\n"))
        );

        p.sendPlayerListFooter(StatsPlayerList);
    }

}
