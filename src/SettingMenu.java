import javax.swing.*;
import java.awt.*;

public class SettingMenu extends JPanel {

    //initialization size of the panel of the window
    int boardWidth = 360;
    int boardHeight = 640;

    //button for the back button
    JButton backButton;

    //Variable Image
    Image imageBg;

    //Check box for the "sounds on"
    JCheckBox soundCheck;

    //Bird skins
    static final BirdSkins[] BIRD_OPTIONS = {
            //new BirdSkins("Normal Birb", "./Birbs/flappybird.png", "./Birbs/flappy_sfx.wav"),
            new BirdSkins("Allen", "Birbs/allen.png", "Birbs/allenjump.wav"),
            new BirdSkins("Ernesto", "Birbs/ernesto.png", "Birbs/ernestojump.wav"),
            new BirdSkins("Elijah", "Birbs/elijah.png", "Birbs/elijahjump1.wav"),
            new BirdSkins("Gabriel", "Birbs/gabriel.png", "Birbs/gabrieljump.wav"),
            new BirdSkins("Kelly", "Birbs/kelly.png", "Birbs/kellyjump.wav"),
            new BirdSkins("Erns", "Birbs/erns.png", "Birbs/ernsjump.wav")
    };

    //The list of the birds to select them as the skin
    JComboBox<BirdSkins> birdSelector;

    SettingMenu() {
        //size of the panel that will be shown in the window
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setLayout(null);

        //Image Background
        //try {
            //imageBg = ImageIO.read(getClass().getResource("./menubg.png"));
        //} catch (IOException e) {
           //e.printStackTrace();
        //}

        //Title of the Settings
        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.black);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 100, boardWidth, 50);
        add(title);

        //==Sound Toggle==
        soundCheck = new JCheckBox("Sound On");
        soundCheck.setFont(new Font("Arial", Font.PLAIN, 20));
        soundCheck.setFocusPainted(false);
        soundCheck.setSelected(true);
        soundCheck.setBounds(boardWidth/2 - 70, 300, 160, 30);
        add(soundCheck);

        //==Bird skin selector==
        JLabel birdLabel = new JLabel("Dirb");
        birdLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        birdLabel.setForeground(Color.black);
        birdLabel.setBounds(boardWidth/2 - 60, 340, 80, 30);
        add(birdLabel);

        birdSelector = new JComboBox<>(BIRD_OPTIONS);
        birdSelector.setFont(new Font("Arial", Font.BOLD, 16));
        birdSelector.setBounds(boardWidth/2 - 10, 340, 160, 30);
        add(birdSelector);

        //The structure for the back button to go back to the main menu
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setFocusPainted(false);
        backButton.setBounds(boardWidth - 110, 600, 90, 30);
        add(backButton);
    }

    //public Birdskins so this class can be used in LarpyGame.java
    public BirdSkins getSelectedBird() {
        return (BirdSkins) birdSelector.getSelectedItem();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageBg != null) {
            g.drawImage(imageBg, 0, 0, boardWidth, boardHeight, null);

        }
    }
}