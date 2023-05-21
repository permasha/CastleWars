package ru.permasha.castlewars.objects;

import org.bukkit.*;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.managers.ArenaManager;
import ru.permasha.castlewars.utils.RollbackHandler;

import java.util.List;

public class Arena {

    String name;
    String displayName;
    Game.Mode mode;
    int maxPlayers;
    int minPlayers;
    int winAfter;
    World world;
    Location lobbyPoint;

    int time;

    List<Team> teams;
    List<String> scoreboard;

    List<Flag> flags;

    public Arena(String name) {
        ArenaManager arenaManager = CastleWars.getInstance().getArenaManager();

        this.name = name;
        this.displayName = arenaManager.getDisplayName(name);
        this.mode = Game.Mode.valueOf(arenaManager.getMode(name));
        this.maxPlayers = arenaManager.getMaxPlayers(name);
        this.minPlayers = arenaManager.getMinPlayers(name);
        this.winAfter = arenaManager.getWinAfter(name);

        RollbackHandler.getRollbackHandler().rollback(arenaManager.getWorldName(name));
        this.world = Bukkit.createWorld(new WorldCreator(arenaManager.getWorldName(name) + "_active"));
        world.setGameRule(GameRule.DO_TILE_DROPS, true);
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);

        this.lobbyPoint = arenaManager.getLobbyPoint(name, world);
        this.time = arenaManager.getTime(name);
        this.teams = arenaManager.getTeams(name, world);
        this.scoreboard = arenaManager.getScoreboardLines(name);
        this.flags = arenaManager.getFlags(name, world);
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Game.Mode getMode() {
        return mode;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public World getWorld() {
        return world;
    }

    public int getTime() {
        return time;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public Location getLobbyPoint() {
        return lobbyPoint;
    }

    public void setTime(int i) {
        this.time = i;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public int getWinAfter() {
        return winAfter;
    }

    public List<String> getScoreboard() {
        return scoreboard;
    }

    public Flag getFlagFromFromLocation(Location location) {
        for (Flag flag : getFlags()) {
            if (flag.getFromLocation().distance(location) < 1.8D)
                return flag;
        }
        return null;
    }

    public boolean isFlagFromExits(Location location) {
        return (getFlagFromFromLocation(location) != null);
    }
}
