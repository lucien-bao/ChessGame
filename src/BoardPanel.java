import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * <code>BoardPanel</code> class. The chessboard is stored and displayed inside this.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.26
 * @since 4 APR 2020
 */
class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {
	// CONSTANTS //
	
	/*
	How to get RGBA from int:
	take the last 24 bits, then concatenate the alpha value at the front,
	which is [some color]_ALPHA expressed in binary form out of 255
	 */
	final int LIGHT_SQUARE_OPAQUE   = Color.HSBtoRGB(0, 0, 0.8f);
	final int DARK_SQUARE_OPAQUE    = Color.HSBtoRGB(0, 0, 0.4f);
	// Transparency value of the board, so that you can see the background through it
	final float LIGHT_ALPHA         = 0.5f;
	final float DARK_ALPHA          = 0.5f;
	final float BOARD_ALPHA         = 0.5f;
	
	final int LIGHT_SQUARE_RGBA     = (LIGHT_SQUARE_OPAQUE & 16777215) | ((int) (LIGHT_ALPHA*255) << 24);
	final int DARK_SQUARE_RGBA      = (DARK_SQUARE_OPAQUE & 16777215) | ((int) (DARK_ALPHA*255) << 24);
	final int BOARD_RGBA            = (DARK_SQUARE_OPAQUE & 16777215) | ((int) (BOARD_ALPHA*255) << 24);
	final Color LIGHT_SQUARE_COLOR 	= new Color(LIGHT_SQUARE_RGBA, true);
	final Color DARK_SQUARE_COLOR 	= new Color(DARK_SQUARE_RGBA, true);
	final Color BACKGROUND_COLOR    = new Color(BOARD_RGBA, true);
	
	final int POSS_MOVE_OPAQUE      = Color.HSBtoRGB(0, 0, 0.9f);
	final float POSS_MOVE_ALPHA     = 0.7f;
	final int POSS_MOVE_RGBA        = (POSS_MOVE_OPAQUE & 16777215) | ((int) (POSS_MOVE_ALPHA*255) << 24);
	final Color POSS_MOVE_COLOR     = new Color(POSS_MOVE_RGBA, true);

	final RenderingHints RENDERING_HINTS;
	
	final String[] PROMOTION_OPTIONS = {"Bishop", "Knight", "Rook", "Queen"};
	
	// FIELDS //
	Piece[][] grid;
	// JPanel dimensions. Represents both because it's a square.
	int length;
	int squareSize;
	// Amount of excess length outside the drawn grid (due to int truncation and division).
	int outsideGrid;
	int selectedRank;
	int selectedFile;
	int gameStatus; // 0 -> playing, 1 -> white, 2 -> black, -1 -> stalemate
	boolean whiteToMove;
	HashSet<BoardStateListener> listenerSet;
	
	// This is the chess set (i.e., what the pieces look like) to use.
	int set;

	ArrayDeque<State> doneMoveStack;
	ArrayDeque<State> undoneMoveStack;
	
	int mouseX;
	int mouseY;
	boolean mousePressed;
	
	// CONSTRUCTORS //
	
