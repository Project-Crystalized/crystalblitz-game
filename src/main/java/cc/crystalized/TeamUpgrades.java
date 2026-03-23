package cc.crystalized;

import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class TeamUpgrades {

    List<String> team = new ArrayList<>();
    private static List<upgrades> upgradesBought = new ArrayList<>();

    public TextDisplay staleShardTag;

    public TeamUpgrades(String t) {
        this.team.clear();
        upgradesBought.clear();

        new BukkitRunnable() {
            public void run() {
                List<String> teamList = Teams.get_team_from_string(t);
                if (teamList == null) {
                    return;
                }

                team.addAll(teamList);

                resetStaleShard(crystalBlitz.getInstance().mapdata.getStaleShardLoc(t));
                cancel();
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1);
    }

    private void resetStaleShard(Location center) {
        center.setWorld(Bukkit.getWorld("world")); //throws a NPE if we dont do this, idk why
        Block cb = center.getBlock();
        cb.setType(Material.BLACK_GLAZED_TERRACOTTA);
        Directional dir = (Directional) cb.getBlockData();
        dir.setFacing(BlockFace.EAST);
        cb.setBlockData(dir);
        cb.getState().update(true, false);

        resetSideStaleShard(center.clone().add(0, 0, 1), BlockFace.SOUTH, false);
        resetSideStaleShard(center.clone().add(0, 0, -1), BlockFace.NORTH, false);
        resetSideStaleShard(center.clone().add(1, 0, 0), BlockFace.EAST, false);
        resetSideStaleShard(center.clone().add(-1, 0, 0), BlockFace.WEST, false);

        center.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
        resetSideStaleShard(center.clone().add(0, 1, 1), null, true);
        resetSideStaleShard(center.clone().add(0, 1, -1), null, true);
        resetSideStaleShard(center.clone().add(1, 1, 0), null, true);
        resetSideStaleShard(center.clone().add(-1, 1, 0), null, true);

        //spawn the block's nametag
        staleShardTag = Bukkit.getWorld("world").spawn(center.clone().add(0.5, 1.5, 0.5), TextDisplay.class, entity -> {
            entity.setSeeThrough(true);
            entity.setBillboard(Display.Billboard.CENTER);
            entity.text(translatable("crystalized.game.crystalblitz.weaknode.mine").color(DARK_RED));
        });
    }

    private void resetSideStaleShard(Location loc, BlockFace facing ,boolean remove) {
        if (remove) {
            loc.getBlock().setType(Material.AIR);
        } else {
            Block b = loc.getBlock();
            b.setType(Material.DEAD_BRAIN_CORAL_WALL_FAN);
            CoralWallFan data = (CoralWallFan) b.getBlockData();
            data.setWaterlogged(false);
            data.setFacing(facing);
            b.setBlockData(data);
            b.getState().update(true, false);
        }
    }

    public boolean hasUpgrade(upgrades u) {
        if (upgradesBought.contains(u)) {
            return true;
        }
        return false;
    }

    public void getUpgrade(upgrades u, Player buyer) {
        if (upgradesBought.contains(u)) {
            return;
        }
        upgradesBought.add(u);

        //do special shit for some upgrades
        switch (u) {
            case sharpness -> {
                for (String s : team) {
                    Player p = Bukkit.getPlayer(s);
                    if (p != null) {
                        for (ItemStack i : p.getInventory()) {
                            //a mess
                            if (i != null) {
                                PersistentDataContainerView pdc = i.getPersistentDataContainer();
                                String internalNameFromPDC = pdc.get(new NamespacedKey("crystalblitz", "internalname"), PersistentDataType.STRING);
                                CBItem cbItem = CrystalBlitzItems.getCBItem(internalNameFromPDC);
                                if (internalNameFromPDC != null && cbItem != null && cbItem.type.equals(CrystalBlitzItems.ItemType.Melee)) {
                                    i.addEnchantment(Enchantment.SHARPNESS, 1);
                                }
                            }
                        }
                    }
                }
            }
            case protection -> {
                for (String s : team) {
                    Player p = Bukkit.getPlayer(s);
                    if (p != null) {
                        //these might return NPEs, but everyone should be wearing armor at this point
                        p.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION, 1);
                        p.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION, 1);
                        p.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION, 1);
                    }
                }
            }
        }

        //send message in chat
        FloodgateApi floodgateapi = FloodgateApi.getInstance();
        for (String s : team) {
            Player p = Bukkit.getPlayer(s);
            if (floodgateapi.isFloodgatePlayer(p.getUniqueId())) {
                p.sendMessage(Component.text("-".repeat(40)).color(WHITE));
            } else {
                p.sendMessage(Component.text(" ".repeat(55)).color(WHITE).decoration(TextDecoration.STRIKETHROUGH,  true));
            }
            p.sendRichMessage("");
            p.sendMessage(buyer.displayName()
                    .append(text(" bought the "))
                    .append(u.item.effectiveName().color(NamedTextColor.GREEN))
                    .append(text(" team upgrade!"))
            );
            p.sendRichMessage("");
            if (floodgateapi.isFloodgatePlayer(p.getUniqueId())) {
                p.sendMessage(Component.text("-".repeat(40)).color(WHITE));
            } else {
                p.sendMessage(Component.text(" ".repeat(55)).color(WHITE).decoration(TextDecoration.STRIKETHROUGH,  true));
            }
        }
    }


    public static ItemStack getUpgradeShopItem(upgrades u, Player p) {
        if (upgradesBought.contains(u)) {
            return u.shopItem_alreadyHas;
        } else if (p.getInventory().containsAtLeast(u.priceType.item, u.price)) {
            return u.shopItem;
        } else {
            return u.shopItem_cantbuy;
        }
    }
}

