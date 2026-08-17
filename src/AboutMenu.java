import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

//extension of jpanel, so it can be switched out
public class AboutMenu extends JPanel {

    //Self-explanatory of initialization
    JButton backButton2;

    int boardWidth = 360;
    int boardHeight = 640;

    Image imageBg;

    AboutMenu() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setLayout(null);

        //try {
            //imageBg = ImageIO.read(getClass().getResource("./menubg.png"));
       // } catch (IOException e) {
            //e.printStackTrace();
       // }

        //Title of the Settings
        JLabel title = new JLabel("The Authors.");
        title.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 25));
        title.setForeground(Color.black);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 0, boardWidth, 50);
        add(title);

        //An Array List for the Authors/Creators
        String[] membersLabel = {
                "BORROMEO, Allen Joseph (Leader)",
                "Members:",
                "BULAN, Elijah",
                "BOX, Gabriel Alexander",
                "ARADO, Kelly",
                "CANILLO, Erns",
                "CESTINA, Prince Khian",
                "PANSACALA, Ernesto"
        };
        int y = 200;
        for (String name : membersLabel) {
            JLabel label = new JLabel(name);
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            label.setForeground(Color.black);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBounds(0, y, boardWidth, 30);
            add(label);
            y +=30;
            }

        //The structure for the back button to go back to the main menu
        backButton2 = new JButton("Back");
        backButton2.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton2.setFocusPainted(false);
        backButton2.setBounds(boardWidth - 100, 600, 90, 30);
        add(backButton2);
    }
}
