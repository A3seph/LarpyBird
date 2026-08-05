import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu extends JPanel {

    //initialization size of the panel of the window
    int boardWidth = 360;
    int boardHeight = 640;

    //button for the play, settings, and about button
    JButton playButton;
    JButton settingsButton;
    JButton aboutButton;

    //Variable Image
    Image imageBackground;

    MainMenu() {

        //size of the panel that will be shown in the window
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setLayout(null);

        //Image Background
        try {
            imageBackground = ImageIO.read(getClass().getResource("./menubg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //==Title==
        JLabel title = new JLabel("Larpy Bird");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.black);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 150, boardWidth, 55);
        add(title);

        //== Button for play button ==
        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.PLAIN, 24));
        playButton.setFocusPainted(false); // removes the dotted focus outline for a cleaner look
        playButton.setBounds(boardWidth/2 - 60, 250, 120, 50); // centered horizontally, 120px wide
        add(playButton);

        //== Button for settings button ==
        settingsButton = new JButton("Settings");
        settingsButton.setFont(new Font("Arial", Font.PLAIN, 20));
        settingsButton.setFocusPainted(false);
        settingsButton.setBounds(boardWidth/2 - 60, 310, 120, 40); // sits just under Play
        add(settingsButton);


        //== Button for settings button ==
        aboutButton = new JButton("About");
        aboutButton.setFont(new Font("Arial", Font.PLAIN, 20));
        aboutButton.setFocusPainted(false);
        aboutButton.setBounds(boardWidth/2 - 60, 360, 120, 40); // sits just under Play
        add(aboutButton);

        //A hints for the gameplay
        JLabel controls = new JLabel("<html><center>SPACE to jump &nbsp; P to pause</center></html>");
        controls.setForeground(Color.white);
        controls.setHorizontalAlignment(SwingConstants.CENTER);
        controls.setBounds(0, 440, boardWidth, 35); // nudged down to make room for Settings
        add(controls);

    }
    //Override for the Image Background
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageBackground != null) {
            g.drawImage(imageBackground, 0, 0, boardWidth, boardHeight, null);
        }
    }
}