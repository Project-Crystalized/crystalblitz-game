package cc.crystalized;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;

public class BossbarManager {

    BossBar bar;

    public BossbarManager() {
        bar = BossBar.bossBar(text("Testing"), 0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        Bukkit.getServer().showBossBar(bar);
    }
}

enum BossBarStates{
    GenUpgradeI,
    GenUpgradeII,
    WorldBorderClosing,
}
