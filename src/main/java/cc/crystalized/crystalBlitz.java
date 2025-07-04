package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class crystalBlitz extends JavaPlugin {

    public Set<Block> Blocks = new HashSet<>();

    public final MapData mapdata = new MapData();
    public GameManager gamemanager;
    public boolean is_force_starting = false;
    private int configVersion = 0;

    @Override
    public void onEnable() {

        Commands dc = new Commands();
        this.getCommand("crystalblitz").setExecutor(dc);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new ShopListener(), this);

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

        Shop.setupShop();
        CrystalBlitzItems.SetupItems();

        //Config stuff
        saveResource("config.yml", false); //I dont trust this
        if (getConfig().getInt("version") != 1) {
            configVersion = getConfig().getInt("version");
            getLogger().log(Level.SEVERE, "Invalid Version, Please update your config. Expecting 1 but found " + configVersion + ". You may experience fatal issues.");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (gamemanager != null) {
                    return;
                }
                if (is_force_starting) {
                    if (Bukkit.getOnlinePlayers().size() > 16) {
                        Bukkit.getServer().sendMessage(text("[!] Too many players are online, aborting game as theres no compatible mode to support " + Bukkit.getOnlinePlayers().size() + " players. Make sure the player size in server.properties is capped to 16 maximum."));
                        return;
                    }
                    is_force_starting = false;

                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
    }

    @Override
    public void onDisable() {

    }

    public void forceStartGame(GameManager.GameTypes type) {
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
                        cancel();
                    }
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
