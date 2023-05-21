package ru.permasha.castlewars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;

public class GameRunTask extends BukkitRunnable {

    private final Game game;
    private int startIn = 5;

    public GameRunTask(Game game) {
        this.game = game;
        this.game.setState(Game.GameState.PREPARATION);
        this.game.assignSpawnPositions();
        this.game.broadcastActionBar("&aИгра начнётся через " + this.startIn + " секунд...");
    }

    @Override
    public void run() {
        if (startIn <= 1) {
            this.cancel();
            this.game.setState(Game.GameState.ACTIVE);
            this.game.broadcastMessage("&aИгра началась!");
            if (game.getArena().getMode().equals(Game.Mode.CTF)) {
                this.game.assignSpawnFlags();
            }

            new GameActiveTask(game).runTaskTimer(CastleWars.getInstance(), 0, 20);
        } else {
            startIn -= 1;
//            this.game.sendMessage("&b[*] " + startIn);
            this.game.sendTitle("&6" + startIn, "", 1,20,10);
            if (game.getPlayers().size() < 1) {
                cancel();
                game.end();
            }

        }
    }
}
