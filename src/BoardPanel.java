import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * <code>BoardPanel</code> class. The chessboard is stored and displayed inside this.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.6
 * @since 4 APR 2020
 */
class BoardPanel extends JPanel {
	// CONSTANTS
	final Color LIGHT_SQUARE_COLOR = new Color(Color.HSBtoRGB(0, 0, 0.9f));
	final Color DARK_SQUARE_COLOR  = new Color(Color.HSBtoRGB(0, 0, 0.5f));
	final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
	);
	
	// IMAGES
	BufferedImage[] pieces;
	BufferedImage[] numbers;
	BufferedImage[] letters;
	
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
	 * @param graphics the Graphics object used to draw
	 */
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHints(RENDERING_HINTS);
		
		// Board squares
		for(int rank = 1; rank < 9; rank++) {
			for(int file = 1; file < 9; file++) {
				// Square background
				if((file + rank) % 2 == 0)
					graphics2d.setColor(LIGHT_SQUARE_COLOR);
				else
					graphics2d.setColor(DARK_SQUARE_COLOR);
				
				graphics2d.fillRect(outsideGrid / 2 + squareSize * file, outsideGrid / 2 + squareSize * rank,
						squareSize, squareSize);
				
				// Pieces
				// TODO: draw pieces
			}
		}
		// Number squares
		for(int i = 8; i >= 1; i--) {
			int j = i - 1;
			// TODO: draw numbers.
			//  Params are (outsideGrid / 2, outsideGrid / 2 + (8 - i) * squareSize, squareSize, squareSize, this)
			//  and (ousideGrid / 2 + 8 * squareSize, outsideGrid / 2 + (8 - i) * squareSize, squareSize, this)
		}
		// Letter squares
		for(int i = 'a'; i <= 'h'; i++) {
			int j = i - 'a';
			// TODO: draw letters.
			//  Params are (outsideGrid / 2 + j * squareSize, outsideGrid / 2, squareSize, squareSize, this)
			//  and (outsideGrid / 2 + j * squareSize, outsideGrid / 2 + 8 * squareSize, squareSize, this)
		}
	}
}
