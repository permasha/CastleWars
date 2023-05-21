package ru.permasha.castlewars.managers;

import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import ru.permasha.castlewars.CastleWars;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SidebarManager {

    CastleWars plugin;

    public SidebarManager(CastleWars plugin) {
        this.plugin = plugin;
    }

    public void refreshSidebar(FastBoard board) {

        Player player = board.getPlayer();

        board.updateTitle(Utils.colorize(getSidebarTitle()));

        if (player.getWorld().getName().equals("world")) {
            board.updateLines(getSidebar(board.getPlayer(), getLobbyLines()));
        }

        Game game = plugin.getGame(player);
        if (game != null) {
            board.updateLines(getSidebar(board.getPlayer(), game.getArena().getScoreboard()));
        }

    }

    private String getSidebarTitle() {
        return plugin.getConfig().getString("board.title");
    }

    public List<String> getSidebar(Player player, List<String> lines) {

        ArrayList<String> footer = new ArrayList<>();

        for (String s : lines) {
            s = PlaceholderAPI.setPlaceholders(player, s);
            footer.add(Utils.colorize(s));
        }

        return footer;
    }

    public List<String> getLobbyLines() {
        return plugin.getConfig().getStringList("board.lobby.rows");
    }

}
