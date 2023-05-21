package ru.permasha.castlewars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.*;
import ru.xezard.glow.data.glow.manager.GlowsManager;

public class PlayerMove3 implements Listener {

    CastleWars plugin;

    public PlayerMove3(CastleWars plugin) {
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
            Flag flag = game.getFlagFromLocation(location);
            if (flag == null) {
                return;
            }

            GamePlayer gamePlayer = game.getGamePlayer(player);
            Team flagTeam = flag.getTeam();

            if (!gamePlayer.hasFlag()) {
                return;
            }

            if (!flagTeam.getName().equalsIgnoreCase(gamePlayer.getTeam().getName())) {
                return;
            }

            if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                return;
            }

            Team enemyTeam = plugin.getGameManager().getEnemyTeam(game, gamePlayer);
            Flag enemyFlag = game.getFlagByTeam(enemyTeam);

            enemyFlag.setStolen(false);
            gamePlayer.setFlag(false);

            GlowsManager.getInstance().removeGlowFrom(player);

            Kit kit = new Kit(gamePlayer);
            player.getEquipment().setHelmet(kit.getHelmet());
            enemyFlag.spawnFlag(game.getArena(), enemyFlag.getFromLocation());
            player.setHealth(20.0D);
            player.getActivePotionEffects().clear();

            game.sendTitle("&fФлаг команды " + enemyTeam.getColor() + enemyTeam.getName() + " &fдоставлен",
                    "на базу противника", 20, 100, 20);

            plugin.getGameManager().damageTeam(game, enemyTeam, 50);
            gamePlayer.getTeam().addScore(1);
            gamePlayer.addBalance(6);

            player.getInventory().setItem(8, kit.getShop());
        }
    }

}
