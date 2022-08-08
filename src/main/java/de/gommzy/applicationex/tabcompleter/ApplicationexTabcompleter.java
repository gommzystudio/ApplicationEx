package de.gommzy.applicationex.tabcompleter;

import de.gommzy.applicationex.groups.Group;
import org.bukkit.Bukkit;
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
        List<String> completions = new ArrayList<>();

        if (args.length < 2) {
            completions = Arrays.asList("info","groups");
        }
        else {
            switch (args[0].toLowerCase()) {
                case "info" -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        completions.add(player.getName());
                    }
                }
                case "groups" -> {
                    if (args.length == 2) {
                        completions = Arrays.asList("list","add","remove","edit");
                    } else if (args.length == 3) {
                        switch (args[1].toLowerCase()) {
                            case "remove", "edit" -> {
                                for (Group group : Group.GROUPS) {
                                    completions.add(group.name);
                                }
                            }
                        }
                    } else if (args.length == 4) {
                        switch (args[1].toLowerCase()) {
                            case "edit" -> {
                                completions = Arrays.asList("prefix","members","permissions");
                            }
                        }
                    } else if (args.length == 5) {
                        switch (args[3].toLowerCase()) {
                            case "members", "permissions" -> {
                                completions = Arrays.asList("list","add","remove");
                            }
                        }
                    } else if (args.length == 6) {
                        switch (args[3].toLowerCase()) {
                            case "members" -> {
                                switch (args[4].toLowerCase()) {
                                    case "add", "remove" -> {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            completions.add(player.getName());
                                        }
                                    }
                                }
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
