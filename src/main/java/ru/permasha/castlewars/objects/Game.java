package ru.permasha.castlewars.objects;

import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.database.DataHandler;
import ru.permasha.castlewars.tasks.GameCountdownTask;
import ru.permasha.castlewars.utils.ItemBuilder;
import ru.permasha.castlewars.utils.RollbackHandler;
import ru.permasha.castlewars.utils.Utils;

import java.util.*;

public class Game {

    String gameName;

    Arena arena;
    List<GamePlayer> players;
    GameState gameState = GameState.LOBBY;

    int time;

    public Game(String gameName) {
        this.gameName = gameName;
        this.arena = new Arena(gameName);
        this.players = new ArrayList<>();
        this.time = 10;
    }

    public String getGameName() {
        return gameName;
    }

    public boolean joinGame(GamePlayer gamePlayer) {

        if (!isState(GameState.ENDING)) {

            if (getPlayers().size() == getArena().getMaxPlayers()) {
                gamePlayer.sendMessage("&cИгра переполнена");
                return false;
            }

            getPlayers().add(gamePlayer);

            getSmallestTeam().addGamePlayer(gamePlayer);

            gamePlayer.getPlayer().getInventory().clear();
            gamePlayer.getPlayer().getInventory().setArmorContents(null);

            gamePlayer.teleport(getArena().getLobbyPoint());

            gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
            gamePlayer.getPlayer().setHealth(20);

            giveLeaveGame(gamePlayer.getPlayer());

            broadcastMessage("[+] &7(" + getPlayers().size() + "&a/&7" + getArena().getMaxPlayers() + ")&f Игрок " + gamePlayer.getName() + "&f подключился!");

            if (getPlayers().size() == getArena().getMinPlayers() && !isState(GameState.STARTING)) {
                setState(GameState.STARTING);
                broadcastActionBar("&aИгра начнётся через 20 секунд...");
                startCountdown();
            }

        } else {
            gamePlayer.sendMessage("&cПодождите... Игра завершается");
            return false;
        }

        CastleWars.getInstance().setGame(gamePlayer.getPlayer(), this);
        return true;
    }

    public void giveLeaveGame(Player player) {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.colorize("&cВыйти в лобби"));
        itemStack.setItemMeta(itemMeta);

