import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * <code>Piece</code> class. Types of pieces and their possible moves.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.5
 * @since 4 APR 2020
 */
class Piece {
	// CONSTANTS
	static final int WHITE		   = 1;
	static final int BLACK		   = 2;
	
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
	
	static final int LABEL_ONE     = 13;
	static final int LABEL_TWO     = 14;
	static final int LABEL_THREE   = 15;
	static final int LABEL_FOUR    = 16;
	static final int LABEL_FIVE    = 17;
	static final int LABEL_SIX     = 18;
	static final int LABEL_SEVEN   = 19;
	static final int LABEL_EIGHT   = 20;

	static final int LABEL_A   = 21;
	static final int LABEL_B   = 22;
	static final int LABEL_C   = 23;
	static final int LABEL_D   = 24;
	static final int LABEL_E   = 25;
	static final int LABEL_F   = 26;
	static final int LABEL_G   = 27;
	static final int LABEL_H   = 28;
	
	// IMAGES
	static BufferedImage[] images; // indexes are the same as associated constants
	
	// FIELDS
	int teamColor;
	int type;
	boolean showPossibleMoves; // when clicked, a piece's possible moves will be highlighted
	
	// CONSTRUCTORS
	Piece(int type) {
		this.type = type;
		this.showPossibleMoves = false;
		if(type == 0)
			this.teamColor = EMPTY;
		else if(type < 7)
			this.teamColor = WHITE;
		else
			this.teamColor = BLACK;
	}
	
	// METHODS
	static void loadImages(Frame frame) {
		try {
			for(int i = 1; i <= 12; i++) {
				File f = new File("../img/" + i + ".png");
				System.out.println(f);
				images[i] = ImageIO.read(f);
			}
		} catch(IOException e) {
			System.out.println("Something went wrong.");
			e.printStackTrace();
		}
	}
}
