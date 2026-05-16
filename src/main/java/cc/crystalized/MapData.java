package cc.crystalized;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MapData {

    public int ConfigVersion;

    public String map_name;
    public String game;
    public double[] queue_spawn;

    public int BuildLimit;
    public int DeathLimit;
    public double[] spectator_spawn;
    public double[] border_mid;
    public int border_size;

    List<MapData_Teams> teamInfo = new ArrayList<>();

    public MapData() {
        try {
            String file_content = Files.readString(Paths.get("./world/map_config.json"));
            JsonObject json = JsonParser.parseString(file_content).getAsJsonObject();

            this.ConfigVersion = json.get("version").getAsInt();
            this.map_name = json.get("map_name").getAsString();
            this.game = json.get("game").getAsString();

            if (!this.game.equals("crystalblitz")) {
                Bukkit.getLogger().log(Level.SEVERE, "You've inserted a game config for \"" + this.game + "\", Please update the world file to be compatible with Crystal Blitz");
                throw new Exception();
            }
            if (ConfigVersion != 2) {
                Bukkit.getLogger().log(Level.SEVERE, "Incorrect Config Version!, Excepted 2 but got " + ConfigVersion);
                throw new Exception();
            }

            JsonArray q_spawn = json.get("queue_spawn").getAsJsonArray();
            this.queue_spawn = new double[] { q_spawn.get(0).getAsDouble(), q_spawn.get(1).getAsDouble(), q_spawn.get(2).getAsDouble() };

            JsonArray s_spawn = json.get("spectator_spawn").getAsJsonArray();
            this.spectator_spawn = new double[] { s_spawn.get(0).getAsDouble(), s_spawn.get(1).getAsDouble(), s_spawn.get(2).getAsDouble() };

            this.DeathLimit = json.get("death_y_coord").getAsInt();
            this.BuildLimit = json.get("build_limit_y_coord").getAsInt();

            JsonArray border_mid_ = json.get("border_mid").getAsJsonArray();
            this.border_mid = new double[] {border_mid_.get(0).getAsDouble(), border_mid_.get(1).getAsDouble()};
            this.border_size = json.get("border_size").getAsInt();

            JsonObject teams = json.get("teams").getAsJsonObject();
            teamInfo.add(new MapData_Teams("blue", teams.get("blue").getAsJsonObject()));
            teamInfo.add(new MapData_Teams("cyan", teams.get("cyan").getAsJsonObject()));
            teamInfo.add(new MapData_Teams("green", teams.get("green").getAsJsonObject()));
            teamInfo.add(new MapData_Teams("lime", teams.get("lime").getAsJsonObject()));
            teamInfo.add(new MapData_Teams("magenta", teams.get("magenta").getAsJsonObject()));
            teamInfo.add(new MapData_Teams("red", teams.get("red").getAsJsonObject()));
            teamInfo.add(new MapData_Teams("yellow", teams.get("yellow").getAsJsonObject()));
            teamInfo.add(new MapData_Teams("white", teams.get("white").getAsJsonObject()));

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load the maps configuration file!\n Error:");
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "The plugin will be disabled!");
            Bukkit.getPluginManager().disablePlugin(crystalBlitz.getInstance());
        }
    }

    public Location get_queue_spawn(World w) {
        return new Location(w, queue_spawn[0], queue_spawn[1], queue_spawn[2]);
    }

    private MapData_Teams getTeamInfo(String team) {
        for (MapData_Teams t : teamInfo) {
            if (t.name.equals(team)) {
                return t;
            }
        }
        return null;
    }

    public double[] getNexus(String team) {
        MapData_Teams t = getTeamInfo(team);
        Location loc = t.nexusBlock;
        return new double[] {loc.getX(), loc.getY(), loc.getZ()};
    }

    public double[] getSpawn(String team) {
        MapData_Teams t = getTeamInfo(team);
        Location loc = t.spawn;
        return new double[] {loc.getX(), loc.getY(), loc.getZ()};
    }

    public double[] getShop(String team) {
        MapData_Teams t = getTeamInfo(team);
        Location loc = t.shopSpawn;
        return new double[] {loc.getX(), loc.getY(), loc.getZ()};
    }

    public Location getStaleShardLoc(String team) {
        MapData_Teams t = getTeamInfo(team);
        Location loc = t.staleShard;
        return loc;
    }
}

class MapData_Teams{

    String name;
    Location nexusBlock; //bottom block
    Location spawn;
    Location shopSpawn;
    Location staleShard; //center block

    public MapData_Teams(String name, JsonObject json) {
        this.name = name;

        JsonArray nb = json.get("nexus").getAsJsonArray();
        this.nexusBlock = new Location(Bukkit.getWorld("world"), nb.get(0).getAsDouble(), nb.get(1).getAsDouble(), nb.get(2).getAsDouble());

        JsonArray s = json.get("spawn").getAsJsonArray();
        this.spawn = new Location(Bukkit.getWorld("world"), s.get(0).getAsDouble(), s.get(1).getAsDouble(), s.get(2).getAsDouble());

        JsonArray shop = json.get("shop").getAsJsonArray();
        this.shopSpawn = new Location(Bukkit.getWorld("world"), shop.get(0).getAsDouble(), shop.get(1).getAsDouble(), shop.get(2).getAsDouble());

        JsonArray stale = json.get("staleshard").getAsJsonArray();
        this.staleShard = new Location(Bukkit.getWorld("world"), stale.get(0).getAsDouble(), stale.get(1).getAsDouble(), stale.get(2).getAsDouble());
    }
}
