package cc.crystalized;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

//Basicly it works similiar to regular bed wars generation, in game it is like the crystal generators overflows and creates extra crystals
//Specificly designed to make it so people can come back to the game after death faster, as there will be something at the base already same as in Bed Wars.
//Instead of forcing the player to mine after death from nothing.
public class CrystalOverFlowGeneration {
    //This the boolean which handles if crystal overflow works, so if devs don't want it can just be disalbed and it will not run at all.
    public static final boolean CRYSTAL_OVERFLOW_ALLOWED = true;
    //This is how much maximum a one overflow generator can produce, will not produce more shards of this type than this at a time
    private static final int STALE_OVERFLOW_CAP = 48;
    private static final int PURE_OVERFLOW_CAP = 12;

    //This where the droped shards will be located
    private final Location dropLocation;
    //This is the oveflow generatory type, and it will primary produce that shard
    private final OverflowGeneratorType overflowGeneratorType;

    //PureShardGenerators is only used for PureOveflow generators, stale oveflow will set it to null
    private final PureShardGenerator pureGenerator;
    //This is the task that will generate the oveflowing crystals
    private BukkitTask overflowGeneratorTask;
    /*Not used anymore
        //This is specificly for stale generators, as they start producing pure shards at gen upgrade 3, so this counts the required repets to generate a pure shard
        //it will spawn one pure shard and reset the repeats.
        //private int staleOverflowRepeatsCountForPureShards = 0;
     */
    //This task was added to be able to seperately tweak time of stale oveflow generators producing pure
    private BukkitTask staleOveflowGeneratorPureProductionTask;
    //The constructor which sets the location the selected oveflow generator type and the pure shard generator if is a pure generator
    public CrystalOverFlowGeneration(Location dropLocation, OverflowGeneratorType overflowGeneratorType, PureShardGenerator pureGenerator) {
        this.dropLocation = dropLocation.clone();
        this.overflowGeneratorType = overflowGeneratorType;
        this.pureGenerator = pureGenerator;
        //starts the generation task
        start();
    }
    //The method to start the overflow generation
    private void start() {
        //The ultimate switch which if not true stops the whole overflow generation
        //now can be disabled in config as well
        if (!isCrystalOverflowGeneratorsAllowed()) {
            return;
        }
        //starts with a slight delay to ensure that gamemanger won't be null
        new BukkitRunnable() {
            @Override
            public void run() {
                if (crystalBlitz.getInstance().gamemanager == null) {
                    return;
                }
                //schedules the overflow task
                scheduleNextOverflow();
                //Schedules pure production for stale oveflow generators, so time can be more easliy tweaked
                //now can be disabled in config
                if (overflowGeneratorType == OverflowGeneratorType.STALE && isStaleOverflowAllowedToProducePureOnGenIII()) {
                    scheduleNextPureProductionForStaleOverflowGenerators();
                }
            }
        }.runTaskLater(crystalBlitz.getInstance(), 1);
    }

    //Schedules the overflow task
    private void scheduleNextOverflow() {
        //Makes sure it will never happened if crystal overflow is not allowed
        if (!isCrystalOverflowGeneratorsAllowed()) {
            return;
        }
        if (crystalBlitz.getInstance().gamemanager == null) {
            return;
        }
        //The delay depending on the gen upgrades, and type of the gen
        int delayBeforeNextShardGenerationOveflow = getOverflowGeneratorDelay();
        overflowGeneratorTask = new BukkitRunnable() {
            @Override
            public void run() {
                //makes sure when it run it is set to null
                overflowGeneratorTask = null;
                if (crystalBlitz.getInstance().gamemanager == null) {
                    return;
                }
                //If it is the pure generator and is destroyed it will not oveflow generate anything
                if (pureGenerator != null && !pureGenerator.isActive()) {
                    //schedules the next task anyway as it will become active at some point due to revival.
                    scheduleNextOverflow();
                    return;
                }
                //does the oveflow
                overflowGenerator();
                //schedules the next task
                scheduleNextOverflow();
            }
        }.runTaskLater(crystalBlitz.getInstance(), delayBeforeNextShardGenerationOveflow);
    }

