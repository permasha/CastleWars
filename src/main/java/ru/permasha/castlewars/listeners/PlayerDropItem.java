package ru.permasha.castlewars.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;
import ru.permasha.castlewars.objects.Kit;

public class PlayerDropItem implements Listener {

    CastleWars plugin;

    public PlayerDropItem(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getItemDrop().getItemStack();

        if (stack.getType().equals(Material.BARRIER) || stack.getType().equals(Material.ARROW) || stack.getType().equals(Material.NAME_TAG)) {
            event.setCancelled(true);
        }

        Game game = plugin.getGame(player);
        if (game != null) {
            GamePlayer gamePlayer = game.getGamePlayer(player);
            Kit kit = new Kit(gamePlayer);
            if (stack.equals(kit.getHealthPotion()) || stack.equals(kit.getSpeedHealth())) {
                event.setCancelled(true);
            }
        }
    }


}
