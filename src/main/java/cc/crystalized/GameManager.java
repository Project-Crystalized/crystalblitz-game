package cc.crystalized;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.FloodgateApi;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class GameManager {

    public Teams teams;
    public static ArrayList<Nexus> nexuses;
    public BossbarManager bossbar = new BossbarManager();
    public WorldBorderManager worldborder = new WorldBorderManager();
    public static List<PlayerData> playerDatas = new ArrayList<>();
    public static List<Shop> shopList = new ArrayList<>();
    public static GameTypes GameType;

    enum GameTypes {
        Custom,
        StandardSolos,
        StandardDuos,
        StandardTrios,
        StandardSquads,
    }

    public GameManager(GameTypes type) {
        GameType = type;
        teams = new Teams(type);
        Teams.TeamStatus.Init();

        Bukkit.getServer().sendMessage(text("Starting Game!"));
        for (Entity e : Bukkit.getWorld("world").getEntities()) {
            if (e instanceof Villager || e instanceof TextDisplay || e instanceof Arrow || e instanceof Item) {
                e.remove();
            }
        }

        playerDatas.clear();
        setupEntities();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getEnderChest().setMaxStackSize(54);
            p.getEnderChest();
            p.getEnderChest().clear();
        }
        for (Player p : crystalBlitz.getInstance().getOnlinePlayers()) {
            givePlayerItems(p);
            Teams.setPlayerDisplayNames(p);
            p.setGameMode(GameMode.SURVIVAL);
            new ScoreboardManager(p);
            for (Player player : Bukkit.getOnlinePlayers()) {
                p.unlistPlayer(player);
            }

            p.getInventory().setItem(0, CrystalBlitzItems.WoodenSword);
            p.getInventory().setItem(1, CrystalBlitzItems.WoodenPickaxe);
            Location ploc = new Location(Bukkit.getWorld("world"),
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[0],
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[1],
                    crystalBlitz.getInstance().mapdata.getSpawn(Teams.getPlayerTeam(p))[2]
            );
            p.teleport(ploc);
            playerDatas.add(new PlayerData(p));
            new CustomPlayerNametags(p);
        }
        for (String s : teams.spectator) {
            Player p = Bukkit.getPlayer(s);
            Teams.setPlayerDisplayNames(p);
            p.teleport(new Location(Bukkit.getWorld("world"),
                    crystalBlitz.getInstance().mapdata.spectator_spawn[0],
                    crystalBlitz.getInstance().mapdata.spectator_spawn[1],
                    crystalBlitz.getInstance().mapdata.spectator_spawn[2]
            ));
            p.setGameMode(GameMode.SPECTATOR);
            new ScoreboardManager(p);
            playerDatas.add(new PlayerData(p));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                //Main game loop
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getGameMode().equals(GameMode.SURVIVAL) && p.getY() < crystalBlitz.getInstance().mapdata.DeathLimit) {
                        p.damage(40, DamageSource.builder(DamageType.OUT_OF_WORLD).build());
                    }
                    TabMenu.sendTabMenu(p);
                }

                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 1);

        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getWorldBorder().isInside(p.getLocation()) && p.getGameMode().equals(GameMode.SURVIVAL)) {
                        p.damage(1, DamageSource.builder(DamageType.OUTSIDE_BORDER).build());
                        /*if (p.getHealth() < 1) {
                            p.setHealth(0);
                        } else {
                            p.setHealth(p.getHealth() - 1);
                        }
                        p.playSound(p, "minecraft:entity.generic.hurt", 50, 1);*/
                    }
                }
                if (crystalBlitz.getInstance().gamemanager == null) {
                    cancel();
                }
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 1, 15);
    }

    public static void ForceEndGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kick();
        }

        for (Entity e : Bukkit.getWorld("world").getEntities()) {
            if (e instanceof Villager || e instanceof TextDisplay || e instanceof Arrow || e instanceof Item) {
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
                    if (Bukkit.getOnlinePlayers().size() != 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.kick(text("[!] We are still clearing blocks, please wait before rejoining..."));
                        }
                    }
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
                        n.resetNexuses();
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

    public void destroyAllNexuses() {
        for (Nexus n : nexuses) {
            n.destroyNexus(n.getTeam());
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, "crystalized:effect.nexus_crystal_destroyed", 50, 1);
        }
    }

    private static void setupEntities() {
        Component name = text("Shop");

        nexuses = new ArrayList<Nexus>();
        for (String team: Teams.teams) {
            if (!team.equals("spectator")) {
                Nexus n = new Nexus(team);
                nexuses.add(n);
            }
        }

        for (String team : Teams.teams) {
            if (!team.equals("spectator")) {
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
                //Do nothing, would throw a runtime exception but I implemented the spectator team which doesn't play the game normally
                break;
        }
    }

    private static ItemStack colorArmor(Color c, ItemStack i) {
        LeatherArmorMeta lam = (LeatherArmorMeta) i.getItemMeta();
        lam.setColor(c);
        lam.setUnbreakable(true);
        i.setItemMeta(lam);
        return i;
    }

    public static void StartEndGame(String winning_team, Teams.TeamData td) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, "crystalized:effect.ls_game_won", 50, 1);
            if (GameManager.GameType.equals(GameTypes.StandardSolos)) {
                Player lastPlayer = Bukkit.getPlayer(Teams.get_team_from_string(winning_team).getFirst());
                p.showTitle(Title.title(
                        lastPlayer.displayName(),
                        translatable("crystalized.game.knockoff.win").color(YELLOW),
                        Title.Times.times(Duration.ofMillis(250), Duration.ofSeconds(5), Duration.ofMillis(1000)))
                );
                p.sendMessage(lastPlayer.displayName().append(text(" ")).append(translatable("crystalized.game.knockoff.win").color(YELLOW)));
            } else {
                p.showTitle(Title.title(
                        text(td.symbol).append(translatable("crystalized.game.generic.team." + td.name).color(TextColor.color(td.color.asRGB()))).append(text(td.symbol)),
                        translatable("crystalized.game.knockoff.win").color(YELLOW),
                        Title.Times.times(Duration.ofMillis(250), Duration.ofSeconds(5), Duration.ofMillis(1000)))
                );
                p.sendMessage(
                        text(td.symbol).append(translatable("crystalized.game.generic.team." + td.name).color(TextColor.color(td.color.asRGB()))).append(text(td.symbol))
                                .append(text(" ")).append(translatable("crystalized.game.knockoff.win").color(YELLOW))
                );
            }
            if (Teams.getPlayerTeam(p).equals(td.name)) {
                p.playSound(p, "crystalized:effect.ls_game_won", 50, 1);
            } else {
                p.playSound(p, "crystalized:effect.ls_game_lost", 50, 1);
            }
        }
        CrystalBlitzDatabase.save_game(winning_team);

        new BukkitRunnable() {
            int timer = 0;
            FloodgateApi floodgateapi = FloodgateApi.getInstance();

            @Override
            public void run() {
                switch (timer) {
                    case 2:
                        Collections.sort(playerDatas, new PlayerDataComparator());
                        Collections.reverse(playerDatas);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (floodgateapi.isFloodgatePlayer(p.getUniqueId())) {
                                p.sendMessage(Component.text("-".repeat(40)).color(GOLD));
                            } else {
                                p.sendMessage(Component.text(" ".repeat(55)).color(GOLD).decoration(TextDecoration.STRIKETHROUGH,  true));
                            }
                        }
                        Bukkit.getServer().sendMessage(Component.text("")
                                .append(Component.text("\n").append(Component.translatable("crystalized.game.crystalblitz.name").color(LIGHT_PURPLE)).append(Component.text(" ").color(WHITE))) //TODO add symbol here
                                .append(Component.text("\n").append(Component.translatable("crystalized.game.generic.gameresults").color(BLUE)))
                        );
                        if (playerDatas.size() > 0) {
                            PlayerData first = playerDatas.get(0);
                            Bukkit.getServer().sendMessage(Component.text("   1st. ")
                                    .append(Component.text(first.p.getName())).color(GREEN).append(text(" ".repeat(20 - first.p.getName().length())))
                                    .append(Component.text("" + first.kills))
                            );
                        }
                        if (playerDatas.size() > 1) {
                            PlayerData second = playerDatas.get(1);
                            Bukkit.getServer().sendMessage(Component.text("   2nd. ")
                                    .append(Component.text(second.p.getName())).color(YELLOW).append(text(" ".repeat(20 - second.p.getName().length())))
                                    .append(Component.text("" + second.kills))
                            );
                        }
                        if (playerDatas.size() > 2) {
                            PlayerData third = playerDatas.get(2);
                            Bukkit.getServer().sendMessage(Component.text("   3rd. ")
                                    .append(Component.text(third.p.getName())).color(YELLOW).append(text(" ".repeat(20 - third.p.getName().length())))
                                    .append(Component.text("" + third.kills))
                            );
                        }

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (floodgateapi.isFloodgatePlayer(p.getUniqueId())) {
                                p.sendMessage(Component.text("-".repeat(40)).color(GOLD));
                            } else {
                                p.sendMessage(Component.text(" ".repeat(55)).color(GOLD).decoration(TextDecoration.STRIKETHROUGH,  true));
                            }
                        }
                        break;
                    case 12, 13, 14:
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player, "minecraft:block.note_block.hat", SoundCategory.MASTER,50, 1); //TODO placeholder sound
                        }
                        break;
                    case 15:
                        ForceEndGame();
                        cancel();
                        break;
                }
                timer++;
            }
        }.runTaskTimer(crystalBlitz.getInstance(), 20,20);
    }

    public static PlayerData getPlayerData(Player p) {
        for (PlayerData pd : playerDatas) {
            if (pd.p == p) {
                return pd;
            }
        }

        return null;
    }
}

