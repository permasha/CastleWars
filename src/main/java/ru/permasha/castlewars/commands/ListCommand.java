package ru.permasha.castlewars.commands;

import org.bukkit.command.CommandSender;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.utils.Utils;

public class ListCommand extends SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        for (Game game : CastleWars.getInstance().getGames()) {
            sender.sendMessage(Utils.colorize("&cCastleWars &7>> &f" + game.getArena().getDisplayName() + " - " + game.getPlayers().size() + " игроков"));
        }
    }
}
