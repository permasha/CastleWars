package ru.permasha.castlewars.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.utils.Utils;

public class LeaveCommand extends SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {

            if (args.length != 0) {
                player.sendMessage(Utils.colorize("&cCastleWars &7>> &c/castlewars leave"));
            } else {

                Game game = CastleWars.getInstance().getGame(player);
                if (game == null) {
                    player.sendMessage(Utils.colorize("&cCastleWars &7>> &cТы не в игре!"));
                    return;
                }

                game.leaveGame(game.getGamePlayer(player));
            }
        } else {
            sender.sendMessage(Utils.colorize("&cCastleWars &7>> &cТы не игрок!"));
        }
    }
}
