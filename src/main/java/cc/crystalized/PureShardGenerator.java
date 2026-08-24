package cc.crystalized;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

//This class is for Pure Shard (Strong Shard) Generators, so that they have health, etc
public class PureShardGenerator {
    //Change max health of the crystals here - mish
    //Changed to 30 as Mite said - Mish
    public static final int MAX_HEALTH = 30;
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
    //The timer display for spike regeneration
    private TextDisplay spikeTimerRegenDisplay;
    //The tick at which next spike should regen for text display
    private int nextSpikeRegenTick;
    //This is used to track if the regeneration for side crystal currently running, to prevent
    //more than one regeneration task running at the same time
    //After one side crystal regens it is set to false, which allows the next missing crystal to regenerate if there is one
    private boolean spikeRegenerationCurrentlyRunning = false;
    //The spike re-generation task to be able to cancel it when needed
    private BukkitTask spikeRegenerationTask;
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
        //For the timer to be displayed, the regeneration itself is triggered when a crystal is broken so not here anymore
        createSpikeTimerRegenDisplay();
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
        //so that any extra crystals not pop up
        cancelSpikeRegeneration();
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
        //So that any potential left over spikes don't regen
        cancelSpikeRegeneration();
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
    //The method to clean up health bar if server shut down unexpectedly
    public void removeHealthBar() {
        if (healthDisplay != null && healthDisplay.isValid()) {
            healthDisplay.remove();
        }
        //now removes the timer as well
        if (spikeTimerRegenDisplay != null && spikeTimerRegenDisplay.isValid()) {
            spikeTimerRegenDisplay.remove();
        }
    }

    //The spike/side crytal re-generation logic
    public void startSpikeRegeneration() {
        //If the generator is broken nothing happens
        if (!isActive()) {
            return;
        }
        //If already counting/regenerating nothing happens
        //That check is specificly for when the player breaks more than one side crystal so it won't start another task early as 1 crystal regen at a time
        if (spikeRegenerationCurrentlyRunning) {
            return;
        }
        //If generator is full nothing happens as there is nothing to regenerate
        if (!hasMissingSpike()) {
            return;
        }
        //the regeneration has started
        spikeRegenerationCurrentlyRunning = true;
        //The regeneration time depending on gen upgrades
        int regenerationTimeInTicks = getSpikeRegenerationTime();
        //when the next side crystal going to regen in ticks, for text display
        nextSpikeRegenTick = Bukkit.getCurrentTick() + regenerationTimeInTicks;
        //setting it to the task to be able to cancel later
        spikeRegenerationTask = new BukkitRunnable() {
            @Override
            public void run() {
                //When it runs it sets the regeneration to false and as task ended it sets to null
                spikeRegenerationCurrentlyRunning = false;
                spikeRegenerationTask = null;
                //makes sure it will only happen during the game
                if (crystalBlitz.getInstance().gamemanager == null) {
                    return;
                }
                //makes sure it will not happen with a broken generator
                if (!isActive()) {
                    return;
                }
                //regenerates one crystal/spike
                regenOneSpike();
                //if there are crystals still mising starts another task
                startSpikeRegeneration();
            }
        }.runTaskLater(crystalBlitz.getInstance(), regenerationTimeInTicks);
    }


    //This method is to get the regeneration time, here is to changed trhe time depending on gen upgrades
    private int getSpikeRegenerationTime() {
        BossBarStates state = crystalBlitz.getInstance().gamemanager.bossbar.currentstate;
        //I adjusted values to fit the gen upgrades nicely
        return switch (state) {
            case starting -> 20 * 12;
            case GenUpgradeI -> 20 * 10;
            case GenUpgradeII -> 20 * 8;
            case GenUpgradeIII -> 20 * 6;
            case GenUpgradeIV, Overtime -> 20 * 4;
        };
    }
    //Regens of the missing spike method
    private void regenOneSpike() {
        for (SpikeData spike : spikes) {
            Block block = spike.location.getBlock();
            //if it ain't air skips
            if (block.getType() != Material.AIR) {
                continue;
            }
            block.setBlockData(spike.blockData.clone(), false);
            //Only regens one spike at a time
            return;
        }
    }
    //checks if there any spikes/side crystals missing
    private boolean hasMissingSpike() {
        for (SpikeData spike : spikes) {
            if (spike.location.getBlock().getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }
    //The timer text display logic
    private void createSpikeTimerRegenDisplay() {
        //The timer slightly below the health bar
        Location displayLocation = theBottomSourceBlockLocation.clone().add(0.5, 2.5, 0.5);
        spikeTimerRegenDisplay = theBottomSourceBlockLocation.getWorld().spawn(displayLocation, TextDisplay.class, display -> {
                    display.setSeeThrough(true);
                    display.setBillboard(Display.Billboard.CENTER);
                }
        );
        //starts the timer task
        startSpikeTimerRegenDisplay();
    }
    private void startSpikeTimerRegenDisplay() {
        new BukkitRunnable() {
            @Override
            public void run() {
                //If not in game canceles
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                    return;
                }
                //If null or not valid cancels
                if (spikeTimerRegenDisplay == null || !spikeTimerRegenDisplay.isValid()) {
                    cancel();
                    return;
                }
                //If generator is not active then display inactive text
                if (!isActive()) {
                    spikeTimerRegenDisplay.text(Component.text("Inactive"));
                    return;
                }
                //If the regeneration is not runing displays that it is full
                if (!spikeRegenerationCurrentlyRunning) {
                    spikeTimerRegenDisplay.text(Component.text("Crystals Full"));
                    return;
                }
                //calculates how many ticks remain before the next regeneration
                //math.max so it will never go into negatives.
                int ticksRemaining = Math.max(0, nextSpikeRegenTick - Bukkit.getCurrentTick());
                //converst ticks to seconds, ceil so it rounds up
                int secondsRemaining = (int) Math.ceil(ticksRemaining / 20.0);
                //Displays when the next side crystal will regenerate in seonds
                spikeTimerRegenDisplay.text(Component.text("Next Side Crystal: " + secondsRemaining + "s"));
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1); //updates quite often so it is almost instant
    }
    //Canceling the task so it won't run on revival. And makes sure spike regeneration is set to false, so it can run again later
    private void cancelSpikeRegeneration() {
        if (spikeRegenerationTask != null) {
            spikeRegenerationTask.cancel();
            spikeRegenerationTask = null;
        }
        spikeRegenerationCurrentlyRunning = false;
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



