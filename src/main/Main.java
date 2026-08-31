package main;

import main.game.Game;
import main.ui.AboutMenu;
import main.ui.MainMenu;
import main.ui.SettingMenu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

//The main class of larpy bird.
public class Main {
    public static void main(String[] args) throws Exception {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        // Basic window setup
        JFrame frame = new JFrame("Larpy Birb");
        frame.setVisible(false);
        frame.setUndecorated(true);
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
        // An about menu
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

        //When clicked in the main menu or returned in the main menu (For main.ui.SettingMenu)
        setting.backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
            menu.requestFocusInWindow();
        });

        //When clicked in the main menu or returned in the main menu (For main.ui.AboutMenu)
        about.backButton2.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
            menu.requestFocusInWindow();
        });

        //This will run the Larpy Bird when clicked "Play"
        menu.playButton.addActionListener(e -> {
            Game larpyGame = new Game(setting);
                mainPanel.add(larpyGame, "game");
                cardLayout.show(mainPanel, "game");

                larpyGame.setOnReturnToMenu(() -> {
                    cardLayout.show(mainPanel, "menu");
                    mainPanel.remove(larpyGame);
                    menu.requestFocusInWindow();
                });

                SwingUtilities.invokeLater(larpyGame::requestFocus);
        });

        //This makes everything visible & everything work
        frame.add(mainPanel);

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(frame);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        }

        menu.requestFocusInWindow();
    }
}