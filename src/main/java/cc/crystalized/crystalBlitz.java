package cc.crystalized;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.ByteBuffer;
import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class crystalBlitz extends JavaPlugin {

    public Set<Block> Blocks = new HashSet<>();

    public final MapData mapdata = new MapData();
    public GameManager gamemanager;
    public boolean is_force_starting = false;
    private boolean isCountingDown = false;
    private int configVersion = 0;
    private boolean GameCountdownStarted = false;
    private int PlayerStartLimit = 3;

    @Override
    public void onEnable() {

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

            LiteralCommandNode<CommandSourceStack> buildCommand = command.build();
            commands.registrar().register(buildCommand);
        });

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new ShopListener(), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "crystalized:crystalblitz");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "crystalized:main");

        World w  = Bukkit.getWorld("world");

        w.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        w.setGameRule(GameRule.DO_FIRE_TICK, false);
        w.setGameRule(GameRule.DO_INSOMNIA, false);
        w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        w.setGameRule(GameRule.MOB_GRIEFING, false);
        w.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        w.setGameRule(GameRule.NATURAL_REGENERATION, true);
        w.setDifficulty(Difficulty.HARD);

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
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showTitle(Title.title(translatable("crystalized.game.generic.go").color(GOLD), text(" "),
                                    Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofSeconds(1))));
                            player.playSound(player, "crystalized:effect.countdown_end", 50, 1);
                        }
                        gamemanager = new GameManager(type);

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("start_game");
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            out.writeUTF(p.getName());
                        }
                        Player p = (Player) Bukkit.getOnlinePlayers().toArray()[0];
                        p.sendPluginMessage(crystalBlitz.getInstance(), "crystalized:crystalblitz", out.toByteArray());

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
                    Bukkit.getServer().sendMessage(text("Game cancelled, too few players!").color(RED));
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
}

//cba making another .java file - Callum
class CrystalBlitzDatabase{
    public static final String URL = "jdbc:sqlite"+ System.getProperty("user.home")+"/databases/crystalblitz_db.sql";

    public static void setup_databases() {
        String create_cb_games = "CREATE TABLE IF NOT EXISTS CrystalBlitzGames ("
                + "map STRING,"
                + "winner_team STRING,"
                + "gametype STRING,"
                + "timestamp INTEGER"
                + ");";
        String create_cb_players = "CREATE TABLE IF NOT EXISTS CbGamesPlayers ("
                + "player_uuid BYTES,"
                + "team STRING,"
                + "kills INTEGER,"
                + "deaths INTEGER,"
                + "nexus_kills INTEGER" //nexuses broken
                + "games_won INTEGER"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            stmt.execute(create_cb_games);
            stmt.execute(create_cb_players);
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
            for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                crystalBlitz.getInstance().getLogger().severe(ste.toString());
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

            String save_player = "INSERT INTO CbGamesPlayers(player_uuid, team, kills, deaths, nexus_kills, games_won)"
                    + " VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement player_stmt = conn.prepareStatement(save_player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                PlayerData pd = gm.getPlayerData(p);

                player_stmt.setBytes(1, uuid_to_bytes(p));
                player_stmt.setString(2, Teams.getPlayerTeam(p));
                player_stmt.setInt(3, pd.kills);
                player_stmt.setInt(4, pd.deaths);
                player_stmt.setInt(5, pd.nexus_kills);
                if (WinningTeam.equals(Teams.getPlayerTeam(p))) {
                    player_stmt.setInt(6, 1);
                } else {
                    player_stmt.setInt(6, 0);
                }
                player_stmt.executeUpdate();
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    private static byte[] uuid_to_bytes(Player p) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        UUID uuid = p.getUniqueId();
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
