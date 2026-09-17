package main.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

//Collisions. Example: The bird and the pipes colliding
public interface Obstacle {

    void update(int velocityX);

    void draw(Graphics2D g);

    Rectangle getHitbox();
}