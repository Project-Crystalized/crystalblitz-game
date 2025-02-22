package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class Nexus {

    public String team = "";
    public int health = 10;

    public Nexus(String t) {
        team = t;
        HealthBar(t);
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

        ItemStack nexusshards = CrystalBlitzItems.NexusShard.clone();
        nexusshards.setAmount(4);
        p.getInventory().addItem(nexusshards);

        List<ComponentLike> NexusBrokenText = new ArrayList<>();
        NexusBrokenText.add(translatable("crystalized.game.crystalblitz." + t + "nexus"));
        NexusBrokenText.add(text(p.getName()));

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
        switch (team) {
            case "blue" -> {
                blockloc1.getBlock().setType(Material.WHITE_GLAZED_TERRACOTTA);
                Directional dir1 = (Directional) blockloc1.getBlock().getBlockData();
                dir1.setFacing(BlockFace.EAST);
                blockloc1.getBlock().setBlockData(dir1);
                blockloc1.getBlock().getState().update();

                blockloc2.getBlock().setType(Material.WHITE_GLAZED_TERRACOTTA);
                Directional dir2 = (Directional) blockloc2.getBlock().getBlockData();
                dir2.setFacing(BlockFace.EAST);
                blockloc2.getBlock().setBlockData(dir2);
                blockloc2.getBlock().getState().update();
            }
            case "cyan" -> {
                blockloc1.getBlock().setType(Material.WHITE_GLAZED_TERRACOTTA);
                Directional dir1 = (Directional) blockloc1.getBlock().getBlockData();
                dir1.setFacing(BlockFace.NORTH);
                blockloc1.getBlock().setBlockData(dir1);
                blockloc1.getBlock().getState().update();

                blockloc2.getBlock().setType(Material.WHITE_GLAZED_TERRACOTTA);
                Directional dir2 = (Directional) blockloc2.getBlock().getBlockData();
                dir2.setFacing(BlockFace.NORTH);
                blockloc2.getBlock().setBlockData(dir2);
                blockloc2.getBlock().getState().update();
            }
            case "green" -> {
                blockloc1.getBlock().setType(Material.WHITE_GLAZED_TERRACOTTA);
                Directional dir1 = (Directional) blockloc1.getBlock().getBlockData();
                dir1.setFacing(BlockFace.SOUTH);
                blockloc1.getBlock().setBlockData(dir1);
                blockloc1.getBlock().getState().update();

                blockloc2.getBlock().setType(Material.WHITE_GLAZED_TERRACOTTA);
                Directional dir2 = (Directional) blockloc2.getBlock().getBlockData();
                dir2.setFacing(BlockFace.SOUTH);
                blockloc2.getBlock().setBlockData(dir2);
                blockloc2.getBlock().getState().update();
            }
            case "lime" -> {
                blockloc1.getBlock().setType(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
                Directional dir1 = (Directional) blockloc1.getBlock().getBlockData();
                dir1.setFacing(BlockFace.EAST);
                blockloc1.getBlock().setBlockData(dir1);
                blockloc1.getBlock().getState().update();

                blockloc2.getBlock().setType(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
                Directional dir2 = (Directional) blockloc2.getBlock().getBlockData();
                dir2.setFacing(BlockFace.EAST);
                blockloc2.getBlock().setBlockData(dir2);
                blockloc2.getBlock().getState().update();
            }
            case "magenta" -> {
                blockloc1.getBlock().setType(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
                Directional dir1 = (Directional) blockloc1.getBlock().getBlockData();
                dir1.setFacing(BlockFace.NORTH);
                blockloc1.getBlock().setBlockData(dir1);
                blockloc1.getBlock().getState().update();

                blockloc2.getBlock().setType(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
                Directional dir2 = (Directional) blockloc2.getBlock().getBlockData();
                dir2.setFacing(BlockFace.NORTH);
                blockloc2.getBlock().setBlockData(dir2);
                blockloc2.getBlock().getState().update();
            }
            case "red" -> {
                blockloc1.getBlock().setType(Material.GRAY_GLAZED_TERRACOTTA);
                Directional dir1 = (Directional) blockloc1.getBlock().getBlockData();
                dir1.setFacing(BlockFace.NORTH);
                blockloc1.getBlock().setBlockData(dir1);
                blockloc1.getBlock().getState().update();

                blockloc2.getBlock().setType(Material.GRAY_GLAZED_TERRACOTTA);
                Directional dir2 = (Directional) blockloc2.getBlock().getBlockData();
                dir2.setFacing(BlockFace.NORTH);
                blockloc2.getBlock().setBlockData(dir2);
                blockloc2.getBlock().getState().update();
            }
            case "white" -> {
                blockloc1.getBlock().setType(Material.GRAY_GLAZED_TERRACOTTA);
                Directional dir1 = (Directional) blockloc1.getBlock().getBlockData();
                dir1.setFacing(BlockFace.SOUTH);
                blockloc1.getBlock().setBlockData(dir1);
                blockloc1.getBlock().getState().update();

                blockloc2.getBlock().setType(Material.GRAY_GLAZED_TERRACOTTA);
                Directional dir2 = (Directional) blockloc2.getBlock().getBlockData();
                dir2.setFacing(BlockFace.SOUTH);
                blockloc2.getBlock().setBlockData(dir2);
                blockloc2.getBlock().getState().update();
            }
            case "yellow" -> {
                blockloc1.getBlock().setType(Material.GRAY_GLAZED_TERRACOTTA);
                Directional dir1 = (Directional) blockloc1.getBlock().getBlockData();
                dir1.setFacing(BlockFace.WEST);
                blockloc1.getBlock().setBlockData(dir1);
                blockloc1.getBlock().getState().update();

                blockloc2.getBlock().setType(Material.GRAY_GLAZED_TERRACOTTA);
                Directional dir2 = (Directional) blockloc2.getBlock().getBlockData();
                dir2.setFacing(BlockFace.WEST);
                blockloc2.getBlock().setBlockData(dir2);
                blockloc2.getBlock().getState().update();
            }
        }

    }
}
