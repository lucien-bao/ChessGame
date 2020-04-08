import javax.swing.*;
import java.awt.*;

/**
 * <code>BoardPanel</code> class. The chessboard is stored and displayed inside this.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.5
 * @since 4 APR 2020
 */
class BoardPanel extends JPanel {
	// FIELDS
	Piece[][] grid;
	int length; // JPanel dimensions. Represents both because it's a square.
	int squareSize;
	int outsideGrid; // Amount of excess length outside the drawn grid (due to int truncation).
	
	// CONSTRUCTORS
	BoardPanel() {
		grid = new Piece[10][10];
	}
	
	// METHODS
	
	/**
	 * Resizes the panel to the specified length
	 *
	 * @param length the new length to use
	 */
	void changeLength(int length) {
		this.length = length;
		this.squareSize = length / 10;
		this.outsideGrid = length - 10 * squareSize;
	}
	
	/**
	 * Moves a piece from the starting square to the ending square
	 *
	 * @param startFile the original file of the piece
	 * @param startRank the original rank of the piece
	 * @param endFile   the new file of the piece
	 * @param endRank   the new rank of the piece
	 */
	void movePiece(int startFile, int startRank, int endFile, int endRank) {
		grid[endRank][endFile] = grid[startRank][startFile];
		grid[startRank][startFile] = null;
	}
	
	/**
	 * Automatically called by repaint(), draws everything
	 *
	 * @param g the Graphics object used to draw
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int rank = 0; rank < 10; rank++) {
			for(int file = 0; file < 10; file++) {
				// Letter squares
				if(rank == 0 || rank == 9) {
					// TODO: draw letter squares
				}
				// Number squares
				else if(file == 0 || file == 9) {
					// TODO: draw number squares
				}
				// Board squares
				else {
					if((file + rank) % 2 == 0)
						g.setColor(Color.WHITE);
					else
						g.setColor(Color.BLACK);
					g.fillRect(outsideGrid / 2 + squareSize * file, outsideGrid / 2 + squareSize * rank,
							squareSize, squareSize);
				}
			}
		}
	}
}
