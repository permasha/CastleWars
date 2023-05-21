package ru.permasha.castlewars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;

public class BlockExplode implements Listener {

    CastleWars plugin;

    public BlockExplode(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent event) {
        World world = event.getLocation().getWorld();
        Game game = plugin.getGame(world);

        event.blockList().removeIf(block ->
                !plugin.getGameManager().canBreak(game, block)
        );
    }

}
