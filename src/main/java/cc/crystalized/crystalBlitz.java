package cc.crystalized;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public final class crystalBlitz extends JavaPlugin {

    public Set<Block> Blocks = new HashSet<>();

    public final MapData mapdata = new MapData();
    public GameManager gamemanager;
    public boolean is_force_starting = false;

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

        new BukkitRunnable() {
            @Override
            public void run() {
                if (gamemanager != null) {
                    return;
                }
                if (is_force_starting) {
                    is_force_starting = false;
                    new BukkitRunnable() {
                        public void run() {
                            gamemanager = new GameManager();
                            cancel();
                        }
                    }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
    }

    @Override
    public void onDisable() {

    }

    public static crystalBlitz getInstance() {
        return getPlugin(crystalBlitz.class);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
