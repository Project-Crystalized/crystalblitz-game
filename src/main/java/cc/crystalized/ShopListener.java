package cc.crystalized;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.util.DeviceOs;
import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class ShopListener implements Listener {

    @EventHandler
    public void onShopClose(InventoryCloseEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {return;}
        Player p = (Player) e.getPlayer();
        PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(p);

        if (pd == null) {return;}
        if (e.getInventory().equals(pd.enderChest)) {
            p.playSound(p, "minecraft:block.ender_chest.close", 1, 1);
        }
    }

    @EventHandler
    public void onShopClick(InventoryClickEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {return;}
        Player p1 = (Player) e.getWhoClicked();
        HumanEntity p = e.getWhoClicked();
        if (e.getCurrentItem() == null) {return;}
        if (crystalBlitz.getInstance().gamemanager == null) {
            return;
        }

        PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(p1);
        if (e.getInventory().equals(pd.enderChest)) {
            return;
        }
        if (p1.getOpenInventory().getTopInventory().contains(Shop.Back) || p1.getOpenInventory().getTopInventory().contains(Shop.CategoryDefence)) { //This is dumb
            e.setCancelled(true);
        } else {
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
        } else if (e.getCurrentItem().equals(Shop.Back)) {
            p1.closeInventory();
            new Shop(p1);
        } else if (e.getCurrentItem().equals(Shop.EnderChest)) {
            Shop.openEnderChest(p1);
        }

        else if (e.getCurrentItem().equals(CrystalBlitzItems.StoneSword)) {
            List<ItemStack> remove = new ArrayList<>();
            remove.add(CrystalBlitzItems.WoodenSword);
            List<ItemStack> nothave = new ArrayList<>();
            nothave.add(CrystalBlitzItems.StoneSword_item);
            nothave.add(CrystalBlitzItems.IronSword_item);
            nothave.add(CrystalBlitzItems.DiamondSword_item);
            buyItem(p, Shop.ShardTypes.Weak, 20, remove, CrystalBlitzItems.StoneSword_item, nothave);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.StonePickaxe)) {
            List<ItemStack> remove = new ArrayList<>();
            remove.add(CrystalBlitzItems.WoodenPickaxe);
            List<ItemStack> nothave = new ArrayList<>();
            nothave.add(CrystalBlitzItems.StonePickaxe_item);
            nothave.add(CrystalBlitzItems.IronPickaxe_item);
            nothave.add(CrystalBlitzItems.DiamondPickaxe_item);
            buyItem(p, Shop.ShardTypes.Weak, 20, remove, CrystalBlitzItems.StonePickaxe_item, nothave);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.IronSword)) {
            List<ItemStack> remove = new ArrayList<>();
            remove.add(CrystalBlitzItems.WoodenSword);
            remove.add(CrystalBlitzItems.StoneSword_item);
            List<ItemStack> nothave = new ArrayList<>();
            nothave.add(CrystalBlitzItems.IronSword_item);
            nothave.add(CrystalBlitzItems.DiamondSword_item);
            buyItem(p, Shop.ShardTypes.Strong, 10, remove, CrystalBlitzItems.IronSword_item, nothave);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.IronPickaxe)) {
            List<ItemStack> remove = new ArrayList<>();
            remove.add(CrystalBlitzItems.WoodenPickaxe);
            remove.add(CrystalBlitzItems.StonePickaxe_item);
            List<ItemStack> nothave = new ArrayList<>();
            nothave.add(CrystalBlitzItems.IronPickaxe_item);
            nothave.add(CrystalBlitzItems.DiamondPickaxe_item);
            buyItem(p, Shop.ShardTypes.Strong, 10, remove, CrystalBlitzItems.IronPickaxe_item, nothave);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.BreezeDagger)) {
            List<ItemStack> remove = new ArrayList<>();
            remove.add(CrystalBlitzItems.WoodenSword);
            remove.add(CrystalBlitzItems.StoneSword_item);
            List<ItemStack> nothave = new ArrayList<>();
            nothave.add(CrystalBlitzItems.BreezeDagger_item);
            buyItem(p, Shop.ShardTypes.Strong, 30, remove, CrystalBlitzItems.BreezeDagger_item, nothave);

            String PCHint = "\uE10B";
            String XboxHint = "\uE118";
            String PSNHint = "\uE10C";
            String NXHint = "\uE10E";
            String a = null;
            FloodgateApi fapi = FloodgateApi.getInstance();

            if (fapi.isFloodgatePlayer(p.getUniqueId())) {
                switch (fapi.getPlayer(p.getUniqueId()).getDeviceOs()) {
                    case DeviceOs.XBOX -> {
                        a = XboxHint;
                    }
                    case DeviceOs.NX -> {
                        a = NXHint;
                    }
                    case DeviceOs.PS4 -> {
                        a = PSNHint;
                    }
                    default -> {
                        a = PCHint;
                    }
                }
            } else {
                a = PCHint;
            }

            p.showTitle(Title.title(translatable("crystalized.sword.wind.name"), text(a + " to Dash."), Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(5), Duration.ofSeconds(1))));
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.DiamondSword)) {
            List<ItemStack> remove = new ArrayList<>();
            remove.add(CrystalBlitzItems.WoodenSword);
            remove.add(CrystalBlitzItems.StoneSword_item);
            remove.add(CrystalBlitzItems.IronSword_item);
            buyItem(p, Shop.ShardTypes.Nexus, 2, remove, CrystalBlitzItems.DiamondSword_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.DiamondPickaxe)) {
            List<ItemStack> remove = new ArrayList<>();
            remove.add(CrystalBlitzItems.WoodenPickaxe);
            remove.add(CrystalBlitzItems.StonePickaxe_item);
            remove.add(CrystalBlitzItems.IronPickaxe_item);
            buyItem(p, Shop.ShardTypes.Nexus, 2, remove, CrystalBlitzItems.DiamondPickaxe_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.Shears)) {
            buyItem(p, Shop.ShardTypes.Weak, 40, null, CrystalBlitzItems.Shears_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.Bow)) {
            buyItem(p, Shop.ShardTypes.Strong, 25, null, CrystalBlitzItems.Bow_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.ChargedCrossbow)) {
            List<ItemStack> remove = new ArrayList<>();
            remove.add(CrystalBlitzItems.Bow_item);
            buyItem(p, Shop.ShardTypes.Nexus, 4, remove, CrystalBlitzItems.ChargedCrossbow_item, null, Shop.ShardTypes.Strong, 20);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.WingedOrb)) {
            buyItem(p, Shop.ShardTypes.Strong, 35, null, CrystalBlitzItems.WingedOrb_item, null);
        }

        else if (e.getCurrentItem().equals(CrystalBlitzItems.ConcreteBlocks)) {
            ItemStack item = null;
            switch (Teams.getPlayerTeam(p1)) {
                case "blue" -> {
                    item = new ItemStack(Material.BLUE_CONCRETE);
                    item.setAmount(16);
                }
                case "cyan" -> {
                    item = new ItemStack(Material.CYAN_CONCRETE);
                    item.setAmount(16);
                }
                case "green" -> {
                    item = new ItemStack(Material.GREEN_CONCRETE);
                    item.setAmount(16);
                }
                case "lime" -> {
                    item = new ItemStack(Material.LIME_CONCRETE);
                    item.setAmount(16);
                }
                case "magenta" -> {
                    item = new ItemStack(Material.MAGENTA_CONCRETE);
                    item.setAmount(16);
                }
                case "red" -> {
                    item = new ItemStack(Material.RED_CONCRETE);
                    item.setAmount(16);
                }
                case "white" -> {
                    item = new ItemStack(Material.WHITE_CONCRETE);
                    item.setAmount(16);
                }
                case "yellow" -> {
                    item = new ItemStack(Material.YELLOW_CONCRETE);
                    item.setAmount(16);
                }
            }
            buyItem(p, Shop.ShardTypes.Weak, 8, null, item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.CopperBlocks)) {
            ItemStack item = new ItemStack(Material.COPPER_BLOCK);
            item.setAmount(8);
            buyItem(p, Shop.ShardTypes.Weak, 10, null, item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.WoolBlocks)) {
            ItemStack item = null;
            switch (Teams.getPlayerTeam(p1)) {
                case "blue" -> {
                    item = new ItemStack(Material.BLUE_WOOL);
                    item.setAmount(8);
                }
                case "cyan" -> {
                    item = new ItemStack(Material.CYAN_WOOL);
                    item.setAmount(8);
                }
                case "green" -> {
                    item = new ItemStack(Material.GREEN_WOOL);
                    item.setAmount(8);
                }
                case "lime" -> {
                    item = new ItemStack(Material.LIME_WOOL);
                    item.setAmount(8);
                }
                case "magenta" -> {
                    item = new ItemStack(Material.MAGENTA_WOOL);
                    item.setAmount(8);
                }
                case "red" -> {
                    item = new ItemStack(Material.RED_WOOL);
                    item.setAmount(8);
                }
                case "white" -> {
                    item = new ItemStack(Material.WHITE_WOOL);
                    item.setAmount(8);
                }
                case "yellow" -> {
                    item = new ItemStack(Material.YELLOW_WOOL);
                    item.setAmount(8);
                }
            }
            buyItem(p, Shop.ShardTypes.Weak, 8, null, item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.ChainmailChestplate)) {
            List<Shop.ArmourType> a = new ArrayList<>();
            a.add(Shop.ArmourType.Chainmail);
            a.add(Shop.ArmourType.Iron);
            a.add(Shop.ArmourType.Diamond);
            buyItem(p, Shop.ShardTypes.Weak, 40, Shop.ArmourType.Chainmail, a);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.IronChestplate)) {
            List<Shop.ArmourType> a = new ArrayList<>();
            a.add(Shop.ArmourType.Iron);
            a.add(Shop.ArmourType.Diamond);
            buyItem(p, Shop.ShardTypes.Strong, 10, Shop.ArmourType.Iron, a);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.DiamondChestplate)) {
            List<Shop.ArmourType> a = new ArrayList<>();
            a.add(Shop.ArmourType.Diamond);
            buyItem(p, Shop.ShardTypes.Nexus, 4, Shop.ArmourType.Diamond, a);
        }

        else if (e.getCurrentItem().equals(CrystalBlitzItems.BoostOrb)) {
            buyItem(p, Shop.ShardTypes.Strong, 20, null, CrystalBlitzItems.BoostOrb_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.Gapples)) {
            buyItem(p, Shop.ShardTypes.Strong, 4, null, new ItemStack(Material.GOLDEN_APPLE), null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.GrapplingOrb)) {
            buyItem(p, Shop.ShardTypes.Strong, 20, null, CrystalBlitzItems.GrapplingOrb_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.CloudTotem)) {
            buyItem(p, Shop.ShardTypes.Strong, 30, null, CrystalBlitzItems.CloudTotem_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.AntiAirTotem)) {
            buyItem(p, Shop.ShardTypes.Strong, 30, null, CrystalBlitzItems.AntiAirTotem_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.LaunchTotem)) {
            buyItem(p, Shop.ShardTypes.Strong, 35, null, CrystalBlitzItems.LaunchTotem_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.BridgeOrb)) {
            buyItem(p, Shop.ShardTypes.Weak, 40, null, CrystalBlitzItems.BridgeOrb_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.PoisonOrb)) {
            buyItem(p, Shop.ShardTypes.Strong, 10, null, CrystalBlitzItems.PoisonOrb_item, null);
        }
        else if (e.getCurrentItem().equals(CrystalBlitzItems.ExplosiveOrb)) {
            buyItem(p, Shop.ShardTypes.Weak, 40, null, CrystalBlitzItems.ExplosiveOrb_item, null);
        }

        //No Category
        else if (e.getCurrentItem().equals(CrystalBlitzItems.Arrows)) {
            buyItem(p, Shop.ShardTypes.Strong, 2, null, CrystalBlitzItems.Arrows_item, null);
        }
    }

    //I hate this but this should make the InventoryClickEvent more clean
    private void buyItem(HumanEntity p, Shop.ShardTypes shard, int price, List<ItemStack> WhatToRemove, @NonNull ItemStack WhatToGive, List<ItemStack> MustNotHave
    ) {
        ItemStack shardtype = null;
        Player player = (Player) p;
        boolean canPlayerBuyThis = true;
        if (MustNotHave != null) {
            for (ItemStack item : MustNotHave) {
                if (p.getInventory().contains(item)) {
                    canPlayerBuyThis = false;
                }
            }
        }
        switch (shard) {
            case Shop.ShardTypes.Weak -> {
                shardtype = CrystalBlitzItems.WeakShard;
            }
            case Shop.ShardTypes.Strong -> {
                shardtype = CrystalBlitzItems.StrongShard;
            }
            case Shop.ShardTypes.Nexus -> {
                shardtype = CrystalBlitzItems.NexusShard;
            }
        }
        shardtype.setAmount(price);
        if (p.getInventory().containsAtLeast(shardtype, price)) {
            if (!canPlayerBuyThis) {
                p.sendMessage(text("[!] You already have this item or something better!")); //TODO make this translatable
            } else {
                player.playSound(p, "minecraft:block.note_block.pling", 50, 2); //TODO placeholder sound, maybe reuse LS's shop sounds if they exist
                if (WhatToRemove == null) {
                    //do nothing, to prevent a NullPointerException
                } else {
                    for (ItemStack item : WhatToRemove) {
                        p.getInventory().removeItem(item);
                    }
                }
                p.getInventory().removeItem(shardtype);
                p.getInventory().addItem(WhatToGive);
            }
        } else {
            p.sendMessage(text("[!] Insufficient funds")); //TODO make this translatable
        }
    }

    //For Armour
    private void buyItem(HumanEntity p, Shop.ShardTypes shard, int price, Shop.ArmourType WhatToGive, List<Shop.ArmourType> MustNotHave) {
        ItemStack shardtype = null;
        Player player = (Player) p;
        boolean canPlayerBuyThis = true;
        if (MustNotHave != null) {
            for (Shop.ArmourType armor : MustNotHave) {
                if (armor == Shop.ArmourType.Leather) {
                    if (p.getInventory().getLeggings().getType().equals(Material.LEATHER_LEGGINGS)) {
                        canPlayerBuyThis = false;
                    }
                } else if (armor == Shop.ArmourType.Chainmail) {
                    if (p.getInventory().getLeggings().getType().equals(Material.CHAINMAIL_LEGGINGS)) {
                        canPlayerBuyThis = false;
                    }
                } else if (armor == Shop.ArmourType.Iron) {
                    if (p.getInventory().getLeggings().getType().equals(Material.IRON_LEGGINGS)) {
                        canPlayerBuyThis = false;
                    }
                } else if (armor == Shop.ArmourType.Diamond) {
                    if (p.getInventory().getLeggings().getType().equals(Material.DIAMOND_LEGGINGS)) {
                        canPlayerBuyThis = false;
                    }
                }
            }
        }
        switch (shard) {
            case Shop.ShardTypes.Weak -> {
                shardtype = CrystalBlitzItems.WeakShard;
            }
            case Shop.ShardTypes.Strong -> {
                shardtype = CrystalBlitzItems.StrongShard;
            }
            case Shop.ShardTypes.Nexus -> {
                shardtype = CrystalBlitzItems.NexusShard;
            }
        }
        shardtype.setAmount(price);
        if (p.getInventory().containsAtLeast(shardtype, price)) {
            if (!canPlayerBuyThis) {
                p.sendMessage(text("[!] You already have this item or something better!")); //TODO make this translatable
            } else {
                player.playSound(p, "minecraft:block.note_block.pling", 50, 2); //TODO placeholder sound, maybe reuse LS's shop sounds if they exist

                ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
                ItemStack item2 = new ItemStack(Material.DIAMOND_LEGGINGS);
                switch (WhatToGive) {
                    case Shop.ArmourType.Leather -> {
                        crystalBlitz.getInstance().gamemanager.givePlayerItems((Player) p);
                        return;
                    }
                    case Shop.ArmourType.Chainmail -> {
                        item.setType(Material.CHAINMAIL_CHESTPLATE); //TODO this method is depricated but theres no replacement as far as I know
                        item2.setType(Material.CHAINMAIL_LEGGINGS);
                    }
                    case Shop.ArmourType.Iron -> {
                        item.setType(Material.IRON_CHESTPLATE);
                        item2.setType(Material.IRON_LEGGINGS);
                    }
                    case Shop.ArmourType.Diamond -> {
                        //Do nothing, its already diamond armour lol
                    }
                }
                ItemMeta meta = item.getItemMeta();
                meta.setUnbreakable(true);
                item.setItemMeta(meta);

                ItemMeta meta2 = item.getItemMeta();
                meta2.setUnbreakable(true);
                item2.setItemMeta(meta2);

                p.getInventory().setItem(38, item);
                p.getInventory().setItem(37, item2);
                p.getInventory().removeItem(shardtype);
            }
        } else {
            p.sendMessage(text("[!] Insufficient funds")); //TODO make this translatable
        }
    }

    //For things that require multiple shard types (eg, charged crossbow)
    private void buyItem(HumanEntity p, Shop.ShardTypes shard, int price, List<ItemStack> WhatToRemove, @NonNull ItemStack WhatToGive, List<ItemStack> MustNotHave, Shop.ShardTypes shard2, int price2) {
        ItemStack shardtype = null;
        ItemStack shardtype2 = null;
        Player player = (Player) p;
        boolean canPlayerBuyThis = true;
        if (MustNotHave != null) {
            for (ItemStack item : MustNotHave) {
                if (p.getInventory().contains(item)) {
                    canPlayerBuyThis = false;
                }
            }
        }
        switch (shard) {
            case Shop.ShardTypes.Weak -> {
                shardtype = CrystalBlitzItems.WeakShard;
            }
            case Shop.ShardTypes.Strong -> {
                shardtype = CrystalBlitzItems.StrongShard;
            }
            case Shop.ShardTypes.Nexus -> {
                shardtype = CrystalBlitzItems.NexusShard;
            }
        }
        shardtype.setAmount(price);
        switch (shard2) {
            case Shop.ShardTypes.Weak -> {
                shardtype2 = CrystalBlitzItems.WeakShard;
            }
            case Shop.ShardTypes.Strong -> {
                shardtype2 = CrystalBlitzItems.StrongShard;
            }
            case Shop.ShardTypes.Nexus -> {
                shardtype2 = CrystalBlitzItems.NexusShard;
            }
        }
        shardtype2.setAmount(price2);
        if (p.getInventory().containsAtLeast(shardtype, price) && p.getInventory().containsAtLeast(shardtype2, price2)) {
            if (!canPlayerBuyThis) {
                p.sendMessage(text("[!] You already have this item or something better!")); //TODO make this translatable
            } else {
                player.playSound(p, "minecraft:block.note_block.pling", 50, 2); //TODO placeholder sound, maybe reuse LS's shop sounds if they exist
                if (WhatToRemove == null) {
                    //do nothing, to prevent a NullPointerException
                } else {
                    for (ItemStack item : WhatToRemove) {
                        p.getInventory().removeItem(item);
                    }
                }
                p.getInventory().addItem(WhatToGive);
                p.getInventory().removeItem(shardtype);
                p.getInventory().removeItem(shardtype2);
            }
        } else {
            p.sendMessage(text("[!] Insufficient funds")); //TODO make this translatable
        }
    }
}
