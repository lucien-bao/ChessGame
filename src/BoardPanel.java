import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <code>BoardPanel</code> class. The chessboard is stored and displayed inside this.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.11
 * @since 4 APR 2020
 */
class BoardPanel extends JPanel implements MouseListener {
	// CONSTANTS
	// How to get RGBA from int:
	// take the last 24 bits, then concatenate the alpha value at the front,
	// which is [some color]_ALPHA expressed in binary form out of 255
	final int LIGHT_SQUARE_OPAQUE   = Color.HSBtoRGB(0, 0, 0.9f);
	final int DARK_SQUARE_OPAQUE    = Color.HSBtoRGB(0, 0, 0.55f);
//	final int BACKGROUND_OPAQUE     = Color.HSBtoRGB(0, 0, 0.3f);
	// Transparency value of the board, so that you can see the background through it
	float BOARD_ALPHA         = 0.5f;
	final int LIGHT_SQUARE_RGBA     = (LIGHT_SQUARE_OPAQUE & 16777215) | ((int) (BOARD_ALPHA*255) << 24);
	final int DARK_SQUARE_RGBA      = (DARK_SQUARE_OPAQUE & 16777215) | ((int) (BOARD_ALPHA*255) << 24);
//	final int BACKGROUND_RGBA       = (BACKGROUND_OPAQUE & 16777215) | ((int) (BOARD_ALPHA*255) << 24);
	final Color LIGHT_SQUARE_COLOR 	= new Color(LIGHT_SQUARE_RGBA, true);
	final Color DARK_SQUARE_COLOR 	= new Color(DARK_SQUARE_RGBA, true);
//	final Color BACKGROUND_COLOR 	= new Color(BACKGROUND_RGBA, true);
	final Color BACKGROUND_COLOR    = DARK_SQUARE_COLOR;
	
	final int POSS_MOVE_OPAQUE      = Color.HSBtoRGB(0, 0, 0.6f);
	final float POSS_MOVE_ALPHA     = 0.9f;
	final int POSS_MOVE_RGBA        = (POSS_MOVE_OPAQUE & 16777215) | ((int) (POSS_MOVE_ALPHA*255) << 24);
	final Color POSS_MOVE_COLOR     = new Color(POSS_MOVE_RGBA, true);

	final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
	);
	
	// FIELDS
	Piece[][] grid;
	int length; // JPanel dimensions. Represents both because it's a square.
	int squareSize;
	int outsideGrid; // Amount of excess length outside the drawn grid (due to int truncation).
	
	// CONSTRUCTORS
	BoardPanel() {
		// Forces Java to recognize drawing of panels beneath this one (i.e. BackgroundPanel)
		this.setOpaque(false);
		addMouseListener(this);
		grid = new Piece[10][10];
		// makes all pieces empty squares to start
        for (int rank = 1; rank < 9; rank++) {
            for (int file = 1; file < 9; file++) {
                grid[rank][file] = new Piece(Piece.EMPTY);
            }
        }
        // TEST OF getPossibleMoves METHOD
        grid[7][7] = new Piece(Piece.WHITE_PAWN);
        grid[2][3] = new Piece(Piece.BLACK_KNIGHT);
        grid[5][4] = new Piece(Piece.WHITE_KNIGHT);
        grid[2][7] = new Piece(Piece.BLACK_PAWN);
        grid[4][5] = new Piece(Piece.BLACK_BISHOP);
        grid[3][6] = new Piece(Piece.WHITE_BISHOP);
        grid[4][4] = new Piece(Piece.WHITE_ROOK);
        grid[7][6] = new Piece(Piece.BLACK_QUEEN);
        grid[5][5] = new Piece(Piece.BLACK_KING);
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

		// BACKGROUND
		// Draw the background, because setOpaque(false) stops Java from drawing it automatically
		graphics2d.setColor(BACKGROUND_COLOR);
		graphics2d.fillRect(0, 0, length, length);
		for(int rank = 0; rank < 10; rank++) {
			for(int file = 0; file < 10; file++) {
				// Square background
				if((file + rank) % 2 == 0)
					graphics2d.setColor(LIGHT_SQUARE_COLOR);
				else
					graphics2d.setColor(DARK_SQUARE_COLOR);

				graphics2d.fillRect(
						outsideGrid / 2 + squareSize * file,
						outsideGrid / 2 + squareSize * rank,
						squareSize, squareSize
				);
			}
		}
		
		// NUMBER/LETTER SQUARES
        for(int rank = 0; rank < 10; rank++) {
            for(int file = 0; file < 10; file++) {
            	// NUMBER SQUARES
                if(rank == 0 || rank == 9) {
                    if(file == 0 || file == 9)
                        continue;
                    int y = rank * squareSize + outsideGrid / 2;
                    int x = file * squareSize + outsideGrid / 2;
                    graphics2d.setColor(Color.CYAN);
                    graphics2d.fillOval(x, y, squareSize, squareSize);
                }
                // LETTER SQUARES
                else if(file == 0 || file == 9) {
                    int y = rank * squareSize + outsideGrid / 2;
                    int x = file * squareSize + outsideGrid / 2;
                    graphics2d.setColor(Color.MAGENTA);
                    graphics2d.fillOval(x, y, squareSize, squareSize);
                }
            }
        }
        // PIECES
        for(int rank = 1; rank <= 8; rank++) {
        	for(int file = 1; file <= 8; file++) {
		        if(grid[rank][file].type != Piece.EMPTY)
			        graphics2d.drawImage(
			                Piece.images[grid[rank][file].type],
			        		outsideGrid / 2 + squareSize * file,
					        outsideGrid / 2 + squareSize * rank,
					        squareSize, squareSize,
                            this
			        );
	        }
        }
		// POSS. MOVES
		for(int rank = 1; rank <= 8; rank++) {
			for(int file = 1; file <= 8; file++) {
				if(grid[rank][file].showPossibleMoves) {
					boolean[][] possibleMoves = MoveRules.getPossibleMoves(grid, rank, file);
					graphics2d.setColor(POSS_MOVE_COLOR);
					for(int moveRank = 1; moveRank <= 8; moveRank++)
						for(int moveFile = 1; moveFile <= 8; moveFile++)
							if(possibleMoves[moveRank][moveFile])
								graphics2d.fillOval(
										outsideGrid / 2 + squareSize * moveFile + squareSize / 4,
										outsideGrid / 2 + squareSize * moveRank + squareSize / 4,
										squareSize / 2, squareSize / 2
								);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int pieceRank = (e.getY() - outsideGrid / 2) / squareSize;
		int pieceFile = (e.getX() - outsideGrid / 2) / squareSize;
		// Clicking on a square toggles selection
		grid[pieceRank][pieceFile].showPossibleMoves = !grid[pieceRank][pieceFile].showPossibleMoves;
		// Set all others to false
		for(int rank = 1; rank <= 8; rank++) {
			for(int file = 1; file <= 8; file++) {
				if(pieceRank != rank || pieceFile != file)
					grid[rank][file].showPossibleMoves = false;
			}
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