class TabMenu {

    static Component StatsPlayerList = text("");
    static Component alive = text("[Alive] ").color(WHITE);
    static Component dead = text("[Dead] ").color(WHITE);
    static Component eliminated = text("[Eliminated] ").color(WHITE);

    private static void addToStatsString(Component s) {
        StatsPlayerList = StatsPlayerList.append(s);
    }

    public static void sendTabMenu(Player p) {
        StatsPlayerList = text("");
        Teams t = crystalBlitz.getInstance().gamemanager.teams;

        p.sendPlayerListHeader(
                text("\n")
                        .append(text("Crystalized: Crystal Blitz").color(LIGHT_PURPLE))
                        .append(text("\n"))
        );


        addToStatsString(text("---------------------------------------------------\n").color(GRAY));
        for (Teams.TeamData td : t.team_datas) {
            List<String> team = t.get_team_from_string(td.name); //probably unsafe, im just shooting in the dark to see if this works
            if (!team.isEmpty()) {
                addToStatsString(text("\n").append(text(td.symbol)).append(translatable("crystalized.game.generic.team." + td.name).color(TextColor.color(td.color.asRGB()))).append(text("\n")));
                for (String s : team) {
                    Player player = Bukkit.getPlayer(s);
                    PlayerData pd = crystalBlitz.getInstance().gamemanager.getPlayerData(player);
                    if (pd.isEliminated) {
                        addToStatsString(eliminated);
                    } else if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                        addToStatsString(dead);
                    } else {
                        addToStatsString(alive);
                    }
                    addToStatsString(text("").append(pd.cachedRankIcon_large).append(text(" ")).append(player.displayName()).append(text(" \uE101: " + pd.kills)).append(text(" \uE101(N): " + pd.nexus_kills)).append(text(" \uE103: " + pd.deaths)).append(text("\n")));
                }
            }
        }
        addToStatsString(text("\n---------------------------------------------------\n\n").color(GRAY)
                .append(text("Crystal Blitz Version: " + crystalBlitz.getInstance().getDescription().getVersion())).color(DARK_GRAY).append(text("\n"))
        );

        p.sendPlayerListFooter(StatsPlayerList);
    }
}
