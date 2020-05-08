import java.util.ArrayDeque;

/**
 * <code>MoveRules</code> class. This is not instantiated, only providing methods to determine if doneMoveStack are valid.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.11
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
	 * Gets possible doneMoveStack of a specific piece
	 * Calls a specific helper method based on the piece type
	 * @param board the current pieces on the board
	 * @param pieceRank the rank of the piece to move
	 * @param pieceFile the file of the piece to move
	 * @return the possible doneMoveStack of the piece
	 */
	static int[][] getPossMoves(Piece[][] board, int pieceRank, int pieceFile, ArrayDeque<State> doneMoveStack) {
		Piece toMove = board[pieceRank][pieceFile];
		int pieceType = toMove.type;
		if(pieceType > 6)
			pieceType -= 6;
		switch(pieceType) {
			case 1:
				return getPawnMoves(board, pieceRank, pieceFile, doneMoveStack);
			case 2:
				return getBishopMoves(board, pieceRank, pieceFile);
			case 3:
				return getKnightMoves(board, pieceRank, pieceFile);
			case 4:
				return getRookMoves(board, pieceRank, pieceFile);
			case 5:
				return getQueenMoves(board, pieceRank, pieceFile);
			case 6:
				return getKingMoves(board, pieceRank, pieceFile);
			default:
				return new int[10][10];
		}
	}

	/**
	 * Finds possible doneMoveStack of a pawn
	 * @param board the current pieces on the board
	 * @param rank the rank of the pawn
	 * @param file the file of the pawn
	 * @return the possible doneMoveStack of the pawn
	 */
	static int[][] getPawnMoves(Piece[][] board, int rank, int file, ArrayDeque<State> doneMoveStack) {
		int[][] possibleMoves = new int[10][10];
		int pawnColor = board[rank][file].teamColor;
		// Separate the diff. color pawns b/c they move in opposite directions
		if(pawnColor == Piece.BLACK) {
			// Forward movement
			if(board[rank + 1][file].teamColor == Piece.EMPTY) {
				// Default 1-square step
				possibleMoves[rank + 1][file] = 1;
				// Starting position 2-step
				if(rank == 2 && board[rank + 2][file].teamColor == Piece.EMPTY)
					possibleMoves[rank + 2][file] = 1;
			}
			// Diag. capture, Southwest
			if(file != 1 && board[rank + 1][file - 1].teamColor != Piece.EMPTY
					&& pawnColor != board[rank + 1][file - 1].teamColor)
				possibleMoves[rank + 1][file - 1] = 1;
			// Diag. capture, Southeast
			if(file != 8 && board[rank + 1][file + 1].teamColor != Piece.EMPTY
					&& pawnColor != board[rank + 1][file + 1].teamColor)
				possibleMoves[rank + 1][file + 1] = 1;

			// En passant
			if(rank == 5) {
				State lastState = doneMoveStack.peekLast();
				if(lastState != null) {
					Piece[][] lastBoard = lastState.getBoard();
					// Capture down and left
					if(file != 1 &&
							board[rank][file-1].type == Piece.WHITE_PAWN &&
							board[rank+2][file-1].type == Piece.EMPTY &&
							board[rank+1][file-1].type == Piece.EMPTY &&
							lastBoard[rank+2][file-1].type == Piece.WHITE_PAWN) {
						possibleMoves[rank+1][file-1] = 2;
					}
					// Capture down and right
					else if(file != 8 &&
							board[rank][file+1].type == Piece.WHITE_PAWN &&
							board[rank+2][file+1].type == Piece.EMPTY &&
							board[rank+1][file+1].type == Piece.EMPTY &&
							lastBoard[rank+2][file+1].type == Piece.WHITE_PAWN) {
						possibleMoves[rank+1][file+1] = 2;
					}
				}
			}
		}
		// pawnColor == Piece.WHITE
		else {
			// Forward movement
			if(board[rank - 1][file].teamColor == Piece.EMPTY) {
				// Default 1-square step
				possibleMoves[rank - 1][file] = 1;
				// Starting position 2-step
				if(rank == 7 && board[rank - 2][file].teamColor == Piece.EMPTY)
					possibleMoves[rank - 2][file] = 1;
			}
			// Diag. capture, Northwest
			if(file != 1 && board[rank - 1][file - 1].teamColor != Piece.EMPTY
					&& pawnColor != board[rank - 1][file - 1].teamColor)
				possibleMoves[rank - 1][file - 1] = 1;
			// Diag. capture, Northeast
			if(file != 8 && board[rank - 1][file + 1].teamColor != Piece.EMPTY
					&& pawnColor != board[rank - 1][file + 1].teamColor)
				possibleMoves[rank - 1][file + 1] = 1;

			// En passant
			if(rank == 4) {
				State lastState = doneMoveStack.peekLast();
				if(lastState != null) {
					Piece[][] lastBoard = lastState.getBoard();
					// Capture up and left
					if(     file != 1 &&
							board[rank][file-1].type == Piece.BLACK_PAWN &&
							board[rank-2][file-1].type == Piece.EMPTY &&
							board[rank-1][file-1].type == Piece.EMPTY &&
							lastBoard[rank-2][file-1].type == Piece.BLACK_PAWN) {
						possibleMoves[rank-1][file-1] = 2;
					}
					// Capture down and right
					else if(file != 8 &&
							board[rank][file+1].type == Piece.BLACK_PAWN &&
							board[rank-2][file+1].type == Piece.EMPTY &&
							board[rank-1][file+1].type == Piece.EMPTY &&
							lastBoard[rank-2][file+1].type == Piece.BLACK_PAWN) {
						possibleMoves[rank-1][file+1] = 2;
					}
				}
			}
		}
		return possibleMoves;
	}

	/**
	 * Finds possible doneMoveStack of a knight
	 * @param board the current pieces on the board
	 * @param rank the rank of the knight
	 * @param file the file of the knight
	 * @return the possible doneMoveStack of the knight
	 */
	static int[][] getKnightMoves(Piece[][] board, int rank, int file) {
		int[][] possibleMoves = new int[10][10];
		int knightColor = board[rank][file].teamColor;
		int[] xOffsets = new int[] {-2, -2, -1, -1, +1, +1, +2, +2};
		int[] yOffsets = new int[] {-1, +1, -2, +2, -2, +2, -1, +1};
		for(int i = 0; i < 8; i++) {
			int newRank = rank + xOffsets[i];
			int newFile = file + yOffsets[i];
			if(newRank >= 1 && newRank <= 8
				&& newFile >= 1 && newFile <= 8
				&& board[newRank][newFile].teamColor != knightColor)
				possibleMoves[newRank][newFile] = 1;
		}

		return possibleMoves;
	}

	/**
	 * Finds possible doneMoveStack of a bishop
	 * @param board the current pieces on the board
	 * @param rank the rank of the bishop
	 * @param file the file of the bishop
	 * @return the possible doneMoveStack of the bishop
	 */
	static int[][] getBishopMoves(Piece[][] board, int rank, int file) {
		int[][] possibleMoves = new int[10][10];

		int rankTo;
		int fileTo;
		int bishopColor = board[rank][file].teamColor;
		int[] xOffsets = new int[] {+1, +1, -1, -1};
		int[] yOffsets = new int[] {+1, -1, +1, -1};
		for(int i = 0; i < 4; i++) {
			rankTo = rank;
			fileTo = file;
			while(true) { // up and right
				rankTo += yOffsets[i];
				fileTo += xOffsets[i];
				if(rankTo >= 1 && rankTo <= 8 && fileTo >= 1 && fileTo <= 8) {
					if(board[rankTo][fileTo].teamColor + bishopColor == 3) { // OPPOSITE COLORS
						possibleMoves[rankTo][fileTo] = 1;
						break;
					} else if(board[rankTo][fileTo].teamColor == Piece.EMPTY) { // EMPTY SQUARE
						possibleMoves[rankTo][fileTo] = 1;
					} else { // SAME COLOR
						break;
					}
				} else {
					break;
				}
			}
		}
		return possibleMoves;
	}

    /**
     * Finds possible doneMoveStack of a rook
     * @param board the current pieces on the board
     * @param rank the rank of the rook
     * @param file the file of the rook
     * @return the possible doneMoveStack of the rook
     */
    static int[][] getRookMoves(Piece[][] board, int rank, int file) {
        int[][] possibleMoves = new int[10][10];

        int rankTo;
        int fileTo;
        int rookColor = board[rank][file].teamColor;
        int[] xOffsets = new int[] {+1, -1, +0, +0};
        int[] yOffsets = new int[] {+0, +0, +1, -1};
        for(int i = 0; i < 4; i++) {
            rankTo = rank;
            fileTo = file;
            while(true) {
                rankTo += yOffsets[i];
                fileTo += xOffsets[i];
                if(rankTo >= 1 && rankTo <= 8 && fileTo >= 1 && fileTo <= 8) {
                    if(board[rankTo][fileTo].teamColor + rookColor == 3) { // OPPOSITE COLORS
                        possibleMoves[rankTo][fileTo] = 1;
                        break;
                    } else if(board[rankTo][fileTo].teamColor == Piece.EMPTY) { // EMPTY SQUARE
                        possibleMoves[rankTo][fileTo] = 1;
                    } else { // SAME COLOR
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return possibleMoves;
    }

    /**
     * Finds possible doneMoveStack of a queen
     * @param board the current pieces on the board
     * @param rank the rank of the queen
     * @param file the file of the queen
     * @return the possible doneMoveStack of the queen
     */
    static int[][] getQueenMoves(Piece[][] board, int rank, int file) {
        int[][] possibleMoves = new int[10][10];
        int[][] bishopMoves   = getBishopMoves(board, rank, file);
        int[][] rookMoves     = getRookMoves(board, rank, file);
        for(int rankTo = 1; rankTo <= 8; rankTo++)
            for(int fileTo = 1; fileTo <= 8; fileTo++)
            	possibleMoves[rankTo][fileTo] = bishopMoves[rankTo][fileTo] + rookMoves[rankTo][fileTo];
        return possibleMoves;
    }

    /**
     * Finds possible doneMoveStack of a king
     * @param board the current pieces on the board
     * @param rank the rank of the king
     * @param file the file of the king
     * @return the possible doneMoveStack of the king
     */
    static int[][] getKingMoves(Piece[][] board, int rank, int file) {
        int[][] possibleMoves = new int[10][10];

        int kingColor = board[rank][file].teamColor;
        int[] xOffsets = new int[] {-1, -1, -1, +0, +0, +1, +1, +1};
        int[] yOffsets = new int[] {-1, +0, +1, -1, +1, -1, +0, +1};
        for(int i = 0; i < 8; i++) {
            int rankTo = rank + yOffsets[i];
            int fileTo = file + xOffsets[i];
            if(rankTo >= 1 && rankTo <= 8 && fileTo >= 1 && fileTo <= 8
                && board[rankTo][fileTo].teamColor != kingColor)
                possibleMoves[rankTo][fileTo] = 1;
        }
        return possibleMoves;
    }
}
