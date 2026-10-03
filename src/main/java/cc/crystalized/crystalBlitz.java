package cc.crystalized;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import gg.crystalized.lobby.Lobby_plugin;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.ByteBuffer;
import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public final class crystalBlitz extends JavaPlugin {

    public Set<Block> Blocks = new HashSet<>();

    //Now mapdata is being created in on enabled as it needs mapmanager
    public MapData mapdata;
    //map manager is for world/map reseting after crashesh and game ends.
    public MapManager mapManager;
    public GameManager gamemanager;
    public boolean is_force_starting = false;
    private boolean isCountingDown = false;
    private int configVersion = 0;
    private boolean GameCountdownStarted = false;
    private int PlayerStartLimit = 3;

    @Override
    public void onLoad(){
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(true).bStats(false);
        PacketEvents.getAPI().load();
        EventManager events = PacketEvents.getAPI().getEventManager();
        events.registerListener(new CrystalBlitzPackets(), PacketListenerPriority.NORMAL);
    }

    @Override
    public void onEnable() {
        //ensures config is saved
        saveDefaultConfig();
        //map manager and map data being created
        mapManager = new MapManager(this);
        mapdata = new MapData();
        //The game world set up, deleting any previous game world dimensions.
        mapManager.setup();

        PacketEvents.getAPI().init();
        Lobby_plugin.getInstance().doNametagsDespitePassive = true;
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("crystalblitz");
            command.then(Commands.literal("start").requires(sender -> sender.getSender().hasPermission("minecraft.command.op"))
                    .executes(ctx -> {
                        if (crystalBlitz.getInstance().gamemanager != null || crystalBlitz.getInstance().isCountingDown) {
                            ctx.getSource().getSender().sendMessage(text("[!] A game is already in progress or is about to start.").color(RED));
                        } else {
                            if (getConfig().getBoolean("teams.enable")) {
                                forceStartGame(GameManager.GameTypes.Custom);
                            } else {
                                ctx.getSource().getSender().sendMessage(text("[!] Missing arguments"));
                            }
                        }
                        return Command.SINGLE_SUCCESS;
                    })
                    .then(Commands.literal("force_StandardSolos").executes(ctx -> {
                        if (crystalBlitz.getInstance().gamemanager != null || crystalBlitz.getInstance().isCountingDown) {
                            ctx.getSource().getSender().sendMessage(text("[!] A game is already in progress or is about to start.").color(RED));
                        } else {
                            if (getConfig().getBoolean("teams.enable")) {
                                forceStartGame(GameManager.GameTypes.Custom);
                            } else {
                                forceStartGame(GameManager.GameTypes.StandardSolos);
                            }
                        }
                        return Command.SINGLE_SUCCESS;
                    }))
                    .then(Commands.literal("force_StandardDuos").executes(ctx -> {
                        if (crystalBlitz.getInstance().gamemanager != null || crystalBlitz.getInstance().isCountingDown) {
                            ctx.getSource().getSender().sendMessage(text("[!] A game is already in progress or is about to start.").color(RED));
                        } else {
                            if (getConfig().getBoolean("teams.enable")) {
                                forceStartGame(GameManager.GameTypes.Custom);
                            } else {
                                forceStartGame(GameManager.GameTypes.StandardDuos);
                            }
                        }
                        return Command.SINGLE_SUCCESS;
                    }))
                    .then(Commands.literal("force_StandardTrios").executes(ctx -> {
                        if (crystalBlitz.getInstance().gamemanager != null || crystalBlitz.getInstance().isCountingDown) {
                            ctx.getSource().getSender().sendMessage(text("[!] A game is already in progress or is about to start.").color(RED));
                        } else {
                            if (getConfig().getBoolean("teams.enable")) {
                                forceStartGame(GameManager.GameTypes.Custom);
                            } else {
                                forceStartGame(GameManager.GameTypes.StandardTrios);
                            }
                        }
                        return Command.SINGLE_SUCCESS;
                    }))
                    .then(Commands.literal("force_StandardSquads").executes(ctx -> {
                        if (crystalBlitz.getInstance().gamemanager != null || crystalBlitz.getInstance().isCountingDown) {
                            ctx.getSource().getSender().sendMessage(text("[!] A game is already in progress or is about to start.").color(RED));
                        } else {
                            if (getConfig().getBoolean("teams.enable")) {
                                forceStartGame(GameManager.GameTypes.Custom);
                            } else {
                                forceStartGame(GameManager.GameTypes.StandardSquads);
                            }
                        }
                        return Command.SINGLE_SUCCESS;
                    }))
            );
            command.then(Commands.literal("end").requires(sender -> sender.getSender().hasPermission("minecraft.command.op")).executes(ctx -> {
                if (getInstance().gamemanager == null) {
                    ctx.getSource().getSender().sendMessage(text("[!] This cannot be used in the waiting lobby."));
                } else {
                    gamemanager.ForceEndGame();
                }
                return Command.SINGLE_SUCCESS;
            }));
            command.then(Commands.literal("debug").requires(sender -> sender.getSender().hasPermission("minecraft.command.op"))
                    .then(Commands.literal("shatter_my_nexus").executes(ctx -> {
                        if (gamemanager != null && ctx.getSource().getSender() instanceof Player p) {
                            TeamData td = Teams.getTeamData(p);
                            td.nexus.health = 0;
                            td.nexus.destroyNexus(td.name, p);
                        }
                        return 0;
                    }))
            );

            LiteralCommandNode<CommandSourceStack> buildCommand = command.build();
            commands.registrar().register(buildCommand);
        });

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new ShopListener(), this);
        this.getServer().getPluginManager().registerEvents(new GameCompass(), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "crystalized:crystalblitz");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "crystalized:main");

        //This is the waiting world rules, the same rules apply to it
        World w  = getSourceWorld();
        if(w != null){
            setupWorldRules(w);
        }

        CrystalBlitzDatabase.setup_databases();
        Shop.setupShop();
        CrystalBlitzItems.SetupItems();

        //Config stuff
        saveResource("config.yml", false); //I dont trust this
        if (getConfig().getInt("version") != 1) {
            configVersion = getConfig().getInt("version");
            getLogger().log(Level.SEVERE, "Invalid Version, Please update your config. Expecting 1 but found " + configVersion + ". You may experience fatal issues.");
        }

        new BukkitRunnable() {
            public void run() {
                if (gamemanager != null) {
                    return;
                }
                if (is_force_starting) {
                    is_force_starting = false;
                    if (Bukkit.getOnlinePlayers().size() > 16) {
                        Bukkit.getServer().sendMessage(text("[!] Too many players are online, aborting game as theres no compatible mode to support " + Bukkit.getOnlinePlayers().size() + " players. Make sure the player size in server.properties is capped to 16 maximum."));
                        return;
                    }
                    if (Bukkit.getOnlinePlayers().isEmpty()) {
                        return;
                    } else {
                        if (Bukkit.getOnlinePlayers().size() > 8) {
                            forceStartGame(GameManager.GameTypes.StandardDuos);
                        } else {
                            forceStartGame(GameManager.GameTypes.StandardSolos);
                        }
                    }
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);

        new BukkitRunnable() {
            public void run() {
                if (gamemanager != null) {
                    GameCountdownStarted = false;
                } else {
                    if (!GameCountdownStarted && !isCountingDown) {
                        if (Bukkit.getOnlinePlayers().size() > PlayerStartLimit || Bukkit.getOnlinePlayers().size() == PlayerStartLimit) {
                            GameCountdown();
                        }
                    } else if (Bukkit.getOnlinePlayers().size() < PlayerStartLimit) {
                        GameCountdownStarted = false;
                    }
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        //Ensuring that all generators will be revived before shut down happens
        //and health bars removed
        if (gamemanager != null) {
            gamemanager.revivePureShardGenerators();
            gamemanager.removePureShardHealthBars();
            gamemanager.cancelOverflowGenerationTasks();
        }
    }

    public void forceStartGame(GameManager.GameTypes type) {
        isCountingDown = true;
        new BukkitRunnable() {
            int timer = 0;
            public void run() {
                timer++;
                switch (timer) {
                    case 3 -> {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showTitle(Title.title(translatable("crystalized.game.generic.startingin").color(GREEN), text("3").color(RED)
                                            .append(Component.text(" 2 1").color(GRAY))
                                    ,Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofSeconds(1))));
                            player.playSound(player, "crystalized:effect.countdown", 50, 1);
                        }
                    }
                    case 4 -> {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showTitle(Title.title(translatable("crystalized.game.generic.startingin").color(GREEN), text("3").color(GRAY)
                                            .append(Component.text(" 2").color(RED))
                                            .append(Component.text(" 1").color(GRAY))
                                    ,Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofSeconds(1))));
                            player.playSound(player, "crystalized:effect.countdown", 50, 1);
                        }
                    }
                    case 5 -> {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showTitle(Title.title(translatable("crystalized.game.generic.startingin").color(GREEN), text("3 2 ").color(GRAY)
                                            .append(Component.text("1").color(RED))
                                    ,Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofSeconds(1))));
                            player.playSound(player, "crystalized:effect.countdown", 50, 1);
                        }
                    }
                    case 6 -> {
                        //Gets the game world and sets it's up for the game.
                        World gameWorld = getGameWorld();
                        if (gameWorld == null) {
                            getLogger().severe("CrystalBlitz game world was not prepared !");
                            return;
                        }
                        setupWorldRules(gameWorld);
                        //The game manager creation moved here, so there is no moments the game would think that game manager is null.
                        gamemanager = new GameManager(type);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showTitle(Title.title(translatable("crystalized.game.generic.go").color(GOLD), text(" "),
                                    Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofSeconds(1))));
                            player.playSound(player, "crystalized:effect.countdown_end", 50, 1);
                        }
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("start_game");
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            out.writeUTF(p.getName());
                        }
                        Player p = (Player) Bukkit.getOnlinePlayers().toArray()[0];
                        p.sendPluginMessage(crystalBlitz.getInstance(), "crystalized:main", out.toByteArray());

                        isCountingDown = false;
                        cancel();
                    }
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
    }

    private void GameCountdown() {
        GameCountdownStarted = true;
        new BukkitRunnable() {
            int timer = 15;
            public void run() {
                if (gamemanager != null || is_force_starting || isCountingDown) {
                    cancel();
                }
                Bukkit.getServer().sendActionBar(translatable("crystalized.game.generic.startingin").color(NamedTextColor.GREEN)
                        .append(text(" " + (timer + 1) ).color(NamedTextColor.DARK_GRAY))
                        .append(text(" " + timer).color(RED))
                        .append(text(" " + (timer - 1) ).color(NamedTextColor.DARK_GRAY))
                );
                timer--;
                if (!GameCountdownStarted && getInstance().is_force_starting) {
                    Bukkit.getServer().sendMessage(translatable("crystalized.game.generic.gamecancelled").color(RED));
                    GameCountdownStarted = false;
                    cancel();
                }
                if (timer == 0) {
                    crystalBlitz.getInstance().is_force_starting = true;
                    GameCountdownStarted = false;
                    cancel();
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
    }

    public static crystalBlitz getInstance() {
        return getPlugin(crystalBlitz.class);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Teams.getPlayerTeam(p).equals("spectator")) {
                players.add(p);
            }
        }

        return players;
    }
    /*Gets the worlds, the source world is the waiting world, game world is where the game is happening and
    * active world is where the game should take the player if the game is going on or not.
    * */
    public World getSourceWorld() {
        return mapManager.getSourceWorld();
    }
    public World getGameWorld() {
        return mapManager.getGameWorld();
    }
    public World getActiveWorld() {
        return mapManager.getActiveWorld();
    }
    //sets up the rules of the world, applies to waiting/source world and the game world
    public void setupWorldRules(World world) {
        if (world == null) {
            return;
        }
        world.setGameRule(GameRules.SHOW_DEATH_MESSAGES, false);
        world.setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, false);
        world.setGameRule(GameRules.FIRE_DAMAGE, false);
        world.setGameRule(GameRules.SPAWN_PHANTOMS, false);
        world.setGameRule(GameRules.SPAWN_MOBS, false);
        world.setGameRule(GameRules.MOB_GRIEFING, false);
        world.setGameRule(GameRules.RANDOM_TICK_SPEED, 0);
        world.setGameRule(GameRules.NATURAL_HEALTH_REGENERATION, true);
        world.setDifficulty(Difficulty.HARD);
        //Removes any hostile entieies, including bats.
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Monster || entity instanceof Bat) {
                entity.remove();
            }
        }
    }

}

