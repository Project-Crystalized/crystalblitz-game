package cc.crystalized;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (crystalBlitz.getInstance().gamemanager == null) {
            e.joinMessage(text(""));
            p.teleport(crystalBlitz.getInstance().mapdata.get_queue_spawn(Bukkit.getWorld("world")));
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            p.setGameMode(GameMode.ADVENTURE);
            p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, PotionEffect.INFINITE_DURATION, 0, false, false, true));
            p.sendPlayerListHeaderAndFooter(
                    //Header
                    text("\nCrystalized: Crystal Blitz\n"),

                    //Footer
                    text("\n")
                            .append(text("Crystal Blitz Version: " + crystalBlitz.getInstance().getDescription().getVersion()))
                            .append(text("\n"))
            );
            new QueueScoreboard(p);

        } else {
            p.kick(text("A game is currently is progress, try joining again later.").color(NamedTextColor.RED));
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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setCancelled(true);
        if (crystalBlitz.getInstance().gamemanager == null) {return;}
        Player p = e.getPlayer();
        p.setGameMode(GameMode.SPECTATOR);
        Location loc = new Location(
                Bukkit.getWorld("world"),
                crystalBlitz.getInstance().mapdata.spectator_spawn[0],
                crystalBlitz.getInstance().mapdata.spectator_spawn[1],
                crystalBlitz.getInstance().mapdata.spectator_spawn[2]
        );
        p.teleport(loc);

        PlayerInventory inv = p.getInventory();
        //Might be a mess but welp, too bad, im lazy and I cant think of anything better
        //Could be optimised
        if (inv.getChestplate().getType().equals(Material.DIAMOND_CHESTPLATE)) {
            ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
            ItemMeta meta = item.getItemMeta();
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
            inv.setChestplate(item);

            ItemStack item2 = new ItemStack(Material.IRON_LEGGINGS);
            ItemMeta meta2 = item.getItemMeta();
            meta2.setUnbreakable(true);
            item2.setItemMeta(meta2);
            inv.setLeggings(item2);
        }
        else if (inv.getChestplate().getType().equals(Material.IRON_CHESTPLATE)) {
            ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
            ItemMeta meta = item.getItemMeta();
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
            inv.setChestplate(item);

            ItemStack item2 = new ItemStack(Material.CHAINMAIL_LEGGINGS);
            ItemMeta meta2 = item.getItemMeta();
            meta2.setUnbreakable(true);
            item2.setItemMeta(meta2);
            inv.setLeggings(item2);
        }
        else if (inv.getChestplate().getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
            crystalBlitz.getInstance().gamemanager.givePlayerItems(p); //Gives players leather armor
        }

        DowngradeItem(inv, CrystalBlitzItems.StoneSword_item, CrystalBlitzItems.WoodenSword);
        DowngradeItem(inv, CrystalBlitzItems.IronSword_item, CrystalBlitzItems.StoneSword_item);
        DowngradeItem(inv, CrystalBlitzItems.DiamondSword_item, CrystalBlitzItems.IronSword_item);

        DowngradeItem(inv, CrystalBlitzItems.StonePickaxe_item, CrystalBlitzItems.WoodenPickaxe);
        DowngradeItem(inv, CrystalBlitzItems.IronPickaxe_item, CrystalBlitzItems.StonePickaxe_item);
        DowngradeItem(inv, CrystalBlitzItems.DiamondPickaxe_item, CrystalBlitzItems.IronPickaxe_item);

        DowngradeItem(inv, CrystalBlitzItems.ChargedCrossbow_item, CrystalBlitzItems.Bow_item); //perhaps make this a crossbow instead of a bow?

        inv.remove(Material.BLUE_CONCRETE);
        inv.remove(Material.BLUE_WOOL);
        inv.remove(Material.CYAN_CONCRETE);
        inv.remove(Material.CYAN_WOOL);
        inv.remove(Material.GREEN_CONCRETE);
        inv.remove(Material.GREEN_WOOL);
        inv.remove(Material.LIME_CONCRETE);
        inv.remove(Material.LIME_WOOL);
        inv.remove(Material.MAGENTA_CONCRETE);
        inv.remove(Material.MAGENTA_WOOL);
        inv.remove(Material.RED_CONCRETE);
        inv.remove(Material.RED_WOOL);
        inv.remove(Material.WHITE_CONCRETE);
        inv.remove(Material.WHITE_WOOL);
        inv.remove(Material.YELLOW_CONCRETE);
        inv.remove(Material.YELLOW_WOOL);
        inv.remove(Material.COPPER_BLOCK);

        inv.remove(CrystalBlitzItems.BoostOrb_item);

        inv.remove(Material.GOLDEN_APPLE);
        inv.remove(Material.ARROW);
        inv.remove(Material.SPECTRAL_ARROW);

        if (crystalBlitz.getInstance().gamemanager.getNexus(Teams.getPlayerTeam(p)).health != 0) {
            new BukkitRunnable() {
                int timer = 5;
                @Override
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
                        cancel();
                    }
                    timer--;
                }
            }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
        } else {
            p.sendMessage(text("[!] You're eliminated from the game!"));
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
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
            crystalBlitz.getInstance().Blocks.add(e.getBlock());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        } else {
            if (crystalBlitz.getInstance().Blocks.contains(e.getBlock())) {
                crystalBlitz.getInstance().Blocks.remove(e.getBlock());
            } else {
                //this and the hit nexus method is a big messy
                if (e.getBlock().getType().equals(Material.WHITE_GLAZED_TERRACOTTA) || e.getBlock().getType().equals(Material.GRAY_GLAZED_TERRACOTTA)
                        || e.getBlock().getType().equals(Material.LIGHT_GRAY_GLAZED_TERRACOTTA)) {
                    if (!p.getInventory().getItemInMainHand().toString().toLowerCase().contains("pickaxe")) {
                        p.sendMessage(text("[!] You need to use your Pickaxe to break this."));
                    }
                    e.setCancelled(true);
                    Directional dir = (Directional) e.getBlock().getBlockData();
                    switch (e.getBlock().getType()) {
                        case Material.WHITE_GLAZED_TERRACOTTA -> {
                            switch (dir.getFacing()) {
                                case BlockFace.EAST:
                                    crystalBlitz.getInstance().gamemanager.getNexus("blue").hitNexus("blue", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.NORTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("cyan").hitNexus("cyan", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.SOUTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("green").hitNexus("green", p.getInventory().getItemInMainHand(), p);
                                    break;
                            }
                        }
                        case Material.GRAY_GLAZED_TERRACOTTA -> {
                            switch (dir.getFacing()) {
                                case BlockFace.NORTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("red").hitNexus("red", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.SOUTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("white").hitNexus("white", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.WEST:
                                    crystalBlitz.getInstance().gamemanager.getNexus("yellow").hitNexus("yellow", p.getInventory().getItemInMainHand(), p);
                                    break;
                            }
                        }
                        case Material.LIGHT_GRAY_GLAZED_TERRACOTTA -> {
                            switch (dir.getFacing()) {
                                case BlockFace.EAST:
                                    crystalBlitz.getInstance().gamemanager.getNexus("lime").hitNexus("lime", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.NORTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("magenta").hitNexus("magenta", p.getInventory().getItemInMainHand(), p);
                                    break;
                            }
                        }
                    }
                }

                //Weak and Strong Shard blocks
                else if (e.getBlock().getType().equals(Material.BLACK_GLAZED_TERRACOTTA)) {
                    e.setCancelled(true);
                    Directional dir = (Directional) e.getBlock().getBlockData();
                    switch (dir.getFacing()) {
                        case BlockFace.EAST:
                            ItemStack weak = CrystalBlitzItems.WeakShard.clone();
                            weak.setAmount(1);
                            p.getInventory().addItem(weak);
                            p.playSound(p, "minecraft:block.note_block.bell", 50, 2);
                            break;
                        case BlockFace.NORTH:
                            ItemStack strong = CrystalBlitzItems.StrongShard.clone();
                            strong.setAmount(1);
                            p.getInventory().addItem(strong);
                            p.playSound(p, "minecraft:block.note_block.bell", 50, 2);
                            break;
                        default:
                            p.sendMessage(text("Broken black terracotta but this isn't weak or strong shards, please report this."));
                            break;

                    }
                }

                //Weak and Strong Shards (not blocks)
                else if (e.getBlock().getType().equals(Material.DEAD_BRAIN_CORAL_FAN) || e.getBlock().getType().equals(Material.DEAD_BRAIN_CORAL_WALL_FAN)
                    || e.getBlock().getType().equals(Material.AMETHYST_CLUSTER)
                ) {
                    if (e.getBlock().getType().equals(Material.DEAD_BRAIN_CORAL_FAN) || e.getBlock().getType().equals(Material.DEAD_BRAIN_CORAL_WALL_FAN)) {
                        ItemStack weak = CrystalBlitzItems.WeakShard.clone();
                        weak.setAmount(2);
                        p.getInventory().addItem(weak);
                        p.playSound(p, "minecraft:block.note_block.bell", 50, 2);
                    } else if (e.getBlock().getType().equals(Material.AMETHYST_CLUSTER)) {
                        ItemStack strong = CrystalBlitzItems.StrongShard.clone();
                        strong.setAmount(2);
                        p.getInventory().addItem(strong);
                        p.playSound(p, "minecraft:block.note_block.bell", 50, 2);
                    }
                    e.setCancelled(true);
                    new CrystalShardBlock(e.getBlock().getType(), e.getBlock().getLocation(), e.getBlock().getBlockData());
                    e.getBlock().setType(Material.AIR);
                }


                else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        Bukkit.getServer().sendMessage(Component.text("")
                .append(player.displayName())
                .append(Component.text(": "))
                .append(event.message()));
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
}

class CrystalShardBlock {
    public CrystalShardBlock(Material input, Location loc, BlockData data) {
        new BukkitRunnable() {
            int timer = crystalBlitz.getInstance().getRandomNumber(3, 9);

            @Override
            public void run () {

                if (timer == 0 || crystalBlitz.getInstance().gamemanager == null) {
                    loc.getBlock().setType(input);
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
                timer--;
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 20);
    }
}
