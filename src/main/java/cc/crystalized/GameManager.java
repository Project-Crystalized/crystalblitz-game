package cc.crystalized;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;

public class GameManager {

    /*
    TODO

    Sort out teams
    TP players to correct Locations
    Colour armor

     */

    public Teams teams;
    public static ArrayList<Nexus> nexuses;
    public BossbarManager bossbar = new BossbarManager();

    public GameManager() {

        teams = new Teams();

        Bukkit.getServer().sendMessage(text("Starting Game!"));
        for (Entity e : Bukkit.getWorld("world").getEntities()) {
            if (e instanceof Villager || e instanceof TextDisplay) {
                e.remove();
            }
        }
        setupEntities();
        for (Player p : Bukkit.getOnlinePlayers()) {
            givePlayerItems(p);
            p.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
            p.getInventory().setItem(1, new ItemStack(Material.WOODEN_PICKAXE));
            Teams.setPlayerDisplayNames(p);
            p.setGameMode(GameMode.SURVIVAL);
            new ScoreboardManager(p);

            Location ploc = new Location(Bukkit.getWorld("world"),
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[0],
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[1],
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[2]
            );
            p.teleport(ploc);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                //Main game loop
                for (Player p : Bukkit.getOnlinePlayers()) {

                }

                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1);
    }

    public static void ForceEndGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kick();
        }

        for (Entity e : Bukkit.getWorld("world").getEntities()) {
            if (e instanceof Villager || e instanceof TextDisplay) {
                e.remove();
            }
        }

        Bukkit.getLogger().log(Level.INFO, "Removing player-made blocks, please wait before rejoining...");

        new BukkitRunnable() {
            int count = crystalBlitz.getInstance().Blocks.size();
            List<Block> BlockList = crystalBlitz.getInstance().Blocks.stream().toList();
            int i = 0;

            @Override
            public void run() {
                Set<Block> remove_set = new HashSet<>();
                for (Block block : crystalBlitz.getInstance().Blocks) {
                    Bukkit.getLogger().log(Level.INFO,
                            "Set Block " + block.getType().toString() + " at X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ() + " to air."
                    );
                    i++;
                    block.setType(Material.AIR);
                    remove_set.add(block);
                    if (i > 10) {
                        break;
                    }
                }
                crystalBlitz.getInstance().Blocks.removeAll(remove_set);
                if (crystalBlitz.getInstance().Blocks.isEmpty()) {
                    Bukkit.getLogger().log(Level.INFO, "Removed all player-made blocks! You may rejoin to start another game");
                    crystalBlitz.getInstance().gamemanager = null;
                    for (Nexus n : nexuses) {
                        n.
                                resetNexuses();
                    }
                    cancel();
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1);
    }

    public Nexus getNexus(String team) {
        for (Nexus n : nexuses) {
            if (n.getTeam().equals(team)) {
                return n;
            }
        }

        Bukkit.getLogger().log(Level.WARNING, "unknown team " + team);
        return null;
    }

    private static void setupEntities() {
        Component name = text("Shop");

        nexuses = new ArrayList<Nexus>();
        for (String team: Teams.teams) {
            Nexus n = new Nexus(team);
            nexuses.add(n);
        }

        for (String team : Teams.teams) {
            Location loc = new Location(
                    Bukkit.getWorld("world"),
                    crystalBlitz.getInstance().mapdata.getShop(team)[0] + 0.5,
                    crystalBlitz.getInstance().mapdata.getShop(team)[1],
                    crystalBlitz.getInstance().mapdata.getShop(team)[2] + 0.5
            );
            Villager shop = Bukkit.getWorld("world").spawn(loc, Villager.class, entity -> {
                entity.setGravity(true);
                entity.setInvulnerable(true);
                entity.setAI(false);
                entity.setCustomNameVisible(true);
                entity.customName(name);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 200, false, false, false));
            });
        }
    }

    //Gives players leather armor of their team's colour, do anything else seperately
    public static void givePlayerItems(Player p) {
        PlayerInventory inv = p.getInventory();

        switch (Teams.getPlayerTeam(p)) {
            case "blue":
                inv.setChestplate(colorArmor(Color.fromRGB(0x0A42BB), new ItemStack(Material.LEATHER_CHESTPLATE)));
                inv.setLeggings(colorArmor(Color.fromRGB(0x0A42BB), new ItemStack(Material.LEATHER_LEGGINGS)));
                inv.setBoots(colorArmor(Color.fromRGB(0x0A42BB), new ItemStack(Material.LEATHER_BOOTS)));
                break;
            case "cyan":
                inv.setChestplate(colorArmor(Color.fromRGB(0x157D91), new ItemStack(Material.LEATHER_CHESTPLATE)));
                inv.setLeggings(colorArmor(Color.fromRGB(0x157D91), new ItemStack(Material.LEATHER_LEGGINGS)));
                inv.setBoots(colorArmor(Color.fromRGB(0x157D91), new ItemStack(Material.LEATHER_BOOTS)));
                break;
            case "green":
                inv.setChestplate(colorArmor(Color.fromRGB(0x0A971E), new ItemStack(Material.LEATHER_CHESTPLATE)));
                inv.setLeggings(colorArmor(Color.fromRGB(0x0A971E), new ItemStack(Material.LEATHER_LEGGINGS)));
                inv.setBoots(colorArmor(Color.fromRGB(0x0A971E), new ItemStack(Material.LEATHER_BOOTS)));
                break;
            case "lime":
                inv.setChestplate(colorArmor(Color.fromRGB(0x67E555), new ItemStack(Material.LEATHER_CHESTPLATE)));
                inv.setLeggings(colorArmor(Color.fromRGB(0x67E555), new ItemStack(Material.LEATHER_LEGGINGS)));
                inv.setBoots(colorArmor(Color.fromRGB(0x67E555), new ItemStack(Material.LEATHER_BOOTS)));
                break;
            case "magenta":
                inv.setChestplate(colorArmor(Color.fromRGB(0xDA50E0), new ItemStack(Material.LEATHER_CHESTPLATE)));
                inv.setLeggings(colorArmor(Color.fromRGB(0xDA50E0), new ItemStack(Material.LEATHER_LEGGINGS)));
                inv.setBoots(colorArmor(Color.fromRGB(0xDA50E0), new ItemStack(Material.LEATHER_BOOTS)));
                break;
            case "red":
                inv.setChestplate(colorArmor(Color.fromRGB(0xF74036), new ItemStack(Material.LEATHER_CHESTPLATE)));
                inv.setLeggings(colorArmor(Color.fromRGB(0xF74036), new ItemStack(Material.LEATHER_LEGGINGS)));
                inv.setBoots(colorArmor(Color.fromRGB(0xF74036), new ItemStack(Material.LEATHER_BOOTS)));
                break;
            case "white":
                inv.setChestplate(colorArmor(Color.fromRGB(0xFFFFFF), new ItemStack(Material.LEATHER_CHESTPLATE)));
                inv.setLeggings(colorArmor(Color.fromRGB(0xFFFFFF), new ItemStack(Material.LEATHER_LEGGINGS)));
                inv.setBoots(colorArmor(Color.fromRGB(0xFFFFFF), new ItemStack(Material.LEATHER_BOOTS)));
                break;
            case "yellow":
                inv.setChestplate(colorArmor(Color.fromRGB(0xFBE059), new ItemStack(Material.LEATHER_CHESTPLATE)));
                inv.setLeggings(colorArmor(Color.fromRGB(0xFBE059), new ItemStack(Material.LEATHER_LEGGINGS)));
                inv.setBoots(colorArmor(Color.fromRGB(0xFBE059), new ItemStack(Material.LEATHER_BOOTS)));
                break;
            default:
                p.sendMessage(text(".getPlayerTeam() with your name produced \"" + Teams.getPlayerTeam(p) + "\", You should not get this message"));
                throw new RuntimeException();
        }
    }

    private static ItemStack colorArmor(Color c, ItemStack i) {
        LeatherArmorMeta lam = (LeatherArmorMeta) i.getItemMeta();
        lam.setColor(c);
        lam.setUnbreakable(true);
        i.setItemMeta(lam);
        return i;
    }
}
