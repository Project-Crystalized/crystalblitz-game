package cc.crystalized;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.List;

//This class is for Pure Shard (Strong Shard) Generators, so that they have health, etc
public class PureShardGenerator {
    //Change max health of the crystals here - mish
    public static final int MAX_HEALTH = 10;
    //This is the material it will become when destroyed
    //change the material when destoryed here - mish
    private static final Material DESTROYED_MATERIAL = Material.WHITE_CONCRETE;

    //The locations so that the generators blocks can be found
    private final Location theBottomSourceBlockLocation;
    //The two source blocks location will be stored in the array list
    /*Storing locations not blocks specificly as location is more important for the generator
    * as the block will be modified etc when broken
    * so location seems more consistant and when block is needed it can be accesed through the location
    */
    private final List<Location> sourceBlocks = new ArrayList<>();
    //The spikes/crystals on the side of generators
    private final List<SpikeData> spikes = new ArrayList<>();
    //Initialy health is set to max health by default
    private int health = MAX_HEALTH;
    //The text display for health of pure shard generator
    private TextDisplay healthDisplay;
    //Takes in the source block when created
    public PureShardGenerator(Block sourceBlock) {
        Location bottomSourceloc = sourceBlock.getLocation();
        //If first block is top block moves block down
        //searching specificly for the bottom block to consistantly put health bar above
        Block below = sourceBlock.getRelative(BlockFace.DOWN);
        if (isPureGeneratorSourceBlock(below)) {
            //sets the correct location to the bottom source blcock
            bottomSourceloc = below.getLocation();
        }
        //sets the source block location
        theBottomSourceBlockLocation = bottomSourceloc.clone();

        //Methods for functionality
        findAndStoreSourceBlocksInArrayList();
        findSpikes();
        createHealthBar();
    }

    //This method is for checking if it is a pure genetator source block
    //Meaning that if the block being checked is a pure shard generator then returns true
    public static boolean isPureGeneratorSourceBlock(Block block) {
        //Checks the type first, if not than returns false as it is not the pure generator source block
        if (block.getType() != Material.BLACK_GLAZED_TERRACOTTA) {
            return false;
        }
        //If it doesn't have directional data then returns false as it is not the pure generator block
        if (!(block.getBlockData() instanceof Directional directional)) {
            return false;
        }
        //If facing the north direction returns true, meaning that this is the pure generator source block
        return directional.getFacing() == BlockFace.NORTH;
    }
    //This method is used to find the source blocks of the generator, for later modification
    private void findAndStoreSourceBlocksInArrayList() {
        //Ensures that the array list is cleared
        sourceBlocks.clear();
        //Setting the bottom block to known bottom source location
        Block bottom = theBottomSourceBlockLocation.getBlock();
        //Ensuring it is a pure generator source block and adding it's location to the array list
        if (isPureGeneratorSourceBlock(bottom)) {
            //ensuring it is cloned for extra safety
            sourceBlocks.add(bottom.getLocation().clone());
        }
        //Setting the top block to the block above
        Block top = bottom.getRelative(BlockFace.UP);
        //And adding it's location to the source blocks array
        if (isPureGeneratorSourceBlock(top)) {
            sourceBlocks.add(top.getLocation().clone());
        }
    }
    //This is to find all it's original spikes/crystals and store them
    //Not updated when the player mines them, so it can later be restored, just the block in the world becomes air when mined.
    private void findSpikes() {
        //ensuring that the array list is cleared
        spikes.clear();
        //Looks around the 2 block tall generator for spikes/crystals, with radius of 2 to find all the spikes
        //The values are intentionaly higher than needed to ensure no crystals is mising
        for (int x = -2; x <= 2; x++) {
            //The y has extra heigh and chcks below just incase
            for (int y = -1; y <= 3; y++) {
                for (int z = -2; z <= 2; z++) {
                    //The blcok with the current search locations
                    Block block = theBottomSourceBlockLocation.clone().add(x, y, z).getBlock();
                    //if it is the spike than it is added to the spike array list
                    if (block.getType() == Material.AMETHYST_CLUSTER || block.getType() == Material.LARGE_AMETHYST_BUD) {
                        spikes.add(new SpikeData(block.getLocation().clone(), block.getType(), block.getBlockData().clone()));
                    }
                }
            }
        }
    }

