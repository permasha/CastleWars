package ru.permasha.castlewars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import ru.permasha.castlewars.CastleWars;

public class PlayerItemConsume implements Listener {

    CastleWars plugin;

    public PlayerItemConsume(CastleWars plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (!e.getItem().getType().equals(Material.POTION))
            return;
        Player p = e.getPlayer();
        int heldSlot = p.getInventory().getHeldItemSlot();
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            ItemStack held = p.getInventory().getItem(heldSlot);
            ItemStack off = p.getInventory().getItemInOffHand();
            if (held != null && held.getType() == Material.GLASS_BOTTLE) {
                held.setAmount(0);
            }
            if (off.getType() == Material.GLASS_BOTTLE) {
                off.setAmount(0);
            }
        },1L);
    }

}
