package ru.permasha.castlewars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;

public class GameEndingTask extends BukkitRunnable {

    private final Game game;
    private int endIn = 10;

    public GameEndingTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

        if (!CastleWars.getInstance().getGames().contains(game)) {
            cancel();
        }

        if (endIn <= 1) {
            this.cancel();
            this.game.end();
        } else {
            endIn -= 1;
            this.game.setTime(endIn);
        }

    }

}
