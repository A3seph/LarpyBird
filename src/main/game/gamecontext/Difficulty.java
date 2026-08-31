package main.game.gamecontext;

/*
Difficulty: this class contains the difficulty of each level ranging from level 1 to 5.
The score needed to get the level to be changed is 50. However, having that the game mode
will change after a 25 score. Making it challenging for players. Each level has its own
difficulty ranging from each to "hacker" difficulty.
 */

public class Difficulty {

    private Difficulty() {}

    public static final int Max_Level = 5;
    public static final int Score_Per_Level = 10;
    public static final int Mode_Switch_Interval = 5;

    //Bird mode difficulties
    public static final int[] Bird_Speed = {-3, -4, -5, -6, -6};
    public static final int[] Pipes_Gap_Divisor = {4, 4, 5, 6, 7}; //The bigger the number the tighter the Gaps will be
    public static final int[] Pipes_Spawn_Interval_Ms = {1500, 1300, 1200, 1100, 1100};
    public static final int[] Pipes_Spawn_Offset = {0, 0, 20, 40, 75};

    //Spider mode difficulties
    public static final int[] Spider_Speed = {-6, -7, -8, -9, -10};
    public static final int[] Spike_Spawn_Interval_Ms = {900, 750, 650, 550, 450};
    public static final int[] Pipe_Chances = {0, 10, 20, 30, 40}; //0 at level 1 so spike can only run

    public static int levelForScore (double score) {
        int computed = 1 + ((int) score) / Score_Per_Level;
        return Math.min(Max_Level, computed);
    }
}
