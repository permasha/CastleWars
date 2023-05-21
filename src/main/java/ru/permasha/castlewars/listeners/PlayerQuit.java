package ru.permasha.castlewars.listeners;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;

public class PlayerQuit implements Listener {

    CastleWars plugin;

    public PlayerQuit(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

        plugin.getSideBars().get(player.getUniqueId()).delete();
        plugin.getSideBars().remove(player.getUniqueId());

        Game game = plugin.getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            GamePlayer gamePlayer = game.getGamePlayer(player);

            if (gamePlayer.getPlayer() == player) {
                game.leaveGame(gamePlayer);
            }
        }
    }

}
