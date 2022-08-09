package de.gommzy.applicationex.tabcompleter;

import de.gommzy.applicationex.groups.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationexTabcompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> filteredCompletions = new ArrayList<>();
        ArrayList<String> completions = new ArrayList<>();

        if (args.length < 2) {
            completions = new ArrayList<>(Arrays.asList("info","groups","user"));
            if (!sender.hasPermission("applicationex.admin.user")) {
                completions.remove("user");
            }
            if (!sender.hasPermission("applicationex.admin.groups")) {
                completions.remove("groups");
            }
        }
        else {
            switch (args[0].toLowerCase()) {
                case "info" -> {
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                        completions.add(player.getName());
                    }
                }
                case "user" -> {
                    if (!sender.hasPermission("applicationex.admin.user")) {
                        return new ArrayList<>();
                    } else if (args.length == 2) {
                        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                            completions.add(player.getName());
                        }
                    } else if (args.length == 3) {
                        completions = new ArrayList<>(Arrays.asList("addgroup","removegroup"));
                    } else if (args.length == 4) {
                        switch (args[2].toLowerCase()) {
                            case "addgroup", "removegroup" -> {
                                for (Group group : Group.GROUPS) {
                                    completions.add(group.name);
                                }
                                completions.remove("default"); //Default Gruppe darf nicht vergeben werden
                            }
                        }
                    }
                }
                case "groups" -> {
                    if (!sender.hasPermission("applicationex.admin.groups")) {
                        return new ArrayList<>();
                    } else if (args.length == 2) {
                        completions = new ArrayList<>(Arrays.asList("list","add","remove","edit"));
                    } else if (args.length == 3) {
                        switch (args[1].toLowerCase()) {
                            case "edit" -> {
                                for (Group group : Group.GROUPS) {
                                    completions.add(group.name);
                                }
                            }
                            case "remove" -> {
                                for (Group group : Group.GROUPS) {
                                    completions.add(group.name);
                                }
                                completions.remove("default"); //Default Gruppe darf nicht gelöscht werden
                            }
                        }
                    } else if (args.length == 4) {
                        switch (args[1].toLowerCase()) {
                            case "edit" -> {
                                completions = new ArrayList<>(Arrays.asList("prefix","permissions"));
                            }
                        }
                    } else if (args.length == 5) {
                        switch (args[3].toLowerCase()) {
                            case "permissions" -> {
                                completions = new ArrayList<>(Arrays.asList("list","add","remove"));
                            }
                        }
                    }
                }
            }
        }

        if (args.length > 0) {
            StringUtil.copyPartialMatches(args[args.length - 1], completions, filteredCompletions); //Zeige nur vorschläge an, welche mit dem bereits angefangenen Argument übereinstimmen
            return filteredCompletions;
        } else {
            return completions;
        }
    }
}
