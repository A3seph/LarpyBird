import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;

        // Basic window setup
        JFrame frame = new JFrame("Larpy Bird");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // A Screen-switching setup
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // A main menu
        MainMenu menu = new MainMenu();
        mainPanel.add(menu,"menu");
        // A settings menu
        SettingMenu setting = new SettingMenu();
        mainPanel.add(setting,"setting");
        // A about menu
        AboutMenu about = new AboutMenu();
        mainPanel.add(about,"about");

        //When clicked the "Settings" flips into Settings menu
        menu.settingsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "setting");
            setting.requestFocusInWindow();
        });

        //When clicked the "About" flips into About menu
        menu.aboutButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "about");
            about.requestFocusInWindow();
        });

        //When clicked in the main menu or returned in the main menu
        setting.backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
            menu.requestFocusInWindow();
        });

        about.backButton2.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
            menu.requestFocusInWindow();
        });


        //This makes everything visible
        frame.add(mainPanel);
        frame.pack();
        menu.requestFocusInWindow();
        frame.setVisible(true);
    }
}