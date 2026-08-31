package main.ui;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

public class MainMenu extends JPanel {

    public JButton playButton;
    public JButton settingsButton;
    public JButton aboutButton;
    public JButton exitButton;

    Font SuperJoyful;
    Font DiaryPixel;

    private final JLabel title;

    Image imageBackground;

    public MainMenu() {
        setLayout(null);

        //Fonts
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Diary_Pixel.ttf");
            DiaryPixel = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(75f);
            is = getClass().getResourceAsStream("/fonts/Super_Joyful.ttf");
            SuperJoyful = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(SuperJoyful);
            ge.registerFont(DiaryPixel);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        //Image Background
        try {
            imageBackground = ImageIO.read(getClass().getResource("/ingamepics/MenuBackground.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //==Title==
        title = new JLabel("Larpy Birb");
        title.setFont(DiaryPixel);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        //== Button for play button ==
        playButton = new JButton("Play");
        playButton.setFont(SuperJoyful);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        add(playButton);

        //== Button for settings button ==
        settingsButton = new JButton("Settings");
        settingsButton.setFont(SuperJoyful);
        settingsButton.setFocusPainted(false);
        settingsButton.setBorderPainted(false);
        add(settingsButton);


        //== Button for about button ==
        aboutButton = new JButton("About");
        aboutButton.setFont(SuperJoyful);
        aboutButton.setFocusPainted(false);
        aboutButton.setBorderPainted(false);
        add(aboutButton);

        //== Button for exit button ==
        exitButton = new JButton("Exit");
        exitButton.setFont(SuperJoyful);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

    }

    @Override
    public void doLayout() {
        super.doLayout();
        int height = getHeight();
        int width = getWidth();

        title.setBounds(0, height/4, width, 100);
        playButton.setBounds(width/2 - 80, height/2 - 60, 160, 50);
        settingsButton.setBounds(width/2 - 80, height/2 - 5, 160, 45);
        aboutButton.setBounds(width/2 - 80, height/2 + 45, 160, 45);
        exitButton.setBounds(width / 2 - 80, height / 2 + 95, 160, 45);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageBackground != null) {
            g.drawImage(imageBackground, 0, 0, getWidth(), getHeight(), null);
        }
    }
}