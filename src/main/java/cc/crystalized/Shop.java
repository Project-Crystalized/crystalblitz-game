package cc.crystalized;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

import static net.kyori.adventure.text.Component.text;

public class Shop{

    public enum ShardTypes {
        Weak,
        Strong,
        Nexus
    }

    public enum ArmourType {
        Leather,
        Chainmail,
        Iron,
        Diamond
    }

    private static Player viewer;

    public static ItemStack CategoryOffence = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    public static ItemStack CategoryDefence = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
    public static ItemStack CategoryUtility = new ItemStack(Material.LIME_STAINED_GLASS_PANE);

    public static Inventory view = Bukkit.getServer().createInventory(null, 54, text("null"));

    public static HashMap<String, Shop> shopList = new HashMap<>();

    public static void setupShop() {
        ItemMeta CategoryOffence_im = CategoryOffence.getItemMeta();
        CategoryOffence_im.customName(text("Offence").decoration(TextDecoration.ITALIC, false));
        CategoryOffence.setItemMeta(CategoryOffence_im);

        ItemMeta CategoryDefence_im = CategoryDefence.getItemMeta();
        CategoryDefence_im.customName(text("Defence").decoration(TextDecoration.ITALIC, false));
        CategoryDefence.setItemMeta(CategoryDefence_im);

        ItemMeta CategoryUtility_im = CategoryUtility.getItemMeta();
        CategoryUtility_im.customName(text("Utility").decoration(TextDecoration.ITALIC, false));
        CategoryUtility.setItemMeta(CategoryUtility_im);
    }

    /*
    Categories:
    Offense, Defense, Utility, All Items

    Offense:
    Swords, Bows, Pickaxes, Armor

    Defense:
    All Totems except Cloud and AntiAir, Blocks

    Utility:
    Cloud and Antiair Totems, Orbs, Gapples, Potions,

     */

    public Shop(Player p) {
        shopList.put(p.getName(), this);

        viewer = p;
        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA001 Shop: Select Category").color(NamedTextColor.WHITE));
        view.clear();

        view.setItem(0, CategoryOffence);
        view.setItem(1, CategoryOffence);
        view.setItem(2, CategoryOffence);
        view.setItem(9, CategoryOffence);
        view.setItem(10, CategoryOffence);
        view.setItem(11, CategoryOffence);
        view.setItem(18, CategoryOffence);
        view.setItem(19, CategoryOffence);
        view.setItem(20, CategoryOffence);

        view.setItem(3, CategoryDefence);
        view.setItem(4, CategoryDefence);
        view.setItem(5, CategoryDefence);
        view.setItem(12, CategoryDefence);
        view.setItem(13, CategoryDefence);
        view.setItem(14, CategoryDefence);
        view.setItem(21, CategoryDefence);
        view.setItem(22, CategoryDefence);
        view.setItem(23, CategoryDefence);

        view.setItem(6, CategoryUtility);
        view.setItem(7, CategoryUtility);
        view.setItem(8, CategoryUtility);
        view.setItem(15, CategoryUtility);
        view.setItem(16, CategoryUtility);
        view.setItem(17, CategoryUtility);
        view.setItem(24, CategoryUtility);
        view.setItem(25, CategoryUtility);
        view.setItem(26, CategoryUtility);

        view.setItem(36, CrystalBlitzItems.Arrows);

        p.openInventory(view);
    }

    public static void openOffence(Player p) {
        viewer = p;
        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA001 Shop: Offence").color(NamedTextColor.WHITE));
        view.clear();
        view.setItem(10, CrystalBlitzItems.StoneSword);
        view.setItem(11, CrystalBlitzItems.StonePickaxe);
        view.setItem(12, CrystalBlitzItems.IronSword);
        view.setItem(13, CrystalBlitzItems.IronPickaxe);
        view.setItem(14, CrystalBlitzItems.DiamondSword);
        view.setItem(15, CrystalBlitzItems.DiamondPickaxe);
        view.setItem(16, CrystalBlitzItems.BreezeDagger);
        view.setItem(19, CrystalBlitzItems.Bow);
        view.setItem(20, CrystalBlitzItems.ChargedCrossbow);
        view.setItem(21, CrystalBlitzItems.WingedOrb);

        p.openInventory(view);
    }

    public static void openDefence(Player p) {
        viewer = p;
        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA001 Shop: Defence").color(NamedTextColor.WHITE));
        view.clear();
        view.setItem(10, CrystalBlitzItems.ConcreteBlocks);
        view.setItem(11, CrystalBlitzItems.CopperBlocks);
        view.setItem(12, CrystalBlitzItems.WoolBlocks);
        view.setItem(13, CrystalBlitzItems.ChainmailChestplate);
        view.setItem(14, CrystalBlitzItems.IronChestplate);
        view.setItem(15, CrystalBlitzItems.DiamondChestplate);

        p.openInventory(view);
    }

    public static void openUtility(Player p) {
        viewer = p;
        view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA001 Shop: Utility").color(NamedTextColor.WHITE));
        view.clear();
        view.setItem(10, CrystalBlitzItems.BoostOrb);
        view.setItem(11, CrystalBlitzItems.Gapples);

        p.openInventory(view);
    }

    public static Shop getShop(Player p) {
        return shopList.get(p.getName());
    }
}
