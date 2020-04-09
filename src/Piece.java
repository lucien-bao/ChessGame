/**
 * <code>Piece</code> class. Types of pieces and their possible moves.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.4
 * @since 4 APR 2020
 */
class Piece {
	// CONSTANTS
	static final int EMPTY         = 0;
	static final int WHITE_PAWN    = 1;
	static final int WHITE_BISHOP  = 2;
	static final int WHITE_KNIGHT  = 3;
	static final int WHITE_ROOK    = 4;
	static final int WHITE_QUEEN   = 5;
	static final int WHITE_KING    = 6;
	static final int BLACK_PAWN    = 7;
	static final int BLACK_BISHOP  = 8;
	static final int BLACK_KNIGHT  = 9;
	static final int BLACK_ROOK    = 10;
	static final int BLACK_QUEEN   = 11;
	static final int BLACK_KING    = 12;
	
	// FIELDS
	boolean isWhite;
	int type;
	
	// CONSTRUCTORS
	Piece(int type) {
		this.type = type;
		this.isWhite = type < 7;
	}
}
