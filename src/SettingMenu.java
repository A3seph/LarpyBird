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

    JCheckBox soundCheck;

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

        //==Checking of the sound system==
        soundCheck = new JCheckBox("Sound On");
        soundCheck.setFont(new Font("Arial", Font.PLAIN, 20));
        soundCheck.setFocusPainted(false);
        soundCheck.setSelected(true);
        soundCheck.setBounds(boardWidth/2 - 70, 300, 160, 30);
        add(soundCheck);

        //The structure for the back button to go back to the main menu
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setFocusPainted(false);
        backButton.setBounds(boardWidth - 110, 600, 90, 30);
        add(backButton);
    }

}
