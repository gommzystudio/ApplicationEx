package de.gommzy.applicationex.guis.durationselection;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DurationSelectionListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        try {
            if (DurationSelection.SESSIONS.containsKey(event.getWhoClicked())) {
                Player player = (Player) event.getWhoClicked(); //Nur Spieler können in SESSIONS sein
                DurationSelectionSession durationSelectionSession = DurationSelection.SESSIONS.get(player);
                event.setCancelled(true);

                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
                if (itemName.startsWith("§f§l+1 ")) {
                    switch (GuiUtils.getApplicationExData(event.getCurrentItem())) {
                        case "day" -> durationSelectionSession.days++;
                        case "hour" -> durationSelectionSession.hours++;
                        case "minute" -> durationSelectionSession.minutes++;
                        case "second" -> durationSelectionSession.seconds++;
                    }
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,100,1);
                    new DurationSelection(durationSelectionSession);
                } else if (itemName.startsWith("§f§l-1 ")) {
                    switch (GuiUtils.getApplicationExData(event.getCurrentItem())) {
                        case "day" -> durationSelectionSession.days--;
                        case "hour" -> durationSelectionSession.hours--;
                        case "minute" -> durationSelectionSession.minutes--;
                        case "second" -> durationSelectionSession.seconds--;
                    }
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,100,1);
                    new DurationSelection(durationSelectionSession);
                } else if (event.getCurrentItem().getType() == Material.CLOCK) {
                    DurationSelection.SESSIONS.remove(player);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,100,1);
                    durationSelectionSession.group.addPlayer(durationSelectionSession.targetUUID, durationSelectionSession.days * 24 * 60 * 60 * 1000L + durationSelectionSession.hours * 60 * 60 * 1000L + durationSelectionSession.minutes * 60 * 1000L + durationSelectionSession.seconds * 1000L);
                    player.sendMessage(Applicationex.getPrefix() +"§a"+  Applicationex.MESSAGES.getMessage("command.groups.edit.success"));
                } else if (event.getCurrentItem().getType() == Material.BARRIER) {
                    DurationSelection.SESSIONS.remove(player);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,100,1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
