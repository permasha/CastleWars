package ru.permasha.castlewars.listeners;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.api.RandomCraftAPI;

public class PlayerJoin implements Listener {

    CastleWars plugin;

    public PlayerJoin(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        event.setJoinMessage(null);

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(25);

        player.teleport(plugin.getLobbyPoint());

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        plugin.getDatabase().insertPlayerData(player.getUniqueId());
        FastBoard board = new FastBoard(player);
        plugin.getSideBars().put(player.getUniqueId(), board);

        player.setAllowFlight(false);
        player.setFlying(false);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getWorld() == world) {
                p.showPlayer(plugin, player);
                player.showPlayer(plugin, p);
                continue;
            }
            p.hidePlayer(plugin, player);
            player.hidePlayer(plugin, p);
        }
    }

}
