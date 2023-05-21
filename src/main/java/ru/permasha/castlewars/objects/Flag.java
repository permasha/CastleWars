package ru.permasha.castlewars.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.permasha.castlewars.CastleWars;
import ru.xezard.glow.GlowAPI;
import ru.xezard.glow.data.glow.Glow;

public class Flag {
    Team team;

    Location fromLocation;
    Location toLocation;

    Location drop;

    Entity entity;

    Glow glow;

    boolean isStolen;

    public Flag(Team team, Location fromLocation, Location toLocation) {
        this.team = team;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.drop = fromLocation;
        this.entity = null;
        this.glow = null;
        this.isStolen = false;
    }

    public Team getTeam() {
        return team;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public Location getDrop() {
        return this.drop;
    }

    public void setDrop(Location location) {
        this.drop = location;
    }

    public Entity getEntity() {
        return entity;
    }

    public Glow getGlow() {
        return glow;
    }

    public void setGlow(Glow glow) {
        this.glow = glow;
    }

    public void setStolen(boolean hasStolen) {
        this.isStolen = hasStolen;
    }

    public boolean isStolen() {
        return isStolen;
    }

    public void spawnFlag(Arena arena, Location location) {
        ArmorStand armorStand = (ArmorStand) arena.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);

        Glow glow = Glow.builder()
                .color(ChatColor.valueOf(getTeam().getName()))
                .name(getTeam().getName())
                .build();
        glow.addHolders(armorStand);
        glow.display(arena.getWorld().getPlayers());

        this.entity = armorStand;
        this.glow = glow;

        String team = getTeam().getName();
        armorStand.getEquipment().setHelmet(new ItemStack(Material.valueOf(team + "_BANNER")));
    }
}
