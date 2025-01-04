package cc.crystalized;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (crystalBlitz.getInstance().gamemanager == null) {
            e.joinMessage(text(""));
            p.teleport(crystalBlitz.getInstance().mapdata.get_queue_spawn(Bukkit.getWorld("world")));
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            p.setGameMode(GameMode.ADVENTURE);
            p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, PotionEffect.INFINITE_DURATION, 0, false, false, true));
            p.sendPlayerListHeaderAndFooter(
                    //Header
                    text("\nCrystalized: Crystal Blitz\n"),

                    //Footer
                    text("\n")
                            .append(text("Crystal Blitz Version: " + crystalBlitz.getInstance().getDescription().getVersion()))
                            .append(text("\n"))
            );
            new QueueScoreboard(p);

        } else {
            p.kick(text("A game is currently is progress, try joining again later.").color(NamedTextColor.RED));
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShopInteract(PlayerInteractEntityEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        } else {
            if (e.getRightClicked() instanceof Villager) {
                new Shop(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(text("block placed"));
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        } else {
            crystalBlitz.getInstance().Blocks.add(e.getBlock());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (crystalBlitz.getInstance().gamemanager == null) {
            e.setCancelled(true);
        } else {
            if (crystalBlitz.getInstance().Blocks.contains(e.getBlock())) {
                crystalBlitz.getInstance().Blocks.remove(e.getBlock());
                p.sendMessage(text("block broken"));
            } else {
                //this and the hit nexus method is a big messy
                if (e.getBlock().getType().equals(Material.WHITE_GLAZED_TERRACOTTA) || e.getBlock().getType().equals(Material.GRAY_GLAZED_TERRACOTTA)
                        || e.getBlock().getType().equals(Material.LIGHT_GRAY_GLAZED_TERRACOTTA)) {
                    if (!p.getInventory().getItemInMainHand().toString().toLowerCase().contains("pickaxe")) {
                        p.sendMessage(text("[!] You need to use your Pickaxe to break this."));
                    }
                    e.setCancelled(true);
                    p.sendMessage(text("nexus hit"));
                    Directional dir = (Directional) e.getBlock().getBlockData();
                    switch (e.getBlock().getType()) {
                        case Material.WHITE_GLAZED_TERRACOTTA -> {
                            switch (dir.getFacing()) {
                                case BlockFace.EAST:
                                    crystalBlitz.getInstance().gamemanager.getNexus("blue").hitNexus("blue", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.NORTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("cyan").hitNexus("cyan", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.SOUTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("green").hitNexus("green", p.getInventory().getItemInMainHand(), p);
                                    break;
                            }
                        }
                        case Material.GRAY_GLAZED_TERRACOTTA -> {
                            switch (dir.getFacing()) {
                                case BlockFace.NORTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("red").hitNexus("red", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.SOUTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("white").hitNexus("white", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.WEST:
                                    crystalBlitz.getInstance().gamemanager.getNexus("yellow").hitNexus("yellow", p.getInventory().getItemInMainHand(), p);
                                    break;
                            }
                        }
                        case Material.LIGHT_GRAY_GLAZED_TERRACOTTA -> {
                            switch (dir.getFacing()) {
                                case BlockFace.EAST:
                                    crystalBlitz.getInstance().gamemanager.getNexus("lime").hitNexus("lime", p.getInventory().getItemInMainHand(), p);
                                    break;
                                case BlockFace.NORTH:
                                    crystalBlitz.getInstance().gamemanager.getNexus("magenta").hitNexus("magenta", p.getInventory().getItemInMainHand(), p);
                                    break;
                            }
                        }
                    }
                } else if (e.getBlock().getType().equals(Material.BLACK_GLAZED_TERRACOTTA)) {
                    e.setCancelled(true);
                    p.sendMessage(text("todo"));
                    //TODO, weak and strong shards
                }

                else {
                    p.sendMessage(text("block breaking cancelled"));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        Bukkit.getServer().sendMessage(Component.text("")
                .append(player.displayName())
                .append(Component.text(": "))
                .append(event.message()));
    }

    @EventHandler
    public void OnPlayerDisconnect(PlayerConnectionCloseEvent event) {
        if (crystalBlitz.getInstance().gamemanager != null) {
            Teams.DisconnectPlayer(event.getPlayerName());
        }
        if (crystalBlitz.getInstance().gamemanager != null && Bukkit.getOnlinePlayers().equals(0)) {
            Bukkit.getLogger().log(Level.INFO, "All players have disconnected. The Game will now end.");
            crystalBlitz.getInstance().gamemanager.ForceEndGame();
        }
    }
}
