/**
 * <code>MoveRules</code> class. This is not instantiated, only providing methods to determine if moves are valid.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.2
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
	/**
	 * Gets possible moves of a specific piece
	 * Calls a specific helper method based on the piece type
	 * @param board the current pieces on the board
	 * @param rank the rank of the piece to move
	 * @param file the file of the piece to move
	 * @return the possible moves of the piece
	 */
	boolean[][] getPossibleMoves(Piece[][] board, int rank, int file) {
		Piece toMove = board[rank][file];
		boolean[][] possibleMoves = new boolean[8][8];
		int pieceType = toMove.type;
		if(pieceType == Piece.BLACK_PAWN || pieceType == Piece.WHITE_PAWN)
			possibleMoves = getPawnMoves(board, rank, file);
		if(pieceType == Piece.BLACK_KNIGHT || pieceType == Piece.WHITE_KNIGHT)
			possibleMoves = getKnightMoves(board, rank, file);
		// TODO: Call helper methods (which will be written) to determine possible moves.
		return possibleMoves;
	}

	/**
	 * Finds possible moves of a pawn
	 * @param board the current pieces on the board
	 * @param rank the rank of the pawn
	 * @param file the file of the pawn
	 * @return the possible moves of the pawn
	 */
	boolean[][] getPawnMoves(Piece[][] board, int rank, int file) {
		boolean[][] possibleMoves = new boolean[8][8]; // default value is false
		boolean isWhite = board[rank][file].isWhite;
		if(isWhite) { // separation of black and white possible moves is only needed for pawns
			if(board[rank + 1][file] == null) { // move one square forward
				possibleMoves[rank + 1][file] = true;
				if(rank == 2 && board[rank + 2][file] == null) // move two squares forward
					possibleMoves[rank + 2][file] = true;
			}
			if(file != 1 && board[rank + 1][file - 1] != null && !board[rank + 1][file - 1].isWhite) // capture up and left
				possibleMoves[rank + 1][file - 1] = true;
			if(file != 8 && board[rank + 1][file + 1] != null && !board[rank + 1][file + 1].isWhite) // capture up and right
				possibleMoves[rank + 1][file + 1] = true;
		}
		else { // black pawn
			if(board[rank - 1][file] == null) { // move one square forward
				possibleMoves[rank - 1][file] = true;
				if(rank == 7 && board[rank - 2][file] == null) // move two squares forward
					possibleMoves[rank - 2][file] = true;
			}
			if(file != 1 && board[rank - 1][file - 1] != null && board[rank - 1][file - 1].isWhite) // capture down and left
				possibleMoves[rank - 1][file - 1] = true;
			if(file != 8 && board[rank - 1][file + 1] != null && board[rank - 1][file + 1].isWhite) // capture up and right
				possibleMoves[rank - 1][file + 1] = true;
		}
		return possibleMoves;
	}

	/**
	 * Finds possible moves of a knight
	 * @param board the current pieces on the board
	 * @param rank the rank of the knight
	 * @param file the file of the knight
	 * @return the possible moves of the knight
	 */
	boolean[][] getKnightMoves(Piece[][] board, int rank, int file) {
		boolean[][] possibleMoves = new boolean[8][8];
		boolean knightColor = board[rank][file].isWhite;
		if(rank <= 6 && file <= 7
				&& board[rank + 2][file + 1] == null
				|| board[rank + 2][file + 1].isWhite != knightColor) {
			possibleMoves[rank + 2][file + 1] = true;
		}
		if(rank <= 7 && file <= 6
				&& board[rank + 1][file + 2] == null
				|| board[rank + 1][file + 2].isWhite != knightColor) {
			possibleMoves[rank + 1][file + 2] = true;
		}
		if(rank >= 2 && file <= 6
				&& board[rank - 1][file + 2] == null
				|| board[rank - 1][file + 2].isWhite != knightColor) {
			possibleMoves[rank - 1][file + 2] = true;
		}
		if(rank >= 3 && file <= 7
				&& board[rank - 2][file + 1] == null
				|| board[rank - 2][file + 1].isWhite != knightColor) {
			possibleMoves[rank - 2][file + 1] = true;
		}
		if(rank >= 3 && file >= 2
				&& board[rank - 2][file - 1] == null
				|| board[rank - 2][file - 1].isWhite != knightColor) {
			possibleMoves[rank - 2][file - 1] = true;
		}
		if(rank >= 2 && file >= 3
				&& board[rank - 1][file - 2] == null
				|| board[rank - 1][file - 2].isWhite != knightColor) {
			possibleMoves[rank - 1][file - 2] = true;
		}
		if(rank <= 7 && file >= 3
				&& board[rank + 1][file - 2] == null
				|| board[rank + 1][file - 2].isWhite != knightColor) {
			possibleMoves[rank + 1][file - 2] = true;
		}
		if(rank <= 6 && file >= 2
				&& board[rank + 2][file - 1] == null
				|| board[rank + 2][file - 1].isWhite != knightColor) {
			possibleMoves[rank + 2][file - 1] = true;
		}

		return possibleMoves;
	}
}
