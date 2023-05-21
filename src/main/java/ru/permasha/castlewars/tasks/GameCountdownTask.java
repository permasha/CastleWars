package ru.permasha.castlewars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;

public class GameCountdownTask extends BukkitRunnable {

    private int time = 20;
    private final Game game;

    public GameCountdownTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        time -= 1;

        if (time == 0) {
            // Start
            cancel();

            new GameRunTask(game).runTaskTimer(CastleWars.getInstance(), 0, 20);

        } else {
            if (game.getPlayers().size() < game.getArena().getMinPlayers()){
                cancel();
                game.setState(Game.GameState.LOBBY);
            }
            game.setTime(time);
            if (time == 15 || time == 10 || time == 5) {
                game.broadcastActionBar("&aВы будете телепортированы через " + time + " секунд");
            }
        }
    }

}
