package cc.crystalized;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.FloodgateApi;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class BossbarManager {

    BossBar bar;
    BossBar texture;
    BossBar texture_br; //Bedrock bossbar texture, because Bedrock is inconsistent compared to java - Callum
    BossBarStates currentstate =  BossBarStates.starting; //Reset state
    //Changed to be 3 minutes as mite requsted
    //This is the time between each gen upgrade
    int timerdefaultvalue = 3 * 60;
    int timer = timerdefaultvalue;
    //aded a specific check for if nexuses can be revived
    private boolean canNexusesBeRevived = true;

    public BossbarManager() {
        texture = BossBar.bossBar(text("\uE402"), 0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        texture_br = BossBar.bossBar(text("\uE403"), 0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (FloodgateApi.getInstance().isFloodgatePlayer(p.getUniqueId())) {
                p.showBossBar(texture_br);
                p.hideBossBar(texture);
            } else {
                p.showBossBar(texture);
                p.hideBossBar(texture_br);
            }
        }
        bar = BossBar.bossBar(text("loading"), 0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        Bukkit.getServer().showBossBar(bar);

        new BukkitRunnable() {
            public void run() {
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                    return;
                }
                timer--;
                ChangeBossbarText();

                if (timer == 30 && currentstate == BossBarStates.GenUpgradeIV) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(text("Overtime will beging shortly, All Nexuses will be destroyed soon!"));
                    }
                }
                //Changed the check as it started being slightly hard to read
                if(timer != 0){
                    return;
                }
                switch (currentstate) {
                    //changed the gen upgrades to be as mite requsted, now there are 4 and overtime
                    case starting -> {
                        currentstate = BossBarStates.GenUpgradeI;
                        //This revies all the broken shards on gen 1 upgrade
                        crystalBlitz.getInstance().gamemanager.revivePureShardGenerators();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(text("Stale and Pure node generators have been Upgraded!"));
                        }
                        timer = timerdefaultvalue;
                    }
                    case GenUpgradeI -> {
                        currentstate = BossBarStates.GenUpgradeII;
                        //This revies all the broken shards on gen 2 upgrade
                        crystalBlitz.getInstance().gamemanager.revivePureShardGenerators();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(text("Stale and Pure node generators have been Upgraded!"));
                        }
                        timer = timerdefaultvalue;
                    }
                    case GenUpgradeII -> {
                        currentstate = BossBarStates.GenUpgradeIII;
                        //This revies all the broken shards on gen 3 upgrade
                        crystalBlitz.getInstance().gamemanager.revivePureShardGenerators();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(text("Stale and Pure node generators have been Upgraded!"));
                        }
                        timer = timerdefaultvalue;
                    }
                    case GenUpgradeIII -> {
                        currentstate = BossBarStates.GenUpgradeIV;
                        //This revies all the broken shards on gen 4 upgrade
                        crystalBlitz.getInstance().gamemanager.revivePureShardGenerators();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(text("Stale and Pure node generators have been Upgraded!"));
                        }
                        timer = timerdefaultvalue;
                    }
                    case GenUpgradeIV -> {
                        //changes to Overtime which basicly combines the Nexus destruction and border moving, as mite requsted
                        currentstate = BossBarStates.Overtime;
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            //TODO add a sound or smth here
                            p.sendMessage(translatable("crystalized.game.crystalblitz.chat.worldborder").color(NamedTextColor.RED));
                        }
                        //ensuring that it is false right before they break, so no perfect time revival.
                        canNexusesBeRevived = false;
                        crystalBlitz.getInstance().gamemanager.destroyAllNexuses();
                        crystalBlitz.getInstance().gamemanager.worldborder.setTrueSizeBorder();
                        crystalBlitz.getInstance().gamemanager.worldborder.ShrinkBorder();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(text("Overtime!! World border closing!").color(NamedTextColor.RED));
                        }
                        //changes the boss bar text here as it will not run again to change it later
                        ChangeBossbarText();
                        cancel();
                    }
                    case Overtime -> {
                        //This should not be reached but incase cancels
                        cancel();
                    }
                    /*
                    case NexusDestroyed -> {
                        currentstate = BossBarStates.WorldBorderClosing;
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(text("World border closing!")); //TODO translatable
                        }
                        timer = 1;
                    }
                    case WorldBorderClosing -> {
                        crystalBlitz.getInstance().gamemanager.worldborder.setTrueSizeBorder();
                        crystalBlitz.getInstance().gamemanager.worldborder.ShrinkBorder();
                        cancel();
                    }*/
                }
            }


        }.runTaskTimer(crystalBlitz.getInstance(), 0, 20);
    }

    //TODO make these translatable
    private void ChangeBossbarText() {
        switch (currentstate) {
            //Changed to match the four gen upgrades and Over tim
            case starting -> {
                bar.name(translatable("crystalized.game.crystalblitz.bossbar.upgrade").append(text("(I)")).color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
            }
            case GenUpgradeI -> {
                bar.name(translatable("crystalized.game.crystalblitz.bossbar.upgrade").append(text("(II)")).color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
            }
            case GenUpgradeII -> {
                bar.name(translatable("crystalized.game.crystalblitz.bossbar.upgrade").append(text("(III)")).color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
                //bar.name(text("All Nexuses will be destroyed in: ").color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
            }
            case GenUpgradeIII -> {
                bar.name(translatable("crystalized.game.crystalblitz.bossbar.upgrade").append(text("(IV)")).color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
            }
            case GenUpgradeIV -> {
                bar.name(text("All Nexuses will be destroyed in: ").color(NamedTextColor.YELLOW).append(text(timer).color(NamedTextColor.WHITE)));
            }
            case Overtime -> {
                bar.name(text("Overtime!!").color(NamedTextColor.YELLOW));
            }
        }
    }
    //Just a getter method to use it in the shop.
    public boolean getCanNexusesBeRevived(){
        return canNexusesBeRevived;
    }
}

enum BossBarStates{
    starting,
    GenUpgradeI,
    GenUpgradeII,
    GenUpgradeIII,
    GenUpgradeIV,
    //NexusDestroyed,
    //WorldBorderClosing,
    Overtime
}
