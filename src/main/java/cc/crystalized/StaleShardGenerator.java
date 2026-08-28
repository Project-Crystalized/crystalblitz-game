package cc.crystalized;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.AmethystCluster;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.translatable;

//Made a seperate class which handles StaleShard generators, firstly due to having the stale generator stuff in one place mostly is better
//Mostly the gen upgrades in team upgrades motivated me to do this, to make it easier to upgrade the generators.
public class StaleShardGenerator {

    //Tracks team name, location, tag and the crystal ofveflow generation
    private final String teamName;
    private final Location centerLocation;
    private TextDisplay staleShardTag;
    private CrystalOverFlowGeneration crystalOverflowGeneration;
    //0 level means no stronger sharge gen upgrade
    //1 level means stronger shard upgrade 1
    //2 level means stronger shard upgrade 2
    private int strongerShardUpgradeLevel = 0;

    //This is a boolean which tracks if second layer has been activated
    private boolean secondLayerActivated = false;

    //The array list which tracks sideShardSlots
    private final List<StaleSideShardSlot> sideShardSlots = new ArrayList<>();
    //The task which generates side shards, depending on level upgrades them with time
    private BukkitTask sideShardsGenerationTask;

    //The constructor takes in the team name and location
    public StaleShardGenerator(String teamName, Location centerLocation) {
        this.teamName = teamName;
        this.centerLocation = centerLocation.clone();
        this.centerLocation.setWorld(Bukkit.getWorld("world"));
        //resets generator, star side shard generation and starts stale crystal overflow generation
        resetGeneratorToDefault();
        startSideShardsGenerationTask();
        startStaleCrystalOverflowGeneration();
    }

    //Resets the generator to the default
    public void resetGeneratorToDefault() {
        //The main center blcok set up
        Block centerBlock = centerLocation.getBlock();
        centerBlock.setType(Material.BLACK_GLAZED_TERRACOTTA);
        Directional dir = (Directional) centerBlock.getBlockData();
        dir.setFacing(BlockFace.EAST);
        centerBlock.setBlockData(dir);
        centerBlock.getState().update(true, false);

        //Bottom side shards set up
        setupBottomSideShardSlots();
        //Cleares the second layer, should not be enabled at this stage.
        if (!secondLayerActivated) {
            centerLocation.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
            //cleares the second layer shards
            clearSecondLayerSideShards();
        }
        //Resets the old and creates the new tag
        resetAndCreateStaleTag();
    }
    //Places the stale side shard, based on slot
    private void placeStaleSideShard(StaleSideShardSlot slot) {
        //gets the current block from the location of the slot
        Block block = slot.location.getBlock();
        //removes from player placed blocks if player covered the generator.
        crystalBlitz.getInstance().Blocks.remove(block);
        //set the default block for stale shard, as it allways should start from this stage
        block.setType(Material.DEAD_BRAIN_CORAL_WALL_FAN, false);
        CoralWallFan data = (CoralWallFan) block.getBlockData();
        data.setWaterlogged(false);
        data.setFacing(slot.facing);
        //sets the correct block data
        block.setBlockData(data, false);
        //Loggers while debuging
        /*
        crystalBlitz.getInstance().getLogger().info("Immediately after stale regen: " + block.getType() + " at location: " + block.getLocation());
        Bukkit.getScheduler().runTaskLater(
                crystalBlitz.getInstance(),
                () -> crystalBlitz.getInstance().getLogger().info("One tick after stale regenration: " +
                        slot.location.getBlock().getType() + " at slot's location" + slot.location),
                1L
        );
         */
        //reset the upgrade roll timer based on the slot, as it just regrew so it should start upgrading from zero
        resetUpgradeRollTimersBasedOnStrongerShardUpgradeLevel(slot);
    }
    //ads side shard slot, based on location and block facing then places it
    private void addSideShardSlot(Location location, BlockFace facing) {
        StaleSideShardSlot slot = new StaleSideShardSlot(location, facing);
        sideShardSlots.add(slot);
        placeStaleSideShard(slot);
    }
    //The set up for bottom side shards
    private void setupBottomSideShardSlots() {
        //ensures that side shjard slots are cleare
        sideShardSlots.clear();
        //adds side shars slots
        addSideShardSlot(centerLocation.clone().add(0, 0, 1), BlockFace.SOUTH);
        addSideShardSlot(centerLocation.clone().add(0, 0, -1), BlockFace.NORTH);
        addSideShardSlot(centerLocation.clone().add(1, 0, 0), BlockFace.EAST);
        addSideShardSlot(centerLocation.clone().add(-1, 0, 0), BlockFace.WEST);
    }
    //cleares the second layers side shards
    private void clearSecondLayerSideShards() {
        centerLocation.clone().add(0, 1, 1).getBlock().setType(Material.AIR);
        centerLocation.clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        centerLocation.clone().add(1, 1, 0).getBlock().setType(Material.AIR);
        centerLocation.clone().add(-1, 1, 0).getBlock().setType(Material.AIR);
    }

