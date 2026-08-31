package main.game.gamecontext;

import main.datastorage.audio.Sounds;
import main.entity.Bird;
import main.entity.Pipe;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/* UFO MODE:
This game mode is like a "Flappy bird" so it is self-explanatory when playing this mode.
 */
public class BirdMode implements GameMode {

    //Images
    private final Image birdImage;
    private final Image topPipeImage;
    private final Image bottomPipeImage;
    private final String jumpSoundPath = "/sounds/flappy_sfx.wav";

    //Fonts
    private final Font customFont;

    //Bird
    private Bird bird;

    //Obstacles (The pipes of Bird)
    private final ArrayList<Pipe> pipes = new ArrayList<>();
    private final Random random = new Random();

    //Bird width and Height. Pipes Width and Height
    private final int birdWidth = 34;
    private final int birdHeight = 24;
    private final int pipeWidth = 64;
    private final int pipeHeight = 512;

    //Speed & gravity for the bird and pipes.
    private int velocityX;
    private int velocityY = 0;
    private final int gravity = 1;

    //Pipes spawn timer
    private long pipeSpawnAccumulate = 0;

    //Game state Flags
    private boolean gameStart = false;
    private boolean gameOver = false;

    //FPS
    private final int frameTimeMs = 1000 / 60;

    //Images loaded here
    public BirdMode(Image birdImage, Image topPipeImage, Image bottomPipeImage, Font customFont) {
        this.birdImage = birdImage;
        this.topPipeImage = topPipeImage;
        this.bottomPipeImage = bottomPipeImage;
        this.customFont = customFont;
    }

    //Required by Game mode. This enters the Bird game mode.
    //This is the first game mode when hit "play"
    @Override
    public void onEnter(GameContext ctx) {
        int birdX = ctx.boardWidth / 9;
        int birdY = ctx.boardHeight / 2;
        bird = new Bird(birdX, birdY, birdWidth, birdHeight, birdImage);

        pipes.clear();
        velocityY = 0;
        pipeSpawnAccumulate = 0;

        gameStart = ctx.started;
        gameOver = false;
    }

    //Required by Game mode. Exiting to the bird mode into spider mode when hit 25.
    @Override
    public void onExit() {}

    //Required by Game mode. Game updates every time (scores, spawn time, hitbox etc.)
    @Override
    public void update(GameContext ctx) {
        if (!gameStart || gameOver) return;

        int levelIndex = ctx.level - 1;
        int velocityX = Difficulty.Bird_Speed[levelIndex];

        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.update(velocityX);

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                ctx.score += 0.5; // two pipes per pair, 0.5 + 0.5 = 1 point
                pipe.passed = true;

                if (pipe.isTop && soundEnabled(ctx)) {
                    Sounds.playSound("/sounds/sfx_point.wav");
                }
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        spawnPipesIfDue(ctx, levelIndex);

        if (bird.y > ctx.boardHeight) {
            gameOver = true;
        }

        if (gameOver && (int) ctx.score > ctx.highScore) {
            ctx.highScore = (int) ctx.score;
        }
    }

    //Pipes spawning
    private void spawnPipesIfDue(GameContext ctx, int levelIndex) {
        pipeSpawnAccumulate += frameTimeMs;
        int pipeSpawnInterval = Difficulty.Pipes_Spawn_Interval_Ms[levelIndex];
        if (pipeSpawnAccumulate >= pipeSpawnInterval) {
            placePipes(ctx, levelIndex);
            pipeSpawnAccumulate -= pipeSpawnInterval;
        }
    }

    //Placement of pipes
    private void placePipes(GameContext ctx, int levelIndex) {
        int pipeX = ctx.boardWidth;
        int pipeY = 0;

        int randomPipeY = (int) (pipeY - pipeHeight / 4.0 - Math.random() * (pipeHeight / 2.0));
        int gapDivisor = Difficulty.Pipes_Gap_Divisor[levelIndex];
        int openSpace = ctx.boardHeight / gapDivisor;

        Pipe topPipe = new Pipe(pipeX, randomPipeY, pipeWidth, pipeHeight, topPipeImage);
        topPipe.isTop = true;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(pipeX, topPipe.y + pipeHeight + openSpace, pipeWidth, pipeHeight, bottomPipeImage);

        int maxOffset = Difficulty.Pipes_Spawn_Offset[levelIndex];
        if (maxOffset > 0) {
            int offset = random.nextInt(maxOffset + 1);
            boolean shiftRight = random.nextBoolean();
            bottomPipe.x = topPipe.x + (shiftRight ? offset : -offset);
        }
        pipes.add(bottomPipe);
    }

    //Collisions
    private boolean collision(Bird birdA, Pipe pipeB) {
        return birdA.x < pipeB.x + pipeB.width &&
                birdA.x + birdA.width > pipeB.x &&
                birdA.y < pipeB.y + pipeB.height &&
                birdA.y + birdA.height > pipeB.y;
    }

    @Override
    public void draw(Graphics2D g, GameContext ctx) {
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }

        g.setColor(Color.black);
        g.setFont(customFont.deriveFont(Font.BOLD, 25f));
        if (gameOver) {
            g.drawString("Game over, skill issue: " + (int) ctx.score, 10, 35);
            g.setFont(customFont.deriveFont(Font.BOLD, 16f));
            if ((int) ctx.score >= ctx.highScore) {
                g.drawString("NEW BEST:", 10, 60);
            } else {
                g.drawString("BEST: " + ctx.highScore, 10, 60);
            }
            g.drawString("Press SPACE to restart", 10, ctx.boardHeight - 20);
        } else {
            g.drawString(String.valueOf((int) ctx.score), 10, 35);
            g.setFont(customFont.deriveFont(Font.BOLD, 16f));
            g.drawString("Best: " + ctx.highScore, 10, 60);
        }

        if (!gameStart && !gameOver) {
            g.setFont(customFont.deriveFont(Font.BOLD, 18f));
            g.drawString("Press SPACE to start", 60, ctx.boardHeight / 2 - 40);
        }
    }

    @Override
    public void handleKeyPressed(KeyEvent e, GameContext ctx) {
        if (e.getKeyCode() != KeyEvent.VK_SPACE || gameOver) return;

        if (!gameStart) {
            gameStart = true;
            ctx.started = true;
        }

        velocityY = -9;

        if (soundEnabled(ctx)) {
            Sounds.playSound(jumpSoundPath);
        }
    }

    private boolean soundEnabled(GameContext ctx) {
        return ctx.settings == null || ctx.settings.soundCheck.isSelected();
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }
}