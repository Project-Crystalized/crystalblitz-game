package cc.crystalized;

import org.bukkit.entity.Player;

import java.util.Comparator;

public class PlayerData {

    Player p;
    boolean isEliminated = false;
    int kills = 0;
    int nexus_kills = 0;
    int deaths = 0;

    public PlayerData(Player player) {
        p = player;
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
