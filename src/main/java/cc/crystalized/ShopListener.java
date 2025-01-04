package cc.crystalized;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static net.kyori.adventure.text.Component.text;

public class ShopListener implements Listener {

    @EventHandler
    public void onShopClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p1 = (Player) e.getWhoClicked();
        HumanEntity p = e.getWhoClicked();
        if (crystalBlitz.getInstance().gamemanager == null) {
            return;
        }

        if (e.getCurrentItem() == null) {return;} //to prevent NullPointerExceptions

        //Couldn't make a switch statement so a constant if else will be used instead. ugly but it works

        if (e.getCurrentItem().equals(Shop.CategoryOffence)) {
            Shop.openOffence((Player) p);
        } else if (e.getCurrentItem().equals(Shop.CategoryDefence)) {
            p.sendMessage(text("shop clicked defence"));
        } else if (e.getCurrentItem().equals(Shop.CategoryUtility)) {
            p.sendMessage(text("shop clicked utility"));
        }

        else if (e.getCurrentItem().displayName().equals(CrystalBlitzItems.StoneSword.displayName())) {
            if (p.getInventory().contains(CrystalBlitzItems.StoneSword)
                    || p.getInventory().contains(Material.IRON_SWORD)
                    || p.getInventory().contains(Material.DIAMOND_SWORD)) {
                p.sendMessage(text("[!] You already have this item or something better!"));
            } else {
                ItemStack WeakShard = CrystalBlitzItems.WeakShard;
                WeakShard.setAmount(10);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, 10)) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(WeakShard);
                    p.getInventory().remove(CrystalBlitzItems.WoodenSword);
                    p.getInventory().addItem(CrystalBlitzItems.StoneSword);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }

        else {
            //do nothing
        }

    }
}
