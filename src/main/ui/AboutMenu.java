package main.ui;

import main.datastorage.audio.Sounds;
import main.datastorage.DeveloperInfo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutMenu extends JPanel {

    public JButton backButton2;

    Image imageBg;

    private final JLabel faceDisplay;
    private final JLabel title;

    static final int FACE_SIZE = 130;
    private final JLabel[] developerLabels;

    Font SuperJoyful;
    Font DiaryPixel;

    static final DeveloperInfo[] DEVELOPERS = {
            new DeveloperInfo("BORROMEO, Allen Joseph (Leader)", "/images/allen.png", "/sounds/allendorya.wav"),
            new DeveloperInfo("BULAN, Elijah", "/images/elijah.png", "/sounds/elijah.wav"),
            new DeveloperInfo("BOX, Gabriel Alexander", "/images/gabriel.png", "/sounds/gabriel.wav"),
            new DeveloperInfo("ARADO, Kelly", "/images/kelly.png", "/sounds/kelly.wav"),
            new DeveloperInfo("CANILLO, Erns", "/images/erns.png", "/sounds/erns.wav"),
            new DeveloperInfo("PANSACALA, Ernesto", "/images/ernesto.png", "/sounds/ernesto.wav")
    };

    public AboutMenu() {
        setLayout(null);

        //Fonts
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Diary_Pixel.ttf");
            DiaryPixel = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/fonts/Super_Joyful.ttf");
            SuperJoyful = Font.createFont(Font.TRUETYPE_FONT, is);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(SuperJoyful);
            ge.registerFont(DiaryPixel);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        //Image background
        try {
            imageBg = ImageIO.read(getClass().getResource("/ingamepics/background2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Title of the Settings
        title = new JLabel("The Developers.");
        title.setFont(DiaryPixel.deriveFont(Font.BOLD,75f));
        title.setForeground(Color.black);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        //Face display area
        faceDisplay = new JLabel("Click a name to say hi!", SwingConstants.CENTER);
        faceDisplay.setFont(DiaryPixel.deriveFont(Font.BOLD,15f));
        faceDisplay.setForeground(Color.darkGray);
        faceDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        add(faceDisplay);

        //Developer name labels — each one is clickable
        developerLabels = new JLabel[DEVELOPERS.length];
        for (int i = 0; i < DEVELOPERS.length; i++) {
            DeveloperInfo developer = DEVELOPERS[i];
            JLabel label = new JLabel(developer.label);
            label.setFont(SuperJoyful.deriveFont(Font.PLAIN,20f));
            label.setForeground(Color.black);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //shows it's clickable

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showDeveloper(developer);
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
            developerLabels[i] = label;
        }

        //The structure for the back button to go back to the main menu
        backButton2 = new JButton("Back");
        backButton2.setFont(SuperJoyful.deriveFont(Font.PLAIN,20f));
        backButton2.setFocusPainted(false);
        backButton2.setBorderPainted(false);
        add(backButton2);

    }

    private void showDeveloper(DeveloperInfo developer) {
        try {
            Image face = ImageIO.read(getClass().getResource(developer.imagepath));
            if (face != null) {
                Image scaled = face.getScaledInstance(FACE_SIZE, FACE_SIZE, Image.SCALE_SMOOTH);
                faceDisplay.setText(null);
                faceDisplay.setIcon(new ImageIcon(scaled));
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Couldn't load face image: " + developer.imagepath);
        }

        Sounds.playSound(developer.soundPath);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int width = getWidth();
        int height = getHeight();

        title.setBounds(0, 45, width, 100);
        faceDisplay.setBounds(width / 2 - 200 / 2, 140, 190, 180);
        int startY = 300;
        for (JLabel label : developerLabels) {
            label.setBounds(0, startY, width, 32);
            startY += 35;
        }
        backButton2.setBounds(width - 140, height - 70, 110, 40);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageBg != null) {
            g.drawImage(imageBg, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
