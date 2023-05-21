package ru.permasha.castlewars.listeners;

import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;
import ru.permasha.castlewars.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;

public class AsyncPlayerChat implements Listener {

    CastleWars plugin;

    public AsyncPlayerChat(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().equals(plugin.getLobbyPoint().getWorld())) {
            plugin.getLobbyPoint().getWorld().getPlayers().forEach(p -> {
                String format = PlaceholderAPI.setPlaceholders(player, "%vault_prefix%" + "%luckperms_meta_staff-prefix%" + "%cw_custom_name%" + "%vault_suffix%"
                        + " &8»&f " + event.getMessage());
                p.sendMessage(Utils.colorize(format));
            });
            if (plugin.getLobbyPoint().getWorld().getPlayers().size() < 2) {
                player.sendMessage(Utils.colorize("&cНа сервере сейчас нет игроков :("));
            }
        }

        if (plugin.getGame(player) != null) {
            Collection<Player> recipients = new ArrayList<>();
            String chatType;
            Game game = plugin.getGame(player);
            GamePlayer gamePlayer = game.getGamePlayer(player);
            if (event.getMessage().startsWith("!")) {
                chatType = "&7[Всем] ";
                game.getPlayers().forEach(pipler -> recipients.add(pipler.getPlayer()));
            } else {
                chatType = "&7[Команде] ";
                gamePlayer.getTeam().getGamePlayers().forEach(pipiler -> recipients.add(pipiler.getPlayer()));
            }

            String solidText = event.getMessage();
            if (solidText.startsWith("!")) solidText = solidText.replaceFirst("!", "");
            String format = PlaceholderAPI.setPlaceholders(player, chatType + "%cw_flag%" + "%vault_prefix%" + "%luckperms_meta_staff-prefix%" + "%cw_custom_name%" + "%vault_suffix%"
                    + " &8»&f " + solidText);

            recipients.forEach(pupler -> pupler.sendMessage(Utils.colorize(format)));
        }

        Bukkit.getLogger().info(player.getName() + " » " + event.getMessage());

        event.setCancelled(true);
    }

}
