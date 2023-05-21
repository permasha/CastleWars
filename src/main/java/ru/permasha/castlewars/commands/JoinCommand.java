package ru.permasha.castlewars.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;
import ru.permasha.castlewars.utils.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class JoinCommand extends SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof Player player) {

            if (args.length != 0) {
                player.sendMessage(Utils.colorize("&cCastleWars &7>> &cСлишком много аргументов!"));
                return;
            }

            for (Game game : CastleWars.getInstance().getGames()) {
                for (GamePlayer gamePlayer : game.getPlayers()) {
                    if (gamePlayer.getPlayer() == player) {
                        player.sendMessage(Utils.colorize("&cCastleWars &7>> &cТы уже в игре!"));
                        return;
                    }
                }
            }

            Set<Game> availableGames = new HashSet<>();
            for (Game game : CastleWars.getInstance().getGames()) {
                if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING)) {
                    if (game.getPlayersSize() != game.getArena().getMaxPlayers()) {
                        availableGames.add(game);
                    }
                }
            }

            if (availableGames.isEmpty()) {
                player.sendMessage(Utils.colorize("&cCastleWars &7>> &cСвободных игр нет"));
                return;
            }

            int maxSize = availableGames.stream().max(Comparator.comparing(Game::getPlayersSize)).get().getPlayersSize();

            Game game;
            if (maxSize == 0)
                game = availableGames.stream().toList().get(new Random().nextInt(availableGames.size()));
            else
                game = availableGames.stream().max(Comparator.comparing(Game::getPlayersSize)).get();


            game.joinGame(new GamePlayer(player));

        } else {
            commandSender.sendMessage(Utils.colorize("&cCastleWars &7>> &cТы не игрок!"));
        }
    }

}