//cba making another .java file - Callum
class CrystalBlitzDatabase{
    private static String dbDir() {
        String d = System.getenv("CRYSTALIZED_DB_DIR");
        if (d == null || d.isBlank()) d = System.getProperty("user.home") + "/databases/test_dbs";
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Path.of(d));
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Could not create database directory: " + d, e);
        }
        return d;
    }
    public static final String URL = "jdbc:sqlite:" + dbDir() + "/crystalblitz_db.sql";

    public static void setup_databases() {
        try {
            String sDriverName = "org.sqlite.JDBC";
            Class.forName(sDriverName);
            new org.sqlite.JDBC();
        } catch (Exception e) {
            e.printStackTrace();
        }


        String create_cb_games = "CREATE TABLE IF NOT EXISTS CrystalBlitzGames ("
                + "game_id INTEGER PRIMARY KEY,"
                + "map STRING,"
                + "winner_team STRING,"
                + "gametype STRING,"
                + "timestamp INTEGER"
                + ");";
        String create_cb_players = "CREATE TABLE IF NOT EXISTS CbGamesPlayers ("
                + "game INTEGER REFERENCES CrystalBlitzGames(game_id),"
                + "player_uuid BLOB,"
                + "team STRING,"
                + "kills INTEGER,"
                + "deaths INTEGER,"
                + "nexus_kills INTEGER," //nexuses broken
                + "games_won INTEGER"
                + ");";
        addGameIdColumn();
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            stmt.execute(create_cb_games);
            stmt.execute(create_cb_players);
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
            for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                crystalBlitz.getInstance().getLogger().severe(ste.toString());
            }
        }
    }

    public static void addGameIdColumn(){
        String create_id_column = "ALTER TABLE CrystalBlitzGames ADD COLUMN game_id INTEGER;";
        String check_id_column = "SELECT game_id FROM CrystalBlitzGames LIMIT 1;";

        String create_game_column = "ALTER TABLE CbGamesPlayers ADD COLUMN game INTEGER REFERENCES CrystalBlitzGames(game_id);";
        String check_game_column = "SELECT game FROM CbGamesPlayers LIMIT 1;";

        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.createStatement().execute(check_id_column);
        } catch (SQLException e) {
            // if we catch a sql error, it mean the column doesnt exist, so we add it
            try (Connection conn = DriverManager.getConnection(URL)) {
                conn.createStatement().execute(create_id_column);
            } catch (SQLException ex) {
                Bukkit.getLogger().severe(ex.getMessage());
                Bukkit.getLogger().severe("uh weird error, idk bro ;-; (id)");
            }
        }

        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.createStatement().execute(check_game_column);
        } catch (SQLException e) {
            try (Connection conn = DriverManager.getConnection(URL)) {
                conn.createStatement().execute(create_game_column);
            } catch (SQLException ex) {
                Bukkit.getLogger().severe("uh weird error, idk bro ;-; (game)");
            }
        }
    }

    public static void save_game(String WinningTeam) {
        String save_game = "INSERT INTO CrystalBlitzGames(map, winner_team, gametype, timestamp) VALUES(?, ?, ?, unixepoch())";
        GameManager gm = crystalBlitz.getInstance().gamemanager;

        try (Connection conn = DriverManager.getConnection(URL)) {
            PreparedStatement game_stmt = conn.prepareStatement(save_game);
            game_stmt.setString(1, crystalBlitz.getInstance().mapdata.map_name);
            game_stmt.setString(2, WinningTeam);
            game_stmt.setString(3, gm.GameType.toString());
            game_stmt.executeUpdate();

            int game_id = conn.prepareStatement("SELECT last_insert_rowid();").executeQuery().getInt("last_insert_rowid()");

            String save_player = "INSERT INTO CbGamesPlayers(game, player_uuid, team, kills, deaths, nexus_kills, games_won)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement player_stmt = conn.prepareStatement(save_player);
            for (PlayerData pd : GameManager.playerDatas) {
                if (pd == null || pd.p == null) continue;
                String team = Teams.getPlayerTeam(pd.p.getName());
                if (team == null || team.equals("spectator")) {
									Bukkit.getLogger().severe("HUH a player was a participant but has no team or is spectator??? this cant happen surely :skull: :pray:");
									continue;
								};

                player_stmt.setInt(1, game_id);
                player_stmt.setBytes(2, uuid_to_bytes(pd.p.getUniqueId()));
                player_stmt.setString(3, team);
                player_stmt.setInt(4, pd.kills);
                player_stmt.setInt(5, pd.deaths);
                player_stmt.setInt(6, pd.nexus_kills);
                if (WinningTeam.equals(team)) {
                    player_stmt.setInt(7, 1);
                } else {
                    player_stmt.setInt(7, 0);
                }
                player_stmt.executeUpdate();
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    private static byte[] uuid_to_bytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}

class CrystalBlitzPackets implements PacketListener {
    @Override
    public void onPacketSend(PacketSendEvent event){
        if(event.getPacketType() != PacketType.Play.Server.ENTITY_METADATA) {
            return;
        }
        event.markForReEncode(true);
        WrapperPlayServerEntityMetadata metaWrapper = new WrapperPlayServerEntityMetadata(event);
        GameManager gc = crystalBlitz.getInstance().gamemanager;
        Player updated_player = get_player_by_entity_id(metaWrapper.getEntityId());
        //seperated the checks as it was giving me exceptions when joining late as spectator.
        if (gc == null || updated_player == null) {
            return;
        }
        //gets the player who is viewing and makes sure it is not null
        Player viewer = Bukkit.getPlayer(event.getUser().getUUID());
        if (viewer == null) {
            return;
        }
        //gets the team of the player and the viewers team, and makes sure they are not null
        String updatedTeam = Teams.getPlayerTeam(updated_player);
        String viewerTeam = Teams.getPlayerTeam(viewer);
        if (updatedTeam == null || viewerTeam == null) {
            return;
        }
        //prevents glowing if not from the same team
        if (!updatedTeam.equals(viewerTeam)) {
            return;
        }
        List<EntityData<?>> data = metaWrapper.getEntityMetadata();
        data.add(new EntityData<>(0, EntityDataTypes.BYTE, ((Integer) 0x40).byteValue()));
        metaWrapper.setEntityMetadata(data);
    }

    private static Player get_player_by_entity_id(int id) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getEntityId() == id) {
                return player;
            }
        }
        return null;
    }
}