        player.getInventory().setItem(8, itemStack);
    }

    public void leaveGame(GamePlayer gamePlayer) {

        gamePlayer.setDamager(null);
        gamePlayer.getTeam().removeGamePlayer(gamePlayer);

        Game game = CastleWars.getInstance().getGame(gamePlayer.getPlayer());
        Player player = gamePlayer.getPlayer();

        int newSize = getPlayersSize()-1;

        CastleWars.getInstance().setGame(gamePlayer.getPlayer(), null);
        getPlayers().remove(gamePlayer);

        if (game.isState(GameState.LOBBY) || game.isState(GameState.STARTING)) {
            game.broadcastMessage(
                    "[-]" + " &7(" + newSize + "&c/&7" + getArena().getMaxPlayers() + ")&f Игрок " + player.getDisplayName() + "&f отключился!");
        }

        else if (!player.getGameMode().equals(GameMode.ADVENTURE)) {
            game.broadcastMessage("&fИгрок " + player.getDisplayName() + " &fпокинул игру");
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.teleport(CastleWars.getInstance().getLobbyPoint());

        player.setAllowFlight(false);
        player.setFlying(false);

        if (!game.isState(GameState.LOBBY) && !game.isState(GameState.STARTING) && !game.isState(GameState.ENDING)) {
            if (game.getPlayers().size() < getArena().getMinPlayers()){
                game.broadcastMessage("&eЗавершаем игру из-за недостаточного количества игроков...");
                end();
            }
        }
    }

    public Team getSmallestTeam() {
        Team smallestTeam = getArena().getTeams().get(1);
        for (Team team : getArena().getTeams()) {
            if (team.isSpectator())
                continue;
            if (getPlayersInTeam(team).size() < getPlayersInTeam(smallestTeam).size())
                smallestTeam = team;
        }
        return smallestTeam;
    }

    public List<GamePlayer> getPlayersInTeam(Team team) {
        return team.getGamePlayers();
    }

    public void activateSpectator(Team team) {

        for (GamePlayer gamePlayer : team.getGamePlayers()) {
            Player player = gamePlayer.getPlayer();
            player.setFallDistance(0);

            for (Player p : getArena().getWorld().getPlayers()) {
                p.hidePlayer(CastleWars.getInstance(), player);
            }

            player.teleport(getArena().getLobbyPoint());

            player.setGameMode(GameMode.SPECTATOR);

            player.setHealth(20);
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
            PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, true);
            player.addPotionEffect(effect);

            player.getInventory().clear();
            giveLeaveGame(player);
        }
    }

    public void switchToSpectator(Team team) {
        team.setTeamState(Team.TeamState.SPECTATOR);
    }

    public void startCountdown() {
        new GameCountdownTask(this).runTaskTimer(CastleWars.getInstance(), 0, 20);
    }

    public void assignSpawnFlags() {
        getArena().getFlags().forEach(flag -> flag.spawnFlag(getArena(), flag.getFromLocation()));
    }

    public void assignSpawnPositions() {

        for (Team team : getArena().getTeams()) {
            team.getGamePlayers().forEach(gamePlayer -> {
                try {
                    gamePlayer.teleport(team.getLocation());
                    gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                    gamePlayer.getPlayer().setHealth(20);
                    gamePlayer.getPlayer().setFallDistance(0);
                    gamePlayer.getPlayer().getInventory().clear();
                    CastleWars.getInstance().getGameManager().getKitPlayer(gamePlayer);
                } catch (IndexOutOfBoundsException ex) {
                    CastleWars.getInstance().getLogger().severe("Not enough spawn points to satisfy game needs (Game is " + getArena().getDisplayName() + ")");
                }
            });
        }
    }

    public Flag getFlagDropFromLocation(Location location) {
        for (Flag flag : arena.getFlags()) {
            if (flag.getDrop().distance(location) < 1.0D)
                return flag;
        }
        return null;
    }

    public Flag getFlagFromLocation(Location location) {
        for (Flag flag : arena.getFlags()) {
            if (flag.getFromLocation().distance(location) < 1.0D)
                return flag;
        }
        return null;
    }

    public Flag getFlagByTeam(Team team) {
        for (Flag flag : arena.getFlags()) {
            if (flag.getTeam().getName().equalsIgnoreCase(team.getName()))
                return flag;
        }
        return null;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isState(GameState state) {
        return getGameState() == state;
    }

    public void setState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Arena getArena() {
        return arena;
    }

    public int getPlayersSize() {
        return players.size();
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public List<Team> getSpectatorTeams() {
        List<Team> spectators = new ArrayList<>();
        for (Team team : getArena().getTeams()) {
            if (team.getTeamState().equals(Team.TeamState.SPECTATOR))
                spectators.add(team);
        }
        return spectators;
    }

    public List<Team> getLivingTeams() {
        List<Team> living = new ArrayList<>();
        for (Team team : getArena().getTeams()) {
            if (team.getTeamState().equals(Team.TeamState.LIVING))
                living.add(team);
        }
        return living;
    }

    public GamePlayer getGamePlayer(Player player) {
        for (GamePlayer gamePlayer : getPlayers()) {
            if (gamePlayer.getPlayer() == player) {
                return gamePlayer;
            }
        }
        return null;
    }

    public Team getTeam(String name) {
        for (Team team : getArena().getTeams()) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    public void broadcastMessage(String message) {
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.sendMessage(message);
        }
    }

    public void broadcastActionBar(String message) {
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.sendActionBar(message);
        }
    }

    public void sendTitle(String firstline, String secondline, int x, int y, int z) {
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.sendTitle(firstline, secondline, x,y,z);
        }
    }

    public void end() {

        for (Player player : getArena().getWorld().getPlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setHealth(20);
            CastleWars.getInstance().setGame(player, null);
            player.teleport(CastleWars.getInstance().getLobbyPoint());

            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

            player.setAllowFlight(false);
            player.setFlying(false);
        }

        CastleWars.getInstance().getGames().remove(this);

        System.out.println("Игра " + getGameName() + " была завершена!");

        RollbackHandler.getRollbackHandler().rollback(getArena().getWorld());

        if (DataHandler.getInstance().getGameInfo().getConfigurationSection("games") != null) {
            Game game = new Game(gameName);
            boolean status = CastleWars.getInstance().registerGame(game);
            if (!status) {
                Bukkit.getLogger().warning("Can't load game " + getGameName() + "! Reached game limit for this server.");
            }
        } else {
            // We can assume that no games are created
            Bukkit.getLogger().warning("No games have been created. Please create one using the creation command.");
        }

    }

    public enum GameState {
        LOBBY, STARTING, PREPARATION, ACTIVE, ENDING
    }

    public enum Mode {
        CTF, DEATHMATCH
    }

}
