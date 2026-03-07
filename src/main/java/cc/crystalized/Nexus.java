package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Nexus {

    public String team = "";
    public int health = 10;

    public Nexus(String t) {
        team = t;
        HealthBar(t);

        new BukkitRunnable() {
            public void run() {
                if (Teams.get_team_from_string(team).isEmpty()) {
                    destroyNexus(team);
                }
                cancel();
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1);
    }

    public Component getColoredHealth() {
        switch (health) {
            case 10, 9 -> {
                return text("(" + health + "/10)").color(GREEN);
            }
            case 8, 7 -> {
                return text("(" + health + "/10)").color(DARK_GREEN);
            }
            case 6, 5 -> {
                return text("(" + health + "/10)").color(YELLOW);
            }
            case 4, 3 -> {
                return text("(" + health + "/10)").color(GOLD);
            }
            case 2, 1, 0 -> {
                return text("(" + health + "/10)").color(RED);
            }
        }
        return text("(" + health + "/10)").color(WHITE);
    }

    public String getTeam() {
        return this.team;
    }

    private void HealthBar(String t) {
        Location loc = new Location(Bukkit.getWorld(""),
                crystalBlitz.getInstance().mapdata.getNexus(t)[0] + 0.5,
                crystalBlitz.getInstance().mapdata.getNexus(t)[1] + 2,
                crystalBlitz.getInstance().mapdata.getNexus(t)[2] + 0.5
        );
        TextDisplay display = Bukkit.getWorld("world").spawn(loc, TextDisplay.class, entity -> {
            entity.setSeeThrough(true);
            entity.setBillboard(Display.Billboard.CENTER);
            entity.text(text("loading..."));
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                display.text(
                        text("\uE11A").append(text("\uE11B".repeat(health))).append(text("\uE11C".repeat(10 - health))).append(text("\uE11D"))
                );
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1);

    }

    public void hitNexus(String t, ItemStack i, Player p) {
        if (team != t) {
            return;
        }
        if (Teams.getPlayerTeam(p).equals(team)) {
            p.sendMessage(text("[!] You cannot break your own Nexus! You need to defend this from the other teams!"));
            return;
        }

        switch (i.getType()) {
            case Material.WOODEN_PICKAXE:
                health--;
                break;
            case Material.STONE_PICKAXE:
                health--;
                health--;
                break;
            case Material.IRON_PICKAXE:
                health--;
                health--;
                health--;
                break;
            case Material.DIAMOND_PICKAXE:
                health--;
                health--;
                health--;
                health--;
                health--;
                break;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Teams.getPlayerTeam(player).equals(team)) {
                player.sendMessage(translatable("crystalized.game.crystalblitz.chat.nexusattack"));
                player.showTitle(
                        Title.title(text(" "), translatable("crystalized.game.crystalblitz.chat.nexusattack").color(NamedTextColor.RED)
                                ,Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofSeconds(1)))
                );
                player.playSound(player, "minecraft:block.note_block.harp", 50, 1);
            }
        }

        if (health < 0 || health == 0) {
            health = 0;
            destroyNexus(t, p);
        }
    }

    public void destroyNexus(String t) {
        Location blockloc1 = new Location(Bukkit.getWorld("world"),
                crystalBlitz.getInstance().mapdata.getNexus(t)[0],
                crystalBlitz.getInstance().mapdata.getNexus(t)[1],
                crystalBlitz.getInstance().mapdata.getNexus(t)[2]
        );
        Location blockloc2 = new Location(Bukkit.getWorld("world"),
                crystalBlitz.getInstance().mapdata.getNexus(t)[0],
                crystalBlitz.getInstance().mapdata.getNexus(t)[1] + 1,
                crystalBlitz.getInstance().mapdata.getNexus(t)[2]
        );
        blockloc1.getBlock().setType(Material.DIORITE);
        blockloc2.getBlock().setType(Material.DIORITE);
        health = 0;
    }

    private void destroyNexus(String t, Player p) {
        p.playSound(p, "crystalized:effect.nexus_crystal_destroyed", 50, 1);
        Location blockloc1 = new Location(Bukkit.getWorld("world"),
                crystalBlitz.getInstance().mapdata.getNexus(t)[0],
                crystalBlitz.getInstance().mapdata.getNexus(t)[1],
                crystalBlitz.getInstance().mapdata.getNexus(t)[2]
        );
        Location blockloc2 = new Location(Bukkit.getWorld("world"),
                crystalBlitz.getInstance().mapdata.getNexus(t)[0],
                crystalBlitz.getInstance().mapdata.getNexus(t)[1] + 1,
                crystalBlitz.getInstance().mapdata.getNexus(t)[2]
        );

        blockloc1.getBlock().setType(Material.DIORITE);
        blockloc2.getBlock().setType(Material.DIORITE);

        ItemStack nexusshards = Shop.ShardTypes.Nexus.item.clone();
        nexusshards.setAmount(4);
        p.getInventory().addItem(nexusshards);

        List<ComponentLike> NexusBrokenText = new ArrayList<>();
        NexusBrokenText.add(translatable("crystalized.game.crystalblitz." + t + "nexus"));
        NexusBrokenText.add(text(p.getName()));

        PlayerData kpd = crystalBlitz.getInstance().gamemanager.getPlayerData(p);
        kpd.nexus_kills++;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(translatable("crystalized.game.crystalblitz.chat.nexusbroken", NexusBrokenText));
        }
    }

    public void resetNexuses() {
        Bukkit.getLogger().log(Level.INFO, "[NEXUS] resetting nexus " + team);
        Location blockloc1 = new Location(Bukkit.getWorld("world"),
                crystalBlitz.getInstance().mapdata.getNexus(team)[0],
                crystalBlitz.getInstance().mapdata.getNexus(team)[1],
                crystalBlitz.getInstance().mapdata.getNexus(team)[2]
        );
        Location blockloc2 = new Location(Bukkit.getWorld("world"),
                crystalBlitz.getInstance().mapdata.getNexus(team)[0],
                crystalBlitz.getInstance().mapdata.getNexus(team)[1] + 1,
                crystalBlitz.getInstance().mapdata.getNexus(team)[2]
        );
        Block block1 = blockloc1.getBlock();
        Block block2 = blockloc2.getBlock();

        switch (team) {
            case "blue", "cyan", "green" -> {
                block1.setType(Material.WHITE_GLAZED_TERRACOTTA);
                block2.setType(Material.WHITE_GLAZED_TERRACOTTA);
            }
            case "magenta", "lime" -> {
                block1.setType(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
                block2.setType(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
            }
            case "red", "white", "yellow" -> {
                block1.setType(Material.GRAY_GLAZED_TERRACOTTA);
                block2.setType(Material.GRAY_GLAZED_TERRACOTTA);
            }
        }

        switch (team) {
            //north
            case "cyan", "magenta", "red" -> {
                Directional dir1 = (Directional) block1.getBlockData();
                dir1.setFacing(BlockFace.NORTH);
                block1.setBlockData(dir1);

                Directional dir2 = (Directional) block1.getBlockData();
                dir2.setFacing(BlockFace.NORTH);
                block2.setBlockData(dir2);
            }
            //east
            case "blue", "lime" -> {
                Directional dir1 = (Directional) block1.getBlockData();
                dir1.setFacing(BlockFace.EAST);
                block1.setBlockData(dir1);

                Directional dir2 = (Directional) block1.getBlockData();
                dir2.setFacing(BlockFace.EAST);
                block2.setBlockData(dir2);
            }
            //south
            case "green", "white" -> {
                Directional dir1 = (Directional) block1.getBlockData();
                dir1.setFacing(BlockFace.SOUTH);
                block1.setBlockData(dir1);

                Directional dir2 = (Directional) block1.getBlockData();
                dir2.setFacing(BlockFace.SOUTH);
                block2.setBlockData(dir2);
            }
            //west
            case "yellow" -> {
                Directional dir1 = (Directional) block1.getBlockData();
                dir1.setFacing(BlockFace.WEST);
                block1.setBlockData(dir1);

                Directional dir2 = (Directional) block1.getBlockData();
                dir2.setFacing(BlockFace.WEST);
                block2.setBlockData(dir2);
            }
        }
        block1.getState().update();
        block2.getState().update();
    }
}
