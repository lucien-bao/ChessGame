/**
 * <code>MoveRules</code> class. This is not instantiated, only providing methods to determine if moves are valid.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1
 * @since 9 APR 2020
 */
abstract class MoveRules {
	// TODO: implement move rules for:
	/*
	- Pawn (and capture)
	- Knight
	- Bishop
	- Rook
	- Queen
	- King
	- Castle
	- Check/Checkmate
	- En Passant
	- Promotion
	 */
	boolean[][] getPossibleMoves(Piece[][] board, int rank, int file) {
		Piece toMove = board[rank][file];
		int pieceType = toMove.type;
		// TODO: Call helper methods (which will be written) to determine possible moves.
		return new boolean[][] {};
	}
}
