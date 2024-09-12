package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        boolean empty = false;
        int size = b.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (b.tile(i,j) == null) {
                    empty = true;
                    return empty;
                }
            }
        }
        return empty;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        boolean ismax = false;
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i,j) != null) {         //the most important is to judge whether the tile is null or not.
                    if (b.tile(i, j).value() == MAX_PIECE) {
                        ismax = true;
                        return ismax;
                    }
                }
            }
        }
        return ismax;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        boolean onemove = false;
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i,j) == null) {        //if there is free space,return true
                    onemove = true;
                    break;
                }

                if (i != 0 && i != b.size() - 1 && j !=0 && j != b.size() - 1) {        //judge whether the middle space have chance to change
                    int value = b.tile(i,j).value();
                    int up = b.tile(i,j - 1).value();
                    int down = b.tile(i, j + 1).value();
                    int left = b.tile(i - 1, j).value();
                    int right = b.tile(i + 1, j).value();
                    if (value == up || value == down || value == left || value == right) {
                        onemove = true;
                        break;
                    }
                }

                if (i == 0 && j == 0) {               //the[0][0]
                    int value00 = b.tile(i,j).value();
                    int right00 = b.tile(i + 1,j).value();
                    int down00 = b.tile(i, j + 1).value();
                    if (value00 == right00 || value00 == down00) {
                        onemove = true;
                        break;
                    }
                }

                if (i == b.size() - 1 && j == 0) {     //[0][size - 1]
                    int value0s = b.tile(i,j).value();
                    int left0s = b.tile(i - 1, j).value();
                    int down0s = b.tile(i, j + 1).value();
                    if (value0s == left0s || value0s == down0s) {
                        onemove = true;
                        break;
                    }
                }

                if (i == 0 && j == b.size() - 1) {    //[size - 1][0]
                    int values0 = b.tile(i,j).value();
                    int ups0 = b.tile(i, j - 1).value();
                    int rights0 = b.tile(i + 1, j).value();
                    if (values0 == ups0 || values0 == rights0) {
                        onemove = true;
                        break;
                    }
                }

                if (i == b.size() && i == b.size() - 1) {   //[size - 1][size - 1]
                    int valuess = b.tile(i,j).value();
                    int leftss = b.tile(i - 1, j).value();
                    int upss = b.tile(i ,j - 1).value();
                    if (valuess == leftss || valuess == upss) {
                        onemove = true;
                        break;
                    }
                }

                if (j == 0 && i != 0 && i != b.size() -1) {    //[0][1]~[0][size - 1 - 1]
                    int valueup = b.tile(i ,j).value();
                    int leftup = b.tile(i - 1 ,j).value();
                    int rightup = b.tile(i + 1, j).value();
                    int downup = b.tile(i ,j + 1).value();
                    if (valueup == leftup || valueup == rightup || valueup == downup) {
                        onemove = true;
                        break;
                    }
                }

                if (j == b.size() - 1 && i != 0 && i != b.size() - 1) {     //[size - 1][1] ~ [size - 1][size - 1 -1]
                    int valuedown = b.tile(i,j).value();
                    int leftdown = b.tile(i - 1,j).value();
                    int rightdown = b.tile(i + 1,j).value();
                    int updown = b.tile(i , j - 1).value();
                    if (valuedown == leftdown || valuedown == rightdown || valuedown == updown) {
                        onemove = true;
                        break;
                    }
                }

                if (i == 0 && j != 0 && j != b.size() - 1) {     //[1][0] ~ [size - 2][0]
                    int valueleft = b.tile(i,j).value();
                    int upleft = b.tile(i , j - 1).value();
                    int downleft = b.tile(i , j + 1).value();
                    int rightleft = b.tile(i + 1, j).value();
                    if (valueleft == upleft || valueleft == downleft || valueleft == rightleft) {
                        onemove = true;
                        break;
                    }
                }

                if (i == b.size() - 1 && j != 0 && j != b.size() - 1) {      //[1][size - 1] ~ [size - 2][size - 1]
                    int valueright = b.tile(i,j).value();
                    int upright = b.tile(i, j - 1).value();
                    int downright = b.tile(i , j + 1).value();
                    int leftright = b.tile(i - 1 ,j).value();
                    if (valueright == upright || valueright == downright || valueright == leftright) {
                        onemove = true;
                        break;
                    }
                }
            }
        }
        return onemove;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
