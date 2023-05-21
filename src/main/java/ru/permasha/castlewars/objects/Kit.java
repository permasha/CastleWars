package ru.permasha.castlewars.objects;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.permasha.castlewars.utils.ItemBuilder;
import ru.permasha.castlewars.utils.Utils;

import java.util.ArrayList;

public class Kit {

    GamePlayer gamePlayer;

    public Kit(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public ItemStack getHelmet() {
        return new ItemBuilder(Material.LEATHER_HELMET)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setUnbreakable(true)
                .setLeatherColor(Utils.stringToColor(gamePlayer.getTeam().getName()))
                .toItemStack();
    }

    public ItemStack getChestPlate() {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setUnbreakable(true)
                .setLeatherColor(Utils.stringToColor(gamePlayer.getTeam().getName()))
                .toItemStack();
    }

    public ItemStack getLeggings() {
        return new ItemBuilder(Material.LEATHER_LEGGINGS)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setUnbreakable(true)
                .setLeatherColor(Utils.stringToColor(gamePlayer.getTeam().getName()))
                .toItemStack();
    }

    public ItemStack getBoots() {
        return new ItemBuilder(Material.LEATHER_BOOTS)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setUnbreakable(true)
                .setLeatherColor(Utils.stringToColor(gamePlayer.getTeam().getName()))
                .toItemStack();
    }

    public ItemStack getSword() {
        return new ItemBuilder(Material.IRON_SWORD)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setUnbreakable(true)
                .toItemStack();
    }

    public ItemStack getBow() {
        return new ItemBuilder(Material.BOW)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setUnbreakable(true)
                .toItemStack();
    }

    public ItemStack getShop() {
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        int balance = gamePlayer.getBalance();
        if (balance <= 0) {
            itemStack.setAmount(1);
        }
        else itemStack.setAmount(Math.min(balance, 64));
        return dev.triumphteam.gui.builder.item.ItemBuilder.from(itemStack)
                .setName(Utils.colorize("&fМагазин &e" + gamePlayer.getBalance()))
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setUnbreakable(true)
                .build();
    }

    public ItemStack getHealthPotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setDisplayName(Utils.colorize("&eЗелье исцеления"));
        meta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1), true);
        potion.setItemMeta(meta);
        return potion;
    }

    public ItemStack getSpeedHealth() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setDisplayName(Utils.colorize("&eЗелье скорости"));
        meta.setColor(Color.AQUA);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 1), true);
        potion.setItemMeta(meta);
        return potion;
    }

    public ItemStack getArrows() {
        return new ItemBuilder(Material.ARROW)
                .setAmount(8)
                .toItemStack();
    }
}
