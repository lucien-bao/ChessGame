import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * <code>BoardPanel</code> class. The chessboard is stored and displayed inside this.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.15
 * @since 4 APR 2020
 */
class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {
	// CONSTANTS
	
	// How to get RGBA from int:
	// take the last 24 bits, then concatenate the alpha value at the front,
	// which is [some color]_ALPHA expressed in binary form out of 255
	final int LIGHT_SQUARE_OPAQUE   = Color.HSBtoRGB(0, 0, 0.9f);
	final int DARK_SQUARE_OPAQUE    = Color.HSBtoRGB(0, 0, 0.4f);
	// Transparency value of the board, so that you can see the background through it
	float BOARD_ALPHA               = 0.5f;
	final int LIGHT_SQUARE_RGBA     = (LIGHT_SQUARE_OPAQUE & 16777215) | ((int) (BOARD_ALPHA*255) << 24);
	final int DARK_SQUARE_RGBA      = (DARK_SQUARE_OPAQUE & 16777215) | ((int) (BOARD_ALPHA*255) << 24);
	final Color LIGHT_SQUARE_COLOR 	= new Color(LIGHT_SQUARE_RGBA, true);
	final Color DARK_SQUARE_COLOR 	= new Color(DARK_SQUARE_RGBA, true);
	final Color BACKGROUND_COLOR    = DARK_SQUARE_COLOR;
	
	final int POSS_MOVE_OPAQUE      = Color.HSBtoRGB(0, 0, 0.7f);
	final float POSS_MOVE_ALPHA     = 0.9f;
	final int POSS_MOVE_RGBA        = (POSS_MOVE_OPAQUE & 16777215) | ((int) (POSS_MOVE_ALPHA*255) << 24);
	final Color POSS_MOVE_COLOR     = new Color(POSS_MOVE_RGBA, true);

	final RenderingHints RENDERING_HINTS;
	
	// FIELDS
	Piece[][] grid;
	int length; // JPanel dimensions. Represents both because it's a square.
	int squareSize;
	int outsideGrid; // Amount of excess length outside the drawn grid (due to int truncation).
	int selectedRank;
	int selectedFile;
	boolean whiteToMove;

	ArrayDeque<State> doneMoveStack;
	ArrayDeque<State> undoneMoveStack;
	
