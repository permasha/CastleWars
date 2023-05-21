package ru.permasha.castlewars.listeners;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.utils.Storage;

import java.util.Map;

public class EntityShootBow implements Listener {

    CastleWars plugin;

    public EntityShootBow(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player player))
            return;
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        if (player.getWorld().getName().equals("world"))
            return;
        String playerName = player.getName();
        if (Storage.arrowsRecoveryTasks.containsKey(playerName))
            scheduler.cancelTask(Storage.arrowsRecoveryTasks.remove(playerName));
        Runnable arrowsRecovery = () -> {
            PlayerInventory inventory = player.getInventory();
            int amount = 0;
            for (Map.Entry<Integer, ? extends ItemStack> entry : inventory.all(Material.ARROW).entrySet())
                amount += entry.getValue().getAmount();
            if (amount > 7)
                return;
            inventory.addItem(new ItemStack(Material.ARROW, 1));
        };
        int taskId = scheduler.scheduleAsyncRepeatingTask(plugin, arrowsRecovery, 50L, 50L);
        Storage.arrowsRecoveryTasks.put(playerName, taskId);
    }

}