    //This is a helper method for other classes
    //Checks if the location is a generator source block
    public boolean isSourceBlock(Location loc) {
        //returns true if sourceBlocks have the location
        //could just check .contains(loc), but this ensurse it is the exact block location
        return sourceBlocks.contains(loc.getBlock().getLocation());
    }

    //This heleper method checks if it has spikes
    public boolean ownsSpike(Location loc) {
        //This is not entierly neccesary but makes sure that comparing the exact block location
        Location blockLockation = loc.getBlock().getLocation();
        //Goes through the spikes and if location is the same returns true
        for (SpikeData spike : spikes) {
            if (spike.location.equals(blockLockation)) {
                return true;
            }
        }
        //False if nothing found
        return false;
    }

    //Returns if it is active or not, active when has more than zero health
    public boolean isActive() {
        return health > 0;
    }
    //A public method to get health of the generator
    public int getHealth() {
        return health;
    }
    //A public method to get max health of the generator
    public int getMaxHealth() {
        return MAX_HEALTH;
    }
    //This is the method that deals damage to the pure shard generator
    public void damage(int amount) {
        //when no active just returns
        if (!isActive()) {
            return;
        }
        health = health - amount;
        //if health got smaller than zero than sets it to zero and destroys the crystal
        if (health <= 0) {
            health = 0;
            destroy();
        }
        //Updates the health bar on damage of the generator
        updateHealthBar();
    }

    //This is the destruction method of the generator
    private void destroy() {
       //This is to change the source blocs to white concrete
        for (Location loc : sourceBlocks) {
            loc.getBlock().setType(DESTROYED_MATERIAL, false);
        }

        //This is to set to air all the spikes when the generator is dead
        for (SpikeData spike : spikes) {
            spike.location.getBlock().setType(Material.AIR, false);
        }
    }

    //This is the method to revive a generator.
    public void revive() {
        //setting health back to the max health
        health = MAX_HEALTH;

        //Restores source blocks as they were
        for (Location loc : sourceBlocks) {
            Block block = loc.getBlock();
            block.setType(Material.BLACK_GLAZED_TERRACOTTA, false);
            Directional directional = (Directional) block.getBlockData();
            directional.setFacing(BlockFace.NORTH);
            block.setBlockData(directional, false);
        }

        //This restores all th broken spikes to it's original block
        for (SpikeData spike : spikes) {
            spike.location.getBlock().setBlockData(spike.blockData.clone(), false);
        }
        //Updates the health bar to visualy set it to full
        updateHealthBar();
    }

    //This method is used to create health bar above the crystals
    private void createHealthBar() {
        //Is roughly 2 blocks higher than the the bottom sorce, with a small x and z offset
        Location displayLocation = theBottomSourceBlockLocation.clone().add(0.5, 2.8, 0.5);

        //creating the health display
        healthDisplay = theBottomSourceBlockLocation.getWorld().spawn(displayLocation,
                TextDisplay.class, textHealthDisplay -> {
                    //ensuring it is see through and centered
                    textHealthDisplay.setSeeThrough(true);
                    textHealthDisplay.setBillboard(Display.Billboard.CENTER);
                }
        );
        //Updates the health bar
        updateHealthBar();
    }

    //This method is to update the health display
    //Health display similiar to Nexus bars for consistansy
    private void updateHealthBar() {
        //when null and not valid returns
        if (healthDisplay == null || !healthDisplay.isValid()) {
            return;
        }
        //replace the display when updateHealthBar is called
        //Similiar to nexus same style health display
        healthDisplay.text(Component.text("\uE11A")
                        .append(Component.text("\uE11B".repeat(health)))
                        .append(Component.text("\uE11C".repeat(MAX_HEALTH - health)))
                        .append(Component.text("\uE11D"))
        );
    }

    //This is the extrac spike data class, for the side crystals, should only be used in PureShardGenerator hence it is private
    //Made it statick as it is is juat a crystal/spike data holder. - Mish
    //Made to avoid having to have multiple extra array lists in PureShard generator
    private static class SpikeData {
        //stores the location, material and block data of the spike
        private final Location location;
        private final Material material;
        private final BlockData blockData;

        //The constructor for it
        private SpikeData(Location location, Material material, BlockData blockData) {
            this.location = location;
            this.material = material;
            this.blockData = blockData;
        }
    }
}



