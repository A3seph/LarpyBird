package main.entity;

import java.awt.*;

//Parent class of pipe

public class Pipe implements Obstacle {
    public int x, y, width, height;
    public Image img;
    public boolean isTop = false;
    public boolean passed = false;

    public Pipe(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    @Override
    public void update(int velocityX) {
        x += velocityX;
    }

    @Override
    public void draw(Graphics2D g) {
        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);

        if (imgWidth <= 0 || imgHeight <= 0) {
            g.drawImage(img, x, y, width, height, null);
            return;
        }

        //scale so the source image's width maps exactly to our target width
        double scale = (double) width / imgWidth;
        int srcHeightNeeded = Math.min(imgHeight, (int) Math.round(height / scale));

        int sx1 = 0, sx2 = imgWidth;
        int sy1, sy2;

        if (isTop) {
            //top pipe hangs down; keep the bottom edge (cap near the gap), crop off the top
            sy2 = imgHeight;
            sy1 = imgHeight - srcHeightNeeded;
        } else {
            //bottom pipe rises up; keep the top edge (cap near the gap), crop off the bottom
            sy1 = 0;
            sy2 = srcHeightNeeded;
        }

        g.drawImage(img, x, y, x + width, y + height, sx1, sy1, sx2, sy2, null);
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }
}