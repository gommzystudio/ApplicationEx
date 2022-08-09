package de.gommzy.applicationex.signs;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.database.SQLite;
import de.gommzy.applicationex.groups.MemberData;
import de.gommzy.applicationex.groups.MemberUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Sign {
    public boolean deleted = false;
    public int x;
    public int y;
    public int z;
    public String worldname;
    public String uuid;
    public String playername;

    public Sign(int x, int y, int z, String world, String uuid, String playername) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldname = world;
        this.uuid = uuid;
        this.playername = playername;

        new BukkitRunnable() { //Updated das Schild alle 20 ticks
            @Override
            public void run() {
                if (Applicationex.PLUGIN.isEnabled() && !deleted) { //Nötig um /reload zu supporten
                    MemberData memberData = MemberUtils.getMemberData(uuid);
                    update(memberData);
                } else {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Applicationex.PLUGIN,0,20);
    }

    public void update(MemberData memberData) {
        String groupName = memberData.group.name;
        String duration = MemberUtils.getDuration(memberData.duration);
        if (groupName.equals("default")) {
            groupName = Applicationex.MESSAGES.getMessage("group.default");
        }
        final String finalGroupName = groupName;
        if (Applicationex.PLUGIN.isEnabled()) { //Nötig um /reload zu supporten
            new BukkitRunnable() {
                @Override
                public void run() {
                    World world = Bukkit.getWorld(worldname);
                    if (world == null) {
                        delete();
                        return;
                    }
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getState() instanceof org.bukkit.block.Sign sign) {
                        sign.line(0, Component.text(playername));
                        sign.line(1, Component.text(""));
                        sign.line(2, Component.text(finalGroupName));
                        sign.line(3, Component.text(duration));
                        sign.update();
                    } else {
                        delete();
                    }
                }
            }.runTask(Applicationex.PLUGIN);
        }
    }

    public void delete() {
        deleted = true;
        Applicationex.SQLITE.deleteSign(x,y,z,worldname);
    }
}
