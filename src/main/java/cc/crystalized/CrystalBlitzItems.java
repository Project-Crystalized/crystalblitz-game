package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class CrystalBlitzItems {
    //ItemStacks with the _item suffix are the items you get when clicking in shop. This is to Make the lore different and prevent a bug where you can buy items in your inventory
    //Some Items are unused currently but have references here. If you're using IntelliJ, Purple/Pink names are used but Greyed out names aren't

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
    public static ItemStack BreezeDagger = new ItemStack(Material.STONE_SWORD);
    public static ItemStack BreezeDagger_item = new ItemStack(Material.STONE_SWORD);
    public static ItemStack Bow = new ItemStack(Material.BOW);
    public static ItemStack Bow_item = new ItemStack(Material.BOW);
    public static ItemStack MarksmanBow = new ItemStack(Material.BOW);
    public static ItemStack MarksmanBow_item = new ItemStack(Material.BOW);
    public static ItemStack ChargedCrossbow = new ItemStack(Material.CROSSBOW);
    public static ItemStack ChargedCrossbow_item = new ItemStack(Material.CROSSBOW);
    public static ItemStack ExplosiveOrb = new ItemStack(Material.COAL); //TODO, remove explosion destroying blocks before releasing this item
    public static ItemStack ExplosiveOrb_item = new ItemStack(Material.COAL);
    public static ItemStack GrapplingOrb = new ItemStack(Material.COAL);
    public static ItemStack GrapplingOrb_item = new ItemStack(Material.COAL);
    public static ItemStack WingedOrb = new ItemStack(Material.COAL);
    public static ItemStack WingedOrb_item = new ItemStack(Material.COAL);
    public static ItemStack PoisonOrb = new ItemStack(Material.COAL);
    public static ItemStack PoisonOrb_item = new ItemStack(Material.COAL);

    //Defence
    public static ItemStack ConcreteBlocks = new ItemStack(Material.TERRACOTTA);
    public static ItemStack CopperBlocks = new ItemStack(Material.COPPER_BLOCK);
    public static ItemStack WoolBlocks = new ItemStack(Material.WHITE_WOOL);
    public static ItemStack ChainmailChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
    public static ItemStack IronChestplate = new ItemStack(Material.IRON_CHESTPLATE);
    public static ItemStack DiamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
    public static ItemStack AntiAirTotem = new ItemStack(Material.COAL);
    public static ItemStack AntiAirTotem_item = new ItemStack(Material.COAL);
    public static ItemStack CloudTotem = new ItemStack(Material.COAL);
    public static ItemStack CloudTotem_item = new ItemStack(Material.COAL);
    public static ItemStack DefenceTotem = new ItemStack(Material.COAL);
    public static ItemStack DefenceTotem_item = new ItemStack(Material.COAL);
    public static ItemStack HealingTotem = new ItemStack(Material.COAL);
    public static ItemStack HealingTotem_item = new ItemStack(Material.COAL);
    public static ItemStack LaunchTotem = new ItemStack(Material.COAL);
    public static ItemStack LaunchTotem_item = new ItemStack(Material.COAL);
    public static ItemStack SlimeTotem = new ItemStack(Material.COAL);
    public static ItemStack SlimeTotem_item = new ItemStack(Material.COAL);

    //Utility
    public static ItemStack BoostOrb = new ItemStack(Material.COAL);
    public static ItemStack BoostOrb_item = new ItemStack(Material.COAL);
    public static ItemStack BridgeOrb = new ItemStack(Material.COAL);
    public static ItemStack BridgeOrb_item = new ItemStack(Material.COAL);
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
        List<ComponentLike> PoisonOrbLoreValues = new ArrayList<>();
        PoisonOrbLoreValues.add(text("4"));
        PoisonOrbLoreValues.add(text("5"));

        //These may be ordered incorrectly, Im too lazy to fix it and its not a big deal
        //Offence
        ItemMeta WoodenSword_im = WoodenSword.getItemMeta();
        List<Component> WoodenSwordLore = new ArrayList<>();
        WoodenSwordLore.add(translatable("crystalized.sword.wood.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        WoodenSword_im.lore(WoodenSwordLore);
        WoodenSword_im.setUnbreakable(true);
        WoodenSword.setItemMeta(WoodenSword_im);

        ItemMeta WoodenPickaxe_im = WoodenPickaxe.getItemMeta();
        List<Component> WoodenPickaxeLore = new ArrayList<>();
        WoodenPickaxeLore.add(translatable("crystalized.item.pickaxe.wooden.desc1").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        WoodenPickaxeLore.add(translatable("crystalized.item.pickaxe.wooden.desc2").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        WoodenPickaxe_im.lore(WoodenPickaxeLore);
        WoodenPickaxe_im.setUnbreakable(true);
        WoodenPickaxe.setItemMeta(WoodenPickaxe_im);

        ItemMeta StoneSword_im = StoneSword.getItemMeta();
        List<Component> StoneSwordLore = new ArrayList<>();
        StoneSwordLore.add(translatable("crystalized.sword.stone.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StoneSwordLore.add(text(" "));
        StoneSwordLore.add(text("20 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        StoneSword_im.lore(StoneSwordLore);
        StoneSword_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        StoneSword.setItemMeta(StoneSword_im);
        StoneSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta StoneSworditem_im = StoneSword_item.getItemMeta();
        List<Component> StoneSworditemLore = new ArrayList<>();
        StoneSworditemLore.add(translatable("crystalized.sword.stone.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StoneSworditem_im.lore(StoneSworditemLore);
        StoneSworditem_im.setUnbreakable(true);
        StoneSword_item.setItemMeta(StoneSworditem_im);
        StoneSword_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta StonePickaxe_im = StonePickaxe.getItemMeta();
        List<Component> StonePickaxeLore = new ArrayList<>();
        StonePickaxeLore.add(translatable("crystalized.item.pickaxe.stone.desc1").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StonePickaxeLore.add(translatable("crystalized.item.pickaxe.stone.desc2").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StonePickaxeLore.add(text(" "));
        StonePickaxeLore.add(text("20 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        StonePickaxe_im.lore(StonePickaxeLore);
        StonePickaxe_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        StonePickaxe.setItemMeta(StonePickaxe_im);
        StonePickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta StonePickaxeitem_im = StonePickaxe_item.getItemMeta();
        List<Component> StonePickaxeitemLore = new ArrayList<>();
        StonePickaxeitemLore.add(translatable("crystalized.item.pickaxe.stone.desc1").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StonePickaxeitemLore.add(translatable("crystalized.item.pickaxe.stone.desc2").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        StonePickaxeitem_im.lore(StonePickaxeitemLore);
        StonePickaxeitem_im.setUnbreakable(true);
        StonePickaxe_item.setItemMeta(StonePickaxeitem_im);
        StonePickaxe_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta IronSword_im = IronSword.getItemMeta();
        List<Component> IronSwordLore = new ArrayList<>();
        IronSwordLore.add(translatable("crystalized.sword.iron.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronSwordLore.add(text(""));
        IronSwordLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronSword_im.lore(IronSwordLore);
        IronSword_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        IronSword.setItemMeta(IronSword_im);
        IronSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta IronSworditem_im = IronSword_item.getItemMeta();
        List<Component> IronSworditemLore = new ArrayList<>();
        IronSworditemLore.add(translatable("crystalized.sword.iron.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronSworditem_im.lore(IronSworditemLore);
        IronSworditem_im.setUnbreakable(true);
        IronSword_item.setItemMeta(IronSworditem_im);
        IronSword_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta IronPickaxe_im = IronPickaxe.getItemMeta();
        List<Component> IronPickaxeLore = new ArrayList<>();
        IronPickaxeLore.add(translatable("crystalized.item.pickaxe.iron.desc1").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronPickaxeLore.add(translatable("crystalized.item.pickaxe.iron.desc2").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronPickaxeLore.add(text(""));
        IronPickaxeLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronPickaxe_im.lore(IronPickaxeLore);
        IronPickaxe_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        IronPickaxe.setItemMeta(IronPickaxe_im);
        IronPickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta IronPickaxeitem_im = IronPickaxe_item.getItemMeta();
        List<Component> IronPickaxeitemLore = new ArrayList<>();
        IronPickaxeitemLore.add(translatable("crystalized.item.pickaxe.iron.desc1").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronPickaxeitemLore.add(translatable("crystalized.item.pickaxe.iron.desc2").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronPickaxeitem_im.lore(IronPickaxeitemLore);
        IronPickaxeitem_im.setUnbreakable(true);
        IronPickaxe_item.setItemMeta(IronPickaxeitem_im);
        IronPickaxe_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);


        ItemMeta DiamondSword_im = DiamondSword.getItemMeta();
        List<Component> DiamondSwordLore = new ArrayList<>();
        DiamondSwordLore.add(translatable("crystalized.sword.diamond.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondSwordLore.add(text(""));
        DiamondSwordLore.add(text("2 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondSword_im.lore(DiamondSwordLore);
        DiamondSword_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        DiamondSword.setItemMeta(DiamondSword_im);
        DiamondSword.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta DiamondSworditem_im = DiamondSword_item.getItemMeta();
        List<Component> DiamondSworditemLore = new ArrayList<>();
        DiamondSworditemLore.add(translatable("crystalized.sword.diamond.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondSworditem_im.lore(DiamondSworditemLore);
        DiamondSworditem_im.setUnbreakable(true);
        DiamondSword_item.setItemMeta(DiamondSworditem_im);
        DiamondSword_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta DiamondPickaxe_im = DiamondPickaxe.getItemMeta();
        List<Component> DiamondPickaxeLore = new ArrayList<>();
        DiamondPickaxeLore.add(translatable("crystalized.item.pickaxe.diamond.desc1").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondPickaxeLore.add(translatable("crystalized.item.pickaxe.diamond.desc2").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondPickaxeLore.add(text(""));
        DiamondPickaxeLore.add(text("2 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondPickaxe_im.lore(DiamondPickaxeLore);
        DiamondPickaxe_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        DiamondPickaxe.setItemMeta(DiamondPickaxe_im);
        DiamondPickaxe.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta DiamondPickaxeitem_im = DiamondPickaxe_item.getItemMeta();
        List<Component> DiamondPickaxeitemLore = new ArrayList<>();
        DiamondPickaxeitemLore.add(translatable("crystalized.item.pickaxe.diamond.desc1").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondPickaxeitemLore.add(translatable("crystalized.item.pickaxe.diamond.desc2").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondPickaxeitem_im.lore(DiamondPickaxeitemLore);
        DiamondPickaxeitem_im.setUnbreakable(true);
        DiamondPickaxe_item.setItemMeta(DiamondPickaxeitem_im);
        DiamondPickaxe_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta BreezeDagger_im = BreezeDagger.getItemMeta();
        List<Component> BreezeDaggerLore = new ArrayList<>();
        BreezeDaggerLore.add(translatable("crystalized.sword.wind.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BreezeDaggerLore.add(text(""));
        BreezeDaggerLore.add(text("40 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        BreezeDagger_im.lore(BreezeDaggerLore);
        BreezeDagger_im.customName(translatable("crystalized.sword.wind.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        BreezeDagger_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        BreezeDagger_im.setItemModel(new NamespacedKey("crystalized", "breeze_dagger"));
        BreezeDagger_im.addEnchant(Enchantment.KNOCKBACK, 1, true);
        BreezeDagger.setItemMeta(BreezeDagger_im);
        BreezeDagger.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ItemMeta BreezeDaggeritem_im = BreezeDagger_item.getItemMeta();
        List<Component> BreezeDaggeritemLore = new ArrayList<>();
        BreezeDaggeritemLore.add(translatable("crystalized.sword.wind.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BreezeDaggeritem_im.lore(BreezeDaggeritemLore);
        BreezeDaggeritem_im.customName(translatable("crystalized.sword.wind.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        BreezeDaggeritem_im.setItemModel(new NamespacedKey("crystalized", "breeze_dagger"));
        BreezeDaggeritem_im.setUnbreakable(true);
        BreezeDagger_im.addEnchant(Enchantment.KNOCKBACK, 2, true);
        BreezeDagger_item.setItemMeta(BreezeDaggeritem_im);
        BreezeDagger_item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta Bow_im = Bow.getItemMeta();
        List<Component> BowLore = new ArrayList<>();
        BowLore.add(translatable("crystalized.bow.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BowLore.add(text(""));
        BowLore.add(text("25 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        Bow_im.lore(BowLore);
        Bow_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        Bow.setItemMeta(Bow_im);
        ItemMeta Bowitem_im = Bow_item.getItemMeta();
        List<Component> BowitemLore = new ArrayList<>();
        BowitemLore.add(translatable("crystalized.bow.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        Bowitem_im.lore(BowitemLore);
        Bowitem_im.setUnbreakable(true);
        Bow_item.setItemMeta(Bowitem_im);

        ItemMeta ChargedCrossbow_im = ChargedCrossbow.getItemMeta();
        List<Component> ChargedCrossbowLore = new ArrayList<>();
        ChargedCrossbowLore.add(translatable("crystalized.crossbow.charged.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ChargedCrossbowLore.add(text(""));
        ChargedCrossbowLore.add(text("4 Nexus Shards & 20 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ChargedCrossbow_im.lore(ChargedCrossbowLore);
        ChargedCrossbow_im.customName(translatable("crystalized.crossbow.charged.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ChargedCrossbow_im.setItemModel(new NamespacedKey("crystalized", "charged_crossbow"));
        ChargedCrossbow_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        ChargedCrossbow.setItemMeta(ChargedCrossbow_im);
        ItemMeta ChargedCrossbowitem_im = ChargedCrossbow_item.getItemMeta();
        List<Component> ChargedCrossbowitemLore = new ArrayList<>();
        ChargedCrossbowitemLore.add(translatable("crystalized.crossbow.charged.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ChargedCrossbowitem_im.lore(ChargedCrossbowitemLore);
        ChargedCrossbowitem_im.customName(translatable("crystalized.crossbow.charged.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ChargedCrossbowitem_im.setItemModel(new NamespacedKey("crystalized", "charged_crossbow"));
        ChargedCrossbowitem_im.setUnbreakable(true);
        ChargedCrossbow_item.setItemMeta(ChargedCrossbowitem_im);

        ItemMeta ExplosiveOrb_im = ExplosiveOrb.getItemMeta();
        List<Component> ExplosiveOrbLore = new ArrayList<>();
        ExplosiveOrbLore.add(translatable("crystalized.orb.explosive.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ExplosiveOrbLore.add(text(""));
        ExplosiveOrbLore.add(text("price here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ExplosiveOrb_im.lore(ExplosiveOrbLore);
        ExplosiveOrb_im.customName(translatable("crystalized.orb.explosive.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ExplosiveOrb_im.setItemModel(new NamespacedKey("crystalized", "explosive_orb"));
        ExplosiveOrb.setItemMeta(ExplosiveOrb_im);
        ItemMeta ExplosiveOrbitem_im = ExplosiveOrb_item.getItemMeta();
        List<Component> ExplosiveOrbitemLore = new ArrayList<>();
        ExplosiveOrbitemLore.add(translatable("crystalized.orb.explosive.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ExplosiveOrbitem_im.lore(ExplosiveOrbitemLore);
        ExplosiveOrbitem_im.customName(translatable("crystalized.orb.explosive.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ExplosiveOrbitem_im.setItemModel(new NamespacedKey("crystalized", "explosive_orb"));
        ExplosiveOrb_item.setItemMeta(ExplosiveOrbitem_im);

        ItemMeta GrapplingOrb_im = GrapplingOrb.getItemMeta();
        List<Component> GrapplingOrbLore = new ArrayList<>();
        GrapplingOrbLore.add(translatable("crystalized.orb.grappling.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        GrapplingOrbLore.add(text(""));
        GrapplingOrbLore.add(text("price here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        GrapplingOrb_im.lore(GrapplingOrbLore);
        GrapplingOrb_im.customName(translatable("crystalized.orb.grappling.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        GrapplingOrb_im.setItemModel(new NamespacedKey("crystalized", "grappling_orb"));
        GrapplingOrb.setItemMeta(GrapplingOrb_im);
        ItemMeta GrapplingOrbitem_im = GrapplingOrb_item.getItemMeta();
        List<Component> GrapplingOrbitemLore = new ArrayList<>();
        GrapplingOrbitemLore.add(translatable("crystalized.orb.grappling.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        GrapplingOrbitem_im.lore(GrapplingOrbitemLore);
        GrapplingOrbitem_im.customName(translatable("crystalized.orb.grappling.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        GrapplingOrbitem_im.setItemModel(new NamespacedKey("crystalized", "grappling_orb"));
        GrapplingOrb_item.setItemMeta(GrapplingOrbitem_im);


        //Defence
        ItemMeta ConcreteBlocks_im = ConcreteBlocks.getItemMeta();
        List<Component> ConcreteBlocksLore = new ArrayList<>();
        ConcreteBlocksLore.add(text("8 Building Blocks").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
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

        ItemMeta ChainmailChestplate_im = ChainmailChestplate.getItemMeta();
        List<Component> ChainmailChestplateLore = new ArrayList<>();
        ChainmailChestplateLore.add(text("description").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        ChainmailChestplateLore.add(text(""));
        ChainmailChestplateLore.add(text("40 Weak Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        ChainmailChestplate_im.lore(ChainmailChestplateLore);
        ChainmailChestplate_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        ChainmailChestplate.setItemMeta(ChainmailChestplate_im);

        ItemMeta IronChestplate_im = IronChestplate.getItemMeta();
        List<Component> IronChestplateLore = new ArrayList<>();
        IronChestplateLore.add(text("description").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        IronChestplateLore.add(text(""));
        IronChestplateLore.add(text("10 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        IronChestplate_im.lore(IronChestplateLore);
        IronChestplate_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        IronChestplate.setItemMeta(IronChestplate_im);

        ItemMeta DiamondChestplate_im = DiamondChestplate.getItemMeta();
        List<Component> DiamondChestplateLore = new ArrayList<>();
        DiamondChestplateLore.add(text("description").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DiamondChestplateLore.add(text(""));
        DiamondChestplateLore.add(text("4 Nexus Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DiamondChestplate_im.lore(DiamondChestplateLore);
        DiamondChestplate_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        DiamondChestplate.setItemMeta(DiamondChestplate_im);

        ItemMeta AntiAirTotem_im = AntiAirTotem.getItemMeta();
        List<Component> AntiAirTotemLore = new ArrayList<>();
        AntiAirTotemLore.add(translatable("crystalized.totem.antiair.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        AntiAirTotemLore.add(text(""));
        AntiAirTotemLore.add(text("price here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        AntiAirTotem_im.lore(AntiAirTotemLore);
        AntiAirTotem_im.customName(translatable("crystalized.totem.antiair.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        AntiAirTotem_im.setItemModel(new NamespacedKey("crystalized", "antiair_totem"));
        AntiAirTotem.setItemMeta(AntiAirTotem_im);
        ItemMeta AntiAirTotemitem_im = AntiAirTotem_item.getItemMeta();
        List<Component> AntiAirTotemitemLore = new ArrayList<>();
        AntiAirTotemitemLore.add(translatable("crystalized.totem.antiair.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        AntiAirTotemitem_im.lore(AntiAirTotemitemLore);
        AntiAirTotemitem_im.customName(translatable("crystalized.totem.antiair.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        AntiAirTotemitem_im.setItemModel(new NamespacedKey("crystalized", "antiair_totem"));
        AntiAirTotem_item.setItemMeta(AntiAirTotemitem_im);

        ItemMeta CloudTotem_im = CloudTotem.getItemMeta();
        List<Component> CloudTotemLore = new ArrayList<>();
        CloudTotemLore.add(translatable("crystalized.totem.cloud.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        CloudTotemLore.add(text(""));
        CloudTotemLore.add(text("price here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        CloudTotem_im.lore(CloudTotemLore);
        CloudTotem_im.customName(translatable("crystalized.totem.cloud.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        CloudTotem_im.setItemModel(new NamespacedKey("crystalized", "cloud_totem"));
        CloudTotem.setItemMeta(CloudTotem_im);
        ItemMeta CloudTotemitem_im = CloudTotem_item.getItemMeta();
        List<Component> CloudTotemitemLore = new ArrayList<>();
        CloudTotemitemLore.add(translatable("crystalized.totem.cloud.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        CloudTotemitem_im.lore(CloudTotemitemLore);
        CloudTotemitem_im.customName(translatable("crystalized.totem.cloud.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        CloudTotemitem_im.setItemModel(new NamespacedKey("crystalized", "cloud_totem"));
        CloudTotem_item.setItemMeta(CloudTotemitem_im);

        ItemMeta DefenceTotem_im = DefenceTotem.getItemMeta();
        List<Component> DefenceTotemLore = new ArrayList<>();
        DefenceTotemLore.add(translatable("crystalized.totem.defence.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DefenceTotemLore.add(text(""));
        DefenceTotemLore.add(text("price here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DefenceTotem_im.lore(DefenceTotemLore);
        DefenceTotem_im.customName(translatable("crystalized.totem.defence.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DefenceTotem_im.setItemModel(new NamespacedKey("crystalized", "defence_totem"));
        DefenceTotem.setItemMeta(DefenceTotem_im);
        ItemMeta DefenceTotemitem_im = DefenceTotem_item.getItemMeta();
        List<Component> DefenceTotemitemLore = new ArrayList<>();
        DefenceTotemitemLore.add(translatable("crystalized.totem.defence.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        DefenceTotemitem_im.lore(DefenceTotemitemLore);
        DefenceTotemitem_im.customName(translatable("crystalized.totem.defence.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        DefenceTotemitem_im.setItemModel(new NamespacedKey("crystalized", "defence_totem"));
        DefenceTotem_item.setItemMeta(DefenceTotemitem_im);

        ItemMeta HealingTotem_im = HealingTotem.getItemMeta();
        List<Component> HealingTotemLore = new ArrayList<>();
        HealingTotemLore.add(translatable("crystalized.totem.healing.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        HealingTotemLore.add(text(""));
        HealingTotemLore.add(text("price here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        HealingTotem_im.lore(HealingTotemLore);
        HealingTotem_im.customName(translatable("crystalized.totem.healing.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        HealingTotem_im.setItemModel(new NamespacedKey("crystalized", "healing_totem"));
        HealingTotem.setItemMeta(HealingTotem_im);
        ItemMeta HealingTotemitem_im = HealingTotem_item.getItemMeta();
        List<Component> HealingTotemitemLore = new ArrayList<>();
        HealingTotemitemLore.add(translatable("crystalized.totem.healing.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        HealingTotemitem_im.lore(HealingTotemitemLore);
        HealingTotemitem_im.customName(translatable("crystalized.totem.healing.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        HealingTotemitem_im.setItemModel(new NamespacedKey("crystalized", "healing_totem"));
        HealingTotem_item.setItemMeta(HealingTotemitem_im);

        ItemMeta LaunchTotem_im = LaunchTotem.getItemMeta();
        List<Component> LaunchTotemLore = new ArrayList<>();
        LaunchTotemLore.add(translatable("crystalized.totem.launch.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        LaunchTotemLore.add(text(""));
        LaunchTotemLore.add(text("price here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        LaunchTotem_im.lore(LaunchTotemLore);
        LaunchTotem_im.customName(translatable("crystalized.totem.launch.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        LaunchTotem_im.setItemModel(new NamespacedKey("crystalized", "launch_totem"));
        LaunchTotem.setItemMeta(LaunchTotem_im);
        ItemMeta LaunchTotemitem_im = LaunchTotem_item.getItemMeta();
        List<Component> LaunchTotemitemLore = new ArrayList<>();
        LaunchTotemitemLore.add(translatable("crystalized.totem.launch.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        LaunchTotemitem_im.lore(LaunchTotemitemLore);
        LaunchTotemitem_im.customName(translatable("crystalized.totem.launch.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        LaunchTotemitem_im.setItemModel(new NamespacedKey("crystalized", "launch_totem"));
        LaunchTotem_item.setItemMeta(LaunchTotemitem_im);

        ItemMeta SlimeTotem_im = SlimeTotem.getItemMeta();
        List<Component> SlimeTotemLore = new ArrayList<>();
        SlimeTotemLore.add(translatable("crystalized.totem.slime.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        SlimeTotemLore.add(text(""));
        SlimeTotemLore.add(text("price here").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        SlimeTotem_im.lore(SlimeTotemLore);
        SlimeTotem_im.customName(translatable("crystalized.totem.slime.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        SlimeTotem_im.setItemModel(new NamespacedKey("crystalized", "slime_totem"));
        SlimeTotem.setItemMeta(SlimeTotem_im);
        ItemMeta SlimeTotemitem_im = SlimeTotem_item.getItemMeta();
        List<Component> SlimeTotemitemLore = new ArrayList<>();
        SlimeTotemitemLore.add(translatable("crystalized.totem.slime.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        SlimeTotemitem_im.lore(SlimeTotemitemLore);
        SlimeTotemitem_im.customName(translatable("crystalized.totem.slime.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        SlimeTotemitem_im.setItemModel(new NamespacedKey("crystalized", "slime_totem"));
        SlimeTotem_item.setItemMeta(SlimeTotemitem_im);


        //Utility
        ItemMeta BoostOrbs_im = BoostOrb.getItemMeta();
        BoostOrbs_im.setItemModel(new NamespacedKey("crystalized", "boost_orb"));
        BoostOrbs_im.displayName(translatable("crystalized.orb.boost.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        List<Component> BoostOrbsLore = new ArrayList<>();
        BoostOrbsLore.add(translatable("crystalized.orb.boost.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BoostOrbsLore.add(text(" "));
        BoostOrbsLore.add(text("20 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        BoostOrbs_im.lore(BoostOrbsLore);
        BoostOrbs_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        BoostOrb.setItemMeta(BoostOrbs_im);
        ItemMeta BoostOrbitem_im = BoostOrb_item.getItemMeta();
        BoostOrbitem_im.setItemModel(new NamespacedKey("crystalized", "boost_orb"));
        BoostOrbitem_im.displayName(translatable("crystalized.orb.boost.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        List<Component> BoostOrbLore = new ArrayList<>();
        BoostOrbLore.add(translatable("crystalized.orb.boost.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BoostOrbitem_im.lore(BoostOrbLore);
        BoostOrb_item.setItemMeta(BoostOrbitem_im);

        ItemMeta WingedOrb_im = WingedOrb.getItemMeta();
        WingedOrb_im.setItemModel(new NamespacedKey("crystalized", "winged_orb"));
        WingedOrb_im.displayName(translatable("crystalized.orb.winged.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        List<Component> WingedOrbLore = new ArrayList<>();
        WingedOrbLore.add(translatable("crystalized.orb.winged.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        WingedOrbLore.add(text(""));
        WingedOrbLore.add(text("35 Strong Shards").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        WingedOrb_im.lore(WingedOrbLore);
        WingedOrb_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        WingedOrb.setItemMeta(WingedOrb_im);
        ItemMeta WingedOrbitem_im = WingedOrb_item.getItemMeta();
        WingedOrbitem_im.setItemModel(new NamespacedKey("crystalized", "winged_orb"));
        WingedOrbitem_im.displayName(translatable("crystalized.orb.winged.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        List<Component> WingedOrbitemLore = new ArrayList<>();
        WingedOrbitemLore.add(translatable("crystalized.orb.winged.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        WingedOrbitem_im.lore(WingedOrbitemLore);
        WingedOrb_item.setItemMeta(WingedOrbitem_im);

        ItemMeta PoisonOrb_im = PoisonOrb.getItemMeta();
        List<Component> PoisonOrbLore = new ArrayList<>();
        PoisonOrbLore.add(translatable("crystalized.orb.poison.desc", PoisonOrbLoreValues).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        PoisonOrbLore.add(text(""));
        PoisonOrbLore.add(text("price here"));
        PoisonOrb_im.lore(PoisonOrbLore);
        PoisonOrb_im.customName(translatable("crystalized.orb.poison.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        PoisonOrb_im.setItemModel(new NamespacedKey("crystalized", "poison_orb"));
        PoisonOrb.setItemMeta(PoisonOrb_im);
        ItemMeta PoisonOrbitem_im = PoisonOrb_item.getItemMeta();
        List<Component> PoisonOrbitemLore = new ArrayList<>();
        PoisonOrbitemLore.add(translatable("crystalized.orb.poison.desc", PoisonOrbLoreValues).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        PoisonOrbitem_im.lore(PoisonOrbitemLore);
        PoisonOrbitem_im.customName(translatable("crystalized.orb.poison.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        PoisonOrbitem_im.setItemModel(new NamespacedKey("crystalized", "poison_orb"));
        PoisonOrb_item.setItemMeta(PoisonOrbitem_im);

        ItemMeta BridgeOrb_im = BridgeOrb.getItemMeta();
        List<Component> BridgeOrbLore = new ArrayList<>();
        BridgeOrbLore.add(translatable("crystalized.orb.bridge.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BridgeOrbLore.add(text(""));
        BridgeOrbLore.add(text("price here"));
        BridgeOrb_im.lore(BridgeOrbLore);
        BridgeOrb_im.customName(translatable("crystalized.orb.bridge.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        BridgeOrb_im.setItemModel(new NamespacedKey("crystalized", "bridge_orb"));
        BridgeOrb.setItemMeta(BridgeOrb_im);
        ItemMeta BridgeOrbitem_im = BridgeOrb_item.getItemMeta();
        List<Component> BridgeOrbitemLore = new ArrayList<>();
        BridgeOrbitemLore.add(translatable("crystalized.orb.bridge.desc").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        BridgeOrbitem_im.lore(BridgeOrbitemLore);
        BridgeOrbitem_im.customName(translatable("crystalized.orb.bridge.name").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE));
        BridgeOrbitem_im.setItemModel(new NamespacedKey("crystalized", "bridge_orb"));
        BridgeOrb_item.setItemMeta(BridgeOrbitem_im);


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
        WeakShard_im.setItemModel(new NamespacedKey("crystalized", "weak_shard"));
        WeakShard_im.setTooltipStyle(new NamespacedKey("crystalized", "weak_shard"));
        WeakShard.setItemMeta(WeakShard_im);

        ItemMeta StrongShard_im = StrongShard.getItemMeta();
        StrongShard_im.customName(translatable("crystalized.item.stoneshard.name").decoration(TextDecoration.ITALIC, false));
        StrongShard_im.setItemModel(new NamespacedKey("crystalized", "strong_shard"));
        StrongShard_im.setTooltipStyle(new NamespacedKey("crystalized", "strong_shard"));
        StrongShard.setItemMeta(StrongShard_im);

        ItemMeta NexusShard_im = NexusShard.getItemMeta();
        NexusShard_im.customName(translatable("crystalized.item.nexusshard.name").decoration(TextDecoration.ITALIC, false));
        NexusShard_im.setItemModel(new NamespacedKey("crystalized", "nexus_shard"));
        NexusShard_im.setTooltipStyle(new NamespacedKey("crystalized", "nexus_shard"));
        NexusShard.setItemMeta(NexusShard_im);
    }
}
