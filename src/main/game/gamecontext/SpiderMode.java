package main.game.gamecontext;

import main.entity.Obstacle;
import main.entity.Pipe;
import main.entity.Spider;
import main.entity.Spike;
import main.datastorage.audio.Sounds;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/* SPIDER MODE :
Unlike the UFO Mode, this mode has a gravity issue. Sticking only in the end of the Y axis.
In other words it can only can sticks in two surfaces, the top and the bottom.
 */

public class SpiderMode implements GameMode {

    //Images
    private final Image spiderImage;
    private final Image SpikeImage;
    //reused images because this level needs the pipes too
    private final Image topPipeImage;
    private final Image bottomPipeImage;

    private final Font customFont;

    //Sound effects
    private final String switchSoundPath = "/sounds/switch.wav";

    //Spider
    private Spider spider;

    //Obstacles (the spikes for spider)
    private final ArrayList<Obstacle> obstacles =  new ArrayList<>();
    private final Random random = new Random();

    //Ceilings of the spikes
    private int floorY, ceilingY;

    //The spawning of the spikes
    private long spawnAccumulate = 0;

    //FPS
    private final int frameTimeMs = 1000/60;

    //Game state flags
    private boolean gameOver = false;

    //Images loaded here
    public SpiderMode(Image spiderImage, Image SpikeImage, Image topPipeImage, Image bottomPipeImage, Font customFont) {
        this.spiderImage = spiderImage;
        this.SpikeImage = SpikeImage;
        this.topPipeImage = topPipeImage;
        this.bottomPipeImage = bottomPipeImage;
        this.customFont = customFont;
    }

    //Required by Game mode. This enters the spider game mode when the score hit 25.
    @Override
    public void onEnter(GameContext ctx) {
        floorY = ctx.boardHeight - 60;
        ceilingY = 60;
        spider = new Spider(ctx.boardWidth / 6, floorY, ceilingY, 34, 34, spiderImage);

        obstacles.clear();
        spawnAccumulate = 0;
        gameOver = false;
    }

    //Required by Game mode. This exits the spider game when the score hits another 25
    //Changing from the Bird mode again.
    @Override
    public void onExit() {}

    //Required by Game mode. Game updates every time (scores, spawn time, hitbox etc.)
    @Override
    public void update(GameContext ctx) {
        if (gameOver) return;

        int levelIndex = ctx.level - 1;
        int velocityX = Difficulty.Spider_Speed[levelIndex];

        for (Obstacle o : obstacles) {
            o.update(velocityX);
        }

        obstacles.removeIf(o -> o.getHitbox().x + o.getHitbox().width < 0);

        spawnAccumulate += frameTimeMs;
        int spawnInterval = Difficulty.Spike_Spawn_Interval_Ms[levelIndex];
        if (spawnAccumulate >= spawnInterval) {
            spawnObstacle(ctx, levelIndex);
            spawnAccumulate -= spawnInterval;
        }

        for (Obstacle o : obstacles) {
            if (spider.getHitbox().intersects(o.getHitbox())) {
                gameOver = true;
            }
        }

        ctx.score += 0.5 / 60.0; //distant scoring since it has no 2 pipes to score.

        if (gameOver && (int) ctx.score >= ctx.highScore) {
            ctx.highScore = (int) ctx.score;
        }
    }

    private void spawnObstacle(GameContext ctx, int levelIndex) {
        boolean onCeiling = random.nextBoolean();
        int pipeChance = Difficulty.Pipe_Chances[levelIndex];

        if (pipeChance > 0 && random.nextInt(100) < pipeChance) {
            spawnPipeObstacle(ctx, onCeiling, levelIndex);
        } else {
            spawnSpike(ctx, onCeiling);
        }
    }

    //Spike spawning. (From top to bottom)
    private void spawnSpike(GameContext ctx, boolean onCeiling) {
        int y = onCeiling ? ceilingY : floorY - 30;
        obstacles.add(new Spike(ctx.boardWidth, y, 30, 30, onCeiling, SpikeImage));
    }

    //Pipes spawning (top to bottom chances only)
    private void spawnPipeObstacle(GameContext ctx, boolean onCeiling, int levelIndex) {
        int width = 50;
        int height = 100 + (levelIndex * 20); // taller at higher levels

        if (onCeiling) {
            Pipe pipe = new Pipe(ctx.boardWidth, ceilingY, width, height, topPipeImage);
            pipe.isTop = true;
            obstacles.add(pipe);
        } else {
            Pipe pipe = new Pipe(ctx.boardWidth, floorY - height, width, height, bottomPipeImage);
            obstacles.add(pipe);
        }
    }

    @Override
    public void handleKeyPressed(KeyEvent e, GameContext ctx) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            spider.toggleSurface();

            if (soundEnabled(ctx)) {
                Sounds.playSound(switchSoundPath);
            }
        }
    }

    private boolean soundEnabled(GameContext ctx) {
        return ctx.settings == null || ctx.settings.soundCheck.isSelected();
    }

    @Override
    public void draw(Graphics2D g, GameContext ctx) {
        g.setColor(new Color(150, 75, 0));
        g.fillRect(0, floorY, ctx.boardWidth, ctx.boardHeight - floorY);
        g.fillRect(0, 0, ctx.boardWidth, ceilingY);

        g.setColor(Color.green);
        g.fillRect(0, floorY, ctx.boardWidth, 4);
        g.fillRect(0, ceilingY - 4, ctx.boardWidth, 4);

        for (Obstacle o : obstacles) {
            o.draw(g);
        }

        // draws the real sprite (Conditional because when the spider is on top the image is needed to be inverted)
        if (spider.isOnCeiling()) {
            g.drawImage(spiderImage, spider.x, spider.getY() + spider.height, spider.width, -spider.height, null);
        } else {
            g.drawImage(spiderImage, spider.x, spider.getY(), spider.width, spider.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 25));
        g.drawString(String.valueOf((int) ctx.score), 10, 35);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Game over: " + (int) ctx.score, 10, 60);
            g.drawString("Press SPACE to restart", 10, ctx.boardHeight - 20);
        }
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

}
