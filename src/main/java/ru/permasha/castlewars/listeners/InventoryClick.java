package ru.permasha.castlewars.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.permasha.castlewars.CastleWars;

public class InventoryClick implements Listener {

    CastleWars plugin;

    public InventoryClick(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack != null) {
            if (itemStack.getType().equals(Material.NAME_TAG)) {
                event.setCancelled(true);
            }

            if (itemStack.getType().toString().endsWith("_BANNER")) {
                event.setCancelled(true);
            }
        }
    }

}
