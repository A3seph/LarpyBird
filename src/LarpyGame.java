import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class LarpyGame extends JPanel implements ActionListener, KeyListener {
    //Sizes for the Panel
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image birdImage;
    Image backgroundImage;
    Image topPipeImage;
    Image bottomPipeImage;

    //Initialization of Bird class (Hitbox of Pipe)
    int birdX = boardHeight / 8;
    int birdY = boardWidth / 2;
    int birdHeight = 34;
    int birdWidth = 24;


    //Class of Bird
    class Bird {
        int x = birdX;
        int y = birdY;
        int height = birdHeight;
        int width = birdWidth;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    //Initialization of Pipe Class (Hitbox of Pipe)
    int pipeX = boardHeight;
    int pipeY = 0;
    int pipeHeight = 64;
    int pipeWidth = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int height = pipeHeight;
        int width = pipeWidth;
        Image img;

        Pipe(Image img) {
            this.img = img;
        }
    }

    //Game Logic of the Bird and the pipes
    //So the System think it's moving
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;


    Timer gameLoop;

    //This class is asked to swapped out without needing to know the cardLayout or JFrames itself.
    private Runnable onReturnToMenu;

    private SettingMenu settings;

    private Clip bgMusic;

    private String jumpSoundPath;

    JButton menuButton;

    public void setOnReturnToMenu(Runnable onReturnToMenu) {
        this.onReturnToMenu = onReturnToMenu;
    }

    LarpyGame(SettingMenu settings) {
        this.settings = settings;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true); //Required so the panel can receive key events
        addKeyListener(this); //Route key events to the class's KeyPressed/KeyTyped/KeyReleased

        setLayout(null);

        //Button for main menu
        menuButton = new JButton("Menu");
        menuButton.setBounds(boardWidth - 70, 10, 60, 30);
        menuButton.setFocusable(false);
        menuButton.addActionListener(e -> returnToMenu());
        add(menuButton);

        //Load images
        backgroundImage = new ImageIcon(getClass().getResource("./example.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("./TopPipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./BottomPipe.png")).getImage();

        //Bird skins images
        BirdSkins skins = (settings != null) ? settings.getSelectedBird(): SettingMenu.BIRD_OPTIONS[0];
        birdImage = new ImageIcon(getClass().getResource(skins.imagePath)).getImage();

        //The birds sound jumping effect
        jumpSoundPath  = skins.jumpSoundPath;

        //background music
        bgMusic = Sounds.loadLoopingClip("./bgmusic.wav");

        //Bird
        bird = new Bird(birdImage);

        //Game time: this is essentially our "frame rate" clock
        //Every 1000/60 milliseconds (~60 times a second)
        //It will call it's own actionPerformed() method below.
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    // This method calls whenever it is needed to be redrawn (when closed and opening again)
    public void painComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    //This draws/load the images in the game.
    public void draw(Graphics g) {
        //Background image
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        //Bird image
        g.drawImage(birdImage, birdX, birdY, birdWidth, birdHeight, null);
    }

    public void move() {
        //bird move
        velocityY += gravity;
        bird.y += velocityY;
        bird.y += Math.max(bird.y, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void returnToMenu() {
        gameLoop.stop();
        if (onReturnToMenu != null) {
            onReturnToMenu.run();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
