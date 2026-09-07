package cc.crystalized;


import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.stream.Stream;

//The map manager is the class which handless map changing, so that the game map can be empty when game ends, or on start up.
//By utlizing the dimensions, and copying from the template locationg in the plugin folder.
public class MapManager {
    //The waitng world overworld
    private static final NamespacedKey SOURCE_WORLD_NAME_KEY = NamespacedKey.minecraft("overworld");
    //The disposable dimension which is  used for crystalblitz games
    private static final NamespacedKey GAME_WORLD_NAME_KEY = NamespacedKey.minecraft("crystalblitz_game");
    private final crystalBlitz plugin;
    private World gameWorld;

    //Takes in the plugin
    public MapManager(crystalBlitz plugin) {
        this.plugin = plugin;
    }

    //The regular overworld, players wait in it before game starts
    public World getSourceWorld() {
        return Bukkit.getWorld(SOURCE_WORLD_NAME_KEY );
    }

    //Gets the game world which will be used for the game, it can exist before the game starts as it is created at start time
    public World getGameWorld() {
        //sets it to game world if it is not set already
        if (gameWorld != null) {
            return gameWorld;
        }
        gameWorld = Bukkit.getWorld(GAME_WORLD_NAME_KEY);
        return gameWorld;
    }

    //This gets the active world where players should be at this time, if game manager is null it is the source/waiting world
    //if game manager is not null meaning that the game is going on so it should be the game world
    public World getActiveWorld() {
        if (plugin.gamemanager != null) {
            if (getGameWorld() != null) {
                return gameWorld;
            }
        }
        return getSourceWorld();
    }

    //This is called on server start, anything left from game world dimensions gets remove, then a new game world dimension gets created
    //Which fixes the crashing during the game issue
    public void setup() {
        //deletes the old game world
        deleteOldGameWorld();
        //tries to create new game world
        if (!createGameWorld()) {
            //if failed says it failed
            plugin.getLogger().severe("Failed to prepare crystalblitz game world!");
            return;
        }
        //on susses
        plugin.getLogger().info("CrystalBlitz's game world is prepared and waiting for game to start.");
    }


    //Creates a new dimension for the game from the clean template in plugin folder
    public boolean createGameWorld() {
        if (getGameWorld() != null) {
            plugin.getLogger().warning("Tried to create a game world dimension, but one already exists!");
            return false;
        }
        //makes sure that the overworld exists.
        World sourceWorld = getSourceWorld();
        if (sourceWorld == null) {
            plugin.getLogger().severe("Could not find the regular overworld!");
            return false;
        }
        /*The path to clean map template
         * world/MapTemplate/crystalblitz_map_Template/
         */
        Path templateDimension = getTemplateDimensionFolder(sourceWorld);

        /*The path to dimension where game takes place
         * world/dimensions/minecraft/crystalblitz_game/
         */
        Path gameDimension = getGameDimensionFolder(sourceWorld);

        //if template doesn't exists
        if (!Files.exists(templateDimension)) {
            plugin.getLogger().severe("Could not find CrystalBlitz map template! Please add the template here: " + templateDimension);
            return false;
        }
        //makes sure it is a directory/folder
        if (!Files.isDirectory(templateDimension)) {
            plugin.getLogger().severe("CrystalBlitz map template is not a folder: " + templateDimension);
            return false;
        }

        //Makes sure the folder is fully cleared and deleted so that nothing is leftt
        if (!deleteDirectory(gameDimension)) {
            plugin.getLogger().severe("Could not clear the old CrystalBlitz game dimension");
            return false;
        }
        //Tries to copy the crystalblitz map template
        try {
            plugin.getLogger().info("Copying CrystalBlitz map template...");
            copyDimenson(templateDimension, gameDimension);

            //Had some issues with the new dimension thinking they are identical, so had to delete the identifier, so that copy must not inherit those files
            //Eddit: Now changed it in copy dimension should be not copied in anyway, but this makes sure they totaly don't exist
            Files.deleteIfExists(gameDimension.resolve("uid.dat"));
            Files.deleteIfExists(gameDimension.resolve("session.lock"));
            //Also the new version stores those extra world identifiers, paper needs to create new ones for crystalblitz_game.To be consindered seperate
            //so deletes those as well
            Files.deleteIfExists(gameDimension
                .resolve("data")
                .resolve("paper")
                .resolve("metadata.dat")
            );
            //catches the exception and deletes the attempt
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to copy CrystalBlitz map template!", e);
            deleteDirectory(gameDimension);
            return false;
        }
        //On succes prints the location of template dimension and run time dimension
        plugin.getLogger().info("Template dimension: " + templateDimension);
        plugin.getLogger().info("Runtime dimension: " + gameDimension);


        //The file now exist inside the correct location dimensions/minecraft. Lastly, paper needs to load the dimension
        gameWorld = Bukkit.createWorld(WorldCreator.ofKey(GAME_WORLD_NAME_KEY));
        if (gameWorld == null) {
            plugin.getLogger().severe("Failed to load CrystalBlitz game dimension!");
            if (!deleteDirectory(gameDimension)) {
                plugin.getLogger().severe("As well failed to delete the broken game dimension!");
                return false;
            }
            plugin.getLogger().info("Deleted the broken game dimension!");
            return false;
        }
        plugin.getLogger().info("Created fresh CrystalBlitz game dimension sussesfully: " + gameWorld.getKey());
        //Makes sure the game dimension is loaded while players wait, as pure shards generators need it, so they must not be unloaded
        preloadGameWorld(gameWorld);
        return true;
    }


