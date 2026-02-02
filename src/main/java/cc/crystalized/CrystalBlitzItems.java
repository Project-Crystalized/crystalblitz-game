package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class CrystalBlitzItems {

    // Just dont touch this class unless you need to, its kind of messy - Callum

    enum ItemType{
        Melee,
        Pickaxe,
        Ranged,
        Shears,
        Blocks,
        Armor,
        other,
    }

    static List<CBItem> items = new ArrayList<>();

    public static void SetupItems() {
        items.clear();

        items.add(setup("wooden_sword", Material.WOODEN_SWORD, null,
                List.of(translatable("crystalized.sword.wood.desc")),
                0, Shop.ShardTypes.Weak, ItemType.Melee,
                "wooden_sword", null)
        );
        items.add(setup("wooden_pickaxe", Material.WOODEN_PICKAXE, null,
                List.of(translatable("crystalized.item.pickaxe.wooden.desc1"), translatable("crystalized.item.pickaxe.wooden.desc2")),
                0, Shop.ShardTypes.Weak, ItemType.Pickaxe,
                "wooden_pickaxe", null)
        );
        items.add(setup("stone_sword", Material.STONE_SWORD, null,
                List.of(translatable("crystalized.sword.stone.desc")),
                20, Shop.ShardTypes.Weak, ItemType.Melee,
                "wooden_sword", List.of("stone_sword", "iron_sword", "diamond_sword", "breeze_dagger"))
        );
        items.add(setup("stone_pickaxe", Material.STONE_PICKAXE, null,
                List.of(translatable("crystalized.item.pickaxe.stone.desc1"), translatable("crystalized.item.pickaxe.stone.desc2")),
                20, Shop.ShardTypes.Weak, ItemType.Pickaxe,
                "wooden_pickaxe", List.of("stone_pickaxe"))
        );
        items.add(setup("iron_sword", Material.IRON_SWORD, null,
                List.of(translatable("crystalized.sword.iron.desc")),
                10, Shop.ShardTypes.Strong, ItemType.Melee,
                "stone_sword", List.of("iron_sword", "diamond_sword"))
        );
        items.add(setup("iron_pickaxe", Material.IRON_PICKAXE, null,
                List.of(translatable("crystalized.item.pickaxe.iron.desc1"), translatable("crystalized.item.pickaxe.iron.desc2")),
                10, Shop.ShardTypes.Strong, ItemType.Pickaxe,
                "stone_pickaxe", List.of("iron_pickaxe", "diamond_pickaxe"))
        );
        items.add(setup("diamond_sword", Material.DIAMOND_SWORD, null,
                List.of(translatable("crystalized.sword.diamond.desc")),
                8, Shop.ShardTypes.Nexus, ItemType.Melee,
                "iron_sword", List.of("diamond_sword"))
        );
        items.add(setup("diamond_pickaxe", Material.DIAMOND_PICKAXE, null,
                List.of(translatable("crystalized.item.pickaxe.diamond.desc1"), translatable("crystalized.item.pickaxe.diamond.desc2")),
                4, Shop.ShardTypes.Nexus, ItemType.Pickaxe,
                "iron_pickaxe", List.of("diamond_pickaxe"))
        );
        items.add(setup("breeze_dagger", Material.STONE_SWORD, translatable("crystalized.sword.wind.name"),
                List.of(translatable("crystalized.sword.wind.desc")),
                30, Shop.ShardTypes.Strong, ItemType.Melee,
                "stone_sword", List.of("breeze_dagger", "diamond_sword"), new NamespacedKey("crystalized", "breeze_dagger"))
        );
        items.add(setup("bow", Material.BOW, null,
                List.of(translatable("crystalized.bow.desc")),
                25, Shop.ShardTypes.Strong, ItemType.Ranged,
                "", null)
        );
        items.add(setup("marksman_bow", Material.BOW, translatable("crystalized.bow.marksman.name"),
                List.of(translatable("crystalized.bow.marksman.desc")),
                40, Shop.ShardTypes.Strong, ItemType.Ranged,
                "bow", List.of("marksman_bow"), new NamespacedKey("crystalized", "marksman_bow"))
        );
        items.add(setup("explosive_bow", Material.BOW, translatable("crystalized.bow.explosive.name"),
                List.of(translatable("crystalized.bow.explosive.desc1"), translatable("crystalized.bow.explosive.desc2")),
                40, Shop.ShardTypes.Strong, ItemType.Ranged,
                "bow", List.of("explosive_bow"), new NamespacedKey("crystalized", "explosive_bow"))
        );
        items.add(setup("shears", Material.SHEARS, null, null, 40, Shop.ShardTypes.Weak, ItemType.Shears, "shears", List.of("shears")));
        items.add(setup("crossbow", Material.BOW, null,
                List.of(translatable("crystalized.crossbow.desc")),
                35, Shop.ShardTypes.Strong, ItemType.Ranged,
                "", null)
        );
        items.add(setup("charged_crossbow", Material.CROSSBOW, translatable("crystalized.crossbow.charged.name"),
                List.of(translatable("crystalized.crossbow.charged.desc")),
                4, Shop.ShardTypes.Nexus, ItemType.Ranged,
                "crossbow", null, new NamespacedKey("crystalized", "charged_crossbow"))
        );

        items.add(setup("boost_orb", Material.COAL, translatable("crystalized.orb.boost.name"),
                List.of(translatable("crystalized.orb.boost.desc")),
                3, Shop.ShardTypes.Nexus, ItemType.other,
                "", null, new NamespacedKey("crystalized", "boost_orb"))
        );
        items.add(setup("bridge_orb", Material.COAL, translatable("crystalized.orb.bridge.name"),
                List.of(translatable("crystalized.orb.bridge.desc")),
                12, Shop.ShardTypes.Weak, ItemType.other,
                "", null, new NamespacedKey("crystalized", "bridge_orb"))
        );
        items.add(setup("explosive_orb", Material.COAL, translatable("crystalized.orb.explosive.name"),
                List.of(translatable("crystalized.orb.explosive.desc")),
                40, Shop.ShardTypes.Weak, ItemType.other,
                "", null, new NamespacedKey("crystalized", "explosive_orb"))
        );
        items.add(setup("grappling_orb", Material.COAL, translatable("crystalized.orb.grappling.name"),
                List.of(translatable("crystalized.orb.grappling.desc")),
                20, Shop.ShardTypes.Strong, ItemType.other,
                "", null, new NamespacedKey("crystalized", "grappling_orb"))
        );
        items.add(setup("poison_orb", Material.COAL, translatable("crystalized.orb.poison.name"),
                List.of(translatable("crystalized.orb.poison.desc", List.of(text("5"), text("4")))),
                10, Shop.ShardTypes.Strong, ItemType.other,
                "", null, new NamespacedKey("crystalized", "poison_orb"))
        );
        items.add(setup("winged_orb", Material.COAL, translatable("crystalized.orb.winged.name"),
                List.of(translatable("crystalized.orb.winged.desc")),
                35, Shop.ShardTypes.Strong, ItemType.other,
                "", null, new NamespacedKey("crystalized", "winged_orb"))
        );

        items.add(setup("antiair_totem", Material.COAL, translatable("crystalized.totem.antiair.name"),
                List.of(translatable("crystalized.totem.antiair.desc")),
                40, Shop.ShardTypes.Weak, ItemType.other,
                "", null, new NamespacedKey("crystalized", "antiair_totem"))
        );
        items.add(setup("cloud_totem", Material.COAL, translatable("crystalized.totem.cloud.name"),
                List.of(translatable("crystalized.totem.cloud.desc")),
                30, Shop.ShardTypes.Strong, ItemType.other,
                "", null, new NamespacedKey("crystalized", "cloud_totem"))
        );
        items.add(setup("defence_totem", Material.COAL, translatable("crystalized.totem.defence.name"),
                List.of(translatable("crystalized.totem.defence.desc")),
                30, Shop.ShardTypes.Strong, ItemType.other,
                "", null, new NamespacedKey("crystalized", "defense_totem"))
        );
        items.add(setup("healing_totem", Material.COAL, translatable("crystalized.totem.healing.name"),
                List.of(translatable("crystalized.totem.healing.desc")),
                26, Shop.ShardTypes.Strong, ItemType.other,
                "", null, new NamespacedKey("crystalized", "healing_totem"))
        );
        items.add(setup("launch_totem", Material.COAL, translatable("crystalized.totem.launch.name"),
                List.of(translatable("crystalized.totem.launch.desc1"), translatable("crystalized.totem.launch.desc2")),
                35, Shop.ShardTypes.Strong, ItemType.other,
                "", null, new NamespacedKey("crystalized", "launch_totem"))
        );

        items.add(setupBlock("concrete", Material.WHITE_CONCRETE, 8, Shop.ShardTypes.Weak,
                Material.BLUE_CONCRETE, Material.CYAN_CONCRETE, Material.GREEN_CONCRETE, Material.LIME_CONCRETE,
                Material.MAGENTA_CONCRETE, Material.RED_CONCRETE, Material.YELLOW_CONCRETE, Material.WHITE_CONCRETE, 16
        ));
        items.add(setupBlock("copper", Material.WAXED_COPPER_BLOCK, 10, Shop.ShardTypes.Weak, Material.WAXED_COPPER_BLOCK, 8));
        items.add(setupBlock("wool", Material.WHITE_WOOL, 8, Shop.ShardTypes.Weak,
                Material.BLUE_CONCRETE, Material.CYAN_CONCRETE, Material.GREEN_CONCRETE, Material.LIME_CONCRETE,
                Material.MAGENTA_CONCRETE, Material.RED_CONCRETE, Material.YELLOW_CONCRETE, Material.WHITE_CONCRETE, 8
        ));
        items.add(setupBlock("obsidian", Material.OBSIDIAN, 8, Shop.ShardTypes.Nexus, Material.OBSIDIAN, 10));

        items.add(setupArmor("leather_armor", Material.LEATHER_CHESTPLATE, 0, Shop.ShardTypes.Weak, "leather_armor", List.of("leather_armor", "chainmail_armor", "iron_armor", "diamond_armor")));
        items.add(setupArmor("chainmail_armor", Material.CHAINMAIL_CHESTPLATE, 20, Shop.ShardTypes.Weak, "leather_armor", List.of("chainmail_armor", "iron_armor", "diamond_armor")));
        items.add(setupArmor("iron_armor", Material.IRON_CHESTPLATE, 36, Shop.ShardTypes.Strong, "chainmail_armor", List.of("iron_armor", "diamond_armor")));
        items.add(setupArmor("diamond_armor", Material.DIAMOND_CHESTPLATE, 8, Shop.ShardTypes.Nexus, "iron_armor", List.of("diamond_armor")));

        items.add(setup("arrow", Material.ARROW, null, null, 4, Shop.ShardTypes.Strong, ItemType.other, "", null, 4));
        items.add(setup("gapple", Material.GOLDEN_APPLE, null, null, 20, Shop.ShardTypes.Weak, ItemType.other, "", null));

        crystalBlitz.getInstance().getLogger().log(Level.INFO, items.size() + " items registered");
    }

    public static CBItem getCBItem(String internalName) {
        for (CBItem cb : items) {
            if (cb.internalName.equals(internalName)) {
                return cb;
            }
        }
        return null;
    }

    public static CBItem getCBItem(ItemStack item) {
        for (CBItem cb : items) {
            ItemStack i = item.clone();
            i.setAmount(cb.item.getAmount());
            if (cb.item.equals(i)) {
                return cb;
            }
        }
        return null;
    }

    public static CBItem getCBShopItem(ItemStack shopItem) {
        for (CBItem cb : items) {
            if (cb.shopItem.equals(shopItem) || cb.shopItem_cantbuy.equals(shopItem)) {
                return cb;
            }
        }
        return null;
    }

    public static ItemStack getShopItem(String internalName, Player p) {
        for (CBItem cb : items) {
            if (cb.internalName.equals(internalName)) {
                PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(p);
                if (p.getInventory().containsAtLeast(cb.priceType.item, cb.price)) {
                    return cb.shopItem;
                } else {
                    return cb.shopItem_cantbuy;
                }
            }
        }
        return null;
    }


    /// Mess of setup methods, better than what I had before tho - Callum

    public static CBItem setup(String internalName, Material mat, Component name, List<Component> desc, int price, Shop.ShardTypes priceType, ItemType type, String downgradeTo, List<String> mustNotHave) {
        return setup(internalName, mat, name, desc, price, priceType, type, downgradeTo, mustNotHave, 1);
    }

    public static CBItem setup(String internalName, Material mat, Component name, List<Component> desc, int price, Shop.ShardTypes priceType, ItemType type, String downgradeTo, List<String> mustNotHave, NamespacedKey itemModel) {
        return setup(internalName, mat, name, desc, price, priceType, type, downgradeTo, mustNotHave, itemModel, 1);
    }

    public static CBItem setup(String internalName, Material mat, Component name, List<Component> desc, int price, Shop.ShardTypes priceType, ItemType type, String downgradeTo, List<String> mustNotHave, int itemAmount) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.displayName(name.color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        }
        if (desc != null) {
            List<Component> lore = new ArrayList<>();
            for (Component c : desc) {
                lore.add(c.color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            }
            meta.lore(lore);
        }
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        item.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setAmount(itemAmount);
        return new CBItem(item, internalName, price, priceType, type, downgradeTo, mustNotHave);
    }

    public static CBItem setup(String internalName, Material mat, Component name, List<Component> desc, int price, Shop.ShardTypes priceType, ItemType type, String downgradeTo, List<String> mustNotHave, NamespacedKey itemModel, int itemAmount) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.displayName(name.color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        }
        if (desc != null) {
            List<Component> lore = new ArrayList<>();
            for (Component c : desc) {
                lore.add(c.color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            }
            meta.lore(lore);
        }
        meta.setUnbreakable(true);
        meta.setItemModel(itemModel);
        item.setItemMeta(meta);
        item.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setAmount(itemAmount);
        return new CBItem(item, internalName, price, priceType, type, downgradeTo, mustNotHave);
    }

    // Yes its messy, its better than what I had before this - Callum
    public static CBItem_Block setupBlock(String internalName, Material mat, int price, Shop.ShardTypes priceType,
                                          Material blueBlock, Material cyanBlock, Material greenBlock, Material limeBlock,
                                          Material magentaBlock, Material redBlock, Material whiteBlock, Material yellowBlock, int blockAmount
    ) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        List<Material> blockReturn = List.of(blueBlock, cyanBlock, greenBlock, limeBlock, magentaBlock, redBlock, whiteBlock, yellowBlock);
        return new CBItem_Block(item, internalName, price, priceType, blockReturn, blockAmount);
    }

    public static CBItem_Block setupBlock(String internalName, Material mat, int price, Shop.ShardTypes priceType, Material returnBlock, int blockAmount) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        List<Material> blockReturn = new ArrayList<>();
        for (int i = 0 ; i < 8 ; i++) {
            blockReturn.add(returnBlock);
        }
        return new CBItem_Block(item, internalName, price, priceType, blockReturn, blockAmount);
    }

    public static CBItem_Armor setupArmor(String internalName, Material chestplateMat, int price, Shop.ShardTypes priceType, String downgradeTo, List<String> mustNotHave) {
        ItemStack item = new ItemStack(chestplateMat);
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return new CBItem_Armor(item, internalName, price, priceType, downgradeTo, mustNotHave);
    }
}


