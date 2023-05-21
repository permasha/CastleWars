package ru.permasha.castlewars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;

public class GameActiveTask extends BukkitRunnable {

    private final Game game;
    private int endIn;

    public GameActiveTask(Game game) {
        this.game = game;
        this.endIn = game.getArena().getTime()*60;
    }

    @Override
    public void run() {

        if (!CastleWars.getInstance().getGames().contains(game)) {
            cancel();
        }

        if (!game.getGameState().equals(Game.GameState.ACTIVE)) {
            cancel();
        }

        if (endIn <= 1) {
            this.cancel();
            new GameEndingTask(game).runTaskTimer(CastleWars.getInstance(), 0, 20);
        } else {
            endIn -= 1;
            this.game.setTime(endIn);
        }
    }
}
