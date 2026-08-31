package main.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SettingMenu extends JPanel {

    public JButton backButton;

    Image imageBg;

    Font SuperJoyful;
    Font DiaryPixel;

    private final JLabel title;
    public JCheckBox soundCheck;

    public SettingMenu() {
        setLayout(null);

        //Fonts
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Diary_Pixel.ttf");
            DiaryPixel = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(75f);
            is = getClass().getResourceAsStream("/fonts/Super_Joyful.ttf");
            SuperJoyful = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(SuperJoyful);
            ge.registerFont(DiaryPixel);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }


        //Image Background
        try {
            imageBg = ImageIO.read(getClass().getResource("/ingamepics/background1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Title of the Settings
        title = new JLabel("Settings");
        title.setFont(DiaryPixel);
        title.setForeground(Color.black);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        //==Sound Toggle==
        soundCheck = new JCheckBox("Sound On");
        soundCheck.setFont(SuperJoyful);
        soundCheck.setFocusPainted(false);
        soundCheck.setSelected(true);
        add(soundCheck);

        //The structure for the back button to go back to the main menu
        backButton = new JButton("Back");
        backButton.setFont(SuperJoyful);
        backButton.setFocusPainted(false);
        add(backButton);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int width = getWidth();
        int height = getHeight();

        title.setBounds(0, height / 6, width, 100);
        soundCheck.setBounds(width / 2 - 80, height / 2 - 40, 160, 30);
        backButton.setBounds(width - 140, height - 70, 110, 40);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageBg != null) {
            g.drawImage(imageBg, 0, 0, getWidth(), getHeight(), null);

        }
    }
}