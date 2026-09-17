package main.entity;

import java.awt.Image;
import java.awt.Rectangle;

//Parent class of Spider

public class Spider {

    public int x;
    public int width, height;
    public Image img;

    private final int floorY;
    private final int ceilingY;
    private boolean onCeiling;

    public Spider(int x, int floorY, int ceilingY, int width, int height, Image img) {
        this.x = x;
        this.floorY = floorY;
        this.ceilingY = ceilingY;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    public int getY() {
        return onCeiling ? ceilingY : floorY - height;
    }

    public void toggleSurface() {
        onCeiling = !onCeiling;
    }

    public boolean isOnCeiling() {
        return onCeiling;
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, getY(), width, height);
    }
}

