package ru.permasha.castlewars.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.permasha.castlewars.CastleWars;

public class PlayerInteract implements Listener {

    CastleWars plugin;

    public PlayerInteract(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().getName().equals("world")) {
            event.setCancelled(true);
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.BARRIER)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                player.chat("/cw leave");
            }
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity.getType().equals(EntityType.ARMOR_STAND)) {
            event.setCancelled(true);
        }
    }

}
