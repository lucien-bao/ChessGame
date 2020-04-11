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
		// makes all pieces empty squares to start
        for (int rank = 1; rank < 9; rank++) {
            for (int file = 1; file < 9; file++) {
                grid[rank][file] = new Piece(Piece.EMPTY);
            }
        }
        // TEST OF getPossibleMoves METHOD
        // TODO: remove this
        grid[7][7] = new Piece(Piece.WHITE_PAWN);
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
		grid[startRank][startFile] = new Piece(Piece.EMPTY);
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

		// DRAW PIECES
        for (int rank = 0; rank < 10; rank++) {
            for (int file = 0; file < 10; file++) {
                if(rank == 0 || rank == 9) {
                    if(file == 0 || file == 9)
                        continue;
                    int y = rank * squareSize + outsideGrid / 2;
                    int x = file * squareSize + outsideGrid / 2;
                    graphics2d.setColor(Color.CYAN);
                    graphics2d.fillOval(x, y, squareSize, squareSize);
                }
                else if(file == 0 || file == 9) {
                    int y = rank * squareSize + outsideGrid / 2;
                    int x = file * squareSize + outsideGrid / 2;
                    graphics2d.setColor(Color.MAGENTA);
                    graphics2d.fillOval(x, y, squareSize, squareSize);
                }
                else {
                    boolean[][] possibleMoves = MoveRules.getPossibleMoves(grid, 7, 7);
                    // TODO: remove this, it's just a test
                    if (grid[rank][file].type == Piece.WHITE_PAWN) {
                        graphics2d.setColor(Color.GREEN);
                        graphics2d.fillRect(outsideGrid / 2 + squareSize * file, outsideGrid / 2 + squareSize * rank,
                                squareSize, squareSize);
                    }
                    graphics2d.setColor(Color.RED);
                    if (possibleMoves[rank][file]) {
                        graphics2d.fillRect(outsideGrid / 2 + squareSize * file, outsideGrid / 2 + squareSize * rank,
                                squareSize, squareSize);
                    }
                }
            }
        }
	}
}
