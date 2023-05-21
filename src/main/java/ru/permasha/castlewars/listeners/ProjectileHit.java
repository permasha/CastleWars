package ru.permasha.castlewars.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import ru.permasha.castlewars.CastleWars;

public class ProjectileHit implements Listener {

    CastleWars plugin;

    public ProjectileHit(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e){
        if (e.getEntity().getType() == EntityType.ARROW || e.getEntity().getType() == EntityType.SPECTRAL_ARROW) {
            e.getEntity().remove();
        }
    }

}
