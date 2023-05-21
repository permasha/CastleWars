package ru.permasha.castlewars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.permasha.castlewars.CastleWars;

public class EntityDeath implements Listener {

    CastleWars plugin;

    public EntityDeath(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

}
