package ru.permasha.castlewars.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;

public class BlockPlace implements Listener {

    CastleWars plugin;

    public BlockPlace(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        Game game = plugin.getGame(player);
        if (!plugin.getGameManager().canBreak(game, event.getBlockPlaced())) {
            event.setCancelled(true);
        }
    }

}
