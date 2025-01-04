package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class Shop{
    public static ItemStack CategoryOffence = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    public static ItemStack CategoryDefence = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
    public static ItemStack CategoryUtility = new ItemStack(Material.LIME_STAINED_GLASS_PANE);

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
        Inventory view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA001 Shop: Select Category").color(NamedTextColor.WHITE));

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

        p.openInventory(view);
    }

    public static void openOffence(Player p) {
        Inventory view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA001 Shop: Offence").color(NamedTextColor.WHITE));

        //TODO broken
        ItemStack stonesword = CrystalBlitzItems.StoneSword.clone();
        ItemMeta stoneswordim = stonesword.getItemMeta();
        stoneswordim.customName(CrystalBlitzItems.StoneSword.effectiveName());
        List<Component> stoneswordlore = new ArrayList<>(CrystalBlitzItems.StoneSword.lore());
        stoneswordlore.add(text(" "));
        stoneswordlore.add(text("Cost: 10 Weak Shards"));
        stoneswordlore.add(text(" "));
        stoneswordlore.add(text("Buy help"));
        stoneswordim.lore(stoneswordlore);
        stonesword.setItemMeta(stoneswordim);

        view.setItem(0, CrystalBlitzItems.StoneSword);

        p.openInventory(view);
    }
}
