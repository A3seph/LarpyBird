package main.game.gamecontext;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

//This interface implements every Game mode
public interface GameMode {

    void onEnter(GameContext ctx);

    void onExit();

    void update(GameContext ctx);

    void draw(Graphics2D g, GameContext ctx);

    void handleKeyPressed(KeyEvent e, GameContext ctx);

    boolean isGameOver();
}