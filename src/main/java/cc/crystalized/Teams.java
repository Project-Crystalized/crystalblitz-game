package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;

public class Teams {

    public static List<String> teams = new ArrayList<>();

    public static List<String> blue = new ArrayList<>();
    public static List<String> cyan = new ArrayList<>();
    public static List<String> green = new ArrayList<>();
    public static List<String> lime = new ArrayList<>();
    public static List<String> magenta = new ArrayList<>();
    public static List<String> red = new ArrayList<>();
    public static List<String> white = new ArrayList<>();
    public static List<String> yellow = new ArrayList<>();

    public static final TextColor TEAM_BLUE = TextColor.color(0x0A42BB);
    public static final TextColor TEAM_CYAN = TextColor.color(0x157D91);
    public static final TextColor TEAM_GREEN = TextColor.color(0x0A971E);
    public static final TextColor TEAM_LIME = TextColor.color(0x67E555);
    public static final TextColor TEAM_MAGENTA = TextColor.color(0xDA50E0);
    public static final TextColor TEAM_RED = TextColor.color(0xF74036);
    public static final TextColor TEAM_WHITE = TextColor.color(0xFFFFFF);
    public static final TextColor TEAM_YELLOW = TextColor.color(0xFBE059);

    public static final List<TeamData> team_datas = TeamData.create_teams();

    public Teams() {
        List<String> playerlist = new ArrayList<>();

        teams.clear();
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

            if (playerlist.size() > 0) {
                if (blue.isEmpty()) {
                    blue.add(playerlist.get(0));
                    if (playerlist.size() > 8) {
                        blue.add(playerlist.get(8));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Player(s) " + blue + " in Team Blue");
                }
            }else {
                Bukkit.getLogger().log(Level.SEVERE, "Tried to add a player to team Blue but the player list is 0. Please report this as you shouldn't be able to get this error");
                throw new RuntimeException();
            }

            if (playerlist.size() > 1) { //If the player list is 2 or greater
                if (cyan.isEmpty()) {
                    cyan.add(playerlist.get(1));
                    if (playerlist.size() > 9) {
                        cyan.add(playerlist.get(9));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Player(s) " + cyan + " in Team Cyan");
                }
            }else {
                Bukkit.getLogger().log(Level.WARNING, "No player(s) available for Cyan team (FYI: Recommend getting an alt account or someone else to join. 2 or more players is recommended)");
            }

            if (playerlist.size() > 2) { //If the player list is 3 or greater
                if (green.isEmpty()) {
                    green.add(playerlist.get(2));
                    if (playerlist.size() > 10) {
                        green.add(playerlist.get(10));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Player(s) " + green + " in Team Green");
                }
            }else {
                Bukkit.getLogger().log(Level.INFO, "No player(s) available for Green team");
            }

            if (playerlist.size() > 3) { //If the player list is 5 or greater
                if (lime.isEmpty()) {
                    lime.add(playerlist.get(3));
                    if (playerlist.size() > 11) {
                        lime.add(playerlist.get(11));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Player(s) " + lime + " in Team Lime");
                }
            }else {
                Bukkit.getLogger().log(Level.INFO, "No player(s) available for Lime team");
            }

            if (playerlist.size() > 4) { //If the player list is 6 or greater
                if (magenta.isEmpty()) {
                    magenta.add(playerlist.get(4));
                    if (playerlist.size() > 12) {
                        magenta.add(playerlist.get(12));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Player(s) " + magenta + " in Team Magenta");
                }
            }else {
                Bukkit.getLogger().log(Level.INFO, "No player(s) available for Magenta team");
            }

            if (playerlist.size() > 5) { //If the player list is 10 or greater
                if (red.isEmpty()) {
                    red.add(playerlist.get(5));
                    if (playerlist.size() > 13) {
                        red.add(playerlist.get(13));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Player(s) " + red + " in Team Red");
                }
            }else {
                Bukkit.getLogger().log(Level.INFO, "No player(s) available for Red team");
            }

            if (playerlist.size() > 6) { //If the player list is 11 or greater
                if (white.isEmpty()) {
                    white.add(playerlist.get(6));
                    if (playerlist.size() > 14) {
                        white.add(playerlist.get(14));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Player(s) " + white + " in Team White");
                }
            }else {
                Bukkit.getLogger().log(Level.INFO, "No player(s) available for White team");
            }

            if (playerlist.size() > 7) { //If the player list is 12
                if (yellow.isEmpty()) {
                    yellow.add(playerlist.get(7));
                    if (playerlist.size() > 15) {
                        yellow.add(playerlist.get(15));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Player(s) " + yellow + " in Team Yellow");
                }
            }else {
                Bukkit.getLogger().log(Level.INFO, "No player(s) available for Yellow team");
            }
        }
    }

    public static String getPlayerTeam(Player player) {
        if (blue.contains(player.getName())) {
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
        if (blue.contains(Player)) {
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
        if (blue.contains(player.getName())) {
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
        if (s.equals("blue")) {
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
}

//I hate this class still.
//I just copied everything from knockoff I dont wanna write this shit again
class TeamStatus{
    enum Status {
        Alive,
        Dead
    }

    public static HashMap<String, Status> team_statuses = new HashMap<>();

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
        //Why cyan?
        if (Teams.cyan.size() == 0) {
            team_statuses.put(team, Status.Dead);
        } else if (counter == Teams.cyan.size()) {
            team_statuses.put(team, Status.Alive);
        } else {
            team_statuses.put(team, Status.Dead);
        }
    }

    private static boolean is_only_team_alive(String team) {
        if (team_statuses.get(team) == Status.Dead) {
            return false;
        }

        for (String loop_team : team_statuses.keySet()) {
            if (loop_team == team) {
                continue;
            }
            if (team_statuses.get(loop_team) == Status.Alive) {
                return false;
            }
        }
        return true;
    }

    public static void Init() {
        team_statuses.clear();
        for (TeamData td : Teams.team_datas) {
            if (Teams.get_team_from_string(td.name).isEmpty()) {
                team_statuses.put(td.name, Status.Dead);
            } else {
                team_statuses.put(td.name, Status.Alive);
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

class TeamData{
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
