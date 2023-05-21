package ru.permasha.castlewars.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.database.DataHandler;
import ru.permasha.castlewars.objects.Flag;
import ru.permasha.castlewars.objects.Team;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    CastleWars plugin;

    public ArenaManager(CastleWars plugin) {
        this.plugin = plugin;
    }

    FileConfiguration fileConfiguration = DataHandler.getInstance().getGameInfo();

    public String getDisplayName(String name) {
        return fileConfiguration.getString("games." + name + ".displayName");
    }

    public int getMaxPlayers(String name) {
        return fileConfiguration.getInt("games." + name + ".maxPlayers");
    }

    public int getMinPlayers(String name) {
        return fileConfiguration.getInt("games." + name + ".minPlayers");
    }

    public int getWinAfter(String name) {
        return fileConfiguration.getInt("games." + name + ".winAfter");
    }

    public String getWorldName(String name) {
        return fileConfiguration.getString("games." + name + ".worldName");
    }

    public String getMode(String name) {
        return fileConfiguration.getString("games." + name + ".mode");
    }

    public Location getLobbyPoint(String name, World world) {
        try {
            String[] values = fileConfiguration.getString("games." + name + ".lobbyPoint").split(","); // [X:0, Y:0, Z:0]
            double x = Double.parseDouble(values[0].split(":")[1]); // X:0 -> X, 0 -> 0
            double y = Double.parseDouble(values[1].split(":")[1]);
            double z = Double.parseDouble(values[2].split(":")[1]);
            return new Location(world, x, y, z);
        } catch (Exception ex) {
            CastleWars.getInstance().getLogger().severe("Failed to load lobbyPoint with metadata " + fileConfiguration.getString("games." + name + ".lobbyPoint") + " for gameName: '" + name + "'. ExceptionType: " + ex);
        }
        return null;
    }

    public List<Team> getTeams(String name, World world) {
        List<Team> teams = new ArrayList<>();
        List<String> stringList = fileConfiguration.getStringList("games." + name + ".teams");

        stringList.forEach(str -> {
            String[] array = str.split(";");
            String teamName = array[0];
            String color = array[1];
            double x = Double.parseDouble(array[2]);
            double y = Double.parseDouble(array[3]);
            double z = Double.parseDouble(array[4]);
            int yaw = Integer.parseInt(array[5]);
            int pitch = Integer.parseInt(array[6]);
            Location location = new Location(world, x, y, z, yaw, pitch);
            int maxPlayers = Integer.parseInt(array[7]);

            Team team = new Team(teamName, color, getWinAfter(name), location, maxPlayers);
            teams.add(team);
        });

        return teams;
    }

    public List<Flag> getFlags(String name, World world) {
        List<Flag> flags = new ArrayList<>();
        List<String> stringList = fileConfiguration.getStringList("games." + name + ".flags");

        stringList.forEach(str -> {
            String[] array = str.split(";");
            String teamName = array[0];
            double fromX = Double.parseDouble(array[1]);
            double fromY = Double.parseDouble(array[2]);
            double fromZ = Double.parseDouble(array[3]);

            double toX = Double.parseDouble(array[4]);
            double toY = Double.parseDouble(array[5]);
            double toZ = Double.parseDouble(array[6]);

            Location fromLocation = new Location(world, fromX, fromY, fromZ);
            Location toLocation = new Location(world, toX, toY, toZ);

            Team team = getTeamByName(name, world, teamName);

            Flag flag = new Flag(team, fromLocation, toLocation);
            flags.add(flag);
        });

        return flags;
    }

    public Team getTeamByName(String gameName, World world, String teamName) {
        for (Team team : getTeams(gameName, world)) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    public List<String> getScoreboardLines(String name) {
        return fileConfiguration.getStringList("games." + name + ".scoreboard");
    }

    public int getTime(String name) {
        return fileConfiguration.getInt("games." + name + ".time");
    }

}
