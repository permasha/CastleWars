package ru.permasha.castlewars.database;

import mc.obliviate.bloksqliteapi.SQLHandler;
import mc.obliviate.bloksqliteapi.sqlutils.DataType;
import mc.obliviate.bloksqliteapi.sqlutils.SQLTable;
import org.bukkit.Bukkit;
import ru.permasha.castlewars.CastleWars;

import java.util.UUID;

public class Database extends SQLHandler {
    public Database(CastleWars plugin) {
        super(plugin.getDataFolder().getAbsolutePath());
        connect();
    }

    public void connect() {
        super.connect("castlewars");
    }

    public void disconnect() {
        super.disconnect();
    }

    @Override
    public void onDisconnect() {
        Bukkit.getLogger().info("Plugin successfully disconnected of database.");
        super.onDisconnect();
    }

    @Override
    public void onConnect() {
        Bukkit.getLogger().info("Plugin successfully connected to database.");
        createTable();
    }

    public SQLTable createTable() {
        SQLTable sqlTable = new SQLTable("stats", "uuid")

                .addField("uuid", DataType.TEXT, true, true, true)

                .addField("wins", DataType.INTEGER)
                .addField("kills", DataType.INTEGER)
                .addField("deaths", DataType.INTEGER)
                .addField("balance", DataType.INTEGER);

        return sqlTable.create();
    }

    public void setStatistic(UUID uuid, String id, int level) {
        SQLTable sqlTable = new SQLTable("stats", "uuid");

        if (!sqlTable.exist(uuid)) {
            insertPlayerData(uuid);
        }

        sqlTable.update(sqlTable.createUpdate(uuid).putData(id, level));
    }

    public int getStatistic(UUID uuid, String id) {
        SQLTable sqlTable = new SQLTable("stats", "uuid");

        if (!sqlTable.exist(uuid)) {
            insertPlayerData(uuid);
        }

        return sqlTable.getInteger(uuid.toString(),id);
    }

    public void insertPlayerData(UUID uuid) {
        SQLTable sqlTable = new SQLTable("stats", "uuid");

        if (!sqlTable.exist(uuid)) {
            sqlTable.insert(sqlTable
                    .createUpdate(uuid)
                    .putData("uuid", uuid)
                    .putData("wins", 0)
                    .putData("kills", 0)
                    .putData("deaths", 0)
                    .putData("balance", 0)
            );
        }
    }

}