enum upgrades{
    nexusHeal(Material.COAL, "Nexus Heal", "Restores your Nexus with half health", "nexus_shard", 40, Shop.ShardTypes.Weak),
    slimeTotemAlarm(Material.COAL, "Slime Totem Alarm", "desc", "slime_totem", 40, Shop.ShardTypes.Weak),
    doubleStaleShards(Material.COAL, "Double Stale Shards", "adds another stale shard block ontop of your one at base.", "weak_shard", 32, Shop.ShardTypes.Weak),
    strongerShardGen1(Material.LARGE_AMETHYST_BUD, "Stronger Shard Generation 1", "LVL1: The Stale shard block at base may grow Pure shards if left long enough", "", 40, Shop.ShardTypes.Weak),
    strongerShardGen2(Material.AMETHYST_CLUSTER, "Stronger Shard Generation 2", "LVL2: Pure Shards can grow Larger", "", 20, Shop.ShardTypes.Strong),
    autoShardCollect(Material.COAL, "Auto Shard Collection", "Every 10 seconds, a stale shard block will be broken and its contents will go in your echest.", "strong_shard", 30, Shop.ShardTypes.Weak),
    sharpness(Material.ENCHANTED_BOOK, "Sharpness", "Gives melee weapons sharpness automatically", "", 40, Shop.ShardTypes.Weak),
    totemMoreHealth(Material.COAL, "More Totem Health", "Newly placed totems gain more health", "antiair_totem", 40, Shop.ShardTypes.Weak),
    protection(Material.ENCHANTED_BOOK, "Protection", "Gives Protection 1 to armour", "", 40, Shop.ShardTypes.Weak),
    ;

    ItemStack item;
    ItemStack shopItem;
    ItemStack shopItem_cantbuy;
    ItemStack shopItem_alreadyHas;
    int price;
    Shop.ShardTypes priceType;

    upgrades(Material mat, String name, String desc, String itemModel, int price, Shop.ShardTypes priceType) {
        this.item = setup(mat, name, desc, itemModel);
        this.price = price;
        this.priceType = priceType;

        ItemStack shopItem = item.clone();
        ItemMeta shopItem_meta = shopItem.getItemMeta();
        List<Component> shopItem_lore = shopItem_meta.lore();
        if (shopItem_lore == null) {
            shopItem_lore = new ArrayList<>();
        }
        shopItem_lore.add(text(""));
        shopItem_lore.add(text(price + priceType.priceAfterText).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        shopItem_meta.lore(shopItem_lore);
        shopItem_meta.setTooltipStyle(priceType.tooltipStyle);
        shopItem_meta.getPersistentDataContainer().set(new NamespacedKey("crystalized", "canbuy"), PersistentDataType.BOOLEAN, true);
        shopItem.setItemMeta(shopItem_meta);
        this.shopItem = shopItem;

        ItemStack shopItem_cb = item.clone();
        ItemMeta shopItem_cb_meta = shopItem_cb.getItemMeta();
        List<Component> shopItem_cb_lore = shopItem_cb_meta.lore();
        if (shopItem_cb_lore == null) {
            shopItem_cb_lore = new ArrayList<>();
        }
        shopItem_cb_lore.add(text(""));
        shopItem_cb_lore.add(text(price + priceType.priceAfterText).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        shopItem_cb_meta.lore(shopItem_cb_lore);
        shopItem_cb_meta.setTooltipStyle(new NamespacedKey("crystalized", "cb_cantbuy"));
        shopItem_cb_meta.getPersistentDataContainer().set(new NamespacedKey("crystalized", "canbuy"), PersistentDataType.BOOLEAN, false);
        shopItem_cb.setItemMeta(shopItem_cb_meta);
        this.shopItem_cantbuy = shopItem_cb;

        ItemStack shopItem_ah = item.clone();
        ItemMeta shopItem_ah_meta = shopItem_ah.getItemMeta();
        List<Component> shopItem_ah_lore = shopItem_ah_meta.lore();
        if (shopItem_ah_lore == null) {
            shopItem_ah_lore = new ArrayList<>();
        }
        shopItem_ah_lore.add(text(""));
        shopItem_ah_lore.add(text("This upgrade is already bought.").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        shopItem_ah_meta.lore(shopItem_ah_lore);
        shopItem_ah_meta.setTooltipStyle(new NamespacedKey("crystalized", "cb_cantbuy"));
        shopItem_ah_meta.getPersistentDataContainer().set(new NamespacedKey("crystalized", "canbuy"), PersistentDataType.BOOLEAN, false);
        shopItem_ah.setItemMeta(shopItem_ah_meta);
        this.shopItem_alreadyHas = shopItem_ah;
    }

    ItemStack setup(Material mat, String name, String desc, String itemModel) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(translatable(name).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        meta.lore(List.of(translatable(desc).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE)));
        if (itemModel != "") {
            meta.setItemModel(new NamespacedKey("crystalized", itemModel));
        }
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey("crystalblitz", "isupgrade"), PersistentDataType.BOOLEAN, true);
        pdc.set(new NamespacedKey("crystalblitz", "upgradename"), PersistentDataType.STRING, this.toString());
        item.setItemMeta(meta);
        return item;
    }
}