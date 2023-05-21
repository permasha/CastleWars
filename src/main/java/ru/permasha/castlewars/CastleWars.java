package ru.permasha.castlewars;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.permasha.castlewars.commands.CastleWarsAdminCommand;
import ru.permasha.castlewars.commands.CastleWarsCommand;
import ru.permasha.castlewars.database.DataHandler;
import ru.permasha.castlewars.database.Database;
import ru.permasha.castlewars.listeners.*;
import ru.permasha.castlewars.managers.ArenaManager;
import ru.permasha.castlewars.managers.GameManager;
import ru.permasha.castlewars.managers.SidebarManager;
import ru.permasha.castlewars.objects.Game;
import ru.permasha.castlewars.papi.PAPI;
import ru.permasha.castlewars.utils.RollbackHandler;
import ru.xezard.glow.GlowAPI;

import java.util.*;

public final class CastleWars extends JavaPlugin {

    static CastleWars instance;

    private GlowAPI glowing;
    private Database database;

    private final Set<Game> games = new HashSet<>();


    private final Map<Player, Game> playerGameMap = new HashMap<>();
    private Map<UUID, FastBoard> sideBars = new HashMap<>();

    private SidebarManager sidebarManager;
    private GameManager gameManager;

    private ArenaManager arenaManager;

    private Location lobbyPoint = null;

    @Override
    public void onEnable() {
        instance = this;
        database = new Database(this);

        getCommand("castlewars").setExecutor(new CastleWarsCommand());
        getCommand("castlewarsadmin").setExecutor(new CastleWarsAdminCommand());

        arenaManager = new ArenaManager(this);
        gameManager = new GameManager(this);
        sidebarManager = new SidebarManager(this);
        this.sideBars = new HashMap<>();

        setup();
        setupListeners();
        setupSidebars();
        glowing = new GlowAPI(instance);

        PAPI papi = new PAPI();
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papi.register();
        }

        Bukkit.getLogger().info(ChatColor.GREEN +
                "\n" +
                "\n" +
                "▒█▀▀█ █▀▀█ █▀▀ ▀▀█▀▀ █░░ █▀▀ 　 █░░░█ █▀▀█ █▀▀█ █▀▀\n" +
                "▒█░░░ █▄▄█ ▀▀█ ░░█░░ █░░ █▀▀ 　 █▄█▄█ █▄▄█ █▄▄▀ ▀▀█\n" +
                "▒█▄▄█ ▀░░▀ ▀▀▀ ░░▀░░ ▀▀▀ ▀▀▀ 　 ░▀░▀░ ▀░░▀ ▀░▀▀ ▀▀▀\n" +
                "\n" +
                "Is Enable!");
    }

    @Override
    public void onDisable() {
        PAPI papi = new PAPI();
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papi.unregister();
        }
        cleanUp();
    }

    private void setup() {

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        if (DataHandler.getInstance().getGameInfo().getConfigurationSection("games") != null) {
            for (String gameName : DataHandler.getInstance().getGameInfo().getConfigurationSection("games").getKeys(false)) {
                Game game = new Game(gameName);
                boolean status = this.registerGame(game);
                if (!status) {
                    getLogger().warning("Can't load game " + gameName + "! Reached game limit for this server.");
                }
            }
        } else {
            // We can assume that no games are created
            getLogger().warning("No games have been created. Please create one using the creation command.");
        }
    }

    private void cleanUp() {
        for (Map.Entry<Player, Game> entry : playerGameMap.entrySet()) {
            entry.getKey().teleport(getLobbyPoint());
            entry.getKey().getInventory().clear();
            entry.getKey().getInventory().setArmorContents(null);
            entry.getKey().setHealth(20);
            entry.getKey().setGameMode(GameMode.ADVENTURE);
        }

        for (FastBoard board : sideBars.values()) {
            board.delete();
        }

        // Rollback every game
        for (Game game : getGames()) {
            for (Player player : game.getArena().getWorld().getPlayers()) {
                player.teleport(getLobbyPoint());
            }
            RollbackHandler.getRollbackHandler().rollback(game.getArena().getWorld());
        }
    }

    private void setupListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChat(this), this);
        pluginManager.registerEvents(new PlayerJoin(this), this);
        pluginManager.registerEvents(new PlayerQuit(this), this);
        pluginManager.registerEvents(new BlockExplode(this), this);
        pluginManager.registerEvents(new BlockBreak(this), this);
        pluginManager.registerEvents(new BlockPlace(this), this);
        pluginManager.registerEvents(new EntityDamage(this), this);
        pluginManager.registerEvents(new EntityDeath(this), this);
        pluginManager.registerEvents(new FoodLevelChange(this), this);
        pluginManager.registerEvents(new PlayerChangedWorld(this), this);
        pluginManager.registerEvents(new PlayerDeath(this), this);
        pluginManager.registerEvents(new PlayerDropItem(this), this);
        pluginManager.registerEvents(new PlayerItemConsume(this), this);
        pluginManager.registerEvents(new PlayerInteract(this), this);
        pluginManager.registerEvents(new PlayerMove(this), this);
        pluginManager.registerEvents(new PlayerMove2(this), this);
        pluginManager.registerEvents(new PlayerMove3(this), this);
        pluginManager.registerEvents(new ProjectileHit(this), this);
        pluginManager.registerEvents(new EntityShootBow(this), this);
        pluginManager.registerEvents(new InventoryClick(this), this);
    }

    private void setupSidebars() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            FastBoard board = new FastBoard(player);
            sideBars.put(player.getUniqueId(), board);
        });

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : sideBars.values()) {
                getSidebarManager().refreshSidebar(board);
            }
        }, 0, 20);
    }

    public Location getLobbyPoint() {
        if (lobbyPoint == null) {
            int x = 0;
            int y = 0;
            int z = 0;
            String world = "world";

            try {
                x = getConfig().getInt("lobby-point.x");
                y = getConfig().getInt("lobby-point.y");
                z = getConfig().getInt("lobby-point.z");
                world = getConfig().getString("lobby-point.world");
            } catch (Exception ex) {
                getLogger().severe("Lobby point failed with exception: " + ex);
                ex.printStackTrace();
            }

            lobbyPoint = new Location(Bukkit.getWorld(world), x, y, z);
        }

        return lobbyPoint;
    }

    public boolean registerGame(Game game) {

        games.add(game);
        return true;
    }

    public Game getGame(String gameName) {
        for (Game game : games) {
            if (game.getGameName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }
        return null;
    }

    public Game getGame(World world) {
        for (Game game : games) {
            if (game.getArena().getWorld().getName().equalsIgnoreCase(world.getName())) {
                return game;
            }
        }
        return null;
    }

    public Game getGame(Player player) {
        return this.playerGameMap.get(player);
    }

    public void setGame(Player player, Game game) {
        if (game == null) {
            this.playerGameMap.remove(player);
        } else {
            this.playerGameMap.put(player, game);
        }
    }

    public Database getDatabase() {
        return database;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public SidebarManager getSidebarManager() {
        return sidebarManager;
    }

    public Map<UUID, FastBoard> getSideBars() {
        return sideBars;
    }

    public Map<Player, Game> getPlayerGameMap() {
        return playerGameMap;
    }

    public Set<Game> getGames() {
        return games;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public static CastleWars getInstance() {
        return instance;
    }

    public GlowAPI getGlowAPI() {
        return glowing;
    }
}
