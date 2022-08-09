package de.gommzy.applicationex.groups;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.database.SQLite;
import de.gommzy.applicationex.utils.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.HashMap;

public class MemberUtils {
    public static String getDuration(long duration) {
        if (duration == -1) {
            return Applicationex.MESSAGES.getMessage("durationselection.lifetime");
        } else {
            return TimeUtils.timeInMillisToString(duration - Calendar.getInstance().getTimeInMillis());
        }
    }

    public static HashMap<String, MemberData> CACHE = new HashMap<>();
    public static MemberData getMemberData(String uuid) {
        MemberData memberData;
        if (CACHE.containsKey(uuid)) { //Vermeide unnötige MySQL Abfragen
            memberData = CACHE.get(uuid);
        } else {
            memberData = Applicationex.SQLITE.getPlayerGroup(uuid);
        }
        if (memberData == null || memberData.duration != -1 && memberData.duration < Calendar.getInstance().getTimeInMillis()) { //Frage ab ob Spieler keine Gruppe hat oder ob diese nicht lifetime und abgelaufen ist
            memberData = new MemberData(uuid, Group.getGroup("default"), -1);
        }
        CACHE.put(uuid, memberData);
        return memberData;
    }

    public static void checkPlayer(CommandSender commandSender, String playername, String uuid) {
        MemberData memberData = MemberUtils.getMemberData(uuid);
        String groupName = memberData.group.name;
        String duration = MemberUtils.getDuration(memberData.duration);
        if (groupName.equals("default")) {
            groupName = Applicationex.MESSAGES.getMessage("group.default");
        }
        commandSender.sendMessage(Applicationex.getPrefix()+playername+" "+Applicationex.MESSAGES.getMessage("command.info.message")+" §9"+groupName+" §7("+duration+")");
    }
}
