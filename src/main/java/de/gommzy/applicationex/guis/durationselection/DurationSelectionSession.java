package de.gommzy.applicationex.guis.durationselection;

import de.gommzy.applicationex.groups.Group;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DurationSelectionSession {
    public Player player;
    public String targetUUID;
    public Group group;
    public int days = 0;
    public int hours = 0;
    public int minutes = 0;
    public int seconds = 0;

    public DurationSelectionSession(Player player, String targetUUID, Group group) {
        this.player = player;
        this.targetUUID = targetUUID;
        this.group = group;
    }
}
