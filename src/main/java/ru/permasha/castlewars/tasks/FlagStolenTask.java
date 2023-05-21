package ru.permasha.castlewars.tasks;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;
import ru.permasha.castlewars.objects.Team;
import ru.permasha.castlewars.utils.Utils;

public class FlagStolenTask extends BukkitRunnable {

    Game game;

    GamePlayer gamePlayer;

    BossBar bar;

    public FlagStolenTask(Game game, GamePlayer gamePlayer, BossBar bar) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.bar = bar;
    }

    @Override
    public void run() {
        if (!gamePlayer.hasFlag()) {
            bar.removeAll();
            cancel();
        }

        if (!CastleWars.getInstance().getGames().contains(game)) {
            bar.removeAll();
            cancel();
        }

        if (!game.getGameState().equals(Game.GameState.ACTIVE)) {
            bar.removeAll();
            cancel();
        }

        if (gamePlayer == null) {
            bar.removeAll();
            cancel();
        }

        double progress = CastleWars.getInstance().getGameManager().getPercentLocToFlag(game, gamePlayer) / 100;
        if (progress >= 1.0F) {
            bar.setProgress(1.0F);
        } else {
            bar.setProgress(progress);
        }
    }
}
