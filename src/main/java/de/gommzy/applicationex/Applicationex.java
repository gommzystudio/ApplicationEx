package de.gommzy.applicationex;

import de.gommzy.applicationex.commands.ApplicationexCommand;
import de.gommzy.applicationex.database.SQLite;
import de.gommzy.applicationex.guis.durationselection.DurationSelectionListener;
import de.gommzy.applicationex.messages.Messages;
import de.gommzy.applicationex.tabcompleter.ApplicationexTabcompleter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Applicationex extends JavaPlugin {
    public static SQLite SQLITE;
    public static Plugin PLUGIN;
    public static Messages MESSAGES;

    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("applicationex").setExecutor(new ApplicationexCommand());
        Bukkit.getPluginCommand("applicationex").setTabCompleter(new ApplicationexTabcompleter());

        Bukkit.getPluginManager().registerEvents(new DurationSelectionListener(), this);

        PLUGIN = this;
        SQLITE = new SQLite();
        MESSAGES = new Messages();
    }

    public static String getPrefix() {
        return MESSAGES.getMessage("prefix");
    }
}
