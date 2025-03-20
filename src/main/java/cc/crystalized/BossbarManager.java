package cc.crystalized;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;

public class BossbarManager {

    BossBar bar;
    BossBar texture;
    BossBarStates currentstate =  BossBarStates.starting; //Reset state
    int timerdefaultvalue = 300; //300 =5 Minutes, 30 is testing
    int timer = timerdefaultvalue;

    public BossbarManager() {
        bar = BossBar.bossBar(text("loading"), 0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        Bukkit.getServer().showBossBar(bar);
        texture = BossBar.bossBar(text("texture here"), 0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        Bukkit.getServer().showBossBar(texture);

        new BukkitRunnable() {
            public void run() {
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                }
                timer--;
                ChangeBossbarText();

                if (timer == 0) {
                    switch (currentstate) {
                        case starting -> {
                            currentstate = BossBarStates.GenUpgradeI;
                            timer = timerdefaultvalue;
                        }
                        case GenUpgradeI -> {
                            currentstate = BossBarStates.GenUpgradeII;
                            timer = timerdefaultvalue;
                        }
                        case GenUpgradeII -> {
                            currentstate = BossBarStates.WorldBorderClosing;
                            timer = 1;
                        }
                        case WorldBorderClosing -> {
                            crystalBlitz.getInstance().gamemanager.worldborder.ShrinkBorder();
                            cancel();
                        }
                    }
                }
            }

        }.runTaskTimer(crystalBlitz.getInstance(), 0, 20);
    }

    private void ChangeBossbarText() {
        switch (currentstate) {
            case starting -> {
                bar.name(text("Next Gen. Upgrade (I): ").color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
            }
            case GenUpgradeI -> {
                bar.name(text("Next Gen. Upgrade (II): ").color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
            }
            case GenUpgradeII -> {
                bar.name(text("World Border Closing in: ").color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
            }
            case WorldBorderClosing -> {
                bar.name(text("World Border Closing!"));
            }
        }
    }
}

enum BossBarStates{
    starting,
    GenUpgradeI,
    GenUpgradeII,
    WorldBorderClosing
}
