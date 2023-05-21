package ru.permasha.castlewars.objects;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.permasha.castlewars.utils.Utils;

public class GamePlayer {

    Player player;
    Player damager;
    int kills;
    int deaths;
    int assists;
    int balance;

    Team team;

    boolean hasFlag;

    public GamePlayer(Player player) {
        this.player = player;
        this.kills = 0;
        this.damager = null;
        this.team = null;
        this.hasFlag = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getDamager() {
        return damager;
    }

    public void setDamager(Player player) {
        this.damager = player;
    }

    public void sendActionBar(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.colorize(message)));
    }

    public void sendMessage(String message) {
        player.sendMessage(Utils.colorize(message));
    }

    public void sendTitle(String firstline, String secondline, int x, int y, int z) {
        player.sendTitle(Utils.colorize(firstline), Utils.colorize(secondline), x, y, z);
    }

    public void teleport(Location location) {
        if (location == null) {
            return;
        }
        getPlayer().teleport(location);
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public void setFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;
    }

    public String getName() {
        return player.getDisplayName();
    }

    public int getKills() {
        return this.kills;
    }

    public void addKill() {
        this.kills = getKills() + 1;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        this.deaths = getDeaths() + 1;
    }

    public int getAssists() {
        return assists;
    }

    public void addAssist() {
        this.assists = getAssists() + 1;
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int i) {
        this.balance = getBalance() + i;
    }

    public void removeBalance(int i) {
        this.balance = getBalance() - i;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
