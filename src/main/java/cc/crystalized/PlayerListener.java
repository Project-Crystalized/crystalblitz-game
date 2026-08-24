package cc.crystalized;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import gg.crystalized.lobby.Ranks;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class PlayerListener implements Listener {

    //This is the extra time for pure shard side crystals generators
    //As before they generated too fast
    private static final int EXTRA_TIME_FOR_PURE_SHARDS = 5;
    //This maps for tracking the last attacker and time for void kills
    //To give the shards to the killer even after falling
    private final Map<UUID, UUID> lastAttacker = new HashMap<>();
    //This keeps track of the last tick at which the player was attacked, so no longer than 10 seconds kill won't count
    private final Map<UUID, Integer> lastAttackTick = new HashMap<>();
    //The time that the kill credit will last after hit if the player fell off (10 seconds)
    private static final int KILL_CREDIT_TIME = 20 * 10;

    //Prevening amethyst shards from spawning ever as mite requsted
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        //So that it only happens during the game
        if (crystalBlitz.getInstance().gamemanager == null) {
            return;
        }
        //So that amethist shards will never spawn
        if (e.getEntity().getItemStack().getType() == Material.AMETHYST_SHARD) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        FloodgateApi floodgateapi = FloodgateApi.getInstance();
        e.joinMessage(text(""));
        p.setHealth(20);
        p.setFoodLevel(20);
        p.getInventory().clear();
        p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, PotionEffect.INFINITE_DURATION, 0, false, false, true));
        p.removePotionEffect(PotionEffectType.ABSORPTION);

        if (crystalBlitz.getInstance().gamemanager == null) {
            p.teleport(crystalBlitz.getInstance().mapdata.get_queue_spawn(Bukkit.getWorld("world")));
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            p.setGameMode(GameMode.ADVENTURE);
            //TODO make this look better
            p.sendPlayerListHeaderAndFooter(
                    //Header
                    text("\nCrystalized: Crystal Blitz\n"),

                    //Footer
                    text("\n")
                            .append(text("Crystal Blitz Version: " + crystalBlitz.getInstance().getDescription().getVersion()))
                            .append(text("\n"))
            );
            new QueueScoreboard(p);

            if (floodgateapi.isFloodgatePlayer(p.getUniqueId())) {
                p.sendMessage(text("-".repeat(40)));
            } else {
                p.sendMessage(text(" ".repeat(55)).decoration(TextDecoration.STRIKETHROUGH,  true));
            }
            p.sendMessage(
                    text("\n")
                            .append(translatable("crystalized.game.crystalblitz.name").color(NamedTextColor.LIGHT_PURPLE).append(text(" \uE12F").color(NamedTextColor.WHITE)))
                            .append(text("\n").append(translatable("crystalized.game.crystalblitz.chat.tutorial").color(NamedTextColor.GRAY)))
                            .append(text("\n"))
            );
            if (floodgateapi.isFloodgatePlayer(p.getUniqueId())) {
                p.sendMessage(text("-".repeat(40)));
            } else {
                p.sendMessage(text(" ".repeat(55)).decoration(TextDecoration.STRIKETHROUGH,  true));
            }

        } else {
            //p.kick(text("A game is currently is progress, try joining again later.").color(NamedTextColor.RED));
            Location loc = new Location(
                    Bukkit.getWorld("world"),
                    crystalBlitz.getInstance().mapdata.spectator_spawn[0],
                    crystalBlitz.getInstance().mapdata.spectator_spawn[1],
                    crystalBlitz.getInstance().mapdata.spectator_spawn[2]
            );
            p.teleport(loc);
            p.setGameMode(GameMode.SPECTATOR);
            Teams teams = crystalBlitz.getInstance().gamemanager.teams;
            if (!teams.spectator.contains(p.getName())) {
                teams.spectator.add(p.getName());
            }
            p.sendMessage(text("[!] You joined a game that was already in progress, You've been put in Spectator."));
            p.setWorldBorder(crystalBlitz.getInstance().gamemanager.worldborder.border);
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player1.unlistPlayer(player2);
                }
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    public void DowngradeItem (PlayerInventory inv, ItemStack originitem, ItemStack replacementitem) {
        if (inv.contains(originitem)) {
            inv.removeItem(originitem);
            inv.addItem(replacementitem);
        }
    }
    //Added this to prevent infinity fall before game starts - Mish
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        //Ensures this happens only when game manager is null, so not during the game
        if (crystalBlitz.getInstance().gamemanager != null) {
            return;
        }
        //Gets the player
        Player p = e.getPlayer();
        //If player's Y location is beyhond maps death limit
        //Teleports the player back to the original spawn location
        if (p.getY() < crystalBlitz.getInstance().mapdata.DeathLimit) {
            p.teleport(crystalBlitz.getInstance().mapdata.get_queue_spawn(Bukkit.getWorld("world")));
            //makes sure fall distanse is 0
            p.setFallDistance(0);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setCancelled(true);
        if (crystalBlitz.getInstance().gamemanager == null) {return;}
        Player p = e.getPlayer();

        Entity entity = e.getDamageSource().getCausingEntity();
        Player k;
        if (entity instanceof Player) {
            k = (Player) entity;
        } else {
            k = null;
        }
        //If there is no direct killer cheks if another player recently attacked them
        if (k == null) {
            //geting the unique id of the victim and through it getting the last attacker and the last tick at which victim was attacked
            UUID victimUUID = p.getUniqueId();
            UUID attackerUUID = lastAttacker.get(victimUUID);
            Integer attackTick = lastAttackTick.get(victimUUID);

            //if attacker uid is not null and the attack tick is not null, and most importantly the tick didn't exit the kill credit time frame
            //sets the killer to the lastAttacker
            if (attackerUUID != null && attackTick != null && Bukkit.getCurrentTick() - attackTick <= KILL_CREDIT_TIME) {
                //gets the player through id
                k = Bukkit.getPlayer(attackerUUID);
            }
        }
        Component killer;

        Location loc = new Location(
                Bukkit.getWorld("world"),
                crystalBlitz.getInstance().mapdata.spectator_spawn[0],
                crystalBlitz.getInstance().mapdata.spectator_spawn[1],
                crystalBlitz.getInstance().mapdata.spectator_spawn[2]
        );
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(loc);

        //The shards lost will be regardless even if killer is null, takes in the killer to give shard too if not null
        handleShardLoss(p, k);
        //Clears out the the hash maps so no extra potential kill credits, after respawn
        lastAttacker.remove(p.getUniqueId());
        lastAttackTick.remove(p.getUniqueId());
        if (k != null) {
            PlayerData kpd = crystalBlitz.getInstance().gamemanager.getPlayerData(k);
            kpd.kills++;
            //killer = k.displayName();
            killer = kpd.cachedRankIcon_small.append(text(" ")).append(k.displayName());
        } else {
            killer = text("");
        }
        PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(p);
        pd.deaths++;

        PlayerInventory inv = p.getInventory();
        inv.setItemInOffHand(new ItemStack(Material.AIR));

        //downgrade player's items
        for (ItemStack i : inv) {
            if (i != null) {
                CBItem cbItem = CrystalBlitzItems.getCBItem(i);
                if (cbItem != null) {
                    inv.removeItem(i);
                    if (!cbItem.downgradeTo.equals("")) {
                        CBItem newItem = CrystalBlitzItems.getCBItem(cbItem.downgradeTo);
                        if (newItem instanceof CBItem_Armor armor) {
                            armor.add(p);
                        } else if (newItem != null) {
                            inv.addItem(newItem.item);
                        }
                    }
                }
            }
        }

        //Death Message to server
        Component deathprefix = text("[\uE103] ");
        Component deathcauseicon = text(" [\uE103] "); //placeholder

        ItemStack KillerMainHandItem;
        ItemStack KillerOffHandItem;
        if (k == null) {
            KillerMainHandItem = new ItemStack(Material.AIR);
            KillerOffHandItem = new ItemStack(Material.AIR);
        } else {
            KillerMainHandItem = k.getInventory().getItemInMainHand();
            KillerOffHandItem = k.getInventory().getItemInOffHand();
        }

        if (e.getDamageSource().getDamageType().equals(DamageType.ARROW)) {
            if (KillerMainHandItem.getType().toString().toLowerCase().contains("crossbow") || KillerOffHandItem.getType().toString().toLowerCase().contains("crossbow")) {
                deathcauseicon = text(" \uE11E ");
            } else if (KillerMainHandItem.getType().toString().toLowerCase().contains("bow") || KillerOffHandItem.getType().toString().toLowerCase().contains("bow")) {
                deathcauseicon = text(" \uE102 ");
            }
        } else if (e.getDamageSource().getDamageType().equals(DamageType.PLAYER_ATTACK)) {
            if (KillerMainHandItem.getType().toString().toLowerCase().contains("sword")) {
                deathcauseicon = text(" \uE101 ");
            } else if (KillerMainHandItem.getType().toString().toLowerCase().contains("axe")) {
                deathcauseicon = text(" \uE11F ");
            } else {
                deathcauseicon = text(" [").append(KillerMainHandItem.effectiveName()).append(text("] "));
            }
        } else if (e.getDamageSource().getDamageType().equals(DamageType.OUTSIDE_BORDER)) {
            deathcauseicon = text(" [").append(translatable("World Border").append(text("] ")));
        } else if (e.getDamageSource().getDamageType().equals(DamageType.OUT_OF_WORLD)) {
            deathcauseicon = text(" [").append(translatable("Void").append(text("] ")));
        } else if (e.getDamageSource().getDamageType().equals(DamageType.HOT_FLOOR)) {
            deathcauseicon = text(" [").append(translatable("block.minecraft.magma_block").append(text("] ")));
        } else if (e.getDamageSource().getDamageType().equals(DamageType.FALL)) {
            deathcauseicon = text(" [Fall Damage] ");
        } else {
            deathcauseicon  = text(" [Unknown Death Reason] ");
        }

        Bukkit.getServer().sendMessage(deathprefix.append(killer).append(deathcauseicon).append(pd.cachedRankIcon_small.append(text(" ").append(p.displayName()))));

        if (Teams.getTeamData(p).nexus.health != 0) {
            new BukkitRunnable() {
                int timer = 5;
                public void run() {
                    if (crystalBlitz.getInstance().gamemanager == null) {cancel();}
                    p.sendActionBar(translatable("crystalized.game.knockoff.respawn1").append(text(timer)).append(translatable("crystalized.game.knockoff.respawn2")));
                    if (timer == 0) {
                        Location spawnloc = new Location(Bukkit.getWorld("world"),
                                crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[0],
                                crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[1],
                                crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[2]
                        );
                        p.setGameMode(GameMode.SURVIVAL);
                        p.teleport(spawnloc);
                        new CustomPlayerNametags(p);
                        cancel();
                    }
                    timer--;
                }
            }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
        } else {
            p.sendMessage(text("[!] You're eliminated from the game!"));
            p.getInventory().clear();
            pd.isEliminated = true;
            Location spawnloc = new Location(Bukkit.getWorld("world"),
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[0],
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[1],
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[2]
            );
            pd.dropEnderChestContents(spawnloc.add(0, 1, 0));
        }
    }

    @EventHandler
    public void onPlayerDamagebyEntity(EntityDamageByEntityEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
            return;
        }
        //Fixed the check so now the code should execute making the team damage prevention logic work
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player victim = (Player) e.getEntity();
        //Tracking the attacker as it is diffrent for mele and projectiles
        Player attacker = null;
        //mele
        if(e.getDamager() instanceof Player player){
            attacker = player;
        }
        //bows etc
        else if (e.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
            attacker = player;
        }

        if (attacker == null) {
            return;
        }
        //preventing team damage
        if (Teams.getPlayerTeam(victim).equals(Teams.getPlayerTeam(attacker))) {
            e.setCancelled(true);
            return;
        }
        //if player somehow damages themself preventing that
        if (victim.getUniqueId().equals(attacker.getUniqueId())) {
            return;
        }
        //This is to remember who last attacked the player
        //It is used to give credit when throughn into the void and give the killed shards
        UUID victimUUID = victim.getUniqueId();
        UUID attackerUUID = attacker.getUniqueId();
        lastAttacker.put(victimUUID, attackerUUID);
        //Gets the current tick to later see if the diffrence is below the kill credit time
        lastAttackTick.put(victimUUID, Bukkit.getCurrentTick());
    }

    @EventHandler
    public void onPlayerDamage (EntityDamageEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShopInteract(PlayerInteractEntityEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        } else {
            if (e.getRightClicked() instanceof Villager) {
                if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                    return;
                }
                new Shop(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        } else {
            if (e.getBlock().getY() == crystalBlitz.getInstance().mapdata.BuildLimit) {
                e.setCancelled(true);
            } else {
                crystalBlitz.getInstance().Blocks.add(e.getBlock());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        } else {
            if (crystalBlitz.getInstance().Blocks.contains(b)) {
                crystalBlitz.getInstance().Blocks.remove(b);
            } else {
                //this and the hit nexus method is a bit messy
                ItemStack holding = p.getInventory().getItemInMainHand();

                switch (b.getType()) {
                    case OAK_SLAB -> {
                        Slab data = (Slab) b.getBlockData();
                        if (data.getType().equals(Slab.Type.DOUBLE) && data.isWaterlogged()) {
                            //TODO rainbow nexus shard block
                        }
                    }
                    case WHITE_GLAZED_TERRACOTTA, GRAY_GLAZED_TERRACOTTA, LIGHT_GRAY_GLAZED_TERRACOTTA -> {
                        if (!p.getInventory().getItemInMainHand().toString().toLowerCase().contains("pickaxe")) {
                            p.sendMessage(text("[!] You need to use your Pickaxe to break this."));
                            return;
                        }
                        e.setCancelled(true);
                        Directional dir = (Directional) e.getBlock().getBlockData();
                        switch (e.getBlock().getType()) {
                            case Material.WHITE_GLAZED_TERRACOTTA -> {
                                switch (dir.getFacing()) {
                                    case BlockFace.EAST:
                                        Teams.getTeamData("blue").nexus.hitNexus(p.getInventory().getItemInMainHand(), p);
                                        break;
                                    case BlockFace.NORTH:
                                        Teams.getTeamData("cyan").nexus.hitNexus(p.getInventory().getItemInMainHand(), p);
                                        break;
                                    case BlockFace.SOUTH:
                                        Teams.getTeamData("green").nexus.hitNexus(p.getInventory().getItemInMainHand(), p);
                                        break;
                                }
                            }
                            case Material.GRAY_GLAZED_TERRACOTTA -> {
                                switch (dir.getFacing()) {
                                    case BlockFace.NORTH:
                                        Teams.getTeamData("red").nexus.hitNexus(p.getInventory().getItemInMainHand(), p);
                                        break;
                                    case BlockFace.SOUTH:
                                        Teams.getTeamData("white").nexus.hitNexus(p.getInventory().getItemInMainHand(), p);
                                        break;
                                    case BlockFace.WEST:
                                        Teams.getTeamData("yellow").nexus.hitNexus(p.getInventory().getItemInMainHand(), p);
                                        break;
                                }
                            }
                            case Material.LIGHT_GRAY_GLAZED_TERRACOTTA -> {
                                switch (dir.getFacing()) {
                                    case BlockFace.EAST:
                                        Teams.getTeamData("lime").nexus.hitNexus(p.getInventory().getItemInMainHand(), p);
                                        break;
                                    case BlockFace.NORTH:
                                        Teams.getTeamData("magenta").nexus.hitNexus(p.getInventory().getItemInMainHand(), p);
                                        break;
                                }
                            }
                        }
                    }
                    case BLACK_GLAZED_TERRACOTTA -> {
                        e.setCancelled(true);
                        Directional dir = (Directional) e.getBlock().getBlockData();
                        switch (dir.getFacing()) {
                            case BlockFace.EAST:
                                ItemStack weak = Shop.ShardTypes.Weak.item.clone();
                                switch (holding.getType()) {
                                    case Material.DIAMOND_PICKAXE -> {weak.setAmount(4);}
                                    case Material.IRON_PICKAXE -> {weak.setAmount(3);}
                                    case Material.STONE_PICKAXE -> {weak.setAmount(2);}
                                    default -> {weak.setAmount(1);}
                                }
                                p.getInventory().addItem(weak);
                                break;
                            case BlockFace.NORTH:
                                //Gets the pure shard generator based on the blocks location
                                PureShardGenerator pureShardGenerator = crystalBlitz.getInstance().gamemanager.getPureShardGenerator(b.getLocation());
                                //If null or non acctive nothing happens
                                if(pureShardGenerator == null || !pureShardGenerator.isActive()){
                                    return;
                                }
                                ItemStack strong = Shop.ShardTypes.Strong.item.clone();
                                //will take diffrent damage depending on a pick
                                int damage = 0;
                                switch (holding.getType()) {
                                    case Material.DIAMOND_PICKAXE -> {
                                        strong.setAmount(4);
                                        damage = 4;
                                    }
                                    case Material.IRON_PICKAXE -> {
                                        strong.setAmount(3);
                                        damage = 3;
                                    }
                                    case Material.STONE_PICKAXE -> {
                                        strong.setAmount(2);
                                        damage = 2;
                                    }
                                    default -> {
                                        strong.setAmount(1);
                                        damage = 1;
                                    }
                                }
                                //If didn't cause any damage breaks out before giving pure shards
                                if (damage == 0) {
                                    break;
                                }
                                p.getInventory().addItem(strong);
                                //Deals the damage to the generator
                                pureShardGenerator.damage(damage);
                                break;
                            default:
                                p.sendMessage(text("Broken black terracotta but this isn't weak or strong shards, please report this."));
                                break;

                        }
                        p.playSound(p, "minecraft:block.note_block.bell", 50, 2);
                    }
                    case DEAD_BRAIN_CORAL_FAN, DEAD_BRAIN_CORAL_WALL_FAN, AMETHYST_CLUSTER, LARGE_AMETHYST_BUD -> {
                        //The extra time that will be added for pure shards
                        int extraTime = 0;
                        //this is dumb, but decide what shard we're giving to the player
                        switch (b.getType()) {
                            case DEAD_BRAIN_CORAL_WALL_FAN -> {
                                ItemStack weak = Shop.ShardTypes.Weak.item.clone();
                                weak.setAmount(2);
                                p.getInventory().addItem(weak);
                                extraTime = 0;
                            }
                            case LARGE_AMETHYST_BUD -> {
                                ItemStack strong = Shop.ShardTypes.Strong.item.clone();
                                strong.setAmount(1);
                                p.getInventory().addItem(strong);
                                extraTime = EXTRA_TIME_FOR_PURE_SHARDS;

                            }
                            case AMETHYST_CLUSTER -> {
                                ItemStack strong = Shop.ShardTypes.Strong.item.clone();
                                strong.setAmount(2);
                                p.getInventory().addItem(strong);
                            }
                        }
                        p.playSound(p, "minecraft:block.note_block.bell", 50, 2);
                        e.setCancelled(true);
                        b.setType(Material.AIR);
                        //Detects if it is a pure shard generator
                        PureShardGenerator generator = crystalBlitz.getInstance().gamemanager.getPureShardGeneratorFromSpike(b.getLocation());
                        if (generator != null) {
                            //if it is starts the spike regeneration, where it slowly regens one at a time
                            generator.startSpikeRegeneration();
                        }
                        //if it is the weak shard generator does the old generation
                        else {
                            new CrystalShardBlock(p, b.getType(), b.getLocation(), b.getBlockData(), extraTime);
                        }
                    }
                    default -> {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player p = event.getPlayer();
        event.setCancelled(true);
        //this is dumb
        if (crystalBlitz.getInstance().gamemanager == null) {
            Bukkit.getServer().sendMessage(Ranks.getName(Bukkit.getOfflinePlayer(p.getName()))
                    .append(Component.text(": "))
                    .append(event.message()));
        } else {
            PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(p);
            Bukkit.getServer().sendMessage(pd.cachedRankIcon_small
                    .append(text(" "))
                    .append(p.displayName())
                    .append(Component.text(": "))
                    .append(event.message()));
        }
    }

    @EventHandler
    public void OnPlayerDisconnect(PlayerConnectionCloseEvent event) {
        if (crystalBlitz.getInstance().gamemanager != null) {
            Teams.DisconnectPlayer(event.getPlayerName());
        }
        if (crystalBlitz.getInstance().gamemanager != null && Bukkit.getOnlinePlayers().equals(0)) {
            Bukkit.getLogger().log(Level.INFO, "All players have disconnected. The Game will now end.");
            crystalBlitz.getInstance().gamemanager.ForceEndGame();
        }
    }

    @EventHandler
    public void OnInventoryMove(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) {return;}
        if (item.getType().toString().toLowerCase().contains("helmet") || item.getType().toString().toLowerCase().contains("chestplate") || item.getType().toString().toLowerCase().contains("leggings") || item.getType().toString().toLowerCase().contains("boots")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onWaterFlow(FluidLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockUpdate(BlockFromToEvent e) {
        e.setCancelled(true);
    }
    //This method is for handeling shard loss and giving them to the killer
    private void handleShardLoss(Player victim, Player killer) {
        PlayerInventory victimInv = victim.getInventory();

        //Goes through the victims inventory
        for (int slot = 0; slot < victimInv.getStorageContents().length; slot++) {
            ItemStack item = victimInv.getItem(slot);
            //If it is not one of the shards continues
            if (!isShard(item)) {
                continue;
            }
            //clones the shards
            ItemStack lostShards = item.clone();

            //Removes shards from the victim allways
            victimInv.setItem(slot, null);

            //If there is a killer than the killer recives shards
            if (killer != null) {
                //adding the lostShards to the killer inviters and storing left over in the hash map
                Map<Integer, ItemStack> leftovers = killer.getInventory().addItem(lostShards);
                //If killers inventory is full than drops the left over shards next to the killer
                for (ItemStack leftover : leftovers.values()) {
                    killer.getWorld().dropItemNaturally(killer.getLocation(), leftover);
                }
            }
        }
    }

    private boolean isShard(ItemStack item) {
        //If it is null or air returns false
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        //using isSimilar as amount doesn't matter
        //returns true if it is a shard of any type
        return item.isSimilar(Shop.ShardTypes.Weak.item) || item.isSimilar(Shop.ShardTypes.Strong.item) || item.isSimilar(Shop.ShardTypes.Nexus.item);
    }
}

class CrystalShardBlock {
    //Added the extra time parameter whicch will be 5 for Pure shards, and zero for weak/stale shards
    //TODO: Seperate pure shards from this fully, and change the generator speeds
    public CrystalShardBlock(Player p, Material input, Location loc, BlockData data, int extraTime) {
        BossbarManager bossbar = crystalBlitz.getInstance().gamemanager.bossbar;
        int timer = 0;
        switch (bossbar.currentstate) {
            case BossBarStates.starting -> {timer = crystalBlitz.getInstance().getRandomNumber(3, 9);}
            case BossBarStates.GenUpgradeI -> {timer = crystalBlitz.getInstance().getRandomNumber(2, 7);}
            case BossBarStates.GenUpgradeII, BossBarStates.GenUpgradeIII, BossBarStates.GenUpgradeIV, BossBarStates.Overtime -> {
                timer = crystalBlitz.getInstance().getRandomNumber(1, 5);
            }
        }

        //The extra time is being added here
        int finalTimer = timer + extraTime; //I hate this
        new BukkitRunnable() {
            int timer2 = finalTimer;

            public void run () {
                //The original if: (timer2 == 0 || crystalBlitz.getInstance().gamemanager == null)
                //Or it is equal to null then it will run, seemed to be incorrect so I fixed it.
                //Now when null it cancels the task and returns
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                    return;
                }
                //Gets the pure shard generator from the spike/crystal
                PureShardGenerator generator = crystalBlitz.getInstance().gamemanager.getPureShardGeneratorFromSpike(loc);

                //Makes sure that when the crystal belogns to a broken/non-active pure shard generator it will not regenerate
                //Must not be null and be not active, so that the weak shard generator still works
                //so no need to fix this to make the upgrades at base generators work
                if (generator != null && !generator.isActive()) {
                    cancel();
                    return;
                }
                if (timer2 == 0) {
                    //decide material
                    //this is for strongerShardGen team upgrade to work properly
                    Material finalInput = Material.AIR;
                    switch (input) {
                        case AMETHYST_CLUSTER -> {finalInput = input;}
                        case LARGE_AMETHYST_BUD, DEAD_BRAIN_CORAL_WALL_FAN -> {
                            finalInput = Material.DEAD_BRAIN_CORAL_WALL_FAN;
                        }
                    }

                    //set block
                    loc.getBlock().setType(finalInput);
                    Directional dir = (Directional) loc.getBlock().getBlockData();
                    Directional dir2 = (Directional) data;

                    dir.setFacing(dir2.getFacing());
                    loc.getBlock().setBlockData(data);
                    loc.getBlock().setBlockData(dir);
                    Waterlogged water = (Waterlogged) loc.getBlock().getBlockData();
                    water.setWaterlogged(false);
                    loc.getBlock().setBlockData(water);
                    loc.getBlock().getState().update();
                    cancel();
                }
                timer2--;
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
    }
}
