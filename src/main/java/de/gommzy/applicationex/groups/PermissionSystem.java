package de.gommzy.applicationex.groups;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;
import org.w3c.dom.events.Event;

import java.util.HashMap;

public class PermissionSystem implements Listener {
    public PermissionSystem() {
        //Prüft ob ein Rang abgelaufen ist
        //Jede Minute werden die Ränge der online Spieler gecheckt
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayer(player);
                }
            }
        }.runTaskTimerAsynchronously(Applicationex.PLUGIN,20*60,20*60);
    }

    public static HashMap<Player, PermissionAttachment> PERMISSIONS = new HashMap<>();

    public static void updatePlayer(Player player) { //Aktualisiere Gruppen Prefix und Permissions
        MemberData memberData = MemberUtils.getMemberData(player.getUniqueId().toString());
        String name = ChatColor.translateAlternateColorCodes('&',memberData.group.prefix) +player.getName();
        player.setPlayerListName(name);
        player.setDisplayName(name);
        PermissionAttachment permissionAttachment;
        if (PERMISSIONS.containsKey(player)) {
            permissionAttachment = PERMISSIONS.get(player);
            for (String permission : permissionAttachment.getPermissions().keySet()) { //Lösche alle vorhandenen Perms
                permissionAttachment.unsetPermission(permission);
            }
        } else {
            permissionAttachment = player.addAttachment(Applicationex.PLUGIN);
        }
        for (String permission : memberData.group.permissions) {
            permissionAttachment.setPermission(permission,true);
        }

        //Schönste und einfachste Lösung um * Rechte zu vergeben :)
        player.setOp(memberData.group.permissions.contains("*"));

        PERMISSIONS.put(player,permissionAttachment);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        new BukkitRunnable() {
            @Override
            public void run() {
                updatePlayer(event.getPlayer());
                Bukkit.broadcastMessage(Applicationex.MESSAGES.getMessage("join").replace("%player%",event.getPlayer().getDisplayName()));
            }
        }.runTaskAsynchronously(Applicationex.PLUGIN);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.broadcastMessage(Applicationex.MESSAGES.getMessage("quit").replace("%player%",event.getPlayer().getDisplayName()));
        event.setQuitMessage(null);
        PERMISSIONS.remove(event.getPlayer()); //verhindert, dass PERMISSIONS überflüssige Einträge hat
    }
}
