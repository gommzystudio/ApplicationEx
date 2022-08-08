package de.gommzy.applicationex.messages;

import de.gommzy.applicationex.Applicationex;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Messages {
    HashMap<String, String> messages = new HashMap<>();

    File file;
    FileConfiguration config;

    public Messages() {
        file = new File(Applicationex.PLUGIN.getDataFolder(),"messages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);

        setDefaultMessage("prefix", "§9§lApplicationEx §8» §7");

        setDefaultMessage("command.help", "Benutze: /ae [info/groups]");
        setDefaultMessage("command.onlyplayers", "Nur Spieler können dieses Kommando benutzen.");
        setDefaultMessage("command.groups.help", "Benutze: /ae groups [list/add/remove/edit]");
        setDefaultMessage("command.groups.add.help", "Benutze: /ae groups add [<name>]");
        setDefaultMessage("command.groups.add.nametoolong", "Der eingegebene Gruppenname ist zu lang.");
        setDefaultMessage("command.groups.add.nameinuse", "Der eingegebene Gruppenname wird bereits benutzt.");
        setDefaultMessage("command.groups.add.success", "Die Gruppe wurde erstellt.");
        setDefaultMessage("command.groups.remove.help", "Benutze: /ae groups remove [<name>]");
        setDefaultMessage("command.groups.remove.success", "Die Gruppe wurde erfolgreich gelöscht.");
        setDefaultMessage("command.groups.list.title", "Folgende Gruppen existieren:");
        setDefaultMessage("command.groups.list.players", "Spieler");
        setDefaultMessage("command.groups.notexisting", "Der eingegebene Gruppenname wird von keiner Gruppe benutzt.");
        setDefaultMessage("command.groups.edit.help", "Benutze: /ae groups edit [<groupname>] [prefix/members/permissions]");
        setDefaultMessage("command.groups.edit.success", "Die Gruppe wurde erfolgreich aktualisiert.");
        setDefaultMessage("command.groups.edit.prefix.help", "Benutze: /ae groups edit [<groupname>] prefix [<prefix>]");
        setDefaultMessage("command.groups.edit.members.help", "Benutze: /ae groups edit [<groupname>] members [list/add/remove]");
        setDefaultMessage("command.groups.edit.members.list", "Folgende Spieler sind in der Gruppe:");
        setDefaultMessage("command.groups.edit.members.add.help", "Benutze: /ae groups edit [<groupname>] members add [<playername>]");
        setDefaultMessage("command.groups.edit.members.remove.help", "Benutze: /ae groups edit [<groupname>] members remove [<playername>]");
        setDefaultMessage("command.groups.edit.members.remove.notexisting", "Der eingegebene Spielername is nicht in dieser Gruppe.");

        setDefaultMessage("durationselection.days", "Tag(e)");
        setDefaultMessage("durationselection.hours", "Stunde(n)");
        setDefaultMessage("durationselection.minutes", "Minute(n)");
        setDefaultMessage("durationselection.seconds", "Sekunde(n)");
        setDefaultMessage("durationselection.confirm", "Bestätigen");
        setDefaultMessage("durationselection.cancel", "Abbrechen");
        setDefaultMessage("durationselection.lifetime", "Lifetime");
        setDefaultMessage("durationselection.duration", "Dauer");

        for (String message : config.getConfigurationSection("").getKeys(true)) {
            messages.put(message, config.getString(message));
        }
    }

    private void setDefaultMessage(String name, String message) {
        if (!config.isSet(name)) {
            config.set(name, message);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getMessage(String id) {
        return messages.get(id);
    }
}
