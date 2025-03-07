package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class CrystalBlitzItems {
    //ItemStacks with the _item suffix are the items you get when clicking in shop. This is to Make the lore different and prevent a bug where you can buy items in your inventory
    //Some Items are unused currently but have references here (eg, PoisonOrb). If you're using IntelliJ, Purple names are used but Greyed out names aren't

    //Offence
    public static ItemStack WoodenSword = new ItemStack(Material.WOODEN_SWORD); //This
    public static ItemStack WoodenPickaxe = new ItemStack(Material.WOODEN_PICKAXE); //and this are exceptions to the first comment in this class
    public static ItemStack StoneSword = new ItemStack(Material.STONE_SWORD);
    public static ItemStack StoneSword_item = new ItemStack(Material.STONE_SWORD);
    public static ItemStack StonePickaxe = new ItemStack(Material.STONE_PICKAXE);
    public static ItemStack StonePickaxe_item = new ItemStack(Material.STONE_PICKAXE);
    public static ItemStack IronSword = new ItemStack(Material.IRON_SWORD);
    public static ItemStack IronSword_item = new ItemStack(Material.IRON_SWORD);
    public static ItemStack IronPickaxe = new ItemStack(Material.IRON_PICKAXE);
    public static ItemStack IronPickaxe_item = new ItemStack(Material.IRON_PICKAXE);
    public static ItemStack DiamondSword = new ItemStack(Material.DIAMOND_SWORD);
    public static ItemStack DiamondSword_item = new ItemStack(Material.DIAMOND_SWORD);
    public static ItemStack DiamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
    public static ItemStack DiamondPickaxe_item = new ItemStack(Material.DIAMOND_PICKAXE);
    public static ItemStack SlimeSword = new ItemStack(Material.STONE_SWORD);
    public static ItemStack PufferfishSword = new ItemStack(Material.STONE_SWORD);
    public static ItemStack Bow = new ItemStack(Material.BOW);
    public static ItemStack Bow_item = new ItemStack(Material.BOW);
    public static ItemStack MarksmanBow = new ItemStack(Material.BOW);
    public static ItemStack RicochetBow = new ItemStack(Material.BOW);
    public static ItemStack QuickChargeCrossbow = new ItemStack(Material.CROSSBOW);
    public static ItemStack MultiCrossbow = new ItemStack(Material.CROSSBOW);
    public static ItemStack ChargedCrossbow = new ItemStack(Material.CROSSBOW);
    public static ItemStack ChargedCrossbow_item = new ItemStack(Material.CROSSBOW);
    public static ItemStack ExplosiveOrb = new ItemStack(Material.COAL); //TODO, remove explosion destroying blocks before releasing this item
    public static ItemStack GrapplingOrb = new ItemStack(Material.COAL);
    public static ItemStack WingedOrb = new ItemStack(Material.COAL);
    public static ItemStack PoisonOrb = new ItemStack(Material.COAL);

    //Defence
    public static ItemStack ConcreteBlocks = new ItemStack(Material.TERRACOTTA);
    public static ItemStack CopperBlocks = new ItemStack(Material.COPPER_BLOCK);
    public static ItemStack WoolBlocks = new ItemStack(Material.WHITE_WOOL);
    public static ItemStack ChainmailChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
    public static ItemStack IronChestplate = new ItemStack(Material.IRON_CHESTPLATE);
    public static ItemStack DiamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
    public static ItemStack AntiAirTotem = new ItemStack(Material.COAL);
    public static ItemStack CloudTotem = new ItemStack(Material.COAL);
    public static ItemStack DefenceTotem = new ItemStack(Material.COAL);
    public static ItemStack HealingTotem = new ItemStack(Material.COAL);
    public static ItemStack LaunchTotem = new ItemStack(Material.COAL);
    public static ItemStack SlimeTotem = new ItemStack(Material.COAL);

    //Utility
    public static ItemStack BoostOrb = new ItemStack(Material.COAL);
    public static ItemStack BoostOrb_item = new ItemStack(Material.COAL);
    public static ItemStack BridgeOrb = new ItemStack(Material.COAL);

    public static ItemStack Gapples = new ItemStack(Material.GOLDEN_APPLE);

    //Displayed on the front page/no category
    public static ItemStack Arrows = new ItemStack(Material.ARROW);
    public static ItemStack Arrows_item = new ItemStack(Material.ARROW);
    public static ItemStack SpectralArrows = new ItemStack(Material.SPECTRAL_ARROW);
    public static ItemStack DragonArrows = new ItemStack(Material.ARROW);
    public static ItemStack ExplosiveArrows = new ItemStack(Material.ARROW);

    public static ItemStack WeakShard = new ItemStack(Material.COAL);
    public static ItemStack StrongShard = new ItemStack(Material.COAL);
    public static ItemStack NexusShard = new ItemStack(Material.COAL);

    public static void SetupItems() {
        //Offence
        ItemMeta WoodenSword_im = WoodenSword.getItemMeta();
        List<Component> WoodenSwordLore = new ArrayList<>();
        WoodenSwordLore.add(text("Your starting sword, you'll need to upgrade this at some point.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE)); //unused
        WoodenSword_im.lore(WoodenSwordLore);
        WoodenSword_im.setUnbreakable(true);
        WoodenSword.setItemMeta(WoodenSword_im);

        ItemMeta WoodenPickaxe_im = WoodenPickaxe.getItemMeta();
        List<Component> WoodenPickaxeLore = new ArrayList<>();
        WoodenPickaxeLore.add(text("Your starting pickaxe, you'll need to upgrade this at some point.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE)); //unused
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
        StoneSword_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        StoneSword.setItemMeta(StoneSword_im);
        StoneSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta StoneSworditem_im = StoneSword_item.getItemMeta();
        List<Component> StoneSworditemLore = new ArrayList<>();
        StoneSworditemLore.add(text("Stone sword description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StoneSworditem_im.lore(StoneSworditemLore);
        StoneSworditem_im.setUnbreakable(true);
        StoneSword_item.setItemMeta(StoneSworditem_im);
        StoneSword_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta StonePickaxe_im = StonePickaxe.getItemMeta();
        List<Component> StonePickaxeLore = new ArrayList<>();
        StonePickaxeLore.add(text("Stone pickaxe description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StonePickaxeLore.add(text(" "));
        StonePickaxeLore.add(text("20 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        StonePickaxe_im.lore(StonePickaxeLore);
        StonePickaxe_im.setUnbreakable(true);
        StonePickaxe_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        StonePickaxe.setItemMeta(StonePickaxe_im);
        StonePickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta StonePickaxeitem_im = StonePickaxe_item.getItemMeta();
        List<Component> StonePickaxeitemLore = new ArrayList<>();
        StonePickaxeitemLore.add(text("Stone pickaxe description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StonePickaxeitem_im.lore(StonePickaxeitemLore);
        StonePickaxeitem_im.setUnbreakable(true);
        StonePickaxe_item.setItemMeta(StonePickaxeitem_im);
        StonePickaxe_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta IronSword_im = IronSword.getItemMeta();
        List<Component> IronSwordLore = new ArrayList<>();
        IronSwordLore.add(text("Iron sword description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronSwordLore.add(text(""));
        IronSwordLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronSword_im.lore(IronSwordLore);
        IronSword_im.setUnbreakable(true);
        IronSword_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        IronSword.setItemMeta(IronSword_im);
        IronSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta IronSworditem_im = IronSword_item.getItemMeta();
        List<Component> IronSworditemLore = new ArrayList<>();
        IronSworditemLore.add(text("Iron sword description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronSworditem_im.lore(IronSworditemLore);
        IronSworditem_im.setUnbreakable(true);
        IronSword_item.setItemMeta(IronSworditem_im);
        IronSword_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta IronPickaxe_im = IronPickaxe.getItemMeta();
        List<Component> IronPickaxeLore = new ArrayList<>();
        IronPickaxeLore.add(text("Iron sword description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronPickaxeLore.add(text(""));
        IronPickaxeLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronPickaxe_im.lore(IronPickaxeLore);
        IronPickaxe_im.setUnbreakable(true);
        IronPickaxe_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        IronPickaxe.setItemMeta(IronPickaxe_im);
        IronPickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta IronPickaxeitem_im = IronPickaxe_item.getItemMeta();
        List<Component> IronPickaxeitemLore = new ArrayList<>();
        IronPickaxeitemLore.add(text("Iron sword description here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronPickaxeitem_im.lore(IronPickaxeitemLore);
        IronPickaxeitem_im.setUnbreakable(true);
        IronPickaxe_item.setItemMeta(IronPickaxeitem_im);
        IronPickaxe_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);


        ItemMeta DiamondSword_im = DiamondSword.getItemMeta();
        List<Component> DiamondSwordLore = new ArrayList<>();
        DiamondSwordLore.add(text("most powerful sword").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondSwordLore.add(text(""));
        DiamondSwordLore.add(text("2 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondSword_im.lore(DiamondSwordLore);
        DiamondSword_im.setUnbreakable(true);
        DiamondSword_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        DiamondSword.setItemMeta(DiamondSword_im);
        DiamondSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta DiamondSworditem_im = DiamondSword_item.getItemMeta();
        List<Component> DiamondSworditemLore = new ArrayList<>();
        DiamondSworditemLore.add(text("most powerful sword").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondSworditem_im.lore(DiamondSworditemLore);
        DiamondSworditem_im.setUnbreakable(true);
        DiamondSword_item.setItemMeta(DiamondSworditem_im);
        DiamondSword_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta DiamondPickaxe_im = DiamondPickaxe.getItemMeta();
        List<Component> DiamondPickaxeLore = new ArrayList<>();
        DiamondPickaxeLore.add(text("most powerful pickaxe").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondPickaxeLore.add(text(""));
        DiamondPickaxeLore.add(text("2 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondPickaxe_im.lore(DiamondPickaxeLore);
        DiamondPickaxe_im.setUnbreakable(true);
        DiamondPickaxe_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        DiamondPickaxe.setItemMeta(DiamondPickaxe_im);
        DiamondPickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta DiamondPickaxeitem_im = DiamondPickaxe_item.getItemMeta();
        List<Component> DiamondPickaxeitemLore = new ArrayList<>();
        DiamondPickaxeitemLore.add(text("most powerful pickaxe").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondPickaxeitem_im.lore(DiamondPickaxeitemLore);
        DiamondPickaxeitem_im.setUnbreakable(true);
        DiamondPickaxe_item.setItemMeta(DiamondPickaxeitem_im);
        DiamondPickaxe_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta Bow_im = Bow.getItemMeta();
        List<Component> BowLore = new ArrayList<>();
        BowLore.add(text("pew pew thingy idfk").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BowLore.add(text(""));
        BowLore.add(text("25 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        Bow_im.lore(BowLore);
        Bow_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        Bow.setItemMeta(Bow_im);
        ItemMeta Bowitem_im = Bow_item.getItemMeta();
        List<Component> BowitemLore = new ArrayList<>();
        BowitemLore.add(text("pew pew thingy idfk").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BowitemLore.add(text(""));
        BowitemLore.add(text("25 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        Bowitem_im.lore(BowitemLore);
        Bowitem_im.setUnbreakable(true);
        Bowitem_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        Bow_item.setItemMeta(Bowitem_im);

        ItemMeta ChargedCrossbow_im = ChargedCrossbow.getItemMeta();
        List<Component> ChargedCrossbowLore = new ArrayList<>();
        ChargedCrossbowLore.add(translatable("crystalized.crossbow.charged.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ChargedCrossbowLore.add(text(""));
        ChargedCrossbowLore.add(text("6 Nexus Shards & 20 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ChargedCrossbow_im.lore(ChargedCrossbowLore);
        ChargedCrossbow_im.customName(translatable("crystalized.crossbow.charged.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ChargedCrossbow_im.setCustomModelData(3);
        ChargedCrossbow_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        ChargedCrossbow.setItemMeta(ChargedCrossbow_im);
        ItemMeta ChargedCrossbowitem_im = ChargedCrossbow_item.getItemMeta();
        List<Component> ChargedCrossbowitemLore = new ArrayList<>();
        ChargedCrossbowitemLore.add(translatable("crystalized.crossbow.charged.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ChargedCrossbowitem_im.lore(ChargedCrossbowitemLore);
        ChargedCrossbowitem_im.customName(translatable("crystalized.crossbow.charged.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ChargedCrossbowitem_im.setCustomModelData(3);
        ChargedCrossbowitem_im.setUnbreakable(true);
        ChargedCrossbow_item.setItemMeta(ChargedCrossbowitem_im);


        //Defence
        ItemMeta ConcreteBlocks_im = ConcreteBlocks.getItemMeta();
        List<Component> ConcreteBlocksLore = new ArrayList<>();
        ConcreteBlocksLore.add(text("16 Building Blocks").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ConcreteBlocksLore.add(text(""));
        ConcreteBlocksLore.add(text("8 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ConcreteBlocks_im.lore(ConcreteBlocksLore);
        ConcreteBlocks_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        ConcreteBlocks.setAmount(16);
        ConcreteBlocks.setItemMeta(ConcreteBlocks_im);

        ItemMeta CopperBlocks_im = CopperBlocks.getItemMeta();
        List<Component> CopperBlocksLore = new ArrayList<>();
        CopperBlocksLore.add(text("8 Building Blocks").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        CopperBlocksLore.add(text(""));
        CopperBlocksLore.add(text("10 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        CopperBlocks_im.lore(CopperBlocksLore);
        CopperBlocks_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        CopperBlocks.setAmount(8);
        CopperBlocks.setItemMeta(CopperBlocks_im);

        ItemMeta WoolBlocks_im = WoolBlocks.getItemMeta();
        List<Component> WoolBlocksLore = new ArrayList<>();
        WoolBlocksLore.add(text("8 Building Blocks").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        WoolBlocksLore.add(text(""));
        WoolBlocksLore.add(text("8 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        WoolBlocks_im.lore(WoolBlocksLore);
        WoolBlocks_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        WoolBlocks.setAmount(8);
        WoolBlocks.setItemMeta(WoolBlocks_im);

        ItemMeta IronChestplate_im = IronChestplate.getItemMeta();
        List<Component> IronChestplateLore = new ArrayList<>();
        IronChestplateLore.add(text("description").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronChestplateLore.add(text(""));
        IronChestplateLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronChestplate_im.lore(IronChestplateLore);
        IronChestplate_im.setUnbreakable(true);
        IronChestplate_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        IronChestplate.setItemMeta(IronChestplate_im);

        ItemMeta DiamondChestplate_im = DiamondChestplate.getItemMeta();
        List<Component> DiamondChestplateLore = new ArrayList<>();
        DiamondChestplateLore.add(text("description").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondChestplateLore.add(text(""));
        DiamondChestplateLore.add(text("4 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondChestplate_im.lore(DiamondChestplateLore);
        DiamondChestplate_im.setUnbreakable(true);
        DiamondChestplate_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        DiamondChestplate.setItemMeta(DiamondChestplate_im);


        //Utility
        ItemMeta BoostOrbs_im = BoostOrb.getItemMeta();
        BoostOrbs_im.setCustomModelData(1);
        BoostOrbs_im.displayName(translatable("crystalized.orb.boost.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        List<Component> BoostOrbsLore = new ArrayList<>();
        BoostOrbsLore.add(translatable("crystalized.orb.boost.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BoostOrbsLore.add(text(" "));
        BoostOrbsLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        BoostOrbs_im.lore(BoostOrbsLore);
        BoostOrbs_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        BoostOrb.setItemMeta(BoostOrbs_im);
        ItemMeta BoostOrbitem_im = BoostOrb_item.getItemMeta();
        BoostOrbitem_im.setCustomModelData(1);
        BoostOrbitem_im.displayName(translatable("crystalized.orb.boost.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        List<Component> BoostOrbLore = new ArrayList<>();
        BoostOrbLore.add(translatable("crystalized.orb.boost.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BoostOrbitem_im.lore(BoostOrbLore);
        BoostOrb_item.setItemMeta(BoostOrbitem_im);

        ItemMeta Gapples_im = Gapples.getItemMeta();
        List<Component> GapplesLore = new ArrayList<>();
        GapplesLore.add(text("1 Gapple").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        GapplesLore.add(text(""));
        GapplesLore.add(text("4 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        Gapples_im.lore(GapplesLore);
        Gapples_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        Gapples.setItemMeta(Gapples_im);


        //Other/no category
        ItemMeta Arrows_im = Arrows.getItemMeta();
        List<Component> ArrowsLore = new ArrayList<>();
        ArrowsLore.add(text("4 arrows").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ArrowsLore.add(text(""));
        ArrowsLore.add(text("2 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        Arrows_im.lore(ArrowsLore);
        Arrows_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        Arrows.setItemMeta(Arrows_im);
        Arrows.setAmount(4);
        ItemMeta Arrowsitem_im = Arrows_item.getItemMeta();
        Arrows_item.setItemMeta(Arrowsitem_im);
        Arrows_item.setAmount(4);

        ItemMeta WeakShard_im = WeakShard.getItemMeta();
        WeakShard_im.customName(translatable("crystalized.item.weakshard.name").decoration(TextDecoration.ITALIC, false));
        WeakShard_im.setCustomModelData(17);
        WeakShard_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        WeakShard.setItemMeta(WeakShard_im);

        ItemMeta StrongShard_im = StrongShard.getItemMeta();
        StrongShard_im.customName(translatable("crystalized.item.stoneshard.name").decoration(TextDecoration.ITALIC, false));
        StrongShard_im.setCustomModelData(18);
        StrongShard_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        StrongShard.setItemMeta(StrongShard_im);

        ItemMeta NexusShard_im = NexusShard.getItemMeta();
        NexusShard_im.customName(translatable("crystalized.item.nexusshard.name").decoration(TextDecoration.ITALIC, false));
        NexusShard_im.setCustomModelData(19);
        NexusShard_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        NexusShard.setItemMeta(NexusShard_im);
    }
}