	/**
	 * Default constructor.
	 */
	BoardPanel() {
		// Forces Java to recognize drawing of panels beneath this one (i.e. BackgroundPanel)
		this.setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		RENDERING_HINTS = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		RENDERING_HINTS.put(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		set = 0;
		
		grid = new Piece[10][10];
		
		addLabels();
		setBoard();

        selectedRank = 0;
        selectedFile = 0;
        
        mouseX = 0;
        mouseY = 0;
        mousePressed = false;
        
        whiteToMove = true;
        this.gameStatus = 0;
        
        doneMoveStack = new ArrayDeque<>();
        undoneMoveStack = new ArrayDeque<>();

        listenerSet = new HashSet<>();
	}
	
	// METHODS //

	/**
	 * Adds a new <code>BoardStateListener</code> to the internal <code>HashSet</code>.
	 * @param bSL The <code>BoardStateListener</code> to add.
	 */
	void addListener(BoardStateListener bSL) {
		listenerSet.add(bSL);
	}

	/**
	 * Resizes the panel to the specified length
	 *
	 * @param length The new length to use.
	 */
	void changeLength(int length) {
		this.length = length;
		this.squareSize = length / 10;
		this.outsideGrid = length - 10 * squareSize;
	}
	
	/**
	 * Moves a piece from the starting square to the ending square.
	 *
	 * @param startFile The original file of the piece.
	 * @param startRank The original rank of the piece.
	 * @param endFile The new file of the piece.
	 * @param endRank The new rank of the piece.
	 */
	void doMove(int startRank, int startFile, int endRank, int endFile, int moveType) {
		Piece[][] gridCopy = new Piece[10][10];
		for(int rank = 0; rank < 10; rank++) {
			for(int file = 0; file < 10; file++) {
				gridCopy[rank][file] = new Piece(grid[rank][file]);
			}
		}
		doneMoveStack.push(new State(gridCopy));
		undoneMoveStack.clear();
		grid[endRank][endFile] = grid[startRank][startFile];
		grid[startRank][startFile] = new Piece(Piece.EMPTY);
		if(moveType == MoveRules.EN_PASSANT) { // EN PASSANT
			if(grid[endRank][endFile].getTeamColor() == Piece.WHITE) {   // WHITE EN PASSANT
				grid[endRank+1][endFile] = new Piece(Piece.EMPTY);
			} else {                                                // BLACK EN PASSANT
				grid[endRank-1][endFile] = new Piece(Piece.EMPTY);
			}
		} else if(moveType == MoveRules.CASTLE) { // CASTLING
			if(endFile > startFile) { // KINGSIDE
				grid[endRank][6] = grid[endRank][8];
				grid[endRank][8] = new Piece(Piece.EMPTY);
			} else { // QUEENSIDE
				grid[endRank][4] = grid[endRank][1];
				grid[endRank][1] = new Piece(Piece.EMPTY);
			}
		} else if(moveType == MoveRules.PROMOTION) { // PROMOTION
			int piece = JOptionPane.showOptionDialog(
				this,                               // Parent component
				"Select a piece to promote to",     // Message
				"Promotion",                        // Title
				JOptionPane.YES_NO_CANCEL_OPTION,	// Option type
				JOptionPane.QUESTION_MESSAGE,       // Message type
				null,                               // Icon,
				PROMOTION_OPTIONS,                  // Options
				PROMOTION_OPTIONS[3]                // Default option
			);
			switch(piece) {
				case 0: {   // BISHOP
					if(whiteToMove)
						grid[endRank][endFile] = new Piece(Piece.WHITE_BISHOP);
					else 
						grid[endRank][endFile] = new Piece(Piece.BLACK_BISHOP);
					break;
				} case 1: { // KNIGHT
					if(whiteToMove)
						grid[endRank][endFile] = new Piece(Piece.WHITE_KNIGHT);
					else
						grid[endRank][endFile] = new Piece(Piece.BLACK_KNIGHT);
					break;
				} case 2: { // ROOK
					if(whiteToMove)
						grid[endRank][endFile] = new Piece(Piece.WHITE_ROOK);
					else
						grid[endRank][endFile] = new Piece(Piece.BLACK_ROOK);
					break;
				} case 3: { // QUEEN
					if(whiteToMove)
						grid[endRank][endFile] = new Piece(Piece.WHITE_QUEEN);
					else
						grid[endRank][endFile] = new Piece(Piece.BLACK_QUEEN);
					break;
				}
			}
		}
		grid[endRank][endFile].setHasMoved();
		whiteToMove = !whiteToMove;
		boolean isCheckmate = MoveRules.isCheckmate(grid, doneMoveStack, whiteToMove);
		if(whiteToMove && isCheckmate) // white has been checkmated
			gameStatus = Piece.BLACK;
		else if(isCheckmate)
			gameStatus = Piece.WHITE;

		boolean isStalemate = MoveRules.isStalemate(grid, doneMoveStack, whiteToMove);
		if(isStalemate) // white has been stalemated
			gameStatus = MoveRules.STALEMATE;
	}
	
	/**
	 * Undoes a move.
	 */
	void undoMove() {
		State lastBoardState = doneMoveStack.pop();
		undoneMoveStack.push(new State(grid));
		grid = lastBoardState.getBoard();
		whiteToMove = !whiteToMove;
	}
	
	/**
	 * Redoes a move.
	 */
	void redoMove() {
		State lastBoardState = undoneMoveStack.pop();
		doneMoveStack.push(new State(grid));
		grid = lastBoardState.getBoard();
		whiteToMove = !whiteToMove;

	}
	
	/**
	 * Automatically called by <code>repaint()</code>, draws everything.
	 *
	 * @param graphics The <code>Graphics</code> object instance used to draw.
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
				// Outside board
				if(file == 0 || file == 9 || rank == 0 || rank == 9) {
					graphics2d.setColor(LIGHT_SQUARE_COLOR);
					graphics2d.fillRect(
							outsideGrid / 2 + squareSize * file,
							outsideGrid / 2 + squareSize * rank,
							squareSize, squareSize
					);
					continue;
				}
				
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
            	// LETTER SQUARES
                if(rank == 0 || rank == 9) {
                    if(file == 0 || file == 9)
                        continue;
                    int y = rank * squareSize + outsideGrid / 2;
                    int x = file * squareSize + outsideGrid / 2;
                    graphics2d.drawImage(Piece.labels[file-1+Piece.LABEL_A], x, y, squareSize, squareSize, this);
                }
                // NUMBER SQUARES
                else if(file == 0 || file == 9) {
                    int y = rank * squareSize + outsideGrid / 2;
                    int x = file * squareSize + outsideGrid / 2;
                    // Has to be inverted (8-rank) because the computer y-coords are opposite chessboard ones
	                graphics2d.drawImage(Piece.labels[8-rank+Piece.LABEL_ONE], x, y, squareSize, squareSize, this);
                }
            }
        }
        // PIECES
        for(int rank = 1; rank <= 8; rank++) {
        	for(int file = 1; file <= 8; file++) {
		        if(grid[rank][file].getType() != Piece.EMPTY)
		        	if(mousePressed && selectedRank == rank & selectedFile == file) // Ghost piece
				        graphics2d.drawImage(
				        		Piece.pieces[0][grid[rank][file].getType()],
						        mouseX-squareSize/2,
						        mouseY-squareSize/2,
						        squareSize, squareSize,
						        this
				        );
		        	else                                                            // Normal
				        graphics2d.drawImage(
						        Piece.pieces[0][grid[rank][file].getType()],
						        outsideGrid / 2 + squareSize * file,
						        outsideGrid / 2 + squareSize * rank,
						        squareSize, squareSize,
						        this
				        );
	        }
        }
        
		// POSS. MOVES
		int[][] possibleMoves = MoveRules.getPossMoves(grid, selectedRank, selectedFile, doneMoveStack, whiteToMove);
		graphics2d.setColor(POSS_MOVE_COLOR);
		graphics2d.setStroke(new BasicStroke(2));
		for(int moveRank = 1; moveRank <= 8; moveRank++)
			for(int moveFile = 1; moveFile <= 8; moveFile++)
				if(possibleMoves[moveRank][moveFile] != MoveRules.NONE) {
					int cx = (int)(outsideGrid / 2. + squareSize * (moveFile + 0.5));
					int cy = (int)(outsideGrid / 2. + squareSize * (moveRank + 0.5));
					int d1 = squareSize / 2;
					graphics2d.fillOval(cx - d1 / 2, cy - d1 / 2, d1, d1);
					int d2 = squareSize * 5 / 8;
					graphics2d.drawOval(cx - d2 / 2, cy - d2 / 2, d2, d2);
				}
		if(gameStatus != MoveRules.PLAYING)
			notifyListeners();
	}
	
	/**
	 * Notifyes any associated <code>BoardStateListeners</code> of check-/stalemate.
	 */
	void notifyListeners() {
		for(BoardStateListener bSL : listenerSet) {
			bSL.gameIsOver();
		}
	}
	
	/**
	 * Adds letter/number squares and sets all other pieces to <code>Piece.EMPTY</code>.
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
	 * Sets the board to the starting position.
	 */
	void setBoard() {
        doneMoveStack = new ArrayDeque<>();
        undoneMoveStack = new ArrayDeque<>();
		whiteToMove = true;

		for(int rank = 1; rank <= 8; rank++) {
			for(int file = 1; file <= 8; file++) {
				grid[rank][file] = new Piece(Piece.EMPTY);
			}
		}
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
	
	/**
	 * Not used.
	 * @param e -
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	/**
	 * Handles events when the mouse is pressed down.
	 * @param e The <code>MouseEvent</code> given.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		int mouseRank = (e.getY() - outsideGrid / 2) / squareSize;
		int mouseFile = (e.getX() - outsideGrid / 2) / squareSize;

		if(mouseRank >= 1 && mouseRank <= 8 && mouseFile >= 1 && mouseFile <= 8) {
			if(selectedRank == 0) { // NOTHING IS SELECTED
				if((whiteToMove && grid[mouseRank][mouseFile].getTeamColor() == Piece.WHITE) ||
						(!whiteToMove && grid[mouseRank][mouseFile].getTeamColor() == Piece.BLACK)) {
					selectedRank = mouseRank;
					selectedFile = mouseFile;
				}
			} else { // FRIENDLY PIECE SELECTED
				// FRIENDLY PIECE CLICKED
				if((whiteToMove && grid[mouseRank][mouseFile].getTeamColor() == Piece.WHITE) ||
						(!whiteToMove && grid[mouseRank][mouseFile].getTeamColor() == Piece.BLACK)) {
					selectedRank = mouseRank;
					selectedFile = mouseFile;
				} else { // EMPTY / ENEMY PIECE CLICKED
					int[][] possMoves = MoveRules.getPossMoves(grid, selectedRank, selectedFile, doneMoveStack, whiteToMove);
					int moveType = possMoves[mouseRank][mouseFile];
					if(moveType != 0)          // PERFORM VALID MOVE
						doMove(selectedRank, selectedFile, mouseRank, mouseFile, moveType);
					selectedRank = selectedFile = 0;
				}
			}
		}

		repaint();
	}
	
	/**
	 * Handles events when the mouse is released.
	 * @param e The <code>MouseEvent</code> given.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		if(selectedRank == 0)
			return;
		
		int mouseRank = (e.getY() - outsideGrid / 2) / squareSize;
		int mouseFile = (e.getX() - outsideGrid / 2) / squareSize;
		
		if(mouseRank < 0 || mouseRank >= 10 || mouseFile < 0 || mouseFile >= 10) {
			this.repaint();
			return;
		}
		
		int[][] possMoves = MoveRules.getPossMoves(grid, selectedRank, selectedFile, doneMoveStack, whiteToMove);
		int moveType = possMoves[mouseRank][mouseFile];
		if(moveType != 0)          // PERFORM VALID MOVE
			doMove(selectedRank, selectedFile, mouseRank, mouseFile, moveType);
		
		repaint();
	}
	
	/**
	 * Not used.
	 * @param e -
	 */
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	/**
	 * Not used.
	 * @param e -
	 */
	@Override
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Handles mouse movement while pressed down.
	 * @param e The <code>MouseEvent</code> given.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		this.repaint();
	}
	
	/**
	 * Not used.
	 * @param e -
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		this.repaint();
	}
}