class GameCompass implements Listener {
    @EventHandler
    public void onCompassClick(PlayerInteractEvent e){
        if(e.getItem() == null || e.getItem().getType() != Material.COMPASS){
            return;
        }

        if(e.getPlayer().getGameMode() != GameMode.ADVENTURE){
            return;
        }

        int teamSize = 0;
        ArrayList<List<String>> allPlayerSortedInTeams = new ArrayList<>();
        for(String teamName : Teams.teams){
            List<String> team = Teams.get_team_from_string(teamName);
            if(team == null || team.isEmpty()) continue;
            teamSize = Math.max(team.size(), teamSize);
            allPlayerSortedInTeams.add(team);
        }
        //This fails if it is above 54 like 64 is not divivdalbe by 9, but it is bigger than 54 the final number
        //so added a fall back to make sure it will never exside the limit
        int inventorySize = allPlayerSortedInTeams.size() * teamSize * 2;
        int[] possibleSizes = new int[]{9, 18, 27, 36, 45, 54};
        for(int i : possibleSizes){
            if(inventorySize % 9 == 0) break;
            if(inventorySize <= i){
                inventorySize = i;
                break;
            }
        }
        //a fall back incase it managed to excide the invnetory limit.
        if(inventorySize > 54) {
            inventorySize = 54;
        }
        Inventory inv = Bukkit.createInventory(null, inventorySize, Component.text(""));
        int slot = 0;
        for(List<String> team : allPlayerSortedInTeams){
            for(String name : team){
                //Only displays currently players that are currently alive, so not tping to respawning players
                Player player = Bukkit.getPlayerExact(name);
                if(player == null || player.getGameMode() != GameMode.SURVIVAL) {
                    continue;
                }
                inv.setItem(slot, buildItem(name));
                slot++;
            }
            if(slot % 9 != 0) slot++;
        }
        e.getPlayer().openInventory(inv);
    }

