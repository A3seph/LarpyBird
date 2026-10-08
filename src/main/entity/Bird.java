package main.entity;

import java.awt.Image;
import java.awt.Rectangle;

//Parent class of Bird

public class Bird {

    public int x, y, width, height;
    public Image img;

    public Bird(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }
}
