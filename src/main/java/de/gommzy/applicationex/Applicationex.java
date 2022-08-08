package de.gommzy.applicationex;

import de.gommzy.applicationex.commands.ApplicationexCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Applicationex extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("applicationex").setExecutor(new ApplicationexCommand());
        Bukkit.getPluginCommand("applicationex").setTabCompleter(new ApplicationexTabcomp());

        Bukkit.getLogger().info("[ApplicationEx] Das Plugin wurde erfolgreich geladen.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ApplicationEx] Das Plugin ist nun entladen.");
    }
}
