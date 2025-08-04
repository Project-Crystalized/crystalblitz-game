package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.kyori.adventure.text.Component.text;

public class Teams {

    public static List<String> teams = new ArrayList<>();

    public static List<String> spectator = new ArrayList<>();
    public static List<String> blue = new ArrayList<>();
    public static List<String> cyan = new ArrayList<>();
    public static List<String> green = new ArrayList<>();
    public static List<String> lime = new ArrayList<>();
    public static List<String> magenta = new ArrayList<>();
    public static List<String> red = new ArrayList<>();
    public static List<String> white = new ArrayList<>();
    public static List<String> yellow = new ArrayList<>();

    public static final TextColor TEAM_SPECTATOR = TextColor.color(0xFFFFFF);
    public static final TextColor TEAM_BLUE = TextColor.color(0x0A42BB);
    public static final TextColor TEAM_CYAN = TextColor.color(0x157D91);
    public static final TextColor TEAM_GREEN = TextColor.color(0x0A971E);
    public static final TextColor TEAM_LIME = TextColor.color(0x67E555);
    public static final TextColor TEAM_MAGENTA = TextColor.color(0xDA50E0);
    public static final TextColor TEAM_RED = TextColor.color(0xF74036);
    public static final TextColor TEAM_WHITE = TextColor.color(0xFFFFFF);
    public static final TextColor TEAM_YELLOW = TextColor.color(0xFBE059);

    public static final List<TeamData> team_datas = TeamData.create_teams();
    public static List<TeamData> team_datas_without_spectator = null; //exists so I can copy paste code from Knockoff

