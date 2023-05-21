package ru.permasha.castlewars.managers;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.*;
import ru.permasha.castlewars.tasks.*;
import ru.permasha.castlewars.utils.Utils;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GameManager {

    CastleWars plugin;

    public GameManager(CastleWars plugin) {
        this.plugin = plugin;
    }

    HashMap<String, BossBar> playersBossBars = new HashMap<>();

    public void respawnGamePlayer(GamePlayer gamePlayer) {
        Team team = gamePlayer.getTeam();
        if (!team.isSpectator()) {
            Player player = gamePlayer.getPlayer();
            player.teleport(team.getLocation());
            player.setGameMode(GameMode.SPECTATOR);
            new RespawnTask(gamePlayer).runTaskTimer(CastleWars.getInstance(), 0, 20);
        }
    }

    public void damageTeam(Game game, Team team, int health) {
        if ((team.getHealth() - health) <= 0) {
            team.setTeamState(Team.TeamState.SPECTATOR);
            team.setSpectator(true);
            game.activateSpectator(team);
            team.setHealth(0);
            plugin.getGameManager().endGameOnDeathTeam(game);
        } else {
           team.removeHealth(health);
        }
    }

    public void getKitPlayer(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        PlayerInventory inventory = player.getInventory();
        Kit kit = new Kit(gamePlayer);

        if (inventory.getBoots() == null) {
            inventory.setBoots(kit.getBoots());
        }
        if (inventory.getLeggings() == null) {
            inventory.setLeggings(kit.getLeggings());
        }
        if (inventory.getChestplate() == null) {
            inventory.setChestplate(kit.getChestPlate());
        }
        if (inventory.getHelmet() == null) {
            inventory.setHelmet(kit.getHelmet());
        }
        if (!inventory.contains(kit.getSword())) {
            inventory.addItem(kit.getSword());
        }
        if (!inventory.contains(kit.getBow())) {
            inventory.addItem(kit.getBow());
        }

        if (!inventory.contains(kit.getHealthPotion())) {
            inventory.addItem(kit.getHealthPotion());
        }

        if (!inventory.contains(kit.getSpeedHealth())) {
            inventory.addItem(kit.getSpeedHealth());
        }

        inventory.setItem(9, kit.getArrows());
        inventory.setItem(8, kit.getShop());
    }

    public void endGameOnDeathTeam(Game game) {
        if (!game.getSpectatorTeams().isEmpty()) {
            for (Team loseTeam : game.getSpectatorTeams()) {
                loseTeam.getGamePlayers().forEach(losePlayer -> {
                    losePlayer.sendTitle("&cПоражение!", "&fВаша команда проиграла!", 20, 100, 20);
                });
            }
        }

        if (!game.getLivingTeams().isEmpty() || game.getLivingTeams().size() <= 1) {
            Team winnerTeam = game.getLivingTeams().get(0);

            for (GamePlayer winnerPlayer : winnerTeam.getGamePlayers()) {
                Player player = winnerPlayer.getPlayer();
                PlayerData playerData = new PlayerData(player);
                playerData.addWin();
                playerData.addBalance(10);

                winnerPlayer.sendTitle("&aПобеда!", "&fВаша команда выиграла!", 20, 100, 20);
            }

            for (Player player : game.getArena().getWorld().getPlayers()) {
                player.setGameMode(GameMode.SPECTATOR);
                player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
                player.setHealth(20);

                player.getInventory().clear();
                game.giveLeaveGame(player);

                for (Player p : game.getArena().getWorld().getPlayers())
                    player.showPlayer(CastleWars.getInstance(), p);

                game.getGamePlayer(player).setDamager(null);
            }

            game.setState(Game.GameState.ENDING);
            new GameEndingTask(game).runTaskTimer(CastleWars.getInstance(), 0, 20);
        }
    }

    public double getDistanceToFlagLoc(Player player, Flag flag) {
        Location location = player.getLocation();
        Location toLocation = flag.getToLocation();

        return location.distance(toLocation);
    }

    public double getPercentLocToFlag(Game game, GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        Flag flag = game.getFlagByTeam(getEnemyTeam(game, gamePlayer));

        Location location = player.getLocation();
        Location fromLocation = flag.getFromLocation();
        Location toLocation = flag.getToLocation();

        double distance = Math.floor(fromLocation.distance(toLocation));
        double step = distance / 100;

        double distanceFromLoc = fromLocation.distance(location);
        double finalPercent = distanceFromLoc / step;

        return Math.floor(finalPercent);
    }

    public Team getEnemyTeam(Game game, GamePlayer gamePlayer) {
        for (Team team : game.getLivingTeams()) {
            if (!team.getName().equalsIgnoreCase(gamePlayer.getTeam().getName())) {
                return team;
            }
        }
        return null;
    }

    public boolean canBreak(Game game, Block block) {
        if (game != null) {
            if (game.isState(Game.GameState.ENDING)) {
                return false;
            }
            for (Team team : game.getLivingTeams()) {
                Location spawnLoc = team.getLocation();
                if (block.getLocation().distance(spawnLoc) <= 5) {
                    return false;
                }
            }

            Collection<Entity> nearby = block.getWorld().getNearbyEntities(block.getLocation(), 3, 3, 3);
            if (!nearby.isEmpty()) {
                for (Entity entity : nearby) {
                    if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void createStolenBossBar(Game game, GamePlayer gamePlayer) {
        Team enemyTeam = getEnemyTeam(game, gamePlayer);

        String name = Utils.colorize(gamePlayer.getName() + " украл флаг " + enemyTeam.getColor() + enemyTeam.getName());
        BossBar bar = Bukkit.getServer().createBossBar(name, BarColor.valueOf(enemyTeam.getName()), BarStyle.SEGMENTED_12);
        game.getArena().getWorld().getPlayers().forEach(bar::addPlayer);

        new FlagStolenTask(game, gamePlayer, bar).runTaskTimerAsynchronously(plugin, 20L, 20L);
    }

    public void createRecoveryBossBar(Game game, GamePlayer gamePlayer) {
        Team enemyTeam = getEnemyTeam(game, gamePlayer);
        Flag flag = game.getFlagByTeam(enemyTeam);

        flag.setStolen(false);
        flag.setDrop(gamePlayer.getPlayer().getLocation());
        flag.spawnFlag(game.getArena(), flag.getDrop());

        game.broadcastMessage(gamePlayer.getName() + " &fпотерял флаг " + enemyTeam.getColor() + enemyTeam.getName());

        String name = Utils.colorize("Флаг " + enemyTeam.getColor() + enemyTeam.getName() + "&f вернётся через: &e120 сек.");
        BossBar bar = Bukkit.getServer().createBossBar(name, BarColor.valueOf(enemyTeam.getName()), BarStyle.SEGMENTED_12);
        game.getArena().getWorld().getPlayers().forEach(bar::addPlayer);

        new FlagRecoveryTask(game, flag, bar).runTaskTimer(plugin, 20L, 20L);
    }

}
