/**
 * <code>State</code> class. Logs board state at a point in time.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.3
 * @since 23 APR 2020
 */
public class State {
    private final Piece[][] board;
    
    // CONSTRUCTOR
    
    /**
     * Constructor; stores a board state as a 2D array of <code>Piece</code>s.
     */
    State(Piece[][] board) {
        this.board = board;
    }
    
    /**
     * Getter method to return the board.
     * @return the board state stored.
     */
    Piece[][] getBoard() {
        return board;
    }
}
