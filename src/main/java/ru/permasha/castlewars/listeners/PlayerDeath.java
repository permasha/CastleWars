package ru.permasha.castlewars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.*;
import ru.permasha.customjumppads.CustomJumpPads;
import ru.xezard.glow.data.glow.manager.GlowsManager;

public class PlayerDeath implements Listener {

    CastleWars plugin;

    public PlayerDeath(CastleWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        handle(event);
    }

    private void handle(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player || event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof Player player) {
                Game game = plugin.getGame(player);

                if (game != null && game.getGamePlayer(player) != null) {
                    // All entities damage
                    if (isGameNeedState(game) || isSpectator(game.getGamePlayer(player))) {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }

                    // Player damage
                    if (event.getDamager() instanceof Player damager) {
                        if (isSpectator(game.getGamePlayer(damager))) {
                            event.setCancelled(true);
                        } // is Player in spectator mode
                        if (game.getGamePlayer(player).getTeam().getGamePlayers().contains(game.getGamePlayer(damager))) {
                            event.setCancelled(true);
                        } // is player and damager in one team

                        game.getGamePlayer(player).setDamager(damager);
                    }
                }
            }

            // Player damage all entities
            if (event.getDamager() instanceof Player damager) {
                Game game = plugin.getGame(damager);

                if (game != null && game.getGamePlayer(damager) != null) {
                    if (isGameNeedState(game) || isSpectator(game.getGamePlayer(damager))) {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            Game game = plugin.getGame(player);

            if (game != null && game.getGamePlayer(player) != null) {
                GamePlayer gamePlayer = game.getGamePlayer(player);

                if (gamePlayer.getPlayer() == player) {
                    handle(event, game);
                }
            }
        }
    }

    private void handle(EntityDamageEvent event, Game game) {
        Player player = (Player) event.getEntity();

        if (isGameNeedState(game) || isSpectator(game.getGamePlayer(player))) {
            event.setDamage(0);
            event.setCancelled(true);
        }

        if (player.getHealth() - event.getFinalDamage() <= 0) {
            if (!CustomJumpPads.getInstance().getManager().getFallPlayers().contains(player)) {
                event.setCancelled(true);

                GamePlayer gamePlayer = game.getGamePlayer(player);
                if (gamePlayer.hasFlag()) {
                    plugin.getGameManager().createRecoveryBossBar(game, gamePlayer);
                    player.getEquipment().setHelmet(null);
                }

                deathGamePlayer(game, game.getGamePlayer(player));
            }
        }
    }

    @EventHandler
    public void onHealthRegain(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player) {
            Game game = plugin.getGame(player);

            if (game != null && game.getGamePlayer(player) != null) {
                GamePlayer gamePlayer = game.getGamePlayer(player);
                if (gamePlayer.getPlayer() == player) {
                    handle(event, game);
                }
            }
        }
    }

    private void handle(EntityRegainHealthEvent event, Game game) {
        Player player = (Player) event.getEntity();
        int h = (int) (player.getHealth() + event.getAmount());

        if (h == (int) player.getHealthScale()) {
            game.getGamePlayer(player).setDamager(null);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGame(player);

        if (game != null && game.getGamePlayer(player) != null) {
            GamePlayer gamePlayer = game.getGamePlayer(player);
            if (gamePlayer.getPlayer() == player) {
                handle(event, game);
            }
        }
    }

    private void handle(PlayerMoveEvent event, Game game) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getGamePlayer(player);

        if (!isSpectator(gamePlayer)) {
            if (player.getLocation().getY() < 0) {
                if (gamePlayer.hasFlag()) {
                    Team enemyTeam = plugin.getGameManager().getEnemyTeam(game, gamePlayer);
                    Flag enemyFlag = game.getFlagByTeam(enemyTeam);
                    enemyFlag.setStolen(false);
                    enemyFlag.spawnFlag(game.getArena(), enemyFlag.getFromLocation());
                    game.broadcastMessage("&fФлаг команды " + enemyTeam.getColor() + enemyTeam.getName() + " &fвернулся на базу");
                    player.getEquipment().setHelmet(null);
                }

                deathGamePlayer(game, gamePlayer);
            }
        }
    }

    public void deathGamePlayer(Game game, GamePlayer gamePlayer) {
        if (gamePlayer.getDamager() != null) { // If Damager exists
            Team team = gamePlayer.getTeam();

            Player damager = gamePlayer.getDamager();

            GamePlayer damagerPlayer =  game.getGamePlayer(damager); // Damager
            game.broadcastActionBar(damagerPlayer.getTeam().getColor() + damager.getName() + " &c✖&f " + gamePlayer.getTeam().getColor() + gamePlayer.getName());
            damagerPlayer.addKill(); // Local kill
            damagerPlayer.addBalance(1);

            if (game.getArena().getMode().equals(Game.Mode.DEATHMATCH)) {
                Team damagerTeam = gamePlayer.getTeam();
                damagerTeam.addScore(1);
                plugin.getGameManager().damageTeam(game, team, 2);
            }

            PlayerData damagerData = new PlayerData(damagerPlayer.getPlayer());
            damagerData.addKill();
            damagerData.addBalance(1);

            Kit kit = new Kit(damagerPlayer);
            damagerPlayer.getPlayer().getInventory().setItem(8, kit.getShop());

            plugin.getGameManager().damageTeam(game, team, 2);
            gamePlayer.setDamager(null);
        } else { // If Damager not exists
            game.broadcastActionBar("&f" + gamePlayer.getPlayer().getName() + " &fсамоубился");
        }

        gamePlayer.getPlayer().getActivePotionEffects().clear();
        GlowsManager.getInstance().removeGlowFrom(gamePlayer.getPlayer());
        gamePlayer.setFlag(false);
        gamePlayer.addDeath();
        PlayerData playerData = new PlayerData(gamePlayer.getPlayer());
        playerData.addDeath();
        plugin.getGameManager().respawnGamePlayer(gamePlayer);
    }

    private boolean isGameNeedState(Game game) {
        return game.isState(Game.GameState.LOBBY) ||
                game.isState(Game.GameState.STARTING) ||
                game.isState(Game.GameState.ENDING);
    }

    private boolean isSpectator(GamePlayer gamePlayer) {
        return gamePlayer.getTeam().isSpectator();
    }

}
