package cc.crystalized;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.util.DeviceOs;
import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
            return;
        } else if (e.getCurrentItem().equals(Shop.CategoryDefence)) {
            Shop.openDefence((Player) p);
            return;
        } else if (e.getCurrentItem().equals(Shop.CategoryUtility)) {
            Shop.openUtility((Player) p);
            return;
        } else if (e.getCurrentItem().equals(Shop.CategoryUpgrades)) {
            Shop.openTeamUpgrades((Player) p);
            return;
        } else if (e.getCurrentItem().equals(Shop.Back)) {
            p1.closeInventory();
            new Shop(p1);
            return;
        } else if (e.getCurrentItem().equals(Shop.EnderChest)) {
            Shop.openEnderChest(p1);
            return;
        }

        ItemStack item = e.getCurrentItem();
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        String title = PlainTextComponentSerializer.plainText().serialize(e.getView().title());

        if (pdc.has(new NamespacedKey("crystalblitz", "isupgrade"))) {
            upgrades u = upgrades.valueOf(pdc.get(new NamespacedKey("crystalblitz", "upgradename"), PersistentDataType.STRING));
            TeamData td = Teams.getTeamData(Teams.getPlayerTeam((Player) p));

            if (td.teamUpgrades.hasUpgrade(u)) {
                p.sendMessage(text("[!] You already have this team upgrade."));
                return;
            }

            if (p.getInventory().containsAtLeast(u.priceType.item, u.price)) {
                ItemStack removingShards = u.priceType.item.clone();
                removingShards.setAmount(u.price);
                p.getInventory().removeItem(removingShards);

                refreshShop(p, title);
                td.teamUpgrades.getUpgrade(u, (Player) p);
                ((Player) p).playSound(p, "minecraft:block.note_block.pling", 50, 2); //TODO maybe different sound for upgrades?
                refreshShop(p, title);
            } else {
                p.sendMessage(text("[!] Insufficient funds")); //TODO make this translatable
            }
            return;
        }

        if (pdc.has(new NamespacedKey("crystalized", "canbuy"))) {
            boolean canbuy = pdc.get(new NamespacedKey("crystalized", "canbuy"), PersistentDataType.BOOLEAN);
            if (canbuy) {
                CBItem cbItem = CrystalBlitzItems.getCBShopItem(item);
                if (cbItem == null) {
                    return;
                }

                //This is dumb but we have to refresh the shop twice;
                refreshShop(p, title); //removing this one doesn't update the tooltip display (the item's background when hovering over it)
                buyItem(p, cbItem);
                refreshShop(p, title); //but removing this one doesn't takeaway crystals when buying something

            } else {
                p.sendMessage(text("[!] Insufficient funds")); //TODO make this translatable
            }
        }
    }

    private static void refreshShop(HumanEntity p , String title) {
        if (title.contains("\uA00D")) {
            Shop.openOffence((Player) p);
        } else if (title.contains("\uA00C")) {
            Shop.openDefence((Player) p);
        } else if (title.contains("\uA00E")) {
            Shop.openUtility((Player) p);
        } else if (title.contains("\uA00B")) {
            new Shop((Player) p);
        } else if (title.contains("\uA013")) {
            Shop.openTeamUpgrades((Player) p);
        }
    }

    public static void buyItem(HumanEntity p, CBItem cbItem) {
        TeamData td = Teams.getTeamData(Teams.getPlayerTeam((Player) p));

        //check if inventory contains something we shouldn't have (eg, an iron sword when buying a stone sword)
        for (ItemStack i : p.getInventory()) {
            if (i != null) {
                CBItem temp = CrystalBlitzItems.getCBItem(i);
                if (temp != null && cbItem.mustNotHave.contains(temp.internalName)) {
                    p.sendMessage(text("[!] You already have this item or something better!")); //TODO make this translatable
                    return;
                }
            }
        }

        ItemStack removingShards = cbItem.priceType.item.clone();
        removingShards.setAmount(cbItem.price);
        p.getInventory().removeItem(removingShards);
        if (cbItem.price != 0) {
            ((Player) p).playSound(p, "minecraft:block.note_block.pling", 50, 2); //TODO placeholder sound, maybe reuse LS's shop sounds if they exist
        }

        switch (cbItem.type) {
            case Melee, Pickaxe, Ranged, Shears -> {
                ItemStack item = cbItem.item.clone();
                if (cbItem.type.equals(CrystalBlitzItems.ItemType.Melee) && td.teamUpgrades.hasUpgrade(upgrades.sharpness)) {
                    item.addEnchantment(Enchantment.SHARPNESS, 1);
                }
                removeItemType(p, cbItem.type);
                p.getInventory().addItem(item);
            }
            case Armor -> {
                if (cbItem instanceof CBItem_Armor cbArmor) {
                    cbArmor.add((Player) p);
                } else {
                    crystalBlitz.getInstance().getLogger().log(Level.WARNING, "[!] Item \"" + cbItem.internalName + "\" marked as ItemType.Armor but was setup incorrectly in code (cbItem is not an instance of CBItem_Armor)");
                    p.sendRichMessage("<red>Internal Error when buying item, check server console.");
                }
            }
            case Blocks -> {
                if (cbItem instanceof CBItem_Block cbBlock) {
                    p.getInventory().addItem(cbBlock.item((Player) p));
                } else {
                    crystalBlitz.getInstance().getLogger().log(Level.WARNING, "[!] Item \"" + cbItem.internalName + "\" marked as ItemType.Blocks but was setup incorrectly in code (cbItem is not an instance of CBItem_Block)");
                    p.sendRichMessage("<red>Internal Error when buying item, check server console.");
                }
            }
            case other -> {
                p.getInventory().addItem(cbItem.item);
            }
        }
    }

    private static void removeItemType(HumanEntity p, CrystalBlitzItems.ItemType type) {
        for (ItemStack i : p.getInventory()) {
            if (i != null) {
                CBItem cbItem = CrystalBlitzItems.getCBItem(i);
                if (cbItem != null && cbItem.type.equals(type)) {
                    p.getInventory().remove(i);
                }
            }
        }
        CBItem cbItem = CrystalBlitzItems.getCBItem(p.getInventory().getItemInOffHand());
        if (cbItem != null && cbItem.type.equals(type)) {
            p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        }
    }
}
