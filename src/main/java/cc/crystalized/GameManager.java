package cc.crystalized;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

import static net.kyori.adventure.text.Component.text;

public class GameManager {

    /*
    TODO

    Sort out teams
    TP players to correct Locations
    Colour armor

     */

    public Set<String> Blocks = new HashSet<>();

    public GameManager() {
        Bukkit.getServer().sendMessage(text("Starting Game!"));
        for (Player p : Bukkit.getOnlinePlayers()) {
            givePlayerItems(p);
            p.setGameMode(GameMode.SURVIVAL);
        }
    }

    private static void givePlayerItems(Player p) {
        PlayerInventory inv = p.getInventory();

        ItemStack WoodenSword = new ItemStack(Material.WOODEN_SWORD, 1);
        ItemMeta WoodenSwordIM = WoodenSword.getItemMeta();
        ItemStack WoodenPickaxe = new ItemStack(Material.WOODEN_PICKAXE, 1);
        ItemMeta WoodenPickaxeIM = WoodenPickaxe.getItemMeta();

        WoodenPickaxe.setItemMeta(WoodenPickaxeIM);
        WoodenSword.setItemMeta(WoodenSwordIM);
        inv.setItem(0, WoodenSword);
        inv.setItem(1, WoodenPickaxe);
        inv.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        inv.setBoots(new ItemStack(Material.LEATHER_BOOTS));
    }
}
