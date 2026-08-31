package main.entity;

import java.awt.*;

//Parent class of Spike

public class Spike implements Obstacle {
    public int x, y, width, height;
    public boolean onCeiling;
    public Image img;

    public Spike(int x, int y, int width, int height, boolean onCeiling, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onCeiling = onCeiling;
        this.img = img;
    }

    @Override
    public void update(int velocityX) {
        x += velocityX;
    }

    @Override
    public void draw(Graphics2D g) {
        if (onCeiling) {
            g.drawImage(img, x, y + height, width, -height, null);
        } else {
            g.drawImage(img, x, y, width, height, null);
        }
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }
}
