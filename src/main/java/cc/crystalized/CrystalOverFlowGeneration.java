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
    //This is specificly for stale generators, as they start producing pure shards at gen upgrade 3, so this counts the required repets to generate a pure shard
    //it will spawn one pure shard and reset the repeats.
    private int staleOverflowRepeatsCountForPureShards = 0;
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
        if (!CRYSTAL_OVERFLOW_ALLOWED) {
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
            }
        }.runTaskLater(crystalBlitz.getInstance(), 1);
    }

    //Schedules the overflow task
    private void scheduleNextOverflow() {
        //Makes sure it will never happened if crystal overflow is not allowed
        if (!CRYSTAL_OVERFLOW_ALLOWED) {
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
            case STALE -> switch (state) {
                case starting -> 20 * 8;
                case GenUpgradeI -> 20 * 7;
                case GenUpgradeII -> 20 * 6;
                case GenUpgradeIII -> 20 * 5;
                case GenUpgradeIV, Overtime -> 20 * 4;
            };
            //The pure overflow generation is much slower than stale.
            case PURE -> switch (state) {
                case starting -> 20 * 30;
                case GenUpgradeI -> 20 * 25;
                case GenUpgradeII -> 20 * 20;
                case GenUpgradeIII -> 20 * 15;
                case GenUpgradeIV, Overtime -> 20 * 10;
            };
        };
    }
    //Cancels the overflow generator task and sets it to null
    public void cancelOverflowGeneration() {
        if (overflowGeneratorTask != null) {
            overflowGeneratorTask.cancel();
            overflowGeneratorTask = null;
        }
    }
}

//The type of the overflow generators. Primary will produce that type
enum OverflowGeneratorType {
    STALE,
    PURE
}

