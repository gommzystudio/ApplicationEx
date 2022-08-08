package de.gommzy.applicationex.commands;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.groups.Group;
import de.gommzy.applicationex.groups.MemberUtils;
import de.gommzy.applicationex.guis.durationselection.DurationSelection;
import de.gommzy.applicationex.guis.durationselection.DurationSelectionSession;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Member;
import java.util.Map;
import java.util.UUID;

public class ApplicationexCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Applicationex.getPrefix()+ Applicationex.MESSAGES.getMessage("command.help"));
        }
        else {
            switch (args[0].toLowerCase()) {
                case "info" -> {
                    if (args.length == 1) {

                    } else {

                    }
                }
                case "groups" -> {
                    if (args.length > 1) {
                        switch (args[1].toLowerCase()) {
                            case "list" -> {
                                sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.groups.list.itle"));
                                for (Group group : Group.GROUPS) {
                                    sender.sendMessage("§8- §7"+group.name+" (§9"+group.members.size()+" "+Applicationex.MESSAGES.getMessage("command.groups.list.players")+"§7)");
                                }
                            }

                            case "add" -> {
                                if (args.length == 3) {
                                    String name = args[2];
                                    if (name.length() >= 50) {
                                        sender.sendMessage(Applicationex.getPrefix()+"§c"+ Applicationex.MESSAGES.getMessage("command.groups.add.nametoolong"));
                                        return true;
                                    }
                                    if (Group.getGroup(name) != null) {
                                        sender.sendMessage(Applicationex.getPrefix()+"§c"+ Applicationex.MESSAGES.getMessage("command.groups.add.nameinuse"));
                                        return true;
                                    }

                                    new Group(name);
                                    sender.sendMessage(Applicationex.getPrefix()+"§a"+ Applicationex.MESSAGES.getMessage("command.groups.add.success"));
                                } else {
                                    sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.groups.add.help"));
                                }
                            }

                            case "remove" -> {
                                if (args.length == 3) {
                                    String name = args[2];
                                    Group group = Group.getGroup(name);
                                    if (group != null) {
                                        group.delete();
                                        sender.sendMessage(Applicationex.getPrefix()+"§a"+ Applicationex.MESSAGES.getMessage("command.groups.remove.success"));
                                    } else {
                                        sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.groups.notexisting"));
                                    }
                                } else {
                                    sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.groups.remove.help"));
                                }
                            }

                            case "edit" -> {
                                if (args.length > 3) {
                                    String name = args[2];
                                    Group group = Group.getGroup(name);
                                    if (group != null) {
                                        switch (args[3].toLowerCase()) {
                                            case "prefix" -> {
                                                if (args.length == 5) {
                                                    sender.sendMessage(Applicationex.getPrefix() +"§a"+  Applicationex.MESSAGES.getMessage("command.groups.edit.success"));
                                                    group.prefix = args[4];
                                                    group.save();
                                                } else {
                                                    sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.prefix.help"));
                                                }
                                            }

                                            case "members" -> {
                                                if (args.length > 4) {
                                                    switch (args[4].toLowerCase()) {
                                                        case "list" -> {
                                                            sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.groups.edit.members.list"));
                                                            for (Map.Entry<String, Long> member : group.members.entrySet()) {
                                                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(member.getKey()));
                                                                sender.sendMessage("§8- §7"+offlinePlayer.getName()+" ("+Applicationex.MESSAGES.getMessage("durationselection.duration")+": §9"+ MemberUtils.getDuration(member.getValue()) +"§7)");
                                                            }
                                                        }

                                                        case "add" -> {
                                                            if (args.length == 6) {
                                                                String playername = args[5];
                                                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playername);
                                                                if (sender instanceof Player player) {
                                                                    new DurationSelection(new DurationSelectionSession(player, offlinePlayer.getUniqueId().toString(), group));
                                                                } else {
                                                                    sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.onlyplayers"));
                                                                }
                                                            } else {
                                                                sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.members.add.help"));
                                                            }
                                                        }

                                                        case "remove" -> {
                                                            if (args.length == 6) {
                                                                String playername = args[5];
                                                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playername);
                                                                if (group.members.containsKey(offlinePlayer.getUniqueId().toString())) {
                                                                    group.members.remove(offlinePlayer.getUniqueId().toString());
                                                                    sender.sendMessage(Applicationex.getPrefix() +"§a"+  Applicationex.MESSAGES.getMessage("command.groups.edit.success"));
                                                                } else {
                                                                    sender.sendMessage(Applicationex.getPrefix() +"§c"+ Applicationex.MESSAGES.getMessage("command.groups.edit.members.remove.notexisting"));
                                                                }
                                                            } else {
                                                                sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.members.remove.help"));
                                                            }
                                                        }

                                                        default -> {
                                                            sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.members.help"));
                                                        }
                                                    }
                                                } else {
                                                    sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.members.help"));
                                                }
                                            }

                                            case "permissions" -> {

                                            }

                                            default -> {
                                                sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.help"));
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.groups.notexisting"));
                                    }
                                } else {
                                    sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.groups.edit.help"));
                                }
                            }

                            default -> {
                                sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.help"));
                            }
                        }
                    } else {
                        sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.groups.help"));
                    }
                }
            }
        }

        return true;
    }
}
