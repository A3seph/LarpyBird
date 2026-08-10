import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

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
            new BirdSkins("Allen", "./Allen.png", "./Allenjump.wav"),
            new BirdSkins("Ernesto", "./Ernesto.png", "./Ernestojump.wav")
    };

    //The list of the birds to select them as the skin
    JComboBox<BirdSkins> birdSelector;

    SettingMenu() {
        //size of the panel that will be shown in the window
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setLayout(null);

        //Image Background
        try {
            imageBg = ImageIO.read(getClass().getResource("./menubg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        JLabel birdLabel = new JLabel("Birb");
        birdLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        birdLabel.setForeground(Color.white);
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

    public BirdSkins getSelectedBird() {
        return (BirdSkins) birdSelector.getSelectedItem();
    }

}
