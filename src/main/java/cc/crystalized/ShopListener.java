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
        Player p1 = (Player) e.getWhoClicked();
        HumanEntity p = e.getWhoClicked();
        Shop s = Shop.getShop(p1);

        if (e.getInventory() != s.view) {
            return;
        } else {
            e.setCancelled(true);
        }

        if (crystalBlitz.getInstance().gamemanager == null) {
            return;
        }

        if (e.getCurrentItem() == null) {return;} //to prevent NullPointerExceptions

        //Couldn't make a switch statement so a constant if else will be used instead. ugly but it works
        //This is a mess

        if (e.getCurrentItem().equals(Shop.CategoryOffence)) {
            Shop.openOffence((Player) p);
        } else if (e.getCurrentItem().equals(Shop.CategoryDefence)) {
            Shop.openDefence((Player) p);
        } else if (e.getCurrentItem().equals(Shop.CategoryUtility)) {
            Shop.openUtility((Player) p);
        }

        //Offence category of items
        else if (e.getCurrentItem().equals(CrystalBlitzItems.StoneSword)) {
            if (p.getInventory().contains(CrystalBlitzItems.StoneSword)
                    || p.getInventory().contains(Material.IRON_SWORD)
                    || p.getInventory().contains(Material.DIAMOND_SWORD)) {
                p.sendMessage(text("[!] You already have this item or something better!"));
            } else {
                ItemStack WeakShard = CrystalBlitzItems.WeakShard;
                WeakShard.setAmount(20);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, WeakShard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(WeakShard);
                    p.getInventory().remove(CrystalBlitzItems.WoodenSword);
                    p.getInventory().addItem(CrystalBlitzItems.StoneSword);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.StonePickaxe)) {
            if (p.getInventory().contains(CrystalBlitzItems.StonePickaxe)
                    || p.getInventory().contains(Material.IRON_PICKAXE)
                    || p.getInventory().contains(Material.DIAMOND_PICKAXE)) {
                p.sendMessage(text("[!] You already have this item or something better!"));
            } else {
                ItemStack WeakShard = CrystalBlitzItems.WeakShard;
                WeakShard.setAmount(20);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, WeakShard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(WeakShard);
                    p.getInventory().remove(CrystalBlitzItems.WoodenPickaxe);
                    p.getInventory().addItem(CrystalBlitzItems.StonePickaxe);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }

        //Defence category
        else if (e.getCurrentItem().equals(CrystalBlitzItems.ConcreteBlocks)) {
            ItemStack WeakShard = CrystalBlitzItems.WeakShard;
            WeakShard.setAmount(8);
            if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, WeakShard.getAmount())) {
                p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                p.getInventory().removeItem(WeakShard);
                switch (Teams.getPlayerTeam(p1)) {
                    case "blue" -> {
                        ItemStack item = new ItemStack(Material.BLUE_CONCRETE);
                        item.setAmount(16);
                        p.getInventory().addItem(item);
                    }
                    case "cyan" -> {
                        ItemStack item = new ItemStack(Material.CYAN_CONCRETE);
                        item.setAmount(16);
                        p.getInventory().addItem(item);
                    }
                    case "green" -> {
                        ItemStack item = new ItemStack(Material.GREEN_CONCRETE);
                        item.setAmount(16);
                        p.getInventory().addItem(item);
                    }
                    case "lime" -> {
                        ItemStack item = new ItemStack(Material.LIME_CONCRETE);
                        item.setAmount(16);
                        p.getInventory().addItem(item);
                    }
                    case "magenta" -> {
                        ItemStack item = new ItemStack(Material.MAGENTA_CONCRETE);
                        item.setAmount(16);
                        p.getInventory().addItem(item);
                    }
                    case "red" -> {
                        ItemStack item = new ItemStack(Material.RED_CONCRETE);
                        item.setAmount(16);
                        p.getInventory().addItem(item);
                    }
                    case "white" -> {
                        ItemStack item = new ItemStack(Material.WHITE_CONCRETE);
                        item.setAmount(16);
                        p.getInventory().addItem(item);
                    }
                    case "yellow" -> {
                        ItemStack item = new ItemStack(Material.YELLOW_CONCRETE);
                        item.setAmount(16);
                        p.getInventory().addItem(item);
                    }
                }
            } else {
                p.sendMessage(text("[!] Insufficient funds"));
            }
        }

        else {
            //do nothing
        }
    }
}
