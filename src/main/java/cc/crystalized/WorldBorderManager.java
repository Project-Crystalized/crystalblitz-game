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
    //The border shrinking time modified so it ain't instant, current 2 minutes
    //Change the 2 to change in minutes
    //TODO: Maybe in the future we can make a more custom border so people can still build and go out of it while taking damage
    //Something like in Sky Battle 
    private static final int BORDER_SHRINKING_TIME = 20 * (60 * 2);

    public WorldBorderManager() {
        border = Bukkit.getServer().createWorldBorder();
        MapData mapdata = crystalBlitz.getInstance().mapdata;
        border.setCenter(mapdata.border_mid[0] + 0.5, mapdata.border_mid[1] + 0.5);
        border.setSize(mapdata.border_size * 2);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setWorldBorder(border);
        }
    }

    //making sure it's size becomes the intended size before the shrinking begins
    public void setTrueSizeBorder(){
        border.setSize(border.getSize() / 2);
    }
    public void ShrinkBorder() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showTitle(Title.title(text("World Border Shrinking").color(NamedTextColor.RED), text("")));
            p.playSound(p, "minecraft:block.note_block.harp",  50, 1); //TODO Placeholder sound
        }
        border.changeSize(10, BORDER_SHRINKING_TIME);
    }
}
