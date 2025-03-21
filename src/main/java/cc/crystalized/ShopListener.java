package cc.crystalized;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static net.kyori.adventure.text.Component.text;

public class ShopListener implements Listener {

    @EventHandler
    public void onShopClick(InventoryClickEvent e) {
        Player p1 = (Player) e.getWhoClicked();
        HumanEntity p = e.getWhoClicked();
        Shop s = Shop.getShop(p1);

        if (e.getCurrentItem() == null) {return;}

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
                ItemStack WeakShard = CrystalBlitzItems.WeakShard.clone();
                WeakShard.setAmount(20);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, WeakShard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(WeakShard);
                    p.getInventory().remove(Material.WOODEN_SWORD);
                    p.getInventory().addItem(CrystalBlitzItems.StoneSword_item);
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
                ItemStack WeakShard = CrystalBlitzItems.WeakShard.clone();
                WeakShard.setAmount(20);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, WeakShard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(WeakShard);
                    p.getInventory().remove(Material.WOODEN_PICKAXE);
                    p.getInventory().addItem(CrystalBlitzItems.StonePickaxe_item);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.IronSword)) {
            if (p.getInventory().contains(CrystalBlitzItems.IronSword) || p.getInventory().contains(Material.DIAMOND_SWORD)) {
                p.sendMessage(text("[!] You already have this item or something better!"));
            } else {
                ItemStack StrongShard = CrystalBlitzItems.StrongShard.clone();
                StrongShard.setAmount(10);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, StrongShard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(StrongShard);
                    p.getInventory().remove(Material.WOODEN_SWORD);
                    p.getInventory().remove(Material.STONE_SWORD);
                    p.getInventory().addItem(CrystalBlitzItems.IronSword_item);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.IronPickaxe)) {
            if (p.getInventory().contains(CrystalBlitzItems.IronPickaxe) || p.getInventory().contains(Material.DIAMOND_PICKAXE)) {
                p.sendMessage(text("[!] You already have this item or something better!"));
            } else {
                ItemStack StrongShard = CrystalBlitzItems.StrongShard.clone();
                StrongShard.setAmount(10);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, StrongShard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(StrongShard);
                    p.getInventory().remove(Material.WOODEN_PICKAXE);
                    p.getInventory().remove(Material.STONE_PICKAXE);
                    p.getInventory().addItem(CrystalBlitzItems.IronPickaxe_item);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.DiamondSword)) {
            if (p.getInventory().contains(Material.DIAMOND_SWORD)) {
                p.sendMessage(text("[!] You already have this item!"));
            } else {
                ItemStack Shard = CrystalBlitzItems.NexusShard.clone();
                Shard.setAmount(2);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.NexusShard, Shard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(Shard);
                    p.getInventory().remove(Material.WOODEN_SWORD);
                    p.getInventory().remove(Material.STONE_SWORD);
                    p.getInventory().remove(Material.IRON_SWORD);
                    p.getInventory().addItem(CrystalBlitzItems.DiamondSword_item);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.DiamondPickaxe)) {
            if (p.getInventory().contains(Material.DIAMOND_PICKAXE)) {
                p.sendMessage(text("[!] You already have this item!"));
            } else {
                ItemStack Shard = CrystalBlitzItems.NexusShard.clone();
                Shard.setAmount(2);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.NexusShard, Shard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(Shard);
                    p.getInventory().remove(Material.WOODEN_PICKAXE);
                    p.getInventory().remove(Material.STONE_PICKAXE);
                    p.getInventory().remove(Material.IRON_PICKAXE);
                    p.getInventory().addItem(CrystalBlitzItems.DiamondPickaxe_item);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.Bow)) {
            if (p.getInventory().contains(CrystalBlitzItems.Bow_item) || p.getInventory().contains(CrystalBlitzItems.ChargedCrossbow_item)) {
                p.sendMessage(text("[!] You already have this item or something better!"));
            } else {
                ItemStack Shard = CrystalBlitzItems.StrongShard.clone();
                Shard.setAmount(25);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, Shard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(Shard);
                    p.getInventory().addItem(CrystalBlitzItems.Bow_item);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.ChargedCrossbow)) {
            ItemStack nexus = CrystalBlitzItems.NexusShard.clone();
            nexus.setAmount(4);
            ItemStack strong = CrystalBlitzItems.StrongShard.clone();
            strong.setAmount(20);
            if ((p.getInventory().containsAtLeast(CrystalBlitzItems.NexusShard, nexus.getAmount())) && (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, strong.getAmount()))) {
                p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                p.getInventory().removeItem(nexus);
                p.getInventory().removeItem(strong);
                p.getInventory().removeItem(CrystalBlitzItems.Bow_item);
                p.getInventory().addItem(CrystalBlitzItems.ChargedCrossbow_item);
            } else {
                p.sendMessage(text("[!] Insufficient funds"));
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.WingedOrb)) {
            ItemStack strong = CrystalBlitzItems.StrongShard.clone();
            strong.setAmount(35);
            if (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, strong.getAmount())) {
                p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                p.getInventory().removeItem(strong);
                p.getInventory().addItem(CrystalBlitzItems.WingedOrb_item);
            } else {
                p.sendMessage(text("[!] Insufficient funds"));
            }
        }

        //Defence category
        else if (e.getCurrentItem().equals(CrystalBlitzItems.ConcreteBlocks)) {
            ItemStack WeakShard = CrystalBlitzItems.WeakShard.clone();
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
        else if (e.getCurrentItem().equals(CrystalBlitzItems.CopperBlocks)) {
            ItemStack WeakShard = CrystalBlitzItems.WeakShard.clone();
            WeakShard.setAmount(4);
            if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, WeakShard.getAmount())) {
                p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                p.getInventory().removeItem(WeakShard);
                ItemStack item = new ItemStack(Material.COPPER_BLOCK);
                item.setAmount(8);
                p.getInventory().addItem(item);
            } else {
                p.sendMessage(text("[!] Insufficient funds"));
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.WoolBlocks)) {
            ItemStack WeakShard = CrystalBlitzItems.WeakShard.clone();
            WeakShard.setAmount(8);
            if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, WeakShard.getAmount())) {
                p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                p.getInventory().removeItem(WeakShard);
                switch (Teams.getPlayerTeam(p1)) {
                    case "blue" -> {
                        ItemStack item = new ItemStack(Material.BLUE_WOOL);
                        item.setAmount(8);
                        p.getInventory().addItem(item);
                    }
                    case "cyan" -> {
                        ItemStack item = new ItemStack(Material.CYAN_WOOL);
                        item.setAmount(8);
                        p.getInventory().addItem(item);
                    }
                    case "green" -> {
                        ItemStack item = new ItemStack(Material.GREEN_WOOL);
                        item.setAmount(8);
                        p.getInventory().addItem(item);
                    }
                    case "lime" -> {
                        ItemStack item = new ItemStack(Material.LIME_WOOL);
                        item.setAmount(8);
                        p.getInventory().addItem(item);
                    }
                    case "magenta" -> {
                        ItemStack item = new ItemStack(Material.MAGENTA_WOOL);
                        item.setAmount(8);
                        p.getInventory().addItem(item);
                    }
                    case "red" -> {
                        ItemStack item = new ItemStack(Material.RED_WOOL);
                        item.setAmount(8);
                        p.getInventory().addItem(item);
                    }
                    case "white" -> {
                        ItemStack item = new ItemStack(Material.WHITE_WOOL);
                        item.setAmount(8);
                        p.getInventory().addItem(item);
                    }
                    case "yellow" -> {
                        ItemStack item = new ItemStack(Material.YELLOW_WOOL);
                        item.setAmount(8);
                        p.getInventory().addItem(item);
                    }
                }
            } else {
                p.sendMessage(text("[!] Insufficient funds"));
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.ChainmailChestplate)) {
            if (p.getInventory().getItem(38).getType().equals(Material.CHAINMAIL_CHESTPLATE) || p.getInventory().getItem(38).getType().equals(Material.IRON_CHESTPLATE) || p.getInventory().getItem(38).getType().equals(Material.DIAMOND_CHESTPLATE)) {
                p.sendMessage(text("[!] You already have this item or something better!"));
            } else {
                ItemStack Shard = CrystalBlitzItems.WeakShard.clone();
                Shard.setAmount(40);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.WeakShard, Shard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(Shard);
                    ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                    p.getInventory().setItem(38, item);

                    ItemStack item2 = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                    ItemMeta meta2 = item.getItemMeta();
                    meta2.setUnbreakable(true);
                    item2.setItemMeta(meta2);
                    p.getInventory().setItem(37, item2);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.IronChestplate)) {
            if (p.getInventory().getItem(38).getType().equals(Material.IRON_CHESTPLATE) || p.getInventory().getItem(38).getType().equals(Material.DIAMOND_CHESTPLATE)) {
                p.sendMessage(text("[!] You already have this item or something better!"));
            } else {
                ItemStack Shard = CrystalBlitzItems.StrongShard.clone();
                Shard.setAmount(10);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, Shard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(Shard);
                    ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                    p.getInventory().setItem(38, item);

                    ItemStack item2 = new ItemStack(Material.IRON_LEGGINGS);
                    ItemMeta meta2 = item.getItemMeta();
                    meta2.setUnbreakable(true);
                    item2.setItemMeta(meta2);
                    p.getInventory().setItem(37, item2);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.DiamondChestplate)) {
            if (p.getInventory().getItem(38).getType().equals(Material.DIAMOND_CHESTPLATE)) {
                p.sendMessage(text("[!] You already have this item!"));
            } else {
                ItemStack Shard = CrystalBlitzItems.NexusShard.clone();
                Shard.setAmount(4);
                if (p.getInventory().containsAtLeast(CrystalBlitzItems.NexusShard, Shard.getAmount())) {
                    p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                    p.getInventory().removeItem(Shard);
                    ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                    p.getInventory().setItem(38, item);

                    ItemStack item2 = new ItemStack(Material.DIAMOND_LEGGINGS);
                    ItemMeta meta2 = item.getItemMeta();
                    meta2.setUnbreakable(true);
                    item2.setItemMeta(meta2);
                    p.getInventory().setItem(37, item2);
                } else {
                    p.sendMessage(text("[!] Insufficient funds"));
                }
            }
        }

        //Utility
        else if (e.getCurrentItem().equals(CrystalBlitzItems.BoostOrb)) {
            ItemStack strong = CrystalBlitzItems.StrongShard.clone();
            strong.setAmount(20);
            if (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, strong.getAmount())) {
                p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                p.getInventory().removeItem(strong);
                p.getInventory().addItem(CrystalBlitzItems.BoostOrb_item);
            } else {
                p.sendMessage(text("[!] Insufficient funds"));
            }
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.Gapples)) {
            ItemStack strong = CrystalBlitzItems.StrongShard.clone();
            strong.setAmount(4);
            if (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, strong.getAmount())) {
                p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                p.getInventory().removeItem(strong);
                p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            } else {
                p.sendMessage(text("[!] Insufficient funds"));
            }
        }

        //No Category
        else if (e.getCurrentItem().equals(CrystalBlitzItems.Arrows)) {
            ItemStack strong = CrystalBlitzItems.StrongShard.clone();
            strong.setAmount(2);
            if (p.getInventory().containsAtLeast(CrystalBlitzItems.StrongShard, strong.getAmount())) {
                p1.playSound(p, "minecraft:block.note_block.pling", 50, 2);
                p.getInventory().removeItem(strong);
                p.getInventory().addItem(CrystalBlitzItems.Arrows_item);
            } else {
                p.sendMessage(text("[!] Insufficient funds"));
            }
        }

        else {
            //do nothing
        }
    }
}
