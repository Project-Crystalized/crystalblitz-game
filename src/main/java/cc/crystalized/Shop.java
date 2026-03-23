package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class Shop{

    public enum ShardTypes {
        Weak(" Stale Shards", new NamespacedKey("crystalized", "weak_shard"),
                setup("crystalized.item.weakshard.name", "weak_shard", "weak_shard")
        ),
        Strong(" Pure Shards", new NamespacedKey("crystalized", "strong_shard"),
                setup("crystalized.item.stoneshard.name", "strong_shard", "strong_shard")
        ),
        Nexus(" Nexus Shards", new NamespacedKey("crystalized", "nexus_shard"),
                setup("crystalized.item.nexusshard.name", "nexus_shard", "nexus_shard")
        )
        ;

        final String priceAfterText;
        final NamespacedKey tooltipStyle;
        final ItemStack item;
        ShardTypes(String priceAfterText, NamespacedKey tooltipStyle, ItemStack item) {
            this.priceAfterText = priceAfterText;
            this.tooltipStyle = tooltipStyle;
            this.item = item;
        }

        private static ItemStack setup(String name, String itemModel, String tooltipstyle) {
            ItemStack item = new ItemStack(Material.COAL);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(translatable(name).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(new NamespacedKey("crystalized", itemModel));
            meta.setTooltipStyle(new NamespacedKey("crystalized", tooltipstyle));
            item.setItemMeta(meta);
            return item;
        }
    }

    public static ItemStack CategoryOffence = new ItemStack(Material.COAL);
    public static ItemStack CategoryDefence = new ItemStack(Material.COAL);
    public static ItemStack CategoryUtility = new ItemStack(Material.COAL);
    public static ItemStack CategoryUpgrades = new ItemStack(Material.PINK_STAINED_GLASS);
    public static ItemStack EnderChest = new ItemStack(Material.ENDER_CHEST);
    public static ItemStack Back = new ItemStack(Material.COAL);

    public static Inventory view = Bukkit.getServer().createInventory(null, 54, text("null"));
    public Player owner;

    public static void setupShop() {
        ItemMeta CategoryOffence_im = CategoryOffence.getItemMeta();
        CategoryOffence_im.customName(text("Offence").decoration(TextDecoration.ITALIC, false));
        CategoryOffence_im.setItemModel(new NamespacedKey("crystalized", "ui/invisible"));
        CategoryOffence.setItemMeta(CategoryOffence_im);

        ItemMeta CategoryDefence_im = CategoryDefence.getItemMeta();
        CategoryDefence_im.customName(text("Defence").decoration(TextDecoration.ITALIC, false));
        CategoryDefence_im.setItemModel(new NamespacedKey("crystalized", "ui/invisible"));
        CategoryDefence.setItemMeta(CategoryDefence_im);

        ItemMeta CategoryUtility_im = CategoryUtility.getItemMeta();
        CategoryUtility_im.customName(text("Utility").decoration(TextDecoration.ITALIC, false));
        CategoryUtility_im.setItemModel(new NamespacedKey("crystalized", "ui/invisible"));
        CategoryUtility.setItemMeta(CategoryUtility_im);

        ItemMeta CategoryUpgrades_im = CategoryUpgrades.getItemMeta();
        CategoryUpgrades_im.customName(text("Team Upgrades").decoration(TextDecoration.ITALIC, false));
        CategoryUpgrades_im.setItemModel(new NamespacedKey("crystalized", "ui/invisible"));
        CategoryUpgrades.setItemMeta(CategoryUpgrades_im);

        ItemMeta EChest_im = EnderChest.getItemMeta();
        List<Component> EChest_lore = new ArrayList<>();
        EChest_lore.add(text("Your private storage! Access from any Shop including this one.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        EChest_lore.add(text("Notice: When eliminated, all items will drop at your base's spawn.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        EChest_im.lore(EChest_lore);
        EnderChest.setItemMeta(EChest_im);

        ItemMeta Back_im = Back.getItemMeta();
        Back_im.customName(text("Back").decoration(TextDecoration.ITALIC, false));
        Back_im.setItemModel(new NamespacedKey("crystalized", "ui/invisible"));
        Back.setItemMeta(Back_im);
    }

    public Shop(Player p) {
        owner = p;

        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA00B").color(NamedTextColor.WHITE));
        view.clear();

        view.setItem(3, CategoryUpgrades);
        view.setItem(4, CategoryUpgrades);
        view.setItem(5, CategoryUpgrades);
        view.setItem(6, CategoryUpgrades);
        view.setItem(7, CategoryUpgrades);

        view.setItem(9, CategoryOffence);
        view.setItem(10, CategoryOffence);
        view.setItem(11, CategoryOffence);
        view.setItem(18, CategoryOffence);
        view.setItem(19, CategoryOffence);
        view.setItem(20, CategoryOffence);
        view.setItem(27, CategoryOffence);
        view.setItem(28, CategoryOffence);
        view.setItem(29, CategoryOffence);

        view.setItem(12, CategoryDefence);
        view.setItem(13, CategoryDefence);
        view.setItem(14, CategoryDefence);
        view.setItem(21, CategoryDefence);
        view.setItem(22, CategoryDefence);
        view.setItem(23, CategoryDefence);
        view.setItem(30, CategoryDefence);
        view.setItem(31, CategoryDefence);
        view.setItem(32, CategoryDefence);

        view.setItem(15, CategoryUtility);
        view.setItem(16, CategoryUtility);
        view.setItem(17, CategoryUtility);
        view.setItem(24, CategoryUtility);
        view.setItem(25, CategoryUtility);
        view.setItem(26, CategoryUtility);
        view.setItem(33, CategoryDefence);
        view.setItem(34, CategoryDefence);
        view.setItem(35, CategoryDefence);

        view.setItem(40, EnderChest);

        p.openInventory(view);
    }

    public static void openOffence(Player p) {
        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA00D").color(NamedTextColor.WHITE));
        view.clear();
        view.setItem(0, Back);
        view.setItem(1, CrystalBlitzItems.getShopItem("chainmail_armor", p));
        view.setItem(2, CrystalBlitzItems.getShopItem("iron_armor", p));
        view.setItem(3, CrystalBlitzItems.getShopItem("diamond_armor", p));
        view.setItem(4, CrystalBlitzItems.getShopItem("stone_sword", p));
        view.setItem(5, CrystalBlitzItems.getShopItem("iron_sword", p));
        view.setItem(6, CrystalBlitzItems.getShopItem("diamond_sword", p));
        //TODO wiffle bat
        view.setItem(10, CrystalBlitzItems.getShopItem("precise_crossbow", p));
        view.setItem(11, CrystalBlitzItems.getShopItem("crossbow", p));
        view.setItem(12, CrystalBlitzItems.getShopItem("bow", p));
        view.setItem(13, CrystalBlitzItems.getShopItem("arrow", p));

        p.openInventory(view);
    }

    public static void openDefence(Player p) {
        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA00C").color(NamedTextColor.WHITE));
        view.clear();
        view.setItem(0, Back);
        view.setItem(1, CrystalBlitzItems.getShopItem("stone_pickaxe", p));
        view.setItem(2, CrystalBlitzItems.getShopItem("iron_pickaxe", p));
        view.setItem(3, CrystalBlitzItems.getShopItem("diamond_pickaxe", p));
        view.setItem(4, CrystalBlitzItems.getShopItem("concrete", p));
        view.setItem(5, CrystalBlitzItems.getShopItem("wool", p));
        view.setItem(6, CrystalBlitzItems.getShopItem("copper", p));
        view.setItem(7, CrystalBlitzItems.getShopItem("glass", p));
        view.setItem(10, CrystalBlitzItems.getShopItem("bridge_orb", p));

        p.openInventory(view);
    }

    public static void openUtility(Player p) {
        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA00E").color(NamedTextColor.WHITE));
        view.clear();
        view.setItem(0, Back);
        view.setItem(1, CrystalBlitzItems.getShopItem("feather_orb", p));
        view.setItem(2, CrystalBlitzItems.getShopItem("boost_orb", p));
        view.setItem(3, CrystalBlitzItems.getShopItem("gapple", p));
        view.setItem(4, CrystalBlitzItems.getShopItem("wind_arrow", p));
        //TODO defence totem
        //TODO healing totem

        p.openInventory(view);
    }

    public static void openTeamUpgrades(Player p) {
        TeamData td = Teams.getTeamData(Teams.getPlayerTeam(p));

        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA013").color(NamedTextColor.WHITE));
        view.clear();
        view.setItem(0, Back);
        view.setItem(1, TeamUpgrades.getUpgradeShopItem(upgrades.nexusHeal, p));
        view.setItem(2, TeamUpgrades.getUpgradeShopItem(upgrades.slimeTotemAlarm, p));
        if (td.teamUpgrades.hasUpgrade(upgrades.strongerShardGen1)) {
            view.setItem(3, TeamUpgrades.getUpgradeShopItem(upgrades.strongerShardGen2, p));
        } else {
            view.setItem(3, TeamUpgrades.getUpgradeShopItem(upgrades.strongerShardGen1, p));
        }
        view.setItem(4, TeamUpgrades.getUpgradeShopItem(upgrades.sharpness, p));
        view.setItem(5, TeamUpgrades.getUpgradeShopItem(upgrades.totemMoreHealth, p));
        view.setItem(6, TeamUpgrades.getUpgradeShopItem(upgrades.protection, p));

        p.openInventory(view);
    }

    public static void openEnderChest(Player p) {
        PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(p);
        if (pd == null) {
            p.sendMessage(text("[!] Failed to open Ender Chest, your PlayerData is null"));
            return;
        }
        p.playSound(p, "minecraft:block.ender_chest.open", 1, 1);
        p.openInventory(pd.enderChest);
    }
}
