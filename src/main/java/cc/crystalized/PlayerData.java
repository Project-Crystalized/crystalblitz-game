package cc.crystalized;

import gg.crystalized.lobby.Ranks;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;

import static net.kyori.adventure.text.Component.text;

public class PlayerData {

    Player p;
    boolean isEliminated = false;
    public Component cachedRankIcon_small = text("?");
    public Component cachedRankIcon_large = text("rank");
    int kills = 0;
    int nexus_kills = 0;
    int deaths = 0;

    public PlayerData(Player player) {
        p = player;
        cachedRankIcon_small = Ranks.getIcon(Bukkit.getOfflinePlayer(p.getName()));
        cachedRankIcon_large = Ranks.getRankWithName(p);
    }

    public int score() {
        return kills + nexus_kills;
    }
}

class PlayerDataComparator implements Comparator<PlayerData> {
    @Override
    public int compare(PlayerData arg0, PlayerData arg1) {
        return arg0.score() - arg1.score();
    }
}
