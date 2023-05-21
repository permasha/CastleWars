package ru.permasha.castlewars.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.permasha.castlewars.CastleWars;

import java.io.File;
import java.io.IOException;

public class DataHandler {
    private static final DataHandler ourInstance = new DataHandler();
    public static DataHandler getInstance() {
        return ourInstance;
    }
    private DataHandler() {
        this.gameInfoFile = new File(CastleWars.getInstance().getDataFolder(), "gameInfo.yml");
        if (!this.gameInfoFile.exists()) {
            try {
                this.gameInfoFile.getParentFile().mkdirs();
                this.gameInfoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.gameInfo = YamlConfiguration.loadConfiguration(this.gameInfoFile);
    }

    private final File gameInfoFile;
    private final FileConfiguration gameInfo;

    public FileConfiguration getGameInfo() {
        return gameInfo;
    }

    public void saveGameInfo() {
        try {
            this.gameInfo.save(this.gameInfoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
