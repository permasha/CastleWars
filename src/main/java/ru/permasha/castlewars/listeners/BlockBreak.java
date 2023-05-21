package ru.permasha.castlewars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;
import ru.permasha.castlewars.objects.Team;

import java.util.List;

public class BlockBreak implements Listener {

    CastleWars plugin;

    public BlockBreak(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().getName().equals("world")) {
            event.setCancelled(true);
        }

        Game game = plugin.getGame(player);
        if (!plugin.getGameManager().canBreak(game, event.getBlock())) {
            event.setCancelled(true);
        }
    }

}
