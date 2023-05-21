package ru.permasha.castlewars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import ru.permasha.castlewars.CastleWars;

public class FoodLevelChange implements Listener {

    CastleWars plugin;

    public FoodLevelChange(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}
