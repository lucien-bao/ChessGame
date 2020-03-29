import javax.swing.*;

/**
 * BoardPanel class. The chessboard is stored and displayed inside this.
 * @author Chris W. Bao, Ben Megan
 * @since 28 MAR 2020
 * @version 0.1.2
 */
class BoardPanel extends JPanel {
	// FIELDS
	Piece[][] grid;

	// CONSTRUCTORS
	BoardPanel() {
	    grid = new Piece[8][8];
    }
    
	// METHODS
    void movePiece(int startFile, int startRank, int endFile, int endRank) {
	    grid[endRank][endFile] = grid[startRank][startFile];
	    grid[startRank][startFile] = null;
    }
}
