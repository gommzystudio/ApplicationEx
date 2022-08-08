package de.gommzy.applicationex.groups;

import com.google.gson.Gson;
import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.database.SQLite;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Group {
    public static ArrayList<Group> GROUPS = new ArrayList<>();

    public static Group fromJson(String json) {
        final Gson gson = new Gson();
        return gson.fromJson(json, Group.class);
    }

    public static Group getGroup(String name) {
        for (Group group : GROUPS) {
            if (group.name.equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
    }

    public Group(String name) { //Erstellt Gruppe, wenn der Constructor aufgerufen wird
        this.name = name;
        GROUPS.add(this);

        final Group group = this;
        new BukkitRunnable() { //Async, damit der main thread nicht verwendet wird
            @Override
            public void run() {
                Applicationex.SQLITE.addGroup(group);
            }
        }.runTaskAsynchronously(Applicationex.PLUGIN);
    }

    public String name;
    public String prefix = "";
    public HashMap<String, Long> members = new HashMap<>();
    public ArrayList<String> permissions = new ArrayList<>();

    public void save() {
        final Group group = this;
        new BukkitRunnable() { //Async, damit der main thread nicht verwendet wird
            @Override
            public void run() {
                Applicationex.SQLITE.updateGroup(group);
            }
        }.runTaskAsynchronously(Applicationex.PLUGIN);
    }

    public void delete() {
        final Group group = this;
        new BukkitRunnable() { //Async, damit der main thread nicht verwendet wird
            @Override
            public void run() {
                Applicationex.SQLITE.deleteGroup(group);
            }
        }.runTaskAsynchronously(Applicationex.PLUGIN);
    }

    public void addPlayer(String uuid, long duration) {
        if (duration > 0) {
            members.put(uuid, duration + Calendar.getInstance().getTimeInMillis());
        } else {
            members.put(uuid, -1L);
        }
        save();
    }

    public String toJson() { //Wandelt alle Daten dieser Klasse in Json um, um diese besser abzuspeichern (Group class kann leicht ergänzt werden)
        final Gson gson = new Gson();
        return gson.toJson(this);
    }
}
