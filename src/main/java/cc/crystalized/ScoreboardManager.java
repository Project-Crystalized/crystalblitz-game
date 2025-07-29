package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.geysermc.floodgate.api.FloodgateApi;

import static net.kyori.adventure.text.Component.*;

public class ScoreboardManager {

    public ScoreboardManager(Player player) {
        FloodgateApi floodgateapi = FloodgateApi.getInstance();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Component title = text("Crystal Blitz").color(NamedTextColor.LIGHT_PURPLE);
        Objective obj = scoreboard.registerNewObjective("main", Criteria.DUMMY, title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore("12").setScore(12);
        obj.getScore("12").customName(translatable("crystalized.game.generic.team").append(text(": ")).append(translatable("crystalized.game.generic.team." + Teams.getPlayerTeam(player))));

        obj.getScore("11").setScore(11);
        obj.getScore("11").customName(translatable("crystalized.game.crystalblitz.genspeed"));

        obj.getScore("10").setScore(10);
        obj.getScore("10").customName(text(" "));

        obj.getScore("9").setScore(9);
        obj.getScore("9").customName(translatable("crystalized.game.generic.team.blue").append(text(": ")));

        obj.getScore("8").setScore(8);
        obj.getScore("8").customName(translatable("crystalized.game.generic.team.cyan").append(text(": ")));

        obj.getScore("7").setScore(7);
        obj.getScore("7").customName(translatable("crystalized.game.generic.team.green").append(text(": ")));

        obj.getScore("6").setScore(6);
        obj.getScore("6").customName(translatable("crystalized.game.generic.team.lime").append(text(": ")));

        obj.getScore("5").setScore(5);
        obj.getScore("5").customName(translatable("crystalized.game.generic.team.magenta").append(text(": ")));

        obj.getScore("4").setScore(4);
        obj.getScore("4").customName(translatable("crystalized.game.generic.team.red").append(text(": ")));

        obj.getScore("3").setScore(3);
        obj.getScore("3").customName(translatable("crystalized.game.generic.team.white").append(text(": ")));

        obj.getScore("2").setScore(2);
        obj.getScore("2").customName(translatable("crystalized.game.generic.team.yellow").append(text(": ")));

        obj.getScore("1").setScore(1);
        obj.getScore("1").customName(text(""));

        obj.getScore("0").setScore(0);
        obj.getScore("0").customName(text("crystalized.cc ").color(TextColor.color(0xc4b50a)).append(text("(ServID)").color(NamedTextColor.GRAY)));

        Team blue = scoreboard.registerNewTeam("blue");
        blue.setAllowFriendlyFire(false);
        blue.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        blue.addEntry("9");
        blue.suffix(text(""));
        obj.getScore("9").setScore(9);

        Team cyan = scoreboard.registerNewTeam("cyan");
        cyan.setAllowFriendlyFire(false);
        cyan.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        cyan.addEntry("8");
        cyan.suffix(text(""));
        obj.getScore("8").setScore(8);

        Team green = scoreboard.registerNewTeam("green");
        green.setAllowFriendlyFire(false);
        green.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        green.addEntry("7");
        green.suffix(text(""));
        obj.getScore("7").setScore(7);

        Team lime = scoreboard.registerNewTeam("lime");
        lime.setAllowFriendlyFire(false);
        lime.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        lime.addEntry("6");
        lime.suffix(text(""));
        obj.getScore("6").setScore(6);

        Team magenta = scoreboard.registerNewTeam("magenta");
        magenta.setAllowFriendlyFire(false);
        magenta.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        magenta.addEntry("5");
        magenta.suffix(text(""));
        obj.getScore("5").setScore(5);

        Team red = scoreboard.registerNewTeam("red");
        red.setAllowFriendlyFire(false);
        red.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        red.addEntry("4");
        red.suffix(text(""));
        obj.getScore("4").setScore(4);

        Team white = scoreboard.registerNewTeam("white");
        white.setAllowFriendlyFire(false);
        white.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        white.addEntry("3");
        white.suffix(text(""));
        obj.getScore("3").setScore(3);

        Team yellow = scoreboard.registerNewTeam("yellow");
        yellow.setAllowFriendlyFire(false);
        yellow.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        yellow.addEntry("2");
        yellow.suffix(text(""));
        obj.getScore("2").setScore(2);

        Team genspeed = scoreboard.registerNewTeam("genspeed");
        genspeed.addEntry("11");
        genspeed.suffix(text(""));
        obj.getScore("11").setScore(11);

        player.setScoreboard(scoreboard);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                } else {
                    BossbarManager bossbar = crystalBlitz.getInstance().gamemanager.bossbar;
                    if (floodgateapi.isFloodgatePlayer(player.getUniqueId())) {
                        //Bedrock

                        switch (bossbar.currentstate) {
                            case BossBarStates.starting -> {
                                obj.getScore("11").customName(translatable("crystalized.game.crystalblitz.genspeed").append(text("0")));
                            }
                            case BossBarStates.GenUpgradeI -> {
                                obj.getScore("11").customName(translatable("crystalized.game.crystalblitz.genspeed").append(text("I")));
                            }
                            case BossBarStates.GenUpgradeII, BossBarStates.WorldBorderClosing -> {
                                obj.getScore("11").customName(translatable("crystalized.game.crystalblitz.genspeed").append(text("II")));
                            }
                        }
                        obj.getScore("9").customName(translatable("crystalized.game.generic.team.blue").append(text(": ")).append(crystalBlitz.getInstance().gamemanager.getNexus("blue").getColoredHealth()));
                        obj.getScore("8").customName(translatable("crystalized.game.generic.team.cyan").append(text(": ")).append(crystalBlitz.getInstance().gamemanager.getNexus("cyan").getColoredHealth()));
                        obj.getScore("7").customName(translatable("crystalized.game.generic.team.green").append(text(": ")).append(crystalBlitz.getInstance().gamemanager.getNexus("green").getColoredHealth()));
                        obj.getScore("6").customName(translatable("crystalized.game.generic.team.lime").append(text(": ")).append(crystalBlitz.getInstance().gamemanager.getNexus("lime").getColoredHealth()));
                        obj.getScore("5").customName(translatable("crystalized.game.generic.team.magenta").append(text(": ")).append(crystalBlitz.getInstance().gamemanager.getNexus("magenta").getColoredHealth()));
                        obj.getScore("4").customName(translatable("crystalized.game.generic.team.red").append(text(": ")).append(crystalBlitz.getInstance().gamemanager.getNexus("red").getColoredHealth()));
                        obj.getScore("3").customName(translatable("crystalized.game.generic.team.white").append(text(": ")).append(crystalBlitz.getInstance().gamemanager.getNexus("white").getColoredHealth()));
                        obj.getScore("2").customName(translatable("crystalized.game.generic.team.yellow").append(text(": ")).append(crystalBlitz.getInstance().gamemanager.getNexus("yellow").getColoredHealth()));

                    } else {
                        //Java
                        switch (bossbar.currentstate) {
                            case BossBarStates.starting -> {
                                genspeed.suffix(text("0"));
                            }
                            case BossBarStates.GenUpgradeI -> {
                                genspeed.suffix(text("I"));
                            }
                            case BossBarStates.GenUpgradeII, BossBarStates.WorldBorderClosing -> {
                                genspeed.suffix(text("II"));
                            }
                        }
                        blue.suffix(crystalBlitz.getInstance().gamemanager.getNexus("blue").getColoredHealth());
                        cyan.suffix(crystalBlitz.getInstance().gamemanager.getNexus("cyan").getColoredHealth());
                        green.suffix(crystalBlitz.getInstance().gamemanager.getNexus("green").getColoredHealth());
                        lime.suffix(crystalBlitz.getInstance().gamemanager.getNexus("lime").getColoredHealth());
                        magenta.suffix(crystalBlitz.getInstance().gamemanager.getNexus("magenta").getColoredHealth());
                        red.suffix(crystalBlitz.getInstance().gamemanager.getNexus("red").getColoredHealth());
                        white.suffix(crystalBlitz.getInstance().gamemanager.getNexus("white").getColoredHealth());
                        yellow.suffix(crystalBlitz.getInstance().gamemanager.getNexus("yellow").getColoredHealth());
                    }
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 20, 1);
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
        QueuePlayer.suffix(text(" "));
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