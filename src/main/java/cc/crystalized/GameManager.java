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
        //private final List<CrystalOverFlowGeneration> crystalOverflowsStaleGenerators = new ArrayList<>();
    //Now handles entier stale generators
    private final List<StaleShardGenerator> staleShardGenerators = new ArrayList<>();

    enum GameTypes {
        Custom,
        StandardSolos,
        StandardDuos,
        StandardTrios,
        StandardSquads,
    }

    public GameManager(GameTypes type) {
        Bukkit.getServer().sendMessage(translatable("crystalized.game.generic.starting"));
        //changed so it works with the game world
        for (Entity e : crystalBlitz.getInstance().getGameWorld().getEntities()) {
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
        //setupPureShardGenerators();
        //The stale shard generators set up.
        setupStaleShardGeneretors();

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
            Location ploc = new Location(crystalBlitz.getInstance().getGameWorld(),
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
            p.teleport(new Location(crystalBlitz.getInstance().getGameWorld(),
                    crystalBlitz.getInstance().mapdata.spectator_spawn[0],
                    crystalBlitz.getInstance().mapdata.spectator_spawn[1],
                    crystalBlitz.getInstance().mapdata.spectator_spawn[2]
            ));
            p.setGameMode(GameMode.SPECTATOR);
            new ScoreboardManager(p);
            playerDatas.add(new PlayerData(p));
        }
        //set up for pure generators was moved here during debuging
        setupPureShardGenerators();

        new BukkitRunnable() {
            @Override
            public void run() {
                //moved so it checks first and cancels the task propely for the game end, must return so things below don't excecute
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                    return;
                }
                //Main game loop
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getGameMode().equals(GameMode.SURVIVAL) && p.getY() < crystalBlitz.getInstance().mapdata.DeathLimit) {
                        p.damage(40, DamageSource.builder(DamageType.OUT_OF_WORLD).build());
                    }
                    TabMenu.sendTabMenu(p);
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
        //Created a small refernce to crystalBlitz plugin so to not have to write crystalBlitz.getInstance() each time
        //for redability sake
        crystalBlitz cbPlugin = crystalBlitz.getInstance();
        //This is to clean up all the left over tasks
        GameManager oldGameManager = cbPlugin.gamemanager;
        if (oldGameManager != null) {
            oldGameManager.bossbar.removeBossBar();
            oldGameManager.removePureShardHealthBars();
            oldGameManager.cancelOverflowGenerationTasks();
            //TODO: Any more task cancelations should go here which are needing the game world
        }
        World sourceWorld = cbPlugin.getSourceWorld();

        if (sourceWorld == null) {
            cbPlugin.getLogger().severe("Could not return players to waiting world because it is null for some reason!");
            return;
        }
        //Returns everyone to the waiting world.
        Location lobbyLocation = new Location(sourceWorld,
                cbPlugin.mapdata.queue_spawn[0],
                cbPlugin.mapdata.queue_spawn[1],
                cbPlugin.mapdata.queue_spawn[2]
        );
        //this check is specificly to see if players should be kicked at the end of the game in config.
        //For self hosting it can be disabled, for servers with lobby plugin it should kick players.
        boolean kickPlayersAtGameEnd = cbPlugin.getConfig().getBoolean("kick_players_at_game_end");

        for (Player p : Bukkit.getOnlinePlayers()) {
            //cbPlugin.getLogger().info(p.getName() + " passengers on player +" + p.getPassengers().size() + " vehicle =" + (p.getVehicle() != null));
            //This is to make sure that the player has no passanagers as cross dimensional teleperotation doesn't work with pasanagers.
            for (Entity passenger : new ArrayList<>(p.getPassengers())) {
                p.removePassenger(passenger);
                //Removes the pasanager entity completely
                passenger.remove();
            }
            //ensures player is cleared and not riding anything, as teleportation would fail to a diffrent dimension
            p.leaveVehicle();
            p.getInventory().clear();
            p.setFallDistance(0);
            if (kickPlayersAtGameEnd) {
                //The rewards are given only when kicking is enabled, as for self hosting there is no point for rewards.
                try {
                    LevelManager.giveExperience(p, 5);
                    LevelManager.giveMoney(p, 20);
                } catch (NoClassDefFoundError ignored) {}
                p.kick();
                //continues to the next players so the code after will only excecute if kick players is false in the loop
                continue;
            }
            //makes sure player is set to adventure and teleported to the waiting world
            p.setGameMode(GameMode.ADVENTURE);
            boolean teleported = p.teleport(lobbyLocation);
            if (!teleported) {
                cbPlugin.getLogger().warning("Failed to return to the waiting world: " + p.getName());
            }
            //sets the scrorbored to main one
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        //Clears the blocks from memory, doesn't have an effect on the world
        cbPlugin.Blocks.clear();
        //sets the game manager to null
        cbPlugin.gamemanager = null;

        //Does the world recreation on the new tick
        Bukkit.getScheduler().runTask(cbPlugin , () -> {
            //destroys world
            if (!cbPlugin.mapManager.destroyGameWorld()) {
                cbPlugin.getLogger().severe("Failed to destroy the game world!");
                return;
            }
            //recreates it
            if (!cbPlugin.mapManager.createGameWorld()) {
                cbPlugin.getLogger().severe("Failed to prepare the game world for the next game!");
                return;
            }
            //when sussesfull should log that the next game is ready
            cbPlugin.getLogger().info("CrystalBlitz game finished. System is ready for the next game.");
            //sets the world rules to what they supposed to be.
            cbPlugin.setupWorldRules(cbPlugin.getGameWorld());
        });
        //Resets the players tab view and score board for the waiting world.
        if (!kickPlayersAtGameEnd) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                for (Player other : Bukkit.getOnlinePlayers()) {
                    p.listPlayer(other);
                }
                new QueueScoreboard(p);
                p.sendPlayerListHeaderAndFooter(
                        //Header
                        text("\nCrystalized: Crystal Blitz\n"),
                        //Footer
                        text("\n").append(text("Crystal Blitz Version: " + cbPlugin.getDescription().getVersion())).append(text("\n"))
                );
            }
        }
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
        Component name = translatable("crystalized.game.crystalblitz.shop");

        for (String team : Teams.teams) {
            if (!team.equals("spectator")) {
                Location loc = new Location(
                        crystalBlitz.getInstance().getGameWorld(),
                        crystalBlitz.getInstance().mapdata.getShop(team)[0] + 0.5,
                        crystalBlitz.getInstance().mapdata.getShop(team)[1],
                        crystalBlitz.getInstance().mapdata.getShop(team)[2] + 0.5
                );
                Villager shop = crystalBlitz.getInstance().getGameWorld().spawn(loc, Villager.class, entity -> {
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
        //Now works with the new world system
        World world = crystalBlitz.getInstance().getGameWorld();
        //If null nothing happens
        if (world == null) {
            crystalBlitz.getInstance().getLogger().warning("PUREGENS: Game world was null!");
            return;
        }
        //Loggers to see which world it is scaning
        //crystalBlitz.getInstance().getLogger().info("PUREGENS: Scanning world: " + world.getKey());
        //crystalBlitz.getInstance().getLogger().info("PUREGENS: Loaded chunks = " + world.getLoadedChunks().length);

        //tracks how many were found
        int found = 0;
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
                        found++;
                        crystalBlitz.getInstance().getLogger().info("PUREGENS: Found generator at " + x + ", " + y + ", " + z);
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
        for (StaleShardGenerator generator : staleShardGenerators) {
            generator.canelStaleOverflowGenerations();
        }
        pureShardGenerators.clear();
        staleShardGenerators.clear();
    }
    //This method sets up all stale generators.
    private void setupStaleShardGeneretors() {
        new BukkitRunnable() {
            @Override
            public void run() {
                //Goes through team data, and gets the stale shard location
                for (TeamData team : Teams.team_datas) {
                    Location staleLocation = crystalBlitz.getInstance().mapdata.getStaleShardLoc(team.name);
                    //Sets up the stale shard generators, based on team name and location.
                    staleShardGenerators.add(new StaleShardGenerator(team.name, staleLocation));
                }
            } //small delay added during debuging.
        }.runTaskLater(crystalBlitz.getInstance(), 1);
    }
    //This is to be able to get stale shard generator based on team name for upgrades.
    public StaleShardGenerator getStaleShardGenerator(String teamName) {
        for (StaleShardGenerator generator : staleShardGenerators) {
            if (generator.getTeamName().equals(teamName)) {
                return generator;
            }
        }
        return null;
    }
    //This is to be able to get the generator based on the side shard. As behaviours are generators based.
    public StaleShardGenerator getStaleShardGeneratorFromSideShard(Location location) {
        for (StaleShardGenerator generator : staleShardGenerators) {
            if (generator.ownsSideShard(location)) {
                return generator;
            }
        }

        return null;
    }


}

class TabMenu {

    static Component StatsPlayerList = text("");
    static Component alive = translatable("crystalized.game.generic.alive").color(WHITE);
    static Component dead = translatable("crystalized.game.generic.dead").color(WHITE);
    static Component eliminated = translatable("crystalized.game.generic.eliminated").color(WHITE);

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