    @EventHandler
    public void onHeadClick(InventoryClickEvent e){
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() != Material.PLAYER_HEAD){
            return;
        }
        if(e.getWhoClicked().getGameMode() != GameMode.ADVENTURE){
            return;
        }
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = skull.getPlayerProfile();
        if(profile == null || profile.getId() == null) return;
        //Makes sure that you can't teleport to dead players otherwise it is weird.
        //Player could die between when the compass was opened.
        Player spectator = (Player) e.getWhoClicked();
        Player player = Bukkit.getPlayer(profile.getId());
        if(player == null || player.getGameMode() != GameMode.SURVIVAL) {
            spectator.sendMessage(Component.text("Can't teleport to a player as they are dead.").color(RED));
            return;
        }
        e.getWhoClicked().teleport(player.getPlayer());
    }

    public static ItemStack buildItem(String name){
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        PlayerProfile profile = player.getPlayerProfile();
        ItemStack play = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) play.getItemMeta();
        skull.setPlayerProfile(profile);
        play.setItemMeta(skull);

        ItemMeta meta = play.getItemMeta();
        Component displayName = Component.text("\uE103").color(WHITE).decoration(ITALIC, false).append(Component.text(name).color(GRAY).decoration(ITALIC, true));
        if(player.getPlayer() != null && player.getPlayer().getGameMode() != GameMode.ADVENTURE) displayName = player.getPlayer().displayName().decoration(ITALIC, false);
        meta.displayName(displayName);
        play.setItemMeta(meta);

        return play;
    }
}
