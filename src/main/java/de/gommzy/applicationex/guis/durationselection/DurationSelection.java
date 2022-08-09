package de.gommzy.applicationex.guis.durationselection;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;

public class DurationSelection {
    public static HashMap<Player, DurationSelectionSession> SESSIONS = new HashMap<>();

    String greenArrowUp = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhMDI3NDc3MTk3YzZmZDdhZDMzMDE0NTQ2ZGUzOTJiNGE1MWM2MzRlYTY4YzhiN2JjYzAxMzFjODNlM2YifX19";
    String greenArrowDown = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY3NDE2Y2U5ZTgyNmU0ODk5YjI4NGJiMGFiOTQ4NDNhOGY3NTg2ZTUyYjcxZmMzMTI1ZTAyODZmOTI2YSJ9fX0=";
    String grayArrowDown = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5YWU3YTRiZTY1ZmNiYWVlNjUxODEzODlhMmY3ZDQ3ZTJlMzI2ZGI1OWVhM2ViNzg5YTkyYzg1ZWE0NiJ9fX0=";
    DurationSelectionSession durationSelectionSession;
    Inventory inventory;


    public DurationSelection(DurationSelectionSession durationSelectionSession) {
        SESSIONS.put(durationSelectionSession.player,durationSelectionSession);

        this.durationSelectionSession = durationSelectionSession;
        this.inventory = Bukkit.createInventory(durationSelectionSession.player, 9 * 5, "§f§lSELECT DURATION");

        GuiUtils.fillInventory(inventory);

        addSlider(10,durationSelectionSession.days,"day","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmE2NjE0MTlkZTQ5ZmY0YTJjOTdiMjdmODY4MDE0ZmJkYWViOGRkN2Y0MzkyNzc3ODMwYjI3MTRjYWFmZDFmIn19fQ==");
        addSlider(11,durationSelectionSession.hours,"hour","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JhOWMzM2E5NWZhMWU1MTlmODVhNDFjYTU2Nzk5Mzg0ZGI0MWZlN2UxZDdhNzkxNzUxZWNlOWJiYWU1ZDI3ZiJ9fX0=");
        addSlider(12,durationSelectionSession.minutes,"minute","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhkZWM0NjY2YjRjNjdkODc1OTcxNGM4NTcxNGJlNmVhNGUzOWZmOTYyODg0OWY5OGI1MTRlZGYxYzNlNDY4MCJ9fX0=");
        addSlider(13,durationSelectionSession.seconds,"second","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDcxMDEzODQxNjUyODg4OTgxNTU0OGI0NjIzZDI4ZDg2YmJiYWU1NjE5ZDY5Y2Q5ZGJjNWFkNmI0Mzc0NCJ9fX0=");

        String duration = Applicationex.MESSAGES.getMessage("durationselection.lifetime");
        if (durationSelectionSession.days > 0 || durationSelectionSession.minutes > 0 || durationSelectionSession.seconds > 0) {
            duration = durationSelectionSession.days+"d, "+durationSelectionSession.hours+"h, "+durationSelectionSession.minutes+"m & "+durationSelectionSession.seconds+"s";
        }
        inventory.setItem(24,GuiUtils.createGuiItem(Material.CLOCK,"§a§l"+Applicationex.MESSAGES.getMessage("durationselection.confirm"),Arrays.asList("§7"+Applicationex.MESSAGES.getMessage("durationselection.duration")+": "+duration)));
        inventory.setItem(25,GuiUtils.createGuiItem(Material.BARRIER,"§c§l"+Applicationex.MESSAGES.getMessage("durationselection.cancel")));


        new BukkitRunnable() {
            @Override
            public void run() {
                durationSelectionSession.player.openInventory(inventory);
            }
        }.runTask(Applicationex.PLUGIN);
    }

    public void addSlider(int slot, int value, String key, String skull) {
        inventory.setItem(slot, GuiUtils.setApplicationExData(GuiUtils.createGuiSkull("§f§l+1 "+Applicationex.MESSAGES.getMessage("durationselection."+key+"s"), greenArrowUp),key));
        inventory.setItem(slot+9, GuiUtils.editItem(GuiUtils.createGuiSkull("§f§l"+value+" "+ Applicationex.MESSAGES.getMessage("durationselection."+key+"s"), skull),value));
        if (value > 0) {
            inventory.setItem(slot+18, GuiUtils.setApplicationExData(GuiUtils.createGuiSkull("§f§l-1 " + Applicationex.MESSAGES.getMessage("durationselection."+key+"s"),greenArrowDown),key));
        } else {
            inventory.setItem(slot+18, GuiUtils.createGuiSkull("§7§l-1 " + Applicationex.MESSAGES.getMessage("durationselection."+key+"s"), grayArrowDown));
        }
    }
}
