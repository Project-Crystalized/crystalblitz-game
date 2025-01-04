package cc.crystalized;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
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

        Bukkit.getWorld("world").setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);

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
}
