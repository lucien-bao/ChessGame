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
	static final int WHITE		   = 1;
	static final int BLACK		   = 2;

	static final int LABEL_ONE     = -1;
	static final int LABEL_TWO     = -2;
	static final int LABEL_THREE   = -3;
	static final int LABEL_FOUR    = -4;
	static final int LABEL_FIVE    = -5;
	static final int LABEL_SIX     = -6;
	static final int LABEL_SEVEN   = -7;
	static final int LABEL_EIGHT   = -8;

	static final int LABEL_A   = -11;
	static final int LABEL_B   = -12;
	static final int LABEL_C   = -13;
	static final int LABEL_D   = -14;
	static final int LABEL_E   = -15;
	static final int LABEL_F   = -16;
	static final int LABEL_G   = -17;
	static final int LABEL_H   = -18;
	
	// FIELDS
	int teamColor;
	int type;
	
	// CONSTRUCTORS
	Piece(int type) {
		this.type = type;
		if(type == 0)
			this.teamColor = EMPTY;
		else if(type < 7)
			this.teamColor = WHITE;
		else
			this.teamColor = BLACK;
	}
}
