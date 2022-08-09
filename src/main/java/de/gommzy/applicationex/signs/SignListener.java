package de.gommzy.applicationex.signs;

import de.gommzy.applicationex.Applicationex;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SignListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().toString().contains("SIGN")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Block block = event.getBlock();
                    if (Applicationex.SQLITE.existSign(block.getX(),block.getY(),block.getZ(),block.getWorld().getName())) {
                        Applicationex.SQLITE.deleteSign(block.getX(),block.getY(),block.getZ(),block.getWorld().getName());
                        event.getPlayer().sendMessage(Applicationex.getPrefix()+Applicationex.MESSAGES.getMessage("sign.deleted"));
                    }
                }
            }.runTaskAsynchronously(Applicationex.PLUGIN);
        }
    }
}
