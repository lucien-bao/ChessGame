/**
 * <code>Move</code> class. Logs changes in board state.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.1
 * @since 23 APR 2020
 */
public class Move {
    Piece movingPiece;
    Piece targetPiece;
    int startFile;
    int startRank;
    int endFile;
    int endRank;
    /**
     * Normal move (not castle or en passant) constructor
     * @param movingPiece the piece being moved
     * @param targetPiece the piece on the target square
     * @param startFile the starting file of the piece being moved
     * @param startRank the starting rank of the piece being moved
     * @param endFile the ending file of the piece being moved
     * @param endRank the ending rank of the piece being moved
     */
    Move(Piece movingPiece, Piece targetPiece, int startFile, int startRank, int endFile, int endRank) {
        this.movingPiece = movingPiece;
        this.targetPiece = targetPiece;
        this.startFile   = startFile;
        this.startRank   = startRank;
        this.endFile     = endFile;
        this.endRank     = endRank;
    }
}
