/**
 * Piece class. Types of pieces and their possible moves.
 * @author Chris W. Bao, Ben Megan
 * @since 29 MAR 2020
 * @version 0.1.1
 */
class Piece {
    // FIELDS
    boolean isWhite;
    int type;
    
    // CONSTRUCTORS

    // METHODS
    Piece[][] getPossibleMoves(Piece[][] currentBoard) {
        return new Piece[8][8]; // filler return, will change later
    }
}