	// CONSTRUCTORS
	BoardPanel() {
		// Forces Java to recognize drawing of panels beneath this one (i.e. BackgroundPanel)
		this.setOpaque(false);
		addMouseListener(this);
		
		RENDERING_HINTS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RENDERING_HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		grid = new Piece[10][10];
		
		addLabels();
		setBoard();

        selectedRank = 0;
        selectedFile = 0;
        
        whiteToMove = true;
        
        doneMoveStack = new ArrayDeque<>();
        undoneMoveStack = new ArrayDeque<>();
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
	void doMove(int startRank, int startFile, int endRank, int endFile, int moveType) {
		Piece[][] gridCopy = new Piece[10][10];
		for(int i = 0; i < 10; i++)
			System.arraycopy(grid[i], 0, gridCopy[i], 0, 10);
		doneMoveStack.push(new State(gridCopy));
		undoneMoveStack.clear();
		grid[endRank][endFile] = grid[startRank][startFile];
		grid[startRank][startFile] = new Piece(Piece.EMPTY);
		if(moveType == 2) { // EN PASSANT
			if(grid[endRank][endFile].teamColor == Piece.WHITE) {   // WHITE EN PASSANT
				grid[endRank+1][endFile] = new Piece(Piece.EMPTY);
			} else {                                                // BLACK EN PASSANT
				grid[endRank-1][endFile] = new Piece(Piece.EMPTY);
			}
		} else if(moveType == 3) { // CASTLING
			if(endFile > startFile) { // KINGSIDE
				grid[endRank][6] = grid[endRank][8];
				grid[endRank][8] = new Piece(Piece.EMPTY);
			} else { // QUEENSIDE
				grid[endRank][4] = grid[endRank][1];
				grid[endRank][1] = new Piece(Piece.EMPTY);
			}
		}
		grid[endRank][endFile].hasMoved = true;
		whiteToMove = !whiteToMove;
	}
	
	/**
	 * Undoes a move.
	 */
	void undoMove() {
		State lastBoardState = doneMoveStack.pop();
		grid = lastBoardState.getBoard();
		whiteToMove = !whiteToMove;
		undoneMoveStack.push(lastBoardState);
	}
	
	/**
	 * Redoes a move.
	 */
	void redoMove() {
		State lastBoardState = undoneMoveStack.pop();
		grid = lastBoardState.getBoard();
		whiteToMove = !whiteToMove;
		doneMoveStack.push(lastBoardState);
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
		int[][] possibleMoves = MoveRules.getPossMoves(grid, selectedRank, selectedFile, doneMoveStack);
		graphics2d.setColor(POSS_MOVE_COLOR);
		for(int moveRank = 1; moveRank <= 8; moveRank++)
			for(int moveFile = 1; moveFile <= 8; moveFile++)
				if(possibleMoves[moveRank][moveFile] != 0)
					graphics2d.fillOval(
							outsideGrid / 2 + squareSize * moveFile + squareSize / 4,
							outsideGrid / 2 + squareSize * moveRank + squareSize / 4,
							squareSize / 2, squareSize / 2);
	}
	
	/**
	 * Add letter/number squares, set all other pieces to <code>Piece.EMPTY</code>
	 */
	private void addLabels() {
		for(int rank = 0; rank <= 9; rank++) {
			for(int file = 0; file <= 9; file++) {
				grid[rank][file] = new Piece(Piece.EMPTY);
				if((file == 0 || file == 9) && rank > 0 && rank < 9)
					grid[rank][file] = new Piece(Piece.LABEL_ONE + rank - 1);
				else if((rank == 0 || rank == 9) && file > 0 && file < 9)
					grid[rank][file] = new Piece(Piece.LABEL_A + file - 1);
			}
		}
	}
	
	/**
	 * Sets the board to the default arrangement.
	 */
	void setBoard() {
		whiteToMove = true;

		// PAWNS
		for(int file = 1; file <= 8; file++) {
			grid[2][file] = new Piece(Piece.BLACK_PAWN);
			grid[7][file] = new Piece(Piece.WHITE_PAWN);
		}
		
		// PIECES
		for(int file = 1; file <= 8; file++) {
			if(file == 1 || file == 8) {
				grid[1][file] = new Piece(Piece.BLACK_ROOK);
				grid[8][file] = new Piece(Piece.WHITE_ROOK);
			} else if(file == 2 || file == 7) {
				grid[1][file] = new Piece(Piece.BLACK_KNIGHT);
				grid[8][file] = new Piece(Piece.WHITE_KNIGHT);
			} else if(file == 3 || file == 6) {
				grid[1][file] = new Piece(Piece.BLACK_BISHOP);
				grid[8][file] = new Piece(Piece.WHITE_BISHOP);
			} else if(file == 4) {
				grid[1][file] = new Piece(Piece.BLACK_QUEEN);
				grid[8][file] = new Piece(Piece.WHITE_QUEEN);
			} else {
				grid[1][file] = new Piece(Piece.BLACK_KING);
				grid[8][file] = new Piece(Piece.WHITE_KING);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		/*
		NOTE: default value is (0, 0) --> if selected coords equals it, then ignore that selection
		When a piece is selected:
		  If click on valid move, perform that move
		  (Else) If click on valid friendly piece, select that piece
		  Else (Click is on non-valid square), deselect
		When a piece is NOT selected:
		  If click on valid friendly piece, select that piece
		*/
		
		int clickedRank = (e.getY() - outsideGrid / 2) / squareSize;
		int clickedFile = (e.getX() - outsideGrid / 2) / squareSize;

		if(selectedFile != 0) {     // PIECE IS SELECTED
			int[][] possMoves = MoveRules.getPossMoves(grid, selectedRank, selectedFile, doneMoveStack);
			int moveType = possMoves[clickedRank][clickedFile];
			if(moveType != 0) {          // PERFORM VALID MOVE
				doMove(selectedRank, selectedFile, clickedRank, clickedFile, moveType);
				selectedRank = 0;
				selectedFile = 0;
			} else if(grid[selectedRank][selectedFile].teamColor ==
					grid[clickedRank][clickedFile].teamColor) {     // SWITCH SELECTED PIECE
				selectedRank = clickedRank;
				selectedFile = clickedFile;
			} else {                                                // DESELECT PIECE
				selectedRank = 0;
				selectedFile = 0;
			}
		} else {                    // NOTHING SELECTED
			if(whiteToMove && grid[clickedRank][clickedFile].teamColor == Piece.WHITE) {
				selectedRank = clickedRank;
				selectedFile = clickedFile;
			} else if(!whiteToMove && grid[clickedRank][clickedFile].teamColor == Piece.BLACK) {
				selectedRank = clickedRank;
				selectedFile = clickedFile;
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
	
	// TODO: alternate method of moving pieces
	@Override
	public void mouseDragged(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
}
