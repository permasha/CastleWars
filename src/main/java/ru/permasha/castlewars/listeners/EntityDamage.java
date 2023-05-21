package ru.permasha.castlewars.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;

public class EntityDamage implements Listener {

    CastleWars plugin;

    public EntityDamage(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity.getWorld().getName().equals("world")) {
            event.setCancelled(true);
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;

            Game game = plugin.getGame(player);
            if (game != null) {
                if (!game.isState(Game.GameState.ACTIVE)) {
                    event.setCancelled(true);
                }
            }
        }


    }

}
