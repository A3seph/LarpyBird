package main.game.gamecontext;
import main.ui.SettingMenu;

//This bridges the bird and Spider mode

public class GameContext {
        public int boardWidth;
        public int boardHeight;

        public double score = 0;
        public int highScore = 0;
        public int level = 1;

        public boolean started = false;

        public SettingMenu settings;
    }