    //Loads chunks using the map world border and uses tickets to make sure they stay loaded, for later pure shard generators locating
    private void preloadGameWorld(World world) {
        WorldBorder border = world.getWorldBorder();
        //gets the center
        Location center = border.getCenter();
        //gets the half size as it would go half the size in each direction
        double halfSize = plugin.mapdata.border_size / 2.0;
        //Finds the right, left, top and bottom edges of the arena
        int minChunkX = (int) Math.floor((center.getX() - halfSize) / 16.0);
        int maxChunkX = (int) Math.floor((center.getX() + halfSize) / 16.0);
        int minChunkZ = (int) Math.floor((center.getZ() - halfSize) / 16.0);
        int maxChunkZ = (int) Math.floor((center.getZ() + halfSize) / 16.0);
        int loaded = 0;
        //everything between those chunks gets loaded and ticketed to prevent it from being unloaded
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                Chunk chunk = world.getChunkAt(chunkX, chunkZ);
                //Makes sure it is loaded
                chunk.load();
                //And makes sure it will not unload while people are in the waiting world
                chunk.addPluginChunkTicket(plugin);
                loaded++;
            }
        }
        //The loggers
        plugin.getLogger().info("Preloaded and ticketed " + loaded + " CrystalBlitz game arena chunks.");
        //shows how many loaded chunks are there now
        plugin.getLogger().info("CrystalBlitz game world currently has " + world.getLoadedChunks().length + " loaded chunks.");
    }


    //Makes sure everyone is back in the waiting world, removes the chunk tickets, unloads the game dimension and then deletes it fully
    public boolean destroyGameWorld() {
        World world = getGameWorld();
        if (world == null) {
            return true;
        }
        World sourceWorld = getSourceWorld();
        if (sourceWorld == null) {
            plugin.getLogger().severe("Cannot destroy CrystalBlitz game world because the waiting world is null!");
            return false;
        }
        //All player must be removed from game world to unload it
        Location lobbyLocation = new Location(sourceWorld,
                plugin.mapdata.queue_spawn[0],
                plugin.mapdata.queue_spawn[1],
                plugin.mapdata.queue_spawn[2]
        );
        for (Player player : world.getPlayers()) {
            boolean teleported = player.teleport(lobbyLocation);
            if (!teleported) {
                plugin.getLogger().warning("Failed to teleport " + player.getName() + " out of the CrystalBlitz game world!");
            }
        }
        //If the world still has players stops
        if (!world.getPlayers().isEmpty()) {
            plugin.getLogger().severe("Cannot unload CrystalBlitz game world because " + world.getPlayers().size() + " player(s) are still inside!");
            return false;
        }
        //removes the chuck tickets which were added earlier, before unloading the world
        removeGameWorldChunkTickets(world);
        plugin.getLogger().info("Unloading CrystalBlitz game dimension");
        if (!Bukkit.unloadWorld(world, false)) {
            plugin.getLogger().severe("Failed to unload CrystalBlitz game dimension!");
            return false;
        }
        //sets the world to null
        gameWorld = null;
        //makes sure the directory of the game dimensions are deleted
        Path gameDimension = getGameDimensionFolder(sourceWorld);
        if (!deleteDirectory(gameDimension)) {
            plugin.getLogger().severe("Failed to delete CrystalBlitz game dimension!");
            return false;
        }
        //susesfully destroyed the game world
        plugin.getLogger().info("Destroyed CrystalBlitz game dimension fully sussesfully.");
        return true;
    }


    //Removes all chunk tickets in the game world
    private void removeGameWorldChunkTickets(World world) {
        int removed = 0;
        //goes through all the world loaded chunks
        for (Chunk chunk : world.getLoadedChunks()) {
            if (chunk.removePluginChunkTicket(plugin)) {
                removed++;
            }
        }
        plugin.getLogger().info("Removed game world chunk tickets: " + removed);
    }


    //Deletes anything left from a server crash/previous server end
    private void deleteOldGameWorld() {
        World sourceWorld = getSourceWorld();
        if (sourceWorld == null) {
            plugin.getLogger().severe("Source world isn't loaded, cannot clean old game dimension!");
            return;
        }
        World loadedGameWorld = Bukkit.getWorld(GAME_WORLD_NAME_KEY);
        //If runtime dimensions is already loaded unloads it first
        if (loadedGameWorld != null) {
            //makes sure there are no players incase they end up there somehow
            Location lobbyLocation = new Location(sourceWorld,
                    plugin.mapdata.queue_spawn[0],
                    plugin.mapdata.queue_spawn[1],
                    plugin.mapdata.queue_spawn[2]
            );
            for (Player player : loadedGameWorld.getPlayers()) {
                player.teleport(lobbyLocation);
            }
            //removes chunk tickets
            removeGameWorldChunkTickets(loadedGameWorld);
            //Tries to unload the world
            if (!Bukkit.unloadWorld(loadedGameWorld, false)) {
                plugin.getLogger().severe("Could not unload old CrystalBlitz game dimension!");
                return;
            }
        }
        //makes sure game world is null
        gameWorld = null;
        //gets the path to game dimension and deletes it if it exists
        Path gameDimension = getGameDimensionFolder(sourceWorld);
        if (Files.exists(gameDimension)) {
            plugin.getLogger().warning("Found old game dimension. Deleting it...");
            if (!deleteDirectory(gameDimension)) {
                plugin.getLogger().severe("Could not delete old game dimension!");
            }
            else {
                plugin.getLogger().info("Deleted old game dimension!");
            }
        }
    }



    /*The clean map template path
     * world/MapTemplate/crystalblitz_map_Template/
     */
    private Path getTemplateDimensionFolder(World sourceWorld) {
        Path sourceDimension = sourceWorld.getWorldFolder().toPath();
        //Goes up from overworld to minecraft to dimension to world. To return to the main world folder
        Path worldFolder = sourceDimension.getParent().getParent().getParent();
        //path to template from the world folder
        return worldFolder.resolve("MapTemplate").resolve("crystalblitz_map_Template");
    }
    /*The path to dimension where game takes place
     * world/dimensions/minecraft/crystalblitz_game/
     */
    //takes in the overworld to gets it location and map the path from it.
    private Path getGameDimensionFolder(World sourceWorld) {
        Path sourceDimension = sourceWorld.getWorldFolder().toPath();
        return sourceDimension.getParent().resolve("crystalblitz_game");
    }


    //Copies the clean map template into the game world dimension .
    private void copyDimenson(Path source, Path destination) throws IOException {
        //goes through everything in the template
        try (Stream<Path> stream = Files.walk(source)) {
            for (Path sourcePath : stream.toList()) {
                //gets the relative path, so same structure can be recreated
                Path relativePath = source.relativize(sourcePath);
                //makes sure path comperision works on both widnows and linux
                String relative = relativePath.toString().replace('\\', '/');
                //gets tbe file name
                String fileName = sourcePath.getFileName().toString();
                //Prevents from copying of Bukkit/Paper filed identifiers
                if (fileName.equals("uid.dat") || fileName.equals("session.lock")) {
                    continue;
                }
                //Also skips identity metadata
                if (relative.equals("data/paper/metadata.dat")) {
                    continue;
                }
                //Creates the same relative path at the destination
                Path destinationPath = destination.resolve(relativePath);
                //If it is a folder in source creates a folder in destination
                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(destinationPath);
                    continue;
                }
                //when it is a file makes sure that the folder that is in exists, to prevent any issues
                Files.createDirectories(destinationPath.getParent());
                //Copies the file
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }
    }


    //Deletes everything in the folder/directory from bottom to the top
    private boolean deleteDirectory(Path path) {
        try {
            //If file doesn't exists returns true as there is techinicly nothing so already deleted
            if (!Files.exists(path)) {
                return true;
            }
            //goes through the files and reverses the order, as folders need to be empty to be deleted so that is why reversing
            //as it will go from the bottom to the top of the directory.
            try (Stream<Path> paths = Files.walk(path)) {
                paths.sorted(Comparator.reverseOrder()).forEach(file -> {
                    try {
                        //deletes if it exists
                        Files.deleteIfExists(file);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            return true;
            //if some error happens
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to delete directory/folder: " + path, e);
            return false;
        }
    }
}
