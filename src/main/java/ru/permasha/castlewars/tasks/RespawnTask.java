package ru.permasha.castlewars.tasks;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;
import ru.permasha.castlewars.utils.Utils;

public class RespawnTask extends BukkitRunnable {

    GamePlayer gamePlayer;

    private int startIn = 5;

    public RespawnTask(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void run() {
        Player player = gamePlayer.getPlayer();
        if (startIn <= 1) {
            this.cancel();
            CastleWars.getInstance().getGameManager().getKitPlayer(gamePlayer);
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(gamePlayer.getTeam().getLocation());
            player.setHealth(20);
        } else {
            startIn -= 1;
            player.sendTitle(Utils.colorize("&6" + startIn), "", 1, 20, 10);
            if (gamePlayer == null) {
                cancel();
            }
        }
    }

}
