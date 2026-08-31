package main.datastorage;

/*  Stores the high score in memory for the duration of the application session
    It will resets automatically when application is closed
 */
public class ScoreManager {

    private static int highScore = 0;

    public static int getHighScore() {
        return highScore;
    }

    public static void updateHighScore(int newScore) {
        if (newScore > highScore) {
            highScore = newScore;
        }
    }

}
