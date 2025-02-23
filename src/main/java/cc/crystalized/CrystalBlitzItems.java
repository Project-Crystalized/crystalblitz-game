package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
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
    public static ItemStack IronSword = new ItemStack(Material.IRON_SWORD);
    public static ItemStack IronPickaxe = new ItemStack(Material.IRON_PICKAXE);
    public static ItemStack DiamondSword = new ItemStack(Material.DIAMOND_SWORD);
    public static ItemStack DiamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

    public static ItemStack ConcreteBlocks = new ItemStack(Material.TERRACOTTA);
    public static ItemStack CopperBlocks = new ItemStack(Material.COPPER_BLOCK);
    public static ItemStack WoolBlocks = new ItemStack(Material.WHITE_WOOL);
    public static ItemStack IronChestplate = new ItemStack(Material.IRON_CHESTPLATE);
    public static ItemStack DiamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);

    public static ItemStack BoostOrb_shop = new ItemStack(Material.COAL);
    public static ItemStack BoostOrb = new ItemStack(Material.COAL);

    public static ItemStack WeakShard = new ItemStack(Material.COAL);
    public static ItemStack StrongShard = new ItemStack(Material.COAL);
    public static ItemStack NexusShard = new ItemStack(Material.COAL);

    public static ItemStack Arrows = new ItemStack(Material.ARROW);

    public static void SetupItems() {
        ItemMeta WoodenSword_im = WoodenSword.getItemMeta();
        List<Component> WoodenSwordLore = new ArrayList<>();
        WoodenSwordLore.add(text("Your starting sword, you'll need to upgrade this at some point.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        WoodenSword_im.lore(WoodenSwordLore);
        WoodenSword_im.setUnbreakable(true);
        WoodenSword.setItemMeta(WoodenSword_im);

        ItemMeta WoodenPickaxe_im = WoodenPickaxe.getItemMeta();
        List<Component> WoodenPickaxeLore = new ArrayList<>();
        WoodenPickaxeLore.add(text("Your starting pickaxe, you'll need to upgrade this at some point.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        WoodenPickaxe_im.lore(WoodenPickaxeLore);
        WoodenPickaxe_im.setUnbreakable(true);
        WoodenPickaxe.setItemMeta(WoodenPickaxe_im);

        ItemMeta StoneSword_im = StoneSword.getItemMeta();
        List<Component> StoneSwordLore = new ArrayList<>();
        StoneSwordLore.add(text("Stone sword description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StoneSwordLore.add(text(" "));
        StoneSwordLore.add(text("20 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        StoneSword_im.lore(StoneSwordLore);
        StoneSword_im.setUnbreakable(true);
        StoneSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        StoneSword.setItemMeta(StoneSword_im);

        ItemMeta StonePickaxe_im = StonePickaxe.getItemMeta();
        List<Component> StonePickaxeLore = new ArrayList<>();
        StonePickaxeLore.add(text("Stone pickaxe description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StonePickaxeLore.add(text(" "));
        StonePickaxeLore.add(text("20 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        StonePickaxe_im.lore(StonePickaxeLore);
        StonePickaxe_im.setUnbreakable(true);
        StonePickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        StonePickaxe.setItemMeta(StonePickaxe_im);

        ItemMeta IronSword_im = IronSword.getItemMeta();
        List<Component> IronSwordLore = new ArrayList<>();
        IronSwordLore.add(text("Iron sword description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronSwordLore.add(text(""));
        IronSwordLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronSword_im.lore(IronSwordLore);
        IronSword_im.setUnbreakable(true);
        IronSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        IronSword.setItemMeta(IronSword_im);

        ItemMeta IronPickaxe_im = IronPickaxe.getItemMeta();
        List<Component> IronPickaxeLore = new ArrayList<>();
        IronPickaxeLore.add(text("Iron sword description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronPickaxeLore.add(text(""));
        IronPickaxeLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronPickaxe_im.lore(IronPickaxeLore);
        IronPickaxe_im.setUnbreakable(true);
        IronPickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        IronPickaxe.setItemMeta(IronPickaxe_im);

        ItemMeta DiamondSword_im = DiamondSword.getItemMeta();
        List<Component> DiamondSwordLore = new ArrayList<>();
        DiamondSwordLore.add(text("most powerful sword").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondSwordLore.add(text(""));
        DiamondSwordLore.add(text("2 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondSword_im.lore(DiamondSwordLore);
        DiamondSword_im.setUnbreakable(true);
        DiamondSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        DiamondSword.setItemMeta(DiamondSword_im);

        ItemMeta DiamondPickaxe_im = DiamondPickaxe.getItemMeta();
        List<Component> DiamondPickaxeLore = new ArrayList<>();
        DiamondPickaxeLore.add(text("most powerful pickaxe").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondPickaxeLore.add(text(""));
        DiamondPickaxeLore.add(text("2 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondPickaxe_im.lore(DiamondPickaxeLore);
        DiamondPickaxe_im.setUnbreakable(true);
        DiamondPickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        DiamondPickaxe.setItemMeta(DiamondPickaxe_im);



        ItemMeta ConcreteBlocks_im = ConcreteBlocks.getItemMeta();
        List<Component> ConcreteBlocksLore = new ArrayList<>();
        ConcreteBlocksLore.add(text("16 Building Blocks").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ConcreteBlocksLore.add(text(""));
        ConcreteBlocksLore.add(text("8 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ConcreteBlocks_im.lore(ConcreteBlocksLore);
        ConcreteBlocks.setItemMeta(ConcreteBlocks_im);

        ItemMeta CopperBlocks_im = CopperBlocks.getItemMeta();
        List<Component> CopperBlocksLore = new ArrayList<>();
        CopperBlocksLore.add(text("8 Building Blocks").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        CopperBlocksLore.add(text(""));
        CopperBlocksLore.add(text("10 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        CopperBlocks_im.lore(CopperBlocksLore);
        CopperBlocks.setItemMeta(CopperBlocks_im);

        ItemMeta WoolBlocks_im = WoolBlocks.getItemMeta();
        List<Component> WoolBlocksLore = new ArrayList<>();
        WoolBlocksLore.add(text("8 Building Blocks").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        WoolBlocksLore.add(text(""));
        WoolBlocksLore.add(text("8 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        WoolBlocks_im.lore(WoolBlocksLore);
        WoolBlocks.setItemMeta(WoolBlocks_im);

        ItemMeta IronChestplate_im = IronChestplate.getItemMeta();
        List<Component> IronChestplateLore = new ArrayList<>();
        IronChestplateLore.add(text("description").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronChestplateLore.add(text(""));
        IronChestplateLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronChestplate_im.lore(IronChestplateLore);
        IronChestplate.setItemMeta(IronChestplate_im);

        ItemMeta DiamondChestplate_im = DiamondChestplate.getItemMeta();
        List<Component> DiamondChestplateLore = new ArrayList<>();
        DiamondChestplateLore.add(text("description").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondChestplateLore.add(text(""));
        DiamondChestplateLore.add(text("4 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondChestplate_im.lore(DiamondChestplateLore);
        DiamondChestplate.setItemMeta(DiamondChestplate_im);



        ItemMeta BoostOrbs_im = BoostOrb_shop.getItemMeta();
        BoostOrbs_im.setCustomModelData(1);
        BoostOrbs_im.displayName(translatable("crystalized.orb.boost.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        List<Component> BoostOrbsLore = new ArrayList<>();
        BoostOrbsLore.add(translatable("crystalized.orb.boost.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BoostOrbsLore.add(text(" "));
        BoostOrbsLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        BoostOrbs_im.lore(BoostOrbsLore);
        BoostOrb_shop.setItemMeta(BoostOrbs_im);
        ItemMeta BoostOrb_im = BoostOrb.getItemMeta();
        BoostOrb_im.setCustomModelData(1);
        BoostOrb_im.displayName(translatable("crystalized.orb.boost.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        List<Component> BoostOrbLore = new ArrayList<>();
        BoostOrbLore.add(translatable("crystalized.orb.boost.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BoostOrb_im.lore(BoostOrbLore);
        BoostOrb.setItemMeta(BoostOrb_im);


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
