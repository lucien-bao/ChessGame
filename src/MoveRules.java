import java.util.ArrayDeque;

/**
 * <code>MoveRules</code> class. This is not instantiated, only providing methods to determine if doneMoveStack are valid.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.15
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
	 * @param doneMoveStack all completed moves
	 * @param whiteToMove the team to move
	 * @return the possible doneMoveStack of the piece
	 */
	static int[][] getPossMoves(Piece[][] board, int pieceRank, int pieceFile, ArrayDeque<State> doneMoveStack, boolean whiteToMove) {
		Piece toMove = board[pieceRank][pieceFile];
		int pieceType = toMove.type;
		if(pieceType > 6)
			pieceType -= 6;
		switch(pieceType) {
			case 1:
				return getPawnMoves(board, pieceRank, pieceFile, doneMoveStack, whiteToMove);
			case 2:
				return getBishopMoves(board, pieceRank, pieceFile, whiteToMove);
			case 3:
				return getKnightMoves(board, pieceRank, pieceFile, whiteToMove);
			case 4:
				return getRookMoves(board, pieceRank, pieceFile, whiteToMove);
			case 5:
				return getQueenMoves(board, pieceRank, pieceFile, whiteToMove);
			case 6:
				return getKingMoves(board, pieceRank, pieceFile, doneMoveStack, whiteToMove);
			default:
				return new int[10][10];
		}
	}

	/**
	 * Finds possible doneMoveStack of a pawn
	 * @param board the current pieces on the board
	 * @param rank the rank of the pawn
	 * @param file the file of the pawn
	 * @param doneMoveStack all completed moves
	 * @param whiteToMove the team to move
	 * @return the possible doneMoveStack of the pawn

	 */
	static int[][] getPawnMoves(Piece[][] board, int rank, int file, ArrayDeque<State> doneMoveStack, boolean whiteToMove) {
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
			if(rank == 4 && doneMoveStack.size() >= 3) {
				Piece[][] blackLastBoard = doneMoveStack.pop().getBoard();
				// Capture up and left
				if(     file != 1 &&
						board[rank][file-1].type == Piece.BLACK_PAWN &&         // Pawn to capture
						board[rank-2][file-1].type == Piece.EMPTY &&            // Pawn init. square 
						board[rank-1][file-1].type == Piece.EMPTY &&            // Square to move
						blackLastBoard[rank-2][file-1].type == Piece.BLACK_PAWN // Last move by Black
				)
					possibleMoves[rank-1][file-1] = 2;
				// Capture down and right
				else if(file != 8 &&
						board[rank][file+1].type == Piece.BLACK_PAWN &&
						board[rank-2][file+1].type == Piece.EMPTY &&
						board[rank-1][file+1].type == Piece.EMPTY &&
						blackLastBoard[rank-2][file+1].type == Piece.BLACK_PAWN
				)
					possibleMoves[rank-1][file+1] = 2;
				
				doneMoveStack.push(new State(blackLastBoard));
			}
		}
		return possibleMoves;
	}

	/**
	 * Finds possible doneMoveStack of a knight
	 * @param board the current pieces on the board
	 * @param rank the rank of the knight
	 * @param file the file of the knight
	 * @param whiteToMove the team to move
	 * @return the possible doneMoveStack of the knight
	 */
	static int[][] getKnightMoves(Piece[][] board, int rank, int file, boolean whiteToMove) {
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
	 * @param whiteToMove the team to move
	 * @return the possible doneMoveStack of the bishop
	 */
	static int[][] getBishopMoves(Piece[][] board, int rank, int file, boolean whiteToMove) {
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
	 * @param whiteToMove the team to move
     * @return the possible doneMoveStack of the rook
     */
    static int[][] getRookMoves(Piece[][] board, int rank, int file, boolean whiteToMove) {
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
	 * @param whiteToMove the team to move
     * @return the possible doneMoveStack of the queen
     */
    static int[][] getQueenMoves(Piece[][] board, int rank, int file, boolean whiteToMove) {
        int[][] possibleMoves = new int[10][10];
        int[][] bishopMoves   = getBishopMoves(board, rank, file, whiteToMove);
        int[][] rookMoves     = getRookMoves(board, rank, file, whiteToMove);
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
	 * @param doneMoveStack all completed moves
	 * @param whiteToMove the team to move
     * @return the possible doneMoveStack of the king
     */
    static int[][] getKingMoves(Piece[][] board, int rank, int file, ArrayDeque<State> doneMoveStack, boolean whiteToMove) {
        int[][] possibleMoves = new int[10][10];

        int kingColor = board[rank][file].teamColor;
        boolean isKingWhite = (kingColor == Piece.WHITE);
        int[] xOffsets = new int[] {-1, -1, -1, +0, +0, +1, +1, +1};
        int[] yOffsets = new int[] {-1, +0, +1, -1, +1, -1, +0, +1};
        for(int i = 0; i < 8; i++) {
            int rankTo = rank + yOffsets[i];
            int fileTo = file + xOffsets[i];
            if(rankTo >= 1 && rankTo <= 8 && fileTo >= 1 && fileTo <= 8
                && board[rankTo][fileTo].teamColor != kingColor) {
            	if(isKingWhite == whiteToMove) {
            		// move king to square, then check if it's attacked
					// needed to prevent king from moving away from attacking piece
            		Piece oldPiece = board[rankTo][fileTo];
            		board[rankTo][fileTo] = board[rank][file];
            		board[rank][file] = new Piece(Piece.EMPTY);
					if(!isSquareAttacked(board, rankTo, fileTo, whiteToMove, doneMoveStack)) {
						possibleMoves[rankTo][fileTo] = 1;
					}
					board[rank][file] = board[rankTo][fileTo];
					board[rankTo][fileTo] = oldPiece;
				}
            	else
					possibleMoves[rankTo][fileTo] = 1;
			}
        }

        // CASTLING
		if(!board[rank][file].hasMoved) {
			// Kingside
			if(		board[rank][file + 1].type == Piece.EMPTY &&
					board[rank][file + 2].type == Piece.EMPTY &&
					(board[rank][file + 3].type == Piece.WHITE_ROOK ||
							board[rank][file + 3].type == Piece.BLACK_ROOK) &&
					board[rank][file + 3].teamColor == kingColor &&
					!board[rank][file + 3].hasMoved &&
					!isSquareAttacked(board, rank, file, whiteToMove, doneMoveStack) &&
					!isSquareAttacked(board, rank, file + 1, whiteToMove, doneMoveStack) &&
					!isSquareAttacked(board, rank, file + 2, whiteToMove, doneMoveStack)
			) {
				possibleMoves[rank][file + 2] = 3;
			}
			// Queenside
			// Kingside
			if(		board[rank][file - 1].type == Piece.EMPTY &&
					board[rank][file - 2].type == Piece.EMPTY &&
					(board[rank][file - 4].type == Piece.WHITE_ROOK ||
							board[rank][file - 4].type == Piece.BLACK_ROOK) &&
					board[rank][file - 4].teamColor == kingColor &&
					!board[rank][file - 4].hasMoved &&
					!isSquareAttacked(board, rank, file, whiteToMove, doneMoveStack) &&
					!isSquareAttacked(board, rank, file - 1, whiteToMove, doneMoveStack) &&
					!isSquareAttacked(board, rank, file - 2, whiteToMove, doneMoveStack)
			) {
				possibleMoves[rank][file - 2] = 3;
			}
		}

        return possibleMoves;
    }

	/**
	 * Checks whether a square is attacked
	 * @param board the current board state
	 * @param squareRank the rank of the square to check
	 * @param squareFile the file of the square to check
	 * @param whiteToMove which team moves next
	 * @return whether the square is attacked
	 */
    static boolean isSquareAttacked(Piece[][] board, int squareRank, int squareFile, boolean whiteToMove, ArrayDeque<State> doneMoveStack) {
    	for(int rank = 1; rank <= 8; rank++) {
    		for(int file = 1; file <= 8; file++) {
    			if(board[rank][file].teamColor == Piece.EMPTY)
    				continue;
    			boolean pieceColorWhite = (board[rank][file].teamColor == Piece.WHITE);
    			if(pieceColorWhite != whiteToMove) {
					int[][] possibleMoves = getPossMoves(board, rank, file, doneMoveStack, whiteToMove);
					if(possibleMoves[squareRank][squareFile] > 0)
						return true;
				}
			}
		}
    	return false;
	}
}
