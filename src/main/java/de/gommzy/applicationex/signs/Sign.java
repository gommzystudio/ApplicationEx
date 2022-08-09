package de.gommzy.applicationex.signs;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.database.SQLite;
import de.gommzy.applicationex.groups.MemberData;
import de.gommzy.applicationex.groups.MemberUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Sign {
    public int x;
    public int y;
    public int z;
    public String worldName;
    public String uuid;

    public Sign(int x, int y, int z, String world, String uuid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldName = world;
        this.uuid = uuid;

        new BukkitRunnable() { //Updated das Schild alle 20 ticks
            @Override
            public void run() {
                MemberData memberData = MemberUtils.getMemberData(uuid);
                update(memberData);
            }
        }.runTaskTimerAsynchronously(Applicationex.PLUGIN,0,20);
    }

    public void update(MemberData memberData) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(memberData.uuid);
        String groupName = memberData.group.name;
        String duration = MemberUtils.getDuration(memberData.duration);
        if (groupName.equals("default")) {
            groupName = Applicationex.MESSAGES.getMessage("group.default");
        }
        final String finalGroupName = groupName;
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    delete();
                    cancel();
                    return;
                }
                Block block = world.getBlockAt(x,y,z);
                if (block instanceof org.bukkit.block.Sign sign) {
                    sign.line(0, Component.text(memberData.group.prefix+offlinePlayer.getName()));
                    sign.line(1, Component.text(""));
                    sign.line(2, Component.text(finalGroupName));
                    sign.line(3, Component.text(duration));
                    sign.update();
                } else {
                    delete();
                    cancel();
                }
            }
        }.runTask(Applicationex.PLUGIN);
    }

    public void delete() {
        Applicationex.SQLITE.deleteSign(this);
    }
}
