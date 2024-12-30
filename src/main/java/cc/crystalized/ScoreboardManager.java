package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.geysermc.floodgate.api.FloodgateApi;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class ScoreboardManager {

    public ScoreboardManager(Player player) {
        FloodgateApi floodgateapi = FloodgateApi.getInstance();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Component title = text("Crystal Blitz").color(NamedTextColor.LIGHT_PURPLE);
        Objective obj = scoreboard.registerNewObjective("main", Criteria.DUMMY, title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore("12").setScore(12);
        obj.getScore("12").customName(text("Team: "));

        obj.getScore("11").setScore(11);
        obj.getScore("11").customName(text("Gen. Speed: "));

        obj.getScore("10").setScore(10);
        obj.getScore("10").customName(text(" "));

        obj.getScore("9").setScore(9);
        obj.getScore("9").customName(text("Blue: "));

        obj.getScore("8").setScore(8);
        obj.getScore("8").customName(text("Cyan: "));

        obj.getScore("7").setScore(7);
        obj.getScore("7").customName(text("Green: "));

        obj.getScore("6").setScore(6);
        obj.getScore("6").customName(text("Lime: "));

        obj.getScore("5").setScore(5);
        obj.getScore("5").customName(text("Magenta: "));

        obj.getScore("4").setScore(4);
        obj.getScore("4").customName(text("Red: "));

        obj.getScore("3").setScore(3);
        obj.getScore("3").customName(text("White: "));

        obj.getScore("2").setScore(2);
        obj.getScore("2").customName(text("Yellow: "));

        obj.getScore("1").setScore(1);
        obj.getScore("1").customName(text(""));

        obj.getScore("0").setScore(0);
        obj.getScore("0").customName(text("crystalized.cc ").color(TextColor.color(0xc4b50a)).append(text("(ServID)").color(NamedTextColor.GRAY)));

        player.setScoreboard(scoreboard);

    }
}

class QueueScoreboard {
    public QueueScoreboard(Player player) {

        FloodgateApi floodgateapi = FloodgateApi.getInstance();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Component title = text("Crystal Blitz").color(NamedTextColor.LIGHT_PURPLE);
        Objective obj = scoreboard.registerNewObjective("main", Criteria.DUMMY, title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore("5").setScore(5);
        obj.getScore("5").customName(text("  "));

        obj.getScore("4").setScore(4);
        obj.getScore("4").customName(translatable("crystalized.game.knockoff.queue.playing"));

        obj.getScore("3").setScore(3);
        obj.getScore("3").customName(text(" "));

        obj.getScore("2").setScore(2);
        obj.getScore("2").customName(translatable("crystalized.game.knockoff.queue.waiting"));

        obj.getScore("1").setScore(1);
        obj.getScore("1").customName(text(""));

        obj.getScore("0").setScore(0);
        obj.getScore("0").customName(text("crystalized.cc ").color(TextColor.color(0xc4b50a)).append(text("(ServID)").color(NamedTextColor.GRAY)));

        player.setScoreboard(scoreboard);

        Team QueuePlayer = scoreboard.registerNewTeam("QueuePlayers");
        QueuePlayer.addEntry("2");
        QueuePlayer.suffix(text("Placeholder"));
        obj.getScore("2").setScore(2);

        Team QueueMap = scoreboard.registerNewTeam("QueueMap");
        QueueMap.addEntry("4");
        QueueMap.suffix(text(""));
        obj.getScore("4").setScore(4);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (floodgateapi.isFloodgatePlayer(player.getUniqueId())) {
                    obj.getScore("2").customName(text("Waiting for Players: ")
                            .append(text("(" + Bukkit.getOnlinePlayers().size()))
                            .append(text("/"))
                            .append(text("" + Bukkit.getMaxPlayers()))
                            .append(text(")"))
                    );
                    obj.getScore("4").customName(text("You are playing on: " + crystalBlitz.getInstance().mapdata.map_name));
                } else {
                    QueuePlayer.suffix(
                            text("(")
                                    .append(text("" + Bukkit.getOnlinePlayers().size()))
                                    .append(text("/"))
                                    .append(text("" + Bukkit.getMaxPlayers()))
                                    .append(text(")"))
                    );
                    QueueMap.suffix(text(crystalBlitz.getInstance().mapdata.map_name));
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 0 ,1);
    }
}