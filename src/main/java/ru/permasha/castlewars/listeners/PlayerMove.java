package ru.permasha.castlewars.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;

public class PlayerMove implements Listener {

    CastleWars plugin;

    public PlayerMove(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().getName().equalsIgnoreCase("world")) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
                if (event.getTo() != null && event.getTo().getBlock().getType().equals(Material.END_GATEWAY)) {
                    player.chat("/cw join");
                }
            }
        }

        Game game = plugin.getGame(player);
        if (game != null) {
            if (game.getGameState() == Game.GameState.STARTING && player.getLocation().getY() <= 10) {
                player.teleport(game.getArena().getLobbyPoint());
            }
            if (game.getGameState() == Game.GameState.LOBBY && player.getLocation().getY() <= 10) {
                player.teleport(game.getArena().getLobbyPoint());
            }
        }
    }

}
