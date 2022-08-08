package de.gommzy.applicationex.database;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.groups.Group;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class SQLite {
    public SQLite() {
        new BukkitRunnable() {
            @Override
            public void run() {
                createTables();
                Group.GROUPS = loadGroups();
            }
        }.runTaskAsynchronously(Applicationex.PLUGIN);
    }

    Connection getSQLConnection() {
        File dataFolder = new File(Applicationex.PLUGIN.getDataFolder(), "data.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void createTables() { //Erstellt die benötigten Datenbanken
        try {
            getSQLConnection().prepareStatement("CREATE TABLE IF NOT EXISTS groups (`name` VARCHAR(50) NOT NULL, `data` TEXT NOT NULL);").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addGroup(Group group) { //Fügt eine Gruppe in die groups Tabelle hinzu
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("INSERT INTO groups (name,data) VALUES (?,?)");
            preparedStatement.setString(1, group.name);
            preparedStatement.setString(2, group.toJson());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGroup(Group group) { //Aktualisiert eine Gruppe in die der groups Tabelle
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("UPDATE groups SET data = ? WHERE name = ?");
            preparedStatement.setString(1, group.toJson());
            preparedStatement.setString(2, group.name);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGroup(Group group) {
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("DELETE FROM groups WHERE name = ?");
            preparedStatement.setString(1, group.name);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Group> loadGroups() { //Lade all Gruppen aus der groups Tabelle
        ArrayList<Group> groups = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("SELECT * FROM groups");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Group group = Group.fromJson(resultSet.getString("data"));
                groups.add(group);
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return groups;
    }
}
