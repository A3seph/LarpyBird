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
    int birdX = boardHeight/8;
    int birdY = boardWidth/2;
    int birdHeight = 34;
    int birdWidth = 24;


    //Class of Bird
    class Bird {
        int x = birdX;
        int y = birdY;
        int height = birdHeight;
        int width = birdWidth;
        Image img;
        Bird (Image img) {
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
        Pipe (Image img) {
            this.img = img;
        }
    }

    //Game Logic of the Bird and the pipes
    //So the System think it's moving
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    LarpyGame() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        setFocusable(true); //Required so the panel can receive key events
        addKeyListener(this); //Route key events to the class's KeyPressed/KeyTyped/KeyReleased

        //Load images
        backgroundImage
    }





}
