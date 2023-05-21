package ru.permasha.castlewars.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GameFlag {

    Flag flag;

    private String world;

    private double x;

    private double y;

    private double z;

    private int time;

    public Flag getFlag() {
        return this.flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Location getDrop() {
        return new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ());
    }

}
