package de.gommzy.applicationex.database;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.groups.Group;
import de.gommzy.applicationex.groups.MemberData;
import de.gommzy.applicationex.signs.Sign;
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
                if (Group.getGroup("default") == null) { //Erstellt die default group, falls diese noch nicht existiert
                    new Group("default");
                }
                loadSigns();
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
            getSQLConnection().prepareStatement("CREATE TABLE IF NOT EXISTS players (`uuid` VARCHAR(50) NOT NULL, `group` VARCHAR(50) NOT NULL, `duration` INTEGER NOT NULL);").executeUpdate();
            getSQLConnection().prepareStatement("CREATE TABLE IF NOT EXISTS signs (`uuid` VARCHAR(50) NOT NULL, `x` INTEGER NOT NULL, `y` INTEGER NOT NULL, `z` INTEGER NOT NULL, `world` VARCHAR(50) NOT NULL);").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSign(Sign sign) { //Fügt ein Schild in die signs Tabelle hinzu
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("INSERT INTO signs (uuid,x,y,z,world) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, sign.uuid);
            preparedStatement.setInt(2, sign.x);
            preparedStatement.setInt(3, sign.y);
            preparedStatement.setInt(4, sign.z);
            preparedStatement.setString(5, sign.worldName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existSign(int x, int y, int z, String world) { //Frägt ab, ob ein Schild in der signs Tabelle existiert
        boolean exist = false;
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("SELEFT * FROM signs WHERE x = ? AND y = ? AND z = ? AND world = ?");
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setInt(3, z);
            preparedStatement.setString(4, world);
            ResultSet resultSet = preparedStatement.executeQuery();
            exist = resultSet.next();
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }

    public void deleteSign(Sign sign) { //Löscht ein Schild aus der signs Tabelle
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("DELETE FROM signs WHERE x = ? AND y = ? AND z = ? AND world = ?");
            preparedStatement.setInt(1, sign.x);
            preparedStatement.setInt(2, sign.y);
            preparedStatement.setInt(3, sign.z);
            preparedStatement.setString(4, sign.worldName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSigns() { //Lade all Signs aus der signs Tabelle
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("SELECT * FROM signs");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                new Sign(resultSet.getInt("x"),resultSet.getInt("y"),resultSet.getInt("z"),resultSet.getString("world"),resultSet.getString("uuid"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
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

    public MemberData getPlayerGroup(String uuid) { //Findet die Gruppe eines Spielers
        MemberData memberData = null;

        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("SELECT `group`, duration FROM players");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final Group group = Group.getGroup(resultSet.getString("group"));
                final long duration = resultSet.getLong("duration");
                memberData = new MemberData(uuid,group,duration);
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return memberData;
    }

    public void addPlayerGroup(String uuid, String group, long duration) { //Fügt einen Spieler in die players Tabelle hinzu
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("INSERT INTO players (uuid,`group`,duration) VALUES (?,?,?)");
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, group);
            preparedStatement.setLong(3, duration);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePlayerGroup(String uuid) { //Löscht die Gruppe eines Spielers, Der Spieler hat nun keine Gruppe mehr oder die default Gruppe
        try {
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement("DELETE FROM players WHERE uuid = ?");
            preparedStatement.setString(1, uuid);
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
