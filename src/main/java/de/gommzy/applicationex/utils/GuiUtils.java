package de.gommzy.applicationex.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class GuiUtils {
    //Diese Utils Klasse ist eine selbst erstellte Vorlage (vor ca. 2-3 Jahren), welche ich in jedem Plugin verwende.

    public static Inventory fillInventory(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§1");
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i,itemStack);
        }
        return inventory;
    }

    public static ItemStack glow(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createGuiSkull(String name, List<String> lore, String value) {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) item.getItemMeta();
        final PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
        final ProfileProperty profileProperty = new ProfileProperty("textures",value);
        playerProfile.getProperties().add(profileProperty);
        meta.setOwnerProfile(playerProfile);
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setAmount(1);
        item.setDurability((short) 3);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createGuiSkull(String name, String value) {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) item.getItemMeta();
        final PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
        final ProfileProperty profileProperty = new ProfileProperty("textures",value);
        playerProfile.getProperties().add(profileProperty);
        meta.setOwnerProfile(playerProfile);
        meta.setDisplayName(name);
        item.setAmount(1);
        item.setDurability((short) 3);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack editItem(ItemStack itemStack, Integer amount) {
        if (amount < 1) {
            amount = 1; //Leider kann man nur in der 1.8 stacks mit weniger als einem item anzeigen
        }
        itemStack.setAmount(amount);
        return itemStack;
    }

    public static ItemStack editItem(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack editItem(ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack editCustomModelData(ItemStack itemStack, int customModelData) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack editItem(ItemStack itemStack, String name, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return itemStack;
    }

    public static ItemStack createGuiItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createGuiItem(Material material, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createGuiItem(Material material, String name, List<String> lore, int amount) {
        if (amount < 1) {
            amount = 1; //Leider kann man nur in der 1.8 stacks mit weniger als einem item anzeigen
        }
        ItemStack itemStack = new ItemStack(material,amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static final NamespacedKey namespacedKey = new NamespacedKey("applicationex","data");

    public static ItemStack setApplicationExData(ItemStack itemStack, String data) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getCustomTagContainer().setCustomTag(namespacedKey, ItemTagType.STRING, data);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static String getApplicationExData(ItemStack itemStack) {
        return itemStack.getItemMeta().getCustomTagContainer().getCustomTag(namespacedKey,ItemTagType.STRING);
    }
}
