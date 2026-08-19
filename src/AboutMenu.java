import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;

//extension of jpanel, so it can be switched out
public class AboutMenu extends JPanel {

    //Self-explanatory of initialization
    JButton backButton2;

    int boardWidth = 360;
    int boardHeight = 640;

    Image imageBg;

    //Face Display
    JLabel faceDisplay;
    static final int FACE_SIZE = 130;

    static final AuthorInfo[] AUTHORS = {
            new AuthorInfo("BORROMEO, Allen Joseph (Leader)", "./Birbs/allen.png", "./Sounds/allendorya.wav"),
            new AuthorInfo("BULAN, Elijah", "./Birbs/elijah.png", "./Sounds/elijahjump1.wav"),
            new AuthorInfo("BOX, Gabriel Alexander", "./Birbs/gabriel.png", "./Sounds/gabrieljump.wav"),
            new AuthorInfo("ARADO, Kelly", "./Birbs/kelly.png", "./Sounds/kellyjump.wav"),
            new AuthorInfo("CANILLO, Erns", "./Birbs/erns.png", "./Sounds/ernsjump1.wav"),
            new AuthorInfo("CESTINA, Prince Khian", "./Birbs/kio.jpg", "./Sounds/kiojump.wav"),
            new AuthorInfo("PANSACALA, Ernesto", "./Birbs/ernesto.png", "./Sounds/ernestojump.wav")
    };

    AboutMenu() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setLayout(null);

        //Image background
        try {
            imageBg = ImageIO.read(getClass().getResource("./ingamepics/background2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Title of the Settings
        JLabel title = new JLabel("The Authors.");
        title.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 25));
        title.setForeground(Color.black);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 0, boardWidth, 50);
        add(title);

        //Face display area — sits between the title and the name list
        faceDisplay = new JLabel("Click a name to say hi!", SwingConstants.CENTER);
        faceDisplay.setFont(new Font("Arial", Font.ITALIC, 14));
        faceDisplay.setForeground(Color.darkGray);
        faceDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        faceDisplay.setBounds(boardWidth / 2 - FACE_SIZE / 2, 55, FACE_SIZE, FACE_SIZE);
        add(faceDisplay);

        //Author name labels — each one is clickable
        int y = 230;
        for (AuthorInfo author : AUTHORS) {
            JLabel label = new JLabel(author.label);
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            label.setForeground(Color.black);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBounds(0, y, boardWidth, 30);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //shows it's clickable

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showAuthor(author);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setForeground(new Color(70, 70, 200)); //slight highlight on hover
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    label.setForeground(Color.black);
                }
            });

            add(label);
            y += 30;
        }

        //The structure for the back button to go back to the main menu
        backButton2 = new JButton("Back");
        backButton2.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton2.setFocusPainted(false);
        backButton2.setBounds(boardWidth - 100, 600, 90, 30);
        add(backButton2);

    }

    private void showAuthor(AuthorInfo author) {
        try {
            Image face = ImageIO.read(getClass().getResource(author.imagepath));
            if (face != null) {
                Image scaled = face.getScaledInstance(FACE_SIZE, FACE_SIZE, Image.SCALE_SMOOTH);
                faceDisplay.setText(null);
                faceDisplay.setIcon(new ImageIcon(scaled));
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Couldn't load face image: " + author.imagepath);
        }

        Sounds.playSound(author.soundPath);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageBg != null) {
            g.drawImage(imageBg, 0, 0, boardWidth, boardHeight, null);
        }
    }
}
