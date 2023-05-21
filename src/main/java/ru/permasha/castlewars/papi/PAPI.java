package ru.permasha.castlewars.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.api.RandomCraftAPI;
import ru.permasha.castlewars.objects.*;
import ru.permasha.castlewars.utils.ProgressBar;
import ru.permasha.castlewars.utils.Utils;

public class PAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "cw";
    }

    @Override
    public @NotNull String getAuthor() {
        return "permasha";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    public String onPlaceholderRequest(Player player, String identifier) {

        if (identifier.equalsIgnoreCase("custom_name")) {
            return RandomCraftAPI.getColNickViaLPAPI(player);
        }

        PlayerData playerData = new PlayerData(player);

        if (identifier.equalsIgnoreCase("kills")) {
            return String.valueOf(playerData.getKills());
        }

        if (identifier.equalsIgnoreCase("wins")) {
            return String.valueOf(playerData.getWins());
        }

        if (identifier.equalsIgnoreCase("balance")) {
            return String.valueOf(playerData.getBalance());
        }

        if (identifier.equalsIgnoreCase("flag")) {
            if (isGameExists(player)) {
                Game game = getGame(player);
                GamePlayer gamePlayer = game.getGamePlayer(player);
                String color = gamePlayer.getTeam().getColor();
                return Utils.colorize(color + "&l|▘ ");
            } else {
                return "";
            }
        }

        if (isGameExists(player)) {
            Game game = getGame(player);
            Arena arena = game.getArena();
            GamePlayer gamePlayer = game.getGamePlayer(player);

            if (identifier.startsWith("score_")) {
                String teamName = identifier.replace("score_", "");
                Team team = game.getTeam(teamName);
                int score = team.getScore();

                return String.valueOf(score);
            }

            if (identifier.startsWith("health_")) {
                String teamName = identifier.replace("health_", "");
                Team team = game.getTeam(teamName);
                int health = team.getHealth();

                return String.valueOf(health);
            }

            if (identifier.startsWith("bar_")) {
                String teamName = identifier.replace("bar_", "");
                Team team = game.getTeam(teamName);

                int health = team.getHealth();
                int winAfter = arena.getWinAfter();

                String bar = ProgressBar.getProgressBar(health, winAfter, 20, ':', ChatColor.GREEN, ChatColor.RED);
                return "§7[§a" + bar + "§7]§r";
            }

            if (identifier.equalsIgnoreCase("time")) {
                return Utils.getTime(game.getTime());
            }

            if (identifier.equalsIgnoreCase("team")) {
                return gamePlayer.getTeam().getName();
            }

            if (identifier.equalsIgnoreCase("color")) {
                return gamePlayer.getTeam().getColor();
            }

            if (identifier.equalsIgnoreCase("round_kills")) {
                return String.valueOf(gamePlayer.getKills());
            }

            if (identifier.equalsIgnoreCase("round_deaths")) {
                return String.valueOf(gamePlayer.getDeaths());
            }

            if (identifier.equalsIgnoreCase("round_assists")) {
                return String.valueOf(gamePlayer.getAssists());
            }

            if (identifier.equalsIgnoreCase("arena")) {
                return arena.getDisplayName();
            }

            if (identifier.equalsIgnoreCase("distance")) {
                return String.valueOf(CastleWars.getInstance().getGameManager().getPercentLocToFlag(game, gamePlayer));
            }
        }

        return null;
    }

    public boolean isGameExists(Player player) {
        return CastleWars.getInstance().getGame(player) != null;
    }

    public Game getGame(Player player) {
        return CastleWars.getInstance().getGame(player);
    }

}
