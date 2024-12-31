package cc.crystalized;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static net.kyori.adventure.text.Component.text;

public class Shop{

    /*
    Categories:
    Offense, Defense, Utility, All Items

    All Items:
    A list of every single item available in the shop

    Offense:
    Swords, Bows, Pickaxes, Armor

    Defense:
    All Totems except Cloud and AntiAir, Blocks

    Utility:
    Cloud and Antiair Totems, Orbs, Gapples, Potions,

     */

    public Shop(Player p) {
        Inventory view = Bukkit.getServer().createInventory(p, 54, text("\uA000\uA001 Shop: Select Category").color(NamedTextColor.WHITE));
        //TODO, put stuff in the inventory
        p.openInventory(view);

        //TODO get response and change view based on what player has selected
    }
}