class CBItem{

    String internalName;
    int price;
    Shop.ShardTypes priceType;
    CrystalBlitzItems.ItemType type;
    ItemStack item;
    ItemStack shopItem;
    ItemStack shopItem_cantbuy;

    boolean getRemovedOnDeath;
    String downgradeTo; //Only active when above is false, internalName of item
    List<String> mustNotHave; //list of internalNames

    public CBItem(ItemStack item, String internalName, int price, Shop.ShardTypes priceType, CrystalBlitzItems.ItemType type, String downgradeTo, List<String> mustNotHave) {
        this.item = item;
        this.internalName = internalName;
        this.price = price;
        this.priceType = priceType;
        this.type = type;
        this.mustNotHave = mustNotHave;
        if (mustNotHave == null) {
            this.mustNotHave = List.of();
        }

        if (downgradeTo == null) {
            this.getRemovedOnDeath = true;
        } else {
            this.downgradeTo = downgradeTo;
        }

        ItemStack shopItem = item.clone();
        ItemMeta shopItem_meta = shopItem.getItemMeta();
        List<Component> shopItem_lore = shopItem_meta.lore();
        if (shopItem_lore == null) {
            shopItem_lore = new ArrayList<>();
        }
        shopItem_lore.add(text(""));
        shopItem_lore.add(text(price + priceType.priceAfterText).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        shopItem_meta.lore(shopItem_lore);
        shopItem_meta.setTooltipStyle(priceType.tooltipStyle);
        shopItem_meta.getPersistentDataContainer().set(new NamespacedKey("crystalized", "canbuy"), PersistentDataType.BOOLEAN, true);
        shopItem.setItemMeta(shopItem_meta);
        this.shopItem = shopItem;

        ItemStack shopItem_cb = item.clone();
        ItemMeta shopItem_cb_meta = shopItem_cb.getItemMeta();
        List<Component> shopItem_cb_lore = shopItem_cb_meta.lore();
        if (shopItem_cb_lore == null) {
            shopItem_cb_lore = new ArrayList<>();
        }
        shopItem_cb_lore.add(text(""));
        shopItem_cb_lore.add(text(price + priceType.priceAfterText).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        shopItem_cb_meta.lore(shopItem_cb_lore);
        shopItem_cb_meta.setTooltipStyle(new NamespacedKey("crystalized", "cb_cantbuy"));
        shopItem_cb_meta.getPersistentDataContainer().set(new NamespacedKey("crystalized", "canbuy"), PersistentDataType.BOOLEAN, false);
        shopItem_cb.setItemMeta(shopItem_cb_meta);
        this.shopItem_cantbuy = shopItem_cb;
    }
}

class CBItem_Block extends CBItem {

    private final List<Material> returnForEachTeam; //elements in this are in order of teams (eg, 0 = blue, 1 = cyan, 2 = green, etc. See Teams.java's variables for team ordering)
    private int blockAmount;

    public CBItem_Block(ItemStack item, String internalName, int price, Shop.ShardTypes priceType, List<Material> returnForEachTeam, int BlockAmount) {
        super(item, internalName, price, priceType, CrystalBlitzItems.ItemType.Blocks, "", List.of());
        this.returnForEachTeam = returnForEachTeam;
        this.blockAmount = BlockAmount;
    }

    public ItemStack item(Player whoFor) {
        String s = Teams.getPlayerTeam(whoFor);
        Material mat;
        switch (s) {
            case "blue" -> {mat = returnForEachTeam.getFirst();}
            case "cyan" -> {mat = returnForEachTeam.get(1);}
            case "green" -> {mat = returnForEachTeam.get(2);}
            case "lime" -> {mat = returnForEachTeam.get(3);}
            case "magenta" -> {mat = returnForEachTeam.get(4);}
            case "red" -> {mat = returnForEachTeam.get(5);}
            case "white" -> {mat = returnForEachTeam.get(7);} //these are ordered improperly and im too stupid to figure out why - Callum
            case "yellow" -> {mat = returnForEachTeam.get(6);}
            default -> {mat = item.getType();}
        }
        ItemStack item = new ItemStack(mat);
        item.setAmount(blockAmount);
        return item;
    }
}

class CBItem_Armor extends CBItem{

    ItemStack leggings;

    public CBItem_Armor(ItemStack item, String internalName, int price, Shop.ShardTypes priceType, String downgradeTo, List<String> mustNotHave) {
        super(item, internalName, price, priceType, CrystalBlitzItems.ItemType.Armor, downgradeTo, mustNotHave);

        //setup leggings
        Material leggingsMat;
        switch (item.getType()) {
            case LEATHER_CHESTPLATE -> {
                leggingsMat = Material.LEATHER_LEGGINGS;
            }
            case CHAINMAIL_CHESTPLATE -> {
                leggingsMat = Material.CHAINMAIL_LEGGINGS;
            }
            case IRON_CHESTPLATE -> {
                leggingsMat = Material.IRON_LEGGINGS;
            }
            case DIAMOND_CHESTPLATE -> {
                leggingsMat = Material.DIAMOND_LEGGINGS;
            }
            default -> {
                leggingsMat = Material.LEATHER_LEGGINGS; //fallback
            }
        }
        ItemStack leggingsItem = new ItemStack(leggingsMat);
        ItemMeta leggingsItem_meta = leggingsItem.getItemMeta();
        leggingsItem_meta.setUnbreakable(true);
        leggingsItem.setItemMeta(leggingsItem_meta);
        leggings = leggingsItem;
    }

    public void add(Player whoFor) {
        //chestplate
        if (item.getType().equals(Material.LEATHER_CHESTPLATE)) {
            ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(Teams.TeamData.get_team_data(Teams.getPlayerTeam(whoFor)).color);
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
            whoFor.getInventory().setChestplate(item);
        } else {
            whoFor.getInventory().setChestplate(item);
        }

        //leggings
        if (leggings.getType().equals(Material.LEATHER_LEGGINGS)) {
            ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(Teams.TeamData.get_team_data(Teams.getPlayerTeam(whoFor)).color);
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
            whoFor.getInventory().setLeggings(item);
        } else {
            whoFor.getInventory().setLeggings(leggings);
        }

        //boots (will always be leather boots, so no material check here)
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta leatherBoots_meta = (LeatherArmorMeta) leatherBoots.getItemMeta();
        leatherBoots_meta.setColor(Teams.TeamData.get_team_data(Teams.getPlayerTeam(whoFor)).color);
        leatherBoots_meta.setUnbreakable(true);
        leatherBoots.setItemMeta(leatherBoots_meta);
        whoFor.getInventory().setBoots(leatherBoots);

    }
}
