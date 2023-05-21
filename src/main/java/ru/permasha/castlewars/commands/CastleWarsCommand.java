package ru.permasha.castlewars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.permasha.castlewars.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CastleWarsCommand implements CommandExecutor {

    private final JoinCommand joinCommand;
    private final ListCommand listCommand;
    private final LeaveCommand leaveCommand;

    public CastleWarsCommand() {
        this.joinCommand = new JoinCommand();
        this.listCommand = new ListCommand();;
        this.leaveCommand = new LeaveCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(Utils.colorize("&cCastleWars &7>> &f/castlewars &f&ljoin &7- &fПрисоединиться к арене"));
        } else {
            String argument = args[0];
            List<String> newArgs = new ArrayList<>();

            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    continue;
                }

                newArgs.add(args[i]);
            }

            if (argument.equalsIgnoreCase("join")) this.joinCommand.execute(sender, newArgs.toArray(new String[0]));
            else if (argument.equalsIgnoreCase("list")) this.listCommand.execute(sender, newArgs.toArray(new String[0]));
            else if (argument.equalsIgnoreCase("leave")) this.leaveCommand.execute(sender, newArgs.toArray(new String[0]));
            else sender.sendMessage(Utils.colorize("&cCastleWars &7>> &cКоманда не найдена!"));
        }

        return false;
    }

}
