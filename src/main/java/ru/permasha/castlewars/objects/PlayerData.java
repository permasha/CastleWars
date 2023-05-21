package ru.permasha.castlewars.objects;

import org.bukkit.entity.Player;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.database.Database;

public class PlayerData {

    Player player;

    public PlayerData(Player player) {
        this.player = player;
    }

    private final Database database = CastleWars.getInstance().getDatabase();

    public int getWins() {
        return database.getStatistic(player.getUniqueId(), "wins");
    }

    public void addWin() {
        int res = getWins() + 1;
        database.setStatistic(player.getUniqueId(), "wins", res);
    }

    public int getKills() {
        return database.getStatistic(player.getUniqueId(), "kills");
    }

    public void addKill() {
        int res = getKills() + 1;
        database.setStatistic(player.getUniqueId(), "kills", res);
    }

    public int getDeaths() {
        return database.getStatistic(player.getUniqueId(), "deaths");
    }

    public void addDeath() {
        int res = getWins() + 1;
        database.setStatistic(player.getUniqueId(), "deaths", res);
    }

    public int getBalance() {
        return database.getStatistic(player.getUniqueId(), "balance");
    }

    public void addBalance(int num) {
        int res = getWins() + num;
        database.setStatistic(player.getUniqueId(), "balance", res);
    }

    public void removeBalance() {
        int res = getWins() - 1;
        database.setStatistic(player.getUniqueId(), "balance", res);
    }

}
