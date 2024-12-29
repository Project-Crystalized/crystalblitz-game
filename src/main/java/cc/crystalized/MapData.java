package cc.crystalized;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class MapData {

    public int ConfigVersion;

    public String map_name;
    public String game;
    public double[] queue_spawn;

    public int BuildLimit;
    public int DeathLimit;
    public double[] spectator_spawn;

    //In order of:
    //blue, cyan, green, lime, magenta, red, yellow and white
    public double[] nexuses;
    public double[] shops;
    public double[] spawns;

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
            if (ConfigVersion != 1) {
                Bukkit.getLogger().log(Level.SEVERE, "Incorrect Config Version!, Excepted 1 but got " + ConfigVersion);
                throw new Exception();
            }

            JsonArray q_spawn = json.get("queue_spawn").getAsJsonArray();
            this.queue_spawn = new double[] { q_spawn.get(0).getAsDouble(), q_spawn.get(1).getAsDouble(), q_spawn.get(2).getAsDouble() };

            JsonArray s_spawn = json.get("spectator_spawn").getAsJsonArray();
            this.spectator_spawn = new double[] { s_spawn.get(0).getAsDouble(), s_spawn.get(1).getAsDouble(), s_spawn.get(2).getAsDouble() };

            this.DeathLimit = json.get("death_y_coord").getAsInt();
            this.BuildLimit = json.get("build_limit_y_coord").getAsInt();

            JsonArray bluenexus = json.get("blue_nexus").getAsJsonArray();
            JsonArray cyannexus = json.get("cyan_nexus").getAsJsonArray();
            JsonArray greennexus = json.get("green_nexus").getAsJsonArray();
            JsonArray limenexus = json.get("lime_nexus").getAsJsonArray();
            JsonArray magentanexus = json.get("magenta_nexus").getAsJsonArray();
            JsonArray rednexus = json.get("red_nexus").getAsJsonArray();
            JsonArray yellownexus = json.get("yellow_nexus").getAsJsonArray();
            JsonArray whitenexus = json.get("white_nexus").getAsJsonArray();
            this.nexuses = new double[] {
                    bluenexus.get(0).getAsDouble(), bluenexus.get(1).getAsDouble(), bluenexus.get(2).getAsDouble(),
                    cyannexus.get(0).getAsDouble(), cyannexus.get(1).getAsDouble(), cyannexus.get(2).getAsDouble(),
                    greennexus.get(0).getAsDouble(), greennexus.get(1).getAsDouble(), greennexus.get(2).getAsDouble(),
                    limenexus.get(0).getAsDouble(), limenexus.get(1).getAsDouble(), limenexus.get(2).getAsDouble(),
                    magentanexus.get(0).getAsDouble(), magentanexus.get(1).getAsDouble(), magentanexus.get(2).getAsDouble(),
                    rednexus.get(0).getAsDouble(), rednexus.get(1).getAsDouble(), rednexus.get(2).getAsDouble(),
                    yellownexus.get(0).getAsDouble(), yellownexus.get(1).getAsDouble(), yellownexus.get(2).getAsDouble(),
                    whitenexus.get(0).getAsDouble(), whitenexus.get(1).getAsDouble(), whitenexus.get(2).getAsDouble()
            };

            JsonArray blueshop = json.get("blue_shop").getAsJsonArray();
            JsonArray cyanshop = json.get("cyan_shop").getAsJsonArray();
            JsonArray greenshop = json.get("green_shop").getAsJsonArray();
            JsonArray limeshop = json.get("lime_shop").getAsJsonArray();
            JsonArray magentashop = json.get("magenta_shop").getAsJsonArray();
            JsonArray redshop = json.get("red_shop").getAsJsonArray();
            JsonArray yellowshop = json.get("yellow_shop").getAsJsonArray();
            JsonArray whiteshop = json.get("white_shop").getAsJsonArray();
            this.shops = new double[] {
                    blueshop.get(0).getAsDouble(), blueshop.get(1).getAsDouble(), blueshop.get(2).getAsDouble(),
                    cyanshop.get(0).getAsDouble(), cyanshop.get(1).getAsDouble(), cyanshop.get(2).getAsDouble(),
                    greenshop.get(0).getAsDouble(), greenshop.get(1).getAsDouble(), greenshop.get(2).getAsDouble(),
                    limeshop.get(0).getAsDouble(), limeshop.get(1).getAsDouble(), limeshop.get(2).getAsDouble(),
                    magentashop.get(0).getAsDouble(), magentashop.get(1).getAsDouble(), magentashop.get(2).getAsDouble(),
                    redshop.get(0).getAsDouble(), redshop.get(1).getAsDouble(), redshop.get(2).getAsDouble(),
                    yellowshop.get(0).getAsDouble(), yellowshop.get(1).getAsDouble(), yellowshop.get(2).getAsDouble(),
                    whiteshop.get(0).getAsDouble(), whiteshop.get(1).getAsDouble(), whiteshop.get(2).getAsDouble()
            };

            JsonArray bluespawn = json.get("blue_spawn").getAsJsonArray();
            JsonArray cyanspawn = json.get("cyan_spawn").getAsJsonArray();
            JsonArray greenspawn = json.get("green_spawn").getAsJsonArray();
            JsonArray limespawn = json.get("lime_spawn").getAsJsonArray();
            JsonArray magentaspawn = json.get("magenta_spawn").getAsJsonArray();
            JsonArray redspawn = json.get("red_spawn").getAsJsonArray();
            JsonArray yellowspawn = json.get("yellow_spawn").getAsJsonArray();
            JsonArray whitespawn = json.get("white_spawn").getAsJsonArray();
            this.spawns = new double[] {
                    bluespawn.get(0).getAsDouble(), bluespawn.get(1).getAsDouble(), bluespawn.get(2).getAsDouble(),
                    cyanspawn.get(0).getAsDouble(), cyanspawn.get(1).getAsDouble(), cyanspawn.get(2).getAsDouble(),
                    greenspawn.get(0).getAsDouble(), greenspawn.get(1).getAsDouble(), greenspawn.get(2).getAsDouble(),
                    limespawn.get(0).getAsDouble(), limespawn.get(1).getAsDouble(), limespawn.get(2).getAsDouble(),
                    magentaspawn.get(0).getAsDouble(), magentaspawn.get(1).getAsDouble(), magentaspawn.get(2).getAsDouble(),
                    redspawn.get(0).getAsDouble(), redspawn.get(1).getAsDouble(), redspawn.get(2).getAsDouble(),
                    yellowspawn.get(0).getAsDouble(), yellowspawn.get(1).getAsDouble(), yellowspawn.get(2).getAsDouble(),
                    whitespawn.get(0).getAsDouble(), whitespawn.get(1).getAsDouble(), whitespawn.get(2).getAsDouble()
            };

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load the maps configuration file!\n Error:");
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "The plugin will be disabled!");
            Bukkit.getPluginManager().disablePlugin(crystalBlitz.getInstance());
            throw new RuntimeException(new Exception());
        }
    }

    public Location get_queue_spawn(World w) {
        return new Location(w, queue_spawn[0], queue_spawn[1], queue_spawn[2]);
    }

    public double[] getNexus(String team) {
        double[] J = nexuses;
        switch (team) {
            case "blue":
                return new double[] {J[0], J[1], J[2]};
            case "cyan":
                return new double[] {J[3], J[4], J[5]};
            case "green":
                return new double[] {J[6], J[7], J[8]};
            case "lime":
                return new double[] {J[9], J[10], J[11]};
            case "magenta":
                return new double[] {J[12], J[13], J[14]};
            case "red":
                return new double[] {J[15], J[16], J[17]};
            case "yellow":
                return new double[] {J[18], J[19], J[20]};
            case "white":
                return new double[] {J[21], J[22], J[23]};
            default:
                Bukkit.getLogger().log(Level.SEVERE, "unknown team" + team);
                throw new RuntimeException();
        }
    }

    public double[] getSpawn(String team) {
        double[] J = spawns;
        switch (team) {
            case "blue":
                return new double[] {J[0], J[1], J[2]};
            case "cyan":
                return new double[] {J[3], J[4], J[5]};
            case "green":
                return new double[] {J[6], J[7], J[8]};
            case "lime":
                return new double[] {J[9], J[10], J[11]};
            case "magenta":
                return new double[] {J[12], J[13], J[14]};
            case "red":
                return new double[] {J[15], J[16], J[17]};
            case "yellow":
                return new double[] {J[18], J[19], J[20]};
            case "white":
                return new double[] {J[21], J[22], J[23]};
            default:
                Bukkit.getLogger().log(Level.SEVERE, "unknown team" + team);
                throw new RuntimeException();
        }
    }

    public double[] getShop(String team) {
        double[] J = shops;
        switch (team) {
            case "blue":
                return new double[] {J[0], J[1], J[2]};
            case "cyan":
                return new double[] {J[3], J[4], J[5]};
            case "green":
                return new double[] {J[6], J[7], J[8]};
            case "lime":
                return new double[] {J[9], J[10], J[11]};
            case "magenta":
                return new double[] {J[12], J[13], J[14]};
            case "red":
                return new double[] {J[15], J[16], J[17]};
            case "yellow":
                return new double[] {J[18], J[19], J[20]};
            case "white":
                return new double[] {J[21], J[22], J[23]};
            default:
                Bukkit.getLogger().log(Level.SEVERE, "unknown team" + team);
                throw new RuntimeException();
        }
    }
}