    public Teams(GameManager.GameTypes type) {
        team_datas_without_spectator = team_datas;

        List<String> playerlist = new ArrayList<>();

        teams.clear();
        teams.add("spectator");
        teams.add("blue");
        teams.add("cyan");
        teams.add("green");
        teams.add("lime");
        teams.add("magenta");
        teams.add("red");
        teams.add("white");
        teams.add("yellow");


        for (Player p : Bukkit.getOnlinePlayers()) {
            playerlist.add(p.getName());
        }
        Collections.shuffle(playerlist);
        if (playerlist.size() > 7) {
            Bukkit.getLogger().log(Level.INFO, "Sorting Players into teams (duos)...");
        } else {
            Bukkit.getLogger().log(Level.INFO, "Sorting Players into teams (solo)...");
        }

        if (Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.getServer().sendMessage(text("\nStarting the game requires a player to be online. Please login to the server and try again.\n"));
            return;
        } else {
            new BukkitRunnable() {
                @Override
                public void run() { //Clear every team when the game ends and everyone is kicked
                    if (crystalBlitz.getInstance().gamemanager == null) {
                        if (!blue.isEmpty()) {blue.clear();}
                        if (!cyan.isEmpty()) {cyan.clear();}
                        if (!green.isEmpty()) {green.clear();}
                        if (!lime.isEmpty()) {lime.clear();}
                        if (!magenta.isEmpty()) {magenta.clear();}
                        if (!red.isEmpty()) {red.clear();}
                        if (!white.isEmpty()) {white.clear();}
                        if (!yellow.isEmpty()) {yellow.clear();}
                        cancel();
                    }
                }
            }.runTaskTimer(crystalBlitz.getInstance(), 20 ,1);

            try {
                switch (type) {
                    case GameManager.GameTypes.Custom -> {
                        FileConfiguration config = crystalBlitz.getInstance().getConfig();
                        for (Object o : config.getList("teams.spectator").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                spectator.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                        for (Object o : config.getList("teams.blue").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                blue.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                        for (Object o : config.getList("teams.cyan").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                cyan.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                        for (Object o : config.getList("teams.green").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                green.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                        for (Object o : config.getList("teams.lime").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                lime.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                        for (Object o : config.getList("teams.magenta").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                magenta.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                        for (Object o : config.getList("teams.red").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                red.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                        for (Object o : config.getList("teams.yellow").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                yellow.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                        for (Object o : config.getList("teams.white").toArray()) {
                            String s = (String) o;
                            Player p = Bukkit.getPlayer(s);
                            if (p == null) {
                                crystalBlitz.getInstance().getLogger().log(Level.WARNING, "Player \"" + s + "\" is not online. cannot add them to a team.");
                            } else {
                                cyan.add(Bukkit.getPlayer(s).getName());
                            }
                        }
                    }
                    case GameManager.GameTypes.StandardSolos -> {
                        randomizeTeams(1, playerlist);
                    }
                    case GameManager.GameTypes.StandardDuos -> {
                        randomizeTeams(2, playerlist);
                    }
                    case GameManager.GameTypes.StandardTrios -> {
                        randomizeTeams(3, playerlist);
                    }
                    case GameManager.GameTypes.StandardSquads -> {
                        randomizeTeams(4, playerlist);
                    }
                }
            } catch (Exception e) {

            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (getPlayerTeam(p) == null) {
                    spectator.add(p.getName());
                    p.sendMessage(text("[!] You weren't assigned a team, we've put you in Spectator Team."));
                }
            }

            Logger logger = crystalBlitz.getInstance().getLogger();
            logger.log(Level.INFO, "Player(s) " + spectator + " in Team Spectator");
            logger.log(Level.INFO, "Player(s) " + blue + " in Team Blue");
            logger.log(Level.INFO, "Player(s) " + cyan + " in Team Cyan");
            logger.log(Level.INFO, "Player(s) " + green + " in Team Green");
            logger.log(Level.INFO, "Player(s) " + lime + " in Team Lime");
            logger.log(Level.INFO, "Player(s) " + magenta + " in Team Magenta");
            logger.log(Level.INFO, "Player(s) " + red + " in Team Red");
            logger.log(Level.INFO, "Player(s) " + yellow + " in Team Yellow");
            logger.log(Level.INFO, "Player(s) " + white + " in Team White");
        }
    }

    private void randomizeTeams(int TeamSize, List<String> playerlist) {
        Collections.shuffle(team_datas_without_spectator);
        int i = 0;
        for (TeamData td : team_datas_without_spectator) {
            int j = TeamSize;
            while (j != 0) {
                addPlayerToTeamIfPossible(get_team_from_string(td.name), playerlist.get(i));
                j--;
                i++;
            }
        }
    }

    private void addPlayerToTeamIfPossible(List<String> team, String p) {
        try {
            if (p != null) {
                team.add(p);
            }
        } catch (Exception e) {

        }
    }

    public static String getPlayerTeam(Player player) {
        if (spectator.contains(player.getName())) {
            return "spectator";
        } else if (blue.contains(player.getName())) {
            return "blue";
        } else if (cyan.contains(player.getName())) {
            return "cyan";
        } else if (green.contains(player.getName())) {
            return "green";
        } else if (lime.contains(player.getName())) {
            return "lime";
        } else if (magenta.contains(player.getName())) {
            return "magenta";
        } else if (red.contains(player.getName())) {
            return "red";
        } else if (white.contains(player.getName())) {
            return "white";
        } else if (yellow.contains(player.getName())) {
            return "yellow";
        } else {
            return null;
        }
    }

    public static void DisconnectPlayer(String Player) {
        if (spectator.contains(Player)) {
            spectator.remove(spectator.indexOf(Player));
        } else if (blue.contains(Player)) {
            blue.remove(blue.indexOf(Player));
        } else if (cyan.contains(Player)) {
            cyan.remove(cyan.indexOf(Player));
        } else if (green.contains(Player)) {
            green.remove(green.indexOf(Player));
        } else if (lime.contains(Player)) {
            lime.remove(lime.indexOf(Player));
        } else if (magenta.contains(Player)) {
            magenta.remove(magenta.indexOf(Player));
        } else if (red.contains(Player)) {
            red.remove(red.indexOf(Player));
        } else if (white.contains(Player)) {
            white.remove(white.indexOf(Player));
        } else if (yellow.contains(Player)) {
            yellow.remove(yellow.indexOf(Player));
        }
    }

    public static void setPlayerDisplayNames(Player player) {
        if (spectator.contains(player.getName())) {
            player.displayName(text("[Spectator] ").append(text(player.getName()).color(TEAM_SPECTATOR)));
        } else if (blue.contains(player.getName())) {
            player.displayName(text("\uE120 ").append(text(player.getName()).color(TEAM_BLUE)));
        } else if (cyan.contains(player.getName())) {
            player.displayName(text("\uE121 ").append(text(player.getName()).color(TEAM_CYAN)));
        } else if (green.contains(player.getName())) {
            player.displayName(text("\uE122 ").append(text(player.getName()).color(TEAM_GREEN)));
        } else if (lime.contains(player.getName())) {
            player.displayName(text("\uE123 ").append(text(player.getName()).color(TEAM_LIME)));
        } else if (magenta.contains(player.getName())) {
            player.displayName(text("\uE124 ").append(text(player.getName()).color(TEAM_MAGENTA)));
        } else if (red.contains(player.getName())) {
            player.displayName(text("\uE125 ").append(text(player.getName()).color(TEAM_RED)));
        } else if (white.contains(player.getName())) {
            player.displayName(text("\uE126 ").append(text(player.getName()).color(TEAM_WHITE)));
        } else if (yellow.contains(player.getName())) {
            player.displayName(text("\uE127 ").append(text(player.getName()).color(TEAM_YELLOW)));
        } else {
            player.displayName(text("[Unknown Team]").append(text(player.getName())));
        }
    }

    public static List<String> get_team_from_string(String s) {
        if (s.equals("spectator")) {
            return spectator;
        } else if (s.equals("blue")) {
            return blue;
        } else if (s.equals("cyan")) {
            return cyan;
        } else if (s.equals("green")) {
            return green;
        } else if (s.equals("lime")) {
            return lime;
        } else if (s.equals("magenta")) {
            return magenta;
        } else if (s.equals("red")) {
            return red;
        } else if (s.equals("white")) {
            return white;
        } else if (s.equals("yellow")) {
            return yellow;
        } else {
            return null;
        }
    }

//I hate this class still.
//I just copied everything from knockoff I dont wanna write this shit again
class TeamStatus {
    enum Status {
        Alive,
        Dead
    }

    public static HashMap<String, Integer> team_statuses = new HashMap<>();

    private static void update_team_status(String team) {
        int counter = 0;
        for (String p_name : Teams.get_team_from_string(team)) {
            Player p = Bukkit.getPlayer(p_name);
            PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(p);
            if (pd == null) {
                return;
            }
            if (!pd.isEliminated) {
                counter++;
            }
        }

        List<String> td = Teams.get_team_from_string(team);
        team_statuses.put(team, counter);

        /*if (td.isEmpty()) {
            team_statuses.put(team, Status.Dead);
        } else if (counter == td.size()) {
            team_statuses.put(team, Status.Alive);
        } else {
            team_statuses.put(team, Status.Dead);
        }*/
    }

    private static boolean is_only_team_alive(String team) {
        if (team_statuses.get(team) == 0) {
            return false;
        }

        for (String loop_team : team_statuses.keySet()) {
            if (loop_team == team) {
                continue;
            }
            if (team_statuses.get(loop_team) > 0) {
                return false;
            }
        }
        return true;
    }

    public static void Init() {
        team_statuses.clear();
        for (TeamData td : Teams.team_datas) {
            if (Teams.get_team_from_string(td.name).isEmpty()) {
                team_statuses.put(td.name, 0);
            } else {
                team_statuses.put(td.name, Teams.get_team_from_string(td.name).size());
            }
        }

        if (Bukkit.getOnlinePlayers().size() == 1) {
            Bukkit.getServer().sendMessage(text(
                    "1 player detected, To end the game, run \"/crystalBlitz end\" as a player with op. The game will not end automatically due to player size"));
            return;
        }

        new BukkitRunnable() {
            public void run() {

                for (TeamData td : Teams.team_datas) {
                    if (crystalBlitz.getInstance().gamemanager == null) {
                        cancel();
                    }
                    // Check if all players in the team are alive. If not set them to dead
                    update_team_status(td.name);
                }


                for (TeamData td : Teams.team_datas) {
                    if (is_only_team_alive(td.name)) {
                        GameManager.StartEndGame(td.name);
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 20, 1);
    }

}

static class TeamData{
    public final String name;
    public final Color color;
    public final String symbol;

    public static List<TeamData> create_teams() {
        List<TeamData> list = new ArrayList<>();
        list.add(new TeamData("blue", Color.fromRGB(0x0A42BB), "\uE120 "));
        list.add(new TeamData("cyan", Color.fromRGB(0x157D91), "\uE121 "));
        list.add(new TeamData("green", Color.fromRGB(0x0A971E), "\uE122 "));
        list.add(new TeamData("lime", Color.fromRGB(0x67E555), "\uE123 "));
        list.add(new TeamData("magenta", Color.fromRGB(0xDA50E0), "\uE124 "));
        list.add(new TeamData("red", Color.fromRGB(0xF74036), "\uE125 "));
        list.add(new TeamData("white", Color.fromRGB(0xFFFFFF), "\uE126 "));
        list.add(new TeamData("yellow", Color.fromRGB(0xFBE059), "\uE127 "));
        return list;
    }

    public static TeamData get_team_data(String s, String s2) {
        for (TeamData td : Teams.team_datas) {
            if (td.name.equals(s)) {
                return td;
            }
        }
        return null;
    }

    public TeamData(String name, Color color, String symbol) {
        this.name = name;
        this.color = color;
        this.symbol = symbol;
    }
}
}

class CustomPlayerNametags {
    public CustomPlayerNametags(Player p) {
        TextDisplay displayFront = p.getWorld().spawn(p.getLocation(), TextDisplay.class, entity -> {
            entity.setBillboard(Display.Billboard.CENTER);
        });
        p.addPassenger(displayFront);
        p.hideEntity(crystalBlitz.getInstance(), displayFront);

        new BukkitRunnable() {
            public void run() {
                if (crystalBlitz.getInstance().gamemanager == null || !p.isOnline() || p.getGameMode().equals(GameMode.SPECTATOR)) {
                    displayFront.remove();
                    cancel();
                } else {
                    PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(p);
                    displayFront.text(
                            pd.cachedRankIcon_large
                                    .append(text("\n"))
                                    .append(p.displayName())
                    );
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 20, 2);
    }
}
