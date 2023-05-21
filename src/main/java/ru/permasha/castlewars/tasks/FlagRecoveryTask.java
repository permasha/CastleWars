package ru.permasha.castlewars.tasks;

import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Flag;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;
import ru.permasha.castlewars.objects.Team;
import ru.permasha.castlewars.utils.Utils;

public class FlagRecoveryTask extends BukkitRunnable {

    private double time = 30.0;

    Game game;

    Flag flag;

    BossBar bar;

    public FlagRecoveryTask(Game game, Flag flag, BossBar bar) {
        this.game = game;
        this.flag = flag;
        this.bar = bar;
    }

    @Override
    public void run() {
        time -= 1.0;

        if (time <= 1) {
            // Recovery
            bar.removeAll();
            flag.getEntity().remove();
            flag.spawnFlag(game.getArena(), flag.getFromLocation());
            flag.setDrop(flag.getFromLocation());
            flag.setStolen(false);
            game.broadcastMessage("&fФлаг команды " + flag.getTeam().getColor() + flag.getTeam().getName() + " &fвернулся на базу");
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

        if (flag.getEntity() == null) {
            bar.removeAll();
            cancel();
        }

        if (flag.isStolen()) {
            bar.removeAll();
            cancel();
        }

        double progress = getPercentTime() / 100;
        if (progress >= 1.0F) {
            bar.setProgress(1.0F);
        } else {
            bar.setProgress(progress);
            String name = Utils.colorize("Флаг " + flag.getTeam().getColor() + flag.getTeam().getName() +
                    "&f вернётся через: &e" + (int) time + " сек.");
            bar.setTitle(name);
        }
    }

    public double getPercentTime() {
        double val = 30.0;
        double step = val / 100;

        double finalTime = time / step;

        return Math.floor(finalTime);
    }
}