    //Depending on the type of the generator it produces diffrent crystals.
    private void overflowGenerator() {
        switch (overflowGeneratorType) {
            case STALE -> overflowStaleGenerator();
            case PURE -> overflowPureGenerator();
        }
    }
    //This is for stale shards/weak shards overflow generator
    private void overflowStaleGenerator() {
        //This checks the drop cap and if it is smaller than it then drops more
        if (getDroppedSharAmount(Shop.ShardTypes.Weak) < STALE_OVERFLOW_CAP) {
            dropShard(Shop.ShardTypes.Weak);
        }
        //Now has a seperate task to adjust time easier
        /*
            BossBarStates state = crystalBlitz.getInstance().gamemanager.bossbar.currentstate;

            //Starting at GenIII stale can ocasionaly drop pure shards.
            if (state == BossBarStates.GenUpgradeIII || state == BossBarStates.GenUpgradeIV || state == BossBarStates.Overtime) {
                //It needs to repeat enough times so that a pure shard can generate
                staleOverflowRepeatsCountForPureShards = staleOverflowRepeatsCountForPureShards + 1;
                int requiredStaleOverflowsRepeats;
                //on gen 3 requires 5 repeates, on gen 4 3 repeats
                if (state == BossBarStates.GenUpgradeIII) {
                    requiredStaleOverflowsRepeats = 5;
                } else {
                    requiredStaleOverflowsRepeats = 3;
                }
                if (staleOverflowRepeatsCountForPureShards >= requiredStaleOverflowsRepeats) {
                    //resets the number of repeates when it tries to create a pure shard
                    staleOverflowRepeatsCountForPureShards = 0;
                    //The same cap for pure in stale oveflow generators as in pure, could be changed later if it is too op
                    if (getDroppedSharAmount(Shop.ShardTypes.Strong) < PURE_OVERFLOW_CAP) {
                        dropShard(Shop.ShardTypes.Strong);
                    }
                }
            }
         */
    }
    //For pure oveflow generators
    private void overflowPureGenerator() {
        //If meets the cap stops
        if (getDroppedSharAmount(Shop.ShardTypes.Strong) >= PURE_OVERFLOW_CAP) {
            return;
        }
        //drops the pure shard
        dropShard(Shop.ShardTypes.Strong);
    }
    //Drops the shard depending on the type
    private void dropShard(Shop.ShardTypes shardType) {
        //clones
        ItemStack shard = shardType.item.clone();
        //will drop in increments of one
        shard.setAmount(1);
        //Dropes the shard at dropLocation and sets it to droppedShard
        Item droppedShard = dropLocation.getWorld().dropItem(dropLocation, shard);
        //Ensures that drop shard will have no velocity so it shouldn't fly away
        droppedShard.setVelocity(new Vector(0, 0, 0));
    }
    //This is to get the ammount so that the limiters cap work
    private int getDroppedSharAmount(Shop.ShardTypes shardType) {
        int amount = 0;
        //Goes through nearby entites
        for (Entity entity : dropLocation.getWorld().getNearbyEntities(dropLocation, 2, 2, 2)) {
            //if it ain't an item continues
            if (!(entity instanceof Item dropedItem)) {
                continue;
            }
            //gets the item stacks of the droped item
            ItemStack itemStack = dropedItem.getItemStack();
            //If they are the same shard item ads the amount of the stack
            if (itemStack.isSimilar(shardType.item)) {
                amount = amount + itemStack.getAmount();
            }
        }
        return amount;
    }
    //The delay of the oveflow generators, basicly when the next shard will spawn depending on gen upgrade
    private int getOverflowGeneratorDelay() {
        BossBarStates state = crystalBlitz.getInstance().gamemanager.bossbar.currentstate;
        return switch (overflowGeneratorType) {
            //The stale oveflow generation speed, made it not super fast as mining still should be usesful.
            //edit: made slightly faster to be better
            case STALE -> switch (state) {
                case starting -> 20 * 5;
                case GenUpgradeI -> 20 * 4;
                case GenUpgradeII -> 20 * 3;
                case GenUpgradeIII -> 20 * 2;
                case GenUpgradeIV, Overtime -> 20 * 1;
            };
            //The pure overflow generation is much slower than stale.
            case PURE -> switch (state) {
                case starting -> 20 * 25;
                case GenUpgradeI -> 20 * 20;
                case GenUpgradeII -> 20 * 15;
                case GenUpgradeIII -> 20 * 12;
                case GenUpgradeIV, Overtime -> 20 * 10;
            };
        };
    }
    //The logic for pure production on stale shard oveflow generators.
    private void scheduleNextPureProductionForStaleOverflowGenerators() {
        if (crystalBlitz.getInstance().gamemanager == null) {
            return;
        }
        if(!isStaleOverflowAllowedToProducePureOnGenIII()){
            return;
        }
        BossBarStates state = crystalBlitz.getInstance().gamemanager.bossbar.currentstate;

        //When pure from stale oveflow generators is not yet unlocked
        if (state == BossBarStates.starting || state == BossBarStates.GenUpgradeI || state == BossBarStates.GenUpgradeII) {
            //Will check again later with the delay of 1 second, waiting for gen 3 to start producing
            staleOveflowGeneratorPureProductionTask = new BukkitRunnable() {
                @Override
                public void run() {
                    scheduleNextPureProductionForStaleOverflowGenerators();
                }
            }.runTaskLater(crystalBlitz.getInstance(), 20);
            //returns. So that there is no need for further state checks
            return;
        }
        int delayBeforePureProductionInStaleOveflowGeneration = getPureProductionForStaleOverflowGenDelay();
        //This will work only at gen 3, gen 4 and Overtime
        staleOveflowGeneratorPureProductionTask = new BukkitRunnable() {
            @Override
            public void run() {
                staleOveflowGeneratorPureProductionTask = null;
                if (crystalBlitz.getInstance().gamemanager == null) {
                    return;
                }
                //drops the shard if not meet the cap
                if (getDroppedSharAmount(Shop.ShardTypes.Strong) < PURE_OVERFLOW_CAP) {
                    dropShard(Shop.ShardTypes.Strong);
                }
                //schedules the next task
                scheduleNextPureProductionForStaleOverflowGenerators();
            }
        }.runTaskLater(crystalBlitz.getInstance(), delayBeforePureProductionInStaleOveflowGeneration);
    }
    private int getPureProductionForStaleOverflowGenDelay() {
        BossBarStates state = crystalBlitz.getInstance().gamemanager.bossbar.currentstate;
        return switch (state) {
            case GenUpgradeIII -> 20 * 15; //will drop one Pure every 15 seconds on gen 3
            case GenUpgradeIV, Overtime -> 20 * 10; //will drop one pure every 10 second on gen 4 upgrade
            default -> 20 * 20; //keept default same as gen 3
        };
    }


    //Cancels the overflow generator task and sets it to null
    public void cancelOverflowGeneration() {
        if (overflowGeneratorTask != null) {
            overflowGeneratorTask.cancel();
            overflowGeneratorTask = null;
        }
        //Now also cancels the new stale oveflow gnerator pure production task
        if (staleOveflowGeneratorPureProductionTask != null) {
            staleOveflowGeneratorPureProductionTask.cancel();
            staleOveflowGeneratorPureProductionTask = null;
        }
    }
    //Here it checks the config and the hard coded checks. To allow overflow generators.
    private boolean isCrystalOverflowGeneratorsAllowed() {
        return CRYSTAL_OVERFLOW_ALLOWED && crystalBlitz.getInstance().getConfig().getBoolean("crystal-overflow-generator-enabled", true);
    }
    //Checks the config to see if stale overflow is allowed to produce pure shards
    private boolean isStaleOverflowAllowedToProducePureOnGenIII(){
        return crystalBlitz.getInstance().getConfig().getBoolean("stale-generator-oveflow-producing-pure-on-gen-3", true);
    }



}

//The type of the overflow generators. Primary will produce that type
enum OverflowGeneratorType {
    STALE,
    PURE
}

