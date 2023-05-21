package ru.permasha.castlewars.listeners;

import com.google.common.cache.LoadingCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Banner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Flag;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.objects.GamePlayer;
import ru.permasha.castlewars.objects.Team;
import ru.xezard.glow.GlowAPI;
import ru.xezard.glow.data.glow.Glow;
import ru.xezard.glow.data.glow.manager.GlowsManager;

public class PlayerMove2 implements Listener {

    CastleWars plugin;

    public PlayerMove2(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location to = event.getTo();
        Location from = event.getFrom();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        Game game = plugin.getGame(player);
        if (game != null) {
            if (!game.getArena().getMode().equals(Game.Mode.CTF)) {
                return;
            }
            Location location = player.getLocation();
            Flag flag = game.getFlagDropFromLocation(location);

            if (flag == null) {
                return;
            }
            GamePlayer gamePlayer = game.getGamePlayer(player);
            Team flagTeam = flag.getTeam();

            if (flag.isStolen() && flag.getFromLocation().equals(flag.getDrop())) {
                return;
            }

            if (gamePlayer.getTeam().getName().equalsIgnoreCase(flagTeam.getName())) {
                return;
            }

            if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                return;
            }

            flag.setStolen(true);
            gamePlayer.setFlag(true);
            flag.setDrop(flag.getFromLocation());
            Entity flagStand = flag.getEntity();
            game.sendTitle("&fФлаг команды " + flag.getTeam().getColor() + flag.getTeam().getName() + " &fукраден",
                    "игроком " + gamePlayer.getName(), 20, 100, 20);
            gamePlayer.sendMessage("&fВы украли флаг команды " + flag.getTeam().getColor() + flag.getTeam().getName());

            Glow glow = flag.getGlow();
            glow.removeHolders(flagStand);
            glow.addHolders(player);

            ArmorStand armorStand = (ArmorStand) flagStand;
            player.getEquipment().setHelmet(armorStand.getEquipment().getHelmet());

            plugin.getGameManager().createStolenBossBar(game, gamePlayer);

            flagStand.remove();
        }
    }

}
