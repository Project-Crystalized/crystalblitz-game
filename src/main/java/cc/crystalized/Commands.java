package cc.crystalized;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {

        switch (label) {
            case "crystalblitz":
                return run_command(args, commandSender);
            default:
                return false;
        }
    }

    private boolean run_command(String[] args, CommandSender commandSender) {
        if (args[0] == null) {return false;}
        switch (args[0]) {
            case "start":
                crystalBlitz.getInstance().is_force_starting = true;
                return true;
            case "end":
                crystalBlitz.getInstance().gamemanager.ForceEndGame();
                return true;
            case "give":
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("This command can only be ran as a player");
                }
                Player p = (Player) commandSender;
                switch (args[1]) {
                    case "weak":
                        ItemStack weak = CrystalBlitzItems.WeakShard.clone();
                        weak.setAmount(64);
                        p.getInventory().addItem(weak);
                        break;
                    case "strong":
                        ItemStack strong = CrystalBlitzItems.StrongShard.clone();
                        strong.setAmount(64);
                        p.getInventory().addItem(strong);
                        break;
                    case "nexus":
                        ItemStack nexus = CrystalBlitzItems.NexusShard.clone();
                        nexus.setAmount(64);
                        p.getInventory().addItem(nexus);
                        break;
                    default:
                        commandSender.sendMessage("[!] Unknown strong \"" + args[1] + "\"");
                        break;
                }
                return true;
            default:
                commandSender.sendMessage("[!] Unknown subcommand(s) \"" + args.toString() + "\"");
                break;
        }
        return false;
    }
}
