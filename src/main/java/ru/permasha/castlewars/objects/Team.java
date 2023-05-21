package ru.permasha.castlewars.objects;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Team {

    String name;
    String color;

    int health;

    int score;

    Location location;

    List<GamePlayer> gamePlayers;

    int maxPlayers;

    boolean spectator;

    TeamState teamState;

    public Team(String name, String color, int health, Location location, int maxPlayers) {
        this.name = name;
        this.color = color;
        this.health = health;
        this.score = 0;
        this.location = location;
        this.gamePlayers = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.spectator = false;
        this.teamState = TeamState.LIVING;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void removeHealth(int num) {
        this.health = (health - num);
    }

    public int getScore() {
        return score;
    }

    public void addScore(int num) {
        this.score = getScore() + num;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Location getLocation() {
        return location;
    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
        gamePlayer.setTeam(this);
    }

    public void removeGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer);
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public TeamState getTeamState() {
        return teamState;
    }

    public void setTeamState(TeamState teamState) {
        this.teamState = teamState;
    }

    public enum TeamState {
        LIVING, SPECTATOR
    }
}
