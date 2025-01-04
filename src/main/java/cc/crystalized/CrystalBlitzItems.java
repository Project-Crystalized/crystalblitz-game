package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class CrystalBlitzItems {
    public static ItemStack WoodenSword = new ItemStack(Material.WOODEN_SWORD);
    public static ItemStack WoodenPickaxe = new ItemStack(Material.WOODEN_PICKAXE);
    public static ItemStack StoneSword = new ItemStack(Material.STONE_SWORD);
    public static ItemStack StonePickaxe = new ItemStack(Material.STONE_PICKAXE);

    public static ItemStack WeakShard = new ItemStack(Material.COAL);
    public static ItemStack StrongShard = new ItemStack(Material.COAL);
    public static ItemStack NexusShard = new ItemStack(Material.COAL);

    public static void SetupItems() {
        ItemMeta WoodenSword_im = WoodenSword.getItemMeta();
        WoodenSword_im.customName(text("Wooden Sword").decoration(TextDecoration.ITALIC, false));
        List<Component> WoodenSwordLore = new ArrayList<>();
        WoodenSwordLore.add(text("Your starting sword, you'll need to upgrade this at some point."));
        WoodenSword_im.lore(WoodenSwordLore);
        WoodenSword.setItemMeta(WoodenSword_im);

        ItemMeta WoodenPickaxe_im = WoodenPickaxe.getItemMeta();
        WoodenPickaxe_im.customName(text("Wooden Pickaxe").decoration(TextDecoration.ITALIC, false));
        List<Component> WoodenPickaxeLore = new ArrayList<>();
        WoodenPickaxeLore.add(text("Your starting pickaxe, you'll need to upgrade this at some point."));
        WoodenPickaxe_im.lore(WoodenPickaxeLore);
        WoodenPickaxe.setItemMeta(WoodenPickaxe_im);

        ItemMeta StoneSword_im = StoneSword.getItemMeta();
        StoneSword_im.customName(text("Stone Sword").decoration(TextDecoration.ITALIC, false));
        List<Component> StoneSwordLore = new ArrayList<>();
        StoneSwordLore.add(text("Stone sword description here"));
        StoneSword_im.lore(StoneSwordLore);
        StoneSword.setItemMeta(StoneSword_im);

        ItemMeta StonePickaxe_im = StonePickaxe.getItemMeta();
        StonePickaxe_im.customName(text("Stone Pickaxe").decoration(TextDecoration.ITALIC, false));
        List<Component> StonePickaxeLore = new ArrayList<>();
        StonePickaxeLore.add(text("Stone pickaxe description here"));
        StonePickaxe_im.lore(StonePickaxeLore);
        StonePickaxe.setItemMeta(StonePickaxe_im);

        ItemMeta WeakShard_im = WeakShard.getItemMeta();
        WeakShard_im.customName(translatable("crystalized.item.weakshard.name").decoration(TextDecoration.ITALIC, false));
        WeakShard_im.setCustomModelData(17);
        WeakShard.setItemMeta(WeakShard_im);

        ItemMeta StrongShard_im = StrongShard.getItemMeta();
        StrongShard_im.customName(translatable("crystalized.item.stoneshard.name").decoration(TextDecoration.ITALIC, false));
        StrongShard_im.setCustomModelData(18);
        StrongShard.setItemMeta(StrongShard_im);

        ItemMeta NexusShard_im = NexusShard.getItemMeta();
        NexusShard_im.customName(translatable("crystalized.item.nexusshard.name").decoration(TextDecoration.ITALIC, false));
        NexusShard_im.setCustomModelData(19);
        NexusShard.setItemMeta(NexusShard_im);
    }
}
