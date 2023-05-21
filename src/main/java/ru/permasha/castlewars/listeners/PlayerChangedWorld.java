package ru.permasha.castlewars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import ru.permasha.castlewars.CastleWars;
import ru.xezard.glow.data.glow.manager.GlowsManager;

public class PlayerChangedWorld implements Listener {

    CastleWars plugin;

    public PlayerChangedWorld(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        Bukkit.getServer().getBossBars().forEachRemaining(keyedBossBar -> {
            keyedBossBar.removePlayer(player);
        });

        GlowsManager.getInstance().removeGlowFrom(player);

        if (player.getWorld().getName().equals("world")) {
            player.teleport(plugin.getLobbyPoint());
            player.setAllowFlight(false);
            player.setFlying(false);
        }

        World worldfrom = event.getFrom();
        for (Player p : world.getPlayers()) {
            p.showPlayer(plugin, player);
            player.showPlayer(plugin, p);
        }
        for (Player p : worldfrom.getPlayers()) {
            p.hidePlayer(plugin, player);
            player.hidePlayer(plugin, p);
        }
    }

}
