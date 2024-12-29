package cc.crystalized;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.kyori.adventure.text.Component.text;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (crystalBlitz.getInstance().GameManager == null) {
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
        if (crystalBlitz.getInstance().GameManager == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (crystalBlitz.getInstance().GameManager == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (crystalBlitz.getInstance().GameManager == null) {
            e.setCancelled(true);
        }
    }
}