    //resets and creates the stale tag incase it still exists on game resteart
    private void resetAndCreateStaleTag() {
        //checks if it still exists and if so removes it
        if (staleShardTag != null && staleShardTag.isValid()) {
            staleShardTag.remove();
        }
        //gets the location slightly higher than the blcok
        Location tagLocation = centerLocation.clone().add(0.5, 1.5, 0.5);

        //creates the stale shard tag
        staleShardTag = centerLocation.getWorld().spawn(tagLocation, TextDisplay.class, display -> {
                    display.setSeeThrough(true);
                    display.setBillboard(Display.Billboard.CENTER);
                    display.text(translatable("crystalized.game.crystalblitz.weaknode.mine").color(NamedTextColor.DARK_RED));
        });

    }
    //The second layer upgrade activation
    public void activateSecondLayer() {
        //ensures it ain't possible to activate it twice
        if (secondLayerActivated) {
            return;
        }
        //creates the second block
        secondLayerActivated = true;
        Location topCenter = centerLocation.clone().add(0, 1, 0);
        Block centerBlock = topCenter.getBlock();
        centerBlock.setType(Material.BLACK_GLAZED_TERRACOTTA);
        Directional dir = (Directional) centerBlock.getBlockData();
        dir.setFacing(BlockFace.EAST);
        centerBlock.setBlockData(dir);
        centerBlock.getState().update(true, false);
        //The side shard for the second layer
        addSideShardSlot(topCenter.clone().add(0, 0, 1), BlockFace.SOUTH);
        addSideShardSlot(topCenter.clone().add(0, 0, -1), BlockFace.NORTH);
        addSideShardSlot(topCenter.clone().add(1, 0, 0), BlockFace.EAST);
        addSideShardSlot(topCenter.clone().add(-1, 0, 0), BlockFace.WEST);
        //Moves the name tag up
        if (staleShardTag != null && staleShardTag.isValid()) {
            staleShardTag.teleport(staleShardTag.getLocation().clone().add(0, 1, 0));
        }
    }
    //The oveflow stale generation
    private void startStaleCrystalOverflowGeneration() {
        Location overflowLocation = centerLocation.clone().add(0.5, 2.2, 0.5);
        crystalOverflowGeneration = new CrystalOverFlowGeneration(overflowLocation, OverflowGeneratorType.STALE, null);
    }

