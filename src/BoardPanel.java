import javax.swing.*;
import java.awt.*;

/**
 * <code>BoardPanel</code> class. The chessboard is stored and displayed inside this.
 * @author Chris W. Bao, Ben Megan
 * @since 4 APR 2020
 * @version 0.1.4
 */
class BoardPanel extends JPanel {
	// FIELDS
	Piece[][] grid;
	int length;
	int squareSize;

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
		this.squareSize = length / 10; // 10 instead of 8 to leave space on edges
	}

	/**
	 * Moves a piece from a starting to ending square
	 * @param startFile the original file of the piece
	 * @param startRank the original rank of the piece
	 * @param endFile the new file of the piece
	 * @param endRank the new rank of the piece
	 */
    void movePiece(int startFile, int startRank, int endFile, int endRank) {
	    grid[endRank][endFile] = grid[startRank][startFile];
	    grid[startRank][startFile] = null;
    }

	/**
	 * Automatically called by repaint(), draws everything
	 * @param g the Graphics object used to draw
	 */
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);

		for (int y = 1; y < 9; y++) {
			for (int x = 1; x < 9; x++) {
				if ((x + y) % 2 == 0)
					g.setColor(Color.WHITE);
				else
					g.setColor(Color.BLACK);
				g.fillRect(squareSize * x, squareSize * y, squareSize, squareSize);
			}
		}
	}
}
