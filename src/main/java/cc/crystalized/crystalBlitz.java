package cc.crystalized;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public final class crystalBlitz extends JavaPlugin {

    public final MapData mapdata = new MapData();
    public GameManager GameManager;
    public boolean is_force_starting = false;

    @Override
    public void onEnable() {

        Commands dc = new Commands();
        this.getCommand("crystalblitz").setExecutor(dc);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        Bukkit.getWorld("world").setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (GameManager != null) {
                    return;
                }
                if (is_force_starting) {
                    is_force_starting = false;

                    new BukkitRunnable() {
                        int timer = 3;
                        public void run() {

                            switch (timer) {
                                case 3,2,1:
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.showTitle(
                                            Title.title(
                                                    translatable("crystalized.game.generic.startingin").color(NamedTextColor.GREEN),
                                                    text("" + timer)
                                            )
                                        );
                                        p.playSound(p, "crystalized:effect.countdown", 50, 1);
                                    }
                                    break;
                                case 0:
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.showTitle(
                                                Title.title(
                                                        translatable("crystalized.game.generic.go").color(NamedTextColor.GOLD),
                                                        text("" + timer)
                                                )
                                        );
                                        p.playSound(p, "crystalized:effect.countdown_end", 50, 1);
                                    }
                                    GameManager = new GameManager();
                                    break;
                            }

                            timer--;
                        }
                    }.runTaskTimer(crystalBlitz.getInstance(), 20, 20);
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 0, 1);
    }

    @Override
    public void onDisable() {

    }

    public static crystalBlitz getInstance() {
        return getPlugin(crystalBlitz.class);
    }
}
