package ru.permasha.castlewars.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.tasks.GameRunTask;

public class CastleWarsAdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (!sender.hasPermission("cw.admin")) {
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            Player player = Bukkit.getPlayer(sender.getName());

            Game game = CastleWars.getInstance().getGame(player);

            if (game == null) {
                sender.sendMessage("Вы должны быть в игре!");
                return true;
            }

            if (game.isState(Game.GameState.STARTING) || game.isState(Game.GameState.LOBBY)) {
                sender.sendMessage("Запускаем...");

                new GameRunTask(game).runTaskTimer(CastleWars.getInstance(), 0, 20);
                return true;
            }
        }

        return true;
    }

}
