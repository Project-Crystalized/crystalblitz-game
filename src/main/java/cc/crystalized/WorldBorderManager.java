package cc.crystalized;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.Component.text;

public class WorldBorderManager {

    WorldBorder border;

    public WorldBorderManager() {
        border = Bukkit.getServer().createWorldBorder();
        MapData mapdata = crystalBlitz.getInstance().mapdata;
        border.setCenter(mapdata.border_mid[0] + 0.5, mapdata.border_mid[1] + 0.5);
        border.setSize(mapdata.border_size * 2);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setWorldBorder(border);
        }
    }

    public void ShrinkBorder() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showTitle(Title.title(text("World Border Shrinking").color(NamedTextColor.RED), text("")));
            p.playSound(p, "minecraft:block.note_block.harp",  50, 1); //TODO Placeholder sound
        }
        border.changeSize(10, 2 * 60);
    }
}
