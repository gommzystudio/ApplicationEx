package de.gommzy.applicationex.commands;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.database.SQLite;
import de.gommzy.applicationex.groups.Group;
import de.gommzy.applicationex.groups.MemberData;
import de.gommzy.applicationex.groups.MemberUtils;
import de.gommzy.applicationex.guis.durationselection.DurationSelection;
import de.gommzy.applicationex.guis.durationselection.DurationSelectionSession;
import de.gommzy.applicationex.messages.Messages;
import de.gommzy.applicationex.signs.Sign;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Member;
import java.sql.PreparedStatement;
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
                case "info" -> new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (args.length == 1) {
                            if (sender instanceof Player player) {
                                MemberUtils.checkPlayer(sender,player.getName(),player.getUniqueId().toString());
                            } else { //Falls Command durch die Console ausgeführt wird, so kommt eine Hilfestellung
                                sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.info.help"));
                            }
                        } else {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            MemberUtils.checkPlayer(sender,offlinePlayer.getName(),offlinePlayer.getUniqueId().toString());
                        }
                    }
                }.runTaskAsynchronously(Applicationex.PLUGIN);
                case "user" -> {
                    if (!sender.hasPermission("applicationex.admin.user")) {
                        sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.permission")+" applicationex.admin.user");
                        return true;
                    }
                    if (args.length == 4) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                String playername = args[1];
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playername);
                                String groupname = args[3];
                                Group group = Group.getGroup(groupname);
                                if (groupname.equalsIgnoreCase("default")) {
                                    sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.user.default"));
                                } else if (group == null) {
                                    sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.groups.notexisting"));
                                } else {
                                    switch (args[2].toLowerCase()) {
                                        case "removegroup" -> {
                                            group.removePlayer(offlinePlayer.getUniqueId().toString());
                                            sender.sendMessage(Applicationex.getPrefix()+"§a"+Applicationex.MESSAGES.getMessage("command.user.removegroup.success"));
                                        }
                                        case "addgroup" -> {
                                            if (sender instanceof Player player) { //Eigentlich dumm ein GUI einzauben, da die Console somit keine Gurppen vergeben kann. Aber ich wollte gerne noch ein GUI in das Bewerbungsplugin einbauen.
                                                new DurationSelection(new DurationSelectionSession(player,offlinePlayer.getUniqueId().toString(),group));
                                            } else {
                                                sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.onlyplayers"));
                                            }
                                        }
                                    }
                                }
                            }
                        }.runTaskAsynchronously(Applicationex.PLUGIN);
                    } else {
                        sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.user.help"));
                    }
                }
                case "groups" -> {
                    if (!sender.hasPermission("applicationex.admin.groups")) {
                        sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.permission")+" applicationex.admin.groups");
                        return true;
                    }
                    if (args.length > 1) {
                        switch (args[1].toLowerCase()) {
                            case "list" -> {
                                sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.groups.list.title"));
                                for (Group group : Group.GROUPS) {
                                    sender.sendMessage("§8- §7"+group.name+" (§9"+group.permissions.size()+" "+Applicationex.MESSAGES.getMessage("command.groups.list.permissions")+"§7)");
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
                                    if (name.equalsIgnoreCase("default")) {
                                        sender.sendMessage(Applicationex.getPrefix()+"§c"+ Applicationex.MESSAGES.getMessage("command.groups.remove.default"));
                                    } else if (group != null) {
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
                                                if (args.length >= 5) {
                                                    sender.sendMessage(Applicationex.getPrefix() +"§a"+  Applicationex.MESSAGES.getMessage("command.groups.edit.success"));
                                                    group.prefix = "";
                                                    for (int i = 4; i < args.length; i++) {
                                                        group.prefix += args[i] + " ";
                                                    }
                                                    group.prefix = group.prefix.substring(0,group.prefix.length()-1);
                                                    group.save();
                                                    group.refeshPlayers();
                                                } else {
                                                    sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.prefix.help"));
                                                }
                                            }

                                            case "permissions" -> {
                                                if (args.length > 4) {
                                                    switch (args[4].toLowerCase()) {
                                                        case "list" -> {
                                                            sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.groups.edit.permissions.list"));
                                                            for (String permission : group.permissions) {
                                                                sender.sendMessage("§8- §7"+permission);
                                                            }
                                                        }

                                                        case "add" -> {
                                                            if (args.length == 6) {
                                                                String permission = args[5].toLowerCase();
                                                                if (group.permissions.contains(permission)) {
                                                                    sender.sendMessage(Applicationex.getPrefix() +"§c"+ Applicationex.MESSAGES.getMessage("command.groups.edit.members.permissions.existing"));
                                                                } else {
                                                                    group.permissions.add(permission);
                                                                    group.save();
                                                                    group.refeshPlayers();
                                                                    sender.sendMessage(Applicationex.getPrefix() +"§a"+  Applicationex.MESSAGES.getMessage("command.groups.edit.success"));
                                                                }
                                                            } else {
                                                                sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.permissions.add.help"));
                                                            }
                                                        }

                                                        case "remove" -> {
                                                            if (args.length == 6) {
                                                                String permission = args[5].toLowerCase();
                                                                if (group.permissions.contains(permission)) {
                                                                    group.permissions.remove(permission);
                                                                    group.save();
                                                                    group.refeshPlayers();
                                                                    sender.sendMessage(Applicationex.getPrefix() +"§a"+  Applicationex.MESSAGES.getMessage("command.groups.edit.success"));
                                                                } else {
                                                                    sender.sendMessage(Applicationex.getPrefix() +"§c"+ Applicationex.MESSAGES.getMessage("command.groups.edit.members.permissions.notexisting"));
                                                                }
                                                            } else {
                                                                sender.sendMessage(Applicationex.getPrefix() + Applicationex.MESSAGES.getMessage("command.groups.edit.members.permissions.help"));
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
                case "sign" -> {
                    if (!sender.hasPermission("applicationex.admin.sign")) {
                        sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.permission")+" applicationex.admin.sign");
                        return true;
                    }
                    if (args.length == 2) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (sender instanceof Player player) { //Kann logischerweise nur von einen Spieler ausgeführt werden
                                    String playername = args[1];
                                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playername);
                                    Block block = player.getTargetBlock(20);
                                    if (!block.getType().toString().contains("SIGN")) { //Frage ab obs ein Schild ist, nur schilder beinhalten "SIGN"
                                        sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.sign.nosign"));
                                    } else {
                                        int x = block.getX();
                                        int y = block.getY();
                                        int z = block.getZ();
                                        String world = block.getWorld().getName();
                                        if (Applicationex.SQLITE.existSign(x,y,z,world)) {
                                            sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.sign.alreadysign"));
                                        } else {
                                            Sign sign = new Sign(x,y,z,world,offlinePlayer.getUniqueId().toString(),offlinePlayer.getName());
                                            Applicationex.SQLITE.addSign(sign);
                                            sender.sendMessage(Applicationex.getPrefix() +"§a"+  Applicationex.MESSAGES.getMessage("command.sign.success"));
                                        }
                                    }
                                } else {
                                    sender.sendMessage(Applicationex.getPrefix() +"§c"+  Applicationex.MESSAGES.getMessage("command.onlyplayers"));
                                }
                            }
                        }.runTaskAsynchronously(Applicationex.PLUGIN);
                    } else {
                        sender.sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("command.sign.help"));
                    }
                }
            }
        }

        return true;
    }
}
