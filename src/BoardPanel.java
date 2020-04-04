import javax.swing.*;

/**
 * <code>BoardPanel</code> class. The chessboard is stored and displayed inside this.
 * @author Chris W. Bao, Ben Megan
 * @since 4 APR 2020
 * @version 0.1.3
 */
class BoardPanel extends JPanel {
	// FIELDS
	Piece[][] grid;
	int length;

	// CONSTRUCTORS
	BoardPanel() {
	    grid = new Piece[8][8];
    }
    
	// METHODS
	/**
	 * Resizes the panel to the specified length
	 * @param length the new length to use
	 */
	void changeLength(int length) {
		this.length = length;
	}
    void movePiece(int startFile, int startRank, int endFile, int endRank) {
	    grid[endRank][endFile] = grid[startRank][startFile];
	    grid[startRank][startFile] = null;
    }
}