    //This sets the stale upgrade level
    public void setStrongerShardUpgradeLevel(int level) {
        strongerShardUpgradeLevel = level;
        //ensures that all side shards immiditely start following the new upgrade level
        for (StaleSideShardSlot slot : sideShardSlots) {
            resetUpgradeRollTimersBasedOnStrongerShardUpgradeLevel(slot);
        }
    }
    //The getter method for stronger shard upgrade level
    public int getStrongerShardUpgradeLevel() {
        return strongerShardUpgradeLevel;
    }
    //The getter for team name
    public String getTeamName() {
        return teamName;
    }
    //The task which generates side shards, depending on upgrades
    private void startSideShardsGenerationTask() {
        sideShardsGenerationTask = new BukkitRunnable() {
            @Override
            public void run() {
                //stops is game manager is null
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                    sideShardsGenerationTask = null;
                    return;
                }
                //gets the current tick
                int currentTick = Bukkit.getCurrentTick();
                //goes through all the side shard slots
                for (StaleSideShardSlot slot : sideShardSlots) {
                    //gets the current material
                    Material type = slot.location.getBlock().getType();

                    //Checks if it is one of the possible shards
                    boolean validShard = type == Material.DEAD_BRAIN_CORAL_WALL_FAN || type == Material.SMALL_AMETHYST_BUD || type == Material.LARGE_AMETHYST_BUD;
                    //This was added so that if the block is gone without going through break side shard or another block was put into this possition it starts regen
                    //anywasy
                    if (!validShard && slot.regenrateAtTick == -1) {
                        //sets the tick at which it should regenate
                        slot.regenrateAtTick = currentTick + getStaleShardRegenerationTimeInTicks();
                        //sets the small bud and large buds to -1 as shouldn't regen to become them so far
                        slot.nextSmallBudRollTick = -1;
                        slot.nextLargeBudRollTick = -1;
                    }
                    //Does the regenration
                    doTheRegeneration(slot, currentTick);
                    //Checks ones again incase regeneration restored it.
                    type = slot.location.getBlock().getType();
                    validShard = type == Material.DEAD_BRAIN_CORAL_WALL_FAN || type == Material.SMALL_AMETHYST_BUD || type == Material.LARGE_AMETHYST_BUD;
                    if (!validShard) {
                        continue;
                    }
                    //Handles the side shard upgrade growth.
                    handleSideShardUpgradeGrowth(slot, currentTick);
                }
            } //runs every second
        }.runTaskTimer(crystalBlitz.getInstance(), 20, 20);
    }
    //This handles the breaking of the side shard
    public void breakSideShard(Location location) {
        StaleSideShardSlot slot = getSideShardSlot(location);
        if (slot == null) {
            //crystalBlitz.getInstance().getLogger().warning("Stale side slot not found: " + location);
            return;
        }
        //sets to air inside the method
        slot.location.getBlock().setType(Material.AIR);
        //gets the regeneration time
        int regenerationTime = getStaleShardRegenerationTimeInTicks();
        //set the tick at which slot will regenerate
        slot.regenrateAtTick = Bukkit.getCurrentTick() + regenerationTime;

        //Resets the upgrade proggess as it has been broken
        slot.nextSmallBudRollTick = -1;
        slot.nextLargeBudRollTick = -1;
    }
    //gets the side shard slot based on location
    private StaleSideShardSlot getSideShardSlot(Location location) {
        for (StaleSideShardSlot slot : sideShardSlots) {
            if (slot.location.getBlock().equals(location.getBlock())) {
                return slot;
            }
        }
        return null;
    }
    //Checks if it own the side shard
    public boolean ownsSideShard(Location location) {
        return getSideShardSlot(location) != null;
    }
    //This method regenerates the slot
    private void doTheRegeneration(StaleSideShardSlot slot, int currentTick) {
        //if fully regened nothing happens
        if (slot.regenrateAtTick == -1) {
            return;
        }
        //means already regened
        if (currentTick < slot.regenrateAtTick) {
            return;
        }
        //crystalBlitz.getInstance().getLogger().info("Regenerated stale shard at location: " + slot.location + " replacing old " + slot.location.getBlock().getType());

        //Will regrow as the default type allways
        placeStaleSideShard(slot);
        //resets the regenration tick, as it finished generation
        slot.regenrateAtTick = -1;
    }
    //As it no longer uses the previous class for generation now calculates time here
    private int getStaleShardRegenerationTimeInTicks() {
        BossBarStates state = crystalBlitz.getInstance().gamemanager.bossbar.currentstate;
        //Based on gen upgareds/speed which change with time as game proggress, no the team upgrades.
        //TODO: Maybe no need for it to be random and make it more robost, but for now left as it was
        int seconds = switch (state) {
            case starting -> crystalBlitz.getInstance().getRandomNumber(3, 9);
            case GenUpgradeI -> crystalBlitz.getInstance().getRandomNumber(2, 7);
            case GenUpgradeII -> crystalBlitz.getInstance().getRandomNumber(2, 6);
            case GenUpgradeIII -> crystalBlitz.getInstance().getRandomNumber(1, 5);
            case GenUpgradeIV, Overtime -> crystalBlitz.getInstance().getRandomNumber(1, 4);
        };
        //converts seconds to ticks
        return seconds * 20;
    }
    //This to change the upgrade roll timers based on upgrade level
    private void resetUpgradeRollTimersBasedOnStrongerShardUpgradeLevel(StaleSideShardSlot slot) {
        int currentTick = Bukkit.getCurrentTick();
        switch (strongerShardUpgradeLevel) {
            //on level 0 no upgrades hence - 1
            case 0 -> {
                slot.nextSmallBudRollTick = -1;
                slot.nextLargeBudRollTick = -1;
            }
            //om level 1, upgrade only to small bud every 10 seconds try to
            case 1 -> {
                slot.nextSmallBudRollTick = currentTick + (20 * 10);
                slot.nextLargeBudRollTick = -1;
            }
            //On level 2 tries to upgard to small bud every 7 seconds, and to large one every 14 seconds after have been the small one
            case 2 -> {
                slot.nextSmallBudRollTick = currentTick + (20 * 7);
                //changed only when the small bud has growen
                slot.nextLargeBudRollTick = -1;
            }
        }
    }
    //This method handles the side shard upgrade growth
    private void handleSideShardUpgradeGrowth(StaleSideShardSlot slot, int currentTick) {
        //gets the block
        Block block = slot.location.getBlock();
        //if level is 0 then nothing happens
        if (strongerShardUpgradeLevel == 0) {
            return;
        }

        //Regular stale crystals to small bud
        if (block.getType() == Material.DEAD_BRAIN_CORAL_WALL_FAN) {
            int checkingInterval;
            double chance;
            //If strong shard upgrade level 1 then inteval is 10 seconds with the cahcne of 20%
            //If level 2 then inteval is 7 seconds at which it checks and chance is 40%
            if (strongerShardUpgradeLevel == 1) {
                checkingInterval = 20 * 10;
                chance = 0.20;
            } else {
                checkingInterval = 20 * 7;
                chance = 0.40;
            }
            //If it is currently not happening
            if (slot.nextSmallBudRollTick == -1) {
                //gets the current tick + interval at which it is checking
                slot.nextSmallBudRollTick = currentTick + checkingInterval;
                return;
            }
            //when current tick meets the next small bud roll tick
            if (currentTick >= slot.nextSmallBudRollTick) {
                //when the roll is smaller than chance growths sthe small bud
                if (Math.random() < chance) {
                    growSmallBud(slot);
                    return;
                }
                //when the roll was failed, tries again after another interval
                slot.nextSmallBudRollTick = currentTick + checkingInterval;
            }

            return;
        }

        //This for the level 2 upgrade, and the small amethist bud to grow into large
        if (block.getType() == Material.SMALL_AMETHYST_BUD && strongerShardUpgradeLevel >= 2) {
            //when is is -1, sets the next roll in 14 seconds
            if (slot.nextLargeBudRollTick == -1) {
                slot.nextLargeBudRollTick = currentTick + (20 * 14);
                return;
            }
            //when current tick reaches the tick at which it rolls if rolls less than 0.10 then grows the large bud
            if (currentTick >= slot.nextLargeBudRollTick) {
                if (Math.random() < 0.10) {
                    growLargeBud(slot);
                    return;
                }
                //When failed the 10% roll tries again in another 14 seconds.
                slot.nextLargeBudRollTick = currentTick + (20 * 14);
            }
        }

        //Large bud doesn't need to be handled as it is the final growth stage.
    }
    //This growths sthe small bud
    private void growSmallBud(StaleSideShardSlot slot) {
        //sets the block
        Block block = slot.location.getBlock();
        block.setType(Material.SMALL_AMETHYST_BUD, false);
        AmethystCluster data = (AmethystCluster) block.getBlockData();
        data.setFacing(slot.facing);
        data.setWaterlogged(false);
        block.setBlockData(data, false);

        //As it became the small bud no more rolls for it until it is broke and has to regrow so -1
        slot.nextSmallBudRollTick = -1;
        //if the level is 2 then sets the roll to become a large bud.
        if (strongerShardUpgradeLevel >= 2) {
            slot.nextLargeBudRollTick = Bukkit.getCurrentTick() + (20 * 14);
        }
    }
    //This method is to grow the large bud at the slot.
    private void growLargeBud(StaleSideShardSlot slot) {
        //Block set up
        Block block = slot.location.getBlock();
        block.setType(Material.LARGE_AMETHYST_BUD, false);
        AmethystCluster data = (AmethystCluster) block.getBlockData();
        data.setFacing(slot.facing);
        data.setWaterlogged(false);
        block.setBlockData(data, false);
        //Stops the growth as the maximum growth reached
        slot.nextLargeBudRollTick = -1;
    }
    //Cancels the side shard generation task and overflow generation task
    public void canelStaleOverflowGenerations() {
        if (sideShardsGenerationTask != null) {
            sideShardsGenerationTask.cancel();
            sideShardsGenerationTask = null;
        }
        if (crystalOverflowGeneration != null) {
            crystalOverflowGeneration.cancelOverflowGeneration();
        }
    }

    //This tracks the slot of stale side shard generator.
    //slot is slightly diffrent from a shard itself, it is like inventory space belonging to generator which shard can occupy.
    //It can also be occupied by air or player placed blocks, regardless this slot belongs to the generator and shard will allways replaced whatever may occupie the slot
    private static class StaleSideShardSlot {
        //Needs location and block facing in constructor
        private final Location location;
        private final BlockFace facing;
        //This calculates at what tick it needs to be to regenrate, if minues -1 means it doesn't need to regen
        private int regenrateAtTick = -1;
        //Same applies for small and large buds, they start being relevent on upgrade
        private int nextSmallBudRollTick = -1;
        private int nextLargeBudRollTick = -1;
        //Takes in location and block facing
        private StaleSideShardSlot(Location location, BlockFace facing) {
            this.location = location.clone();
            this.facing = facing;
        }
    }
}

