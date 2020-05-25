import java.util.ArrayDeque;

/**
 * <code>MoveRules</code> class. This is not instantiated, only providing methods to determine if doneMoveStack are valid.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.18
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
	 * @param board The current pieces on the board
	 * @param pieceRank The rank of the piece to move
	 * @param pieceFile The file of the piece to move
	 * @param doneMoveStack All completed moves
	 * @param whiteToMove The team to move
	 * @return The possible moves of the piece
	 */
	static int[][] getPossMoves(Piece[][] board, int pieceRank, int pieceFile, ArrayDeque<State> doneMoveStack, boolean whiteToMove) {
		Piece toMove = board[pieceRank][pieceFile];
		int pieceType = toMove.type;
		if(pieceType > 6)
			pieceType -= 6;
		int[][] possibleMoves;
		switch(pieceType) {
			case 1:
				possibleMoves = getPawnMoves(board, pieceRank, pieceFile, doneMoveStack);
				break;
			case 2:
                possibleMoves = getBishopMoves(board, pieceRank, pieceFile);
                break;
			case 3:
                possibleMoves = getKnightMoves(board, pieceRank, pieceFile);
                break;
			case 4:
                possibleMoves = getRookMoves(board, pieceRank, pieceFile);
                break;
			case 5:
                possibleMoves = getQueenMoves(board, pieceRank, pieceFile);
                break;
			case 6:
                possibleMoves = getKingMoves(board, pieceRank, pieceFile, doneMoveStack, whiteToMove);
                break;
			default:
                possibleMoves = new int[10][10];
		}

		// Check if king is attacked after each move
        boolean pieceColorWhite;
        pieceColorWhite = board[pieceRank][pieceFile].teamColor == Piece.WHITE;
		if(whiteToMove == pieceColorWhite) { // only check if it's that side's move
            for(int rank = 1; rank <= 8; rank++) {
                for(int file = 1; file <= 8; file++) {
                    if(possibleMoves[rank][file] > 0) {
                        Piece[][] boardAfterMove = getBoardAfterMove(board, pieceRank, pieceFile, rank, file, possibleMoves[rank][file]);
                        boolean validMove = !isKingCheckedAfterMove(boardAfterMove, whiteToMove, doneMoveStack);
                        if(!validMove)
                            possibleMoves[rank][file] = 0;
                    }
                }
            }
		}

		return possibleMoves;
	}

	/**
	 * Finds possible moves of a pawn
	 * @param board The current pieces on the board
	 * @param rank The rank of the pawn
	 * @param file The file of the pawn
	 * @param doneMoveStack All completed moves
	 * @return The possible moves of the pawn

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
					&& pawnColor != board[rank + 1][file - 1].teamColor) {
					possibleMoves[rank + 1][file - 1] = 1;
			}
			// Diag. capture, Southeast
			if(file != 8 && board[rank + 1][file + 1].teamColor != Piece.EMPTY
					&& pawnColor != board[rank + 1][file + 1].teamColor) {
					possibleMoves[rank + 1][file + 1] = 1;
			}

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
					&& pawnColor != board[rank - 1][file - 1].teamColor) {
					possibleMoves[rank - 1][file - 1] = 1;
			}
			// Diag. capture, Northeast
			if(file != 8 && board[rank - 1][file + 1].teamColor != Piece.EMPTY
					&& pawnColor != board[rank - 1][file + 1].teamColor) {
					possibleMoves[rank - 1][file + 1] = 1;
			}

			// En passant
			if(rank == 4 && doneMoveStack.size() >= 3) {
				Piece[][] blackLastBoard = doneMoveStack.pop().getBoard();
				// Capture up and left
				if(     file != 1 &&
						board[rank][file-1].type == Piece.BLACK_PAWN &&         // Pawn to capture
						board[rank-2][file-1].type == Piece.EMPTY &&            // Pawn init. square 
						board[rank-1][file-1].type == Piece.EMPTY &&            // Square to move
						blackLastBoard[rank-2][file-1].type == Piece.BLACK_PAWN // Last move by Black
				) {
				    possibleMoves[rank-1][file-1] = 2;
				}
				// Capture down and right
				else if(file != 8 &&
						board[rank][file+1].type == Piece.BLACK_PAWN &&
						board[rank-2][file+1].type == Piece.EMPTY &&
						board[rank-1][file+1].type == Piece.EMPTY &&
						blackLastBoard[rank-2][file+1].type == Piece.BLACK_PAWN
				) {
				    possibleMoves[rank-1][file+1] = 2;
				}
				
				doneMoveStack.push(new State(blackLastBoard));
			}
		}
		return possibleMoves;
	}

	/**
	 * Finds possible moves of a knight
	 * @param board The current pieces on the board
	 * @param rank The rank of the knight
	 * @param file The file of the knight
	 * @return The possible moves of the knight
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
	 * Finds possible moves of a bishop
	 * @param board The current pieces on the board
	 * @param rank The rank of the bishop
	 * @param file The file of the bishop
	 * @return The possible moves of the bishop
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
     * Finds possible moves of a rook
     * @param board The current pieces on the board
     * @param rank The rank of the rook
     * @param file The file of the rook
     * @return The possible moves of the rook
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
     * Finds possible moves of a queen
     * @param board The current pieces on the board
     * @param rank The rank of the queen
     * @param file The file of the queen
     * @return The possible moves of the queen
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
     * Finds possible moves of a king
     * @param board The current pieces on the board
     * @param rank The rank of the king
     * @param file The file of the king
	 * @param doneMoveStack All completed moves
	 * @param whiteToMove The team to move
     * @return The possible moves of the king
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
					!board[rank][file + 3].hasMoved) {
			    if(isKingWhite == whiteToMove) {
                    if(     !isSquareAttacked(board, rank, file, whiteToMove, doneMoveStack) &&
                            !isSquareAttacked(board, rank, file + 1, whiteToMove, doneMoveStack) &&
                            !isSquareAttacked(board, rank, file + 2, whiteToMove, doneMoveStack)) {
                        possibleMoves[rank][file + 2] = 3;
                    }
                } else
				    possibleMoves[rank][file + 2] = 3;
			}
			// Queenside
			// Kingside
			if(		board[rank][file - 1].type == Piece.EMPTY &&
					board[rank][file - 2].type == Piece.EMPTY &&
					(board[rank][file - 4].type == Piece.WHITE_ROOK ||
							board[rank][file - 4].type == Piece.BLACK_ROOK) &&
					board[rank][file - 4].teamColor == kingColor &&
					!board[rank][file - 4].hasMoved) {
			    if(isKingWhite == whiteToMove) {
			        if(     !isSquareAttacked(board, rank, file, whiteToMove, doneMoveStack) &&
                            !isSquareAttacked(board, rank, file - 1, whiteToMove, doneMoveStack) &&
                            !isSquareAttacked(board, rank, file - 2, whiteToMove, doneMoveStack)) {
			            possibleMoves[rank][file - 2] = 3;
                    }
                } else
				    possibleMoves[rank][file - 2] = 3;
			}
		}

        return possibleMoves;
    }

	/**
	 * Checks whether a square is attacked
	 * @param board The current board state
	 * @param squareRank The rank of the square to check
	 * @param squareFile The file of the square to check
	 * @param whiteToMove Which team moves next
	 * @return Whether the square is attacked
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

	/**
	 * Determines whether the king is checked after a particular move
	 * @param boardAfterMove The board after the move
	 * @param whiteToMove Which team moves next
	 * @param doneMoveStack All completed moves
	 * @return Whether the king is checked
	 */
	static boolean isKingCheckedAfterMove(Piece[][] boardAfterMove, boolean whiteToMove, ArrayDeque<State> doneMoveStack) {
    	boolean kingChecked;
	    if(whiteToMove) { // is white's king in check after potential white move
			int kingRank = 0;
			int kingFile = 0;
    		for(int rank = 1; rank <= 8; rank++) {
				for(int file = 1; file <= 8; file++) {
					if(boardAfterMove[rank][file].type == Piece.WHITE_KING) {
						kingRank = rank;
						kingFile = file;
					}
				}
			}
    		kingChecked = isSquareAttacked(boardAfterMove, kingRank, kingFile, whiteToMove, doneMoveStack);
		} else { // black king
			int kingRank = 0;
			int kingFile = 0;
			for(int rank = 1; rank <= 8; rank++) {
				for(int file = 1; file <= 8; file++) {
					if(boardAfterMove[rank][file].type == Piece.BLACK_KING) {
						kingRank = rank;
						kingFile = file;
					}
				}
			}
			kingChecked = isSquareAttacked(boardAfterMove, kingRank, kingFile, whiteToMove, doneMoveStack);
		}
    	return kingChecked;
	}

	/**
	 * Gets a board state after a move
	 * @param oldBoard The board before the move
	 * @param startRank The starting rank of the moving piece
	 * @param startFile The starting file of the moving piece
	 * @param endRank The final rank of the moving piece
	 * @param endFile The final file of the moving piece
	 * @param moveType What type
	 * @return The board after the move
	 */
	static Piece[][] getBoardAfterMove(Piece[][] oldBoard, int startRank, int startFile, int endRank, int endFile, int moveType) {
		Piece[][] newBoard = new Piece[10][10];
		for(int rank = 1; rank <= 8; rank++)
			System.arraycopy(oldBoard[rank], 1, newBoard[rank], 1, 8);
		newBoard[endRank][endFile] = newBoard[startRank][startFile];
		newBoard[startRank][startFile] = new Piece(Piece.EMPTY);
		if(moveType == 2) { // EN PASSANT
			if(newBoard[endRank][endFile].teamColor == Piece.WHITE) {   // WHITE EN PASSANT
				newBoard[endRank+1][endFile] = new Piece(Piece.EMPTY);
			} else {                                                // BLACK EN PASSANT
				newBoard[endRank-1][endFile] = new Piece(Piece.EMPTY);
			}
		} else if(moveType == 3) { // CASTLING
			if(endFile > startFile) { // KINGSIDE
				newBoard[endRank][6] = newBoard[endRank][8];
				newBoard[endRank][8] = new Piece(Piece.EMPTY);
			} else { // QUEENSIDE
				newBoard[endRank][4] = newBoard[endRank][1];
				newBoard[endRank][1] = new Piece(Piece.EMPTY);
			}
		}

		return newBoard;
	}

    /**
     * Detects whether one side has checkmated
     * @param board The current board state
     * @param doneMoveStack All completed moves
     * @param whiteToMove Which side moves next
     * @return Whether one side has checkmated
     */
	static boolean isCheckmate(Piece[][] board, ArrayDeque<State> doneMoveStack, boolean whiteToMove) {
	    int sideToMove;
	    if(whiteToMove)
	        sideToMove = Piece.WHITE;
	    else
	        sideToMove = Piece.BLACK;

        boolean kingAttacked = isKingCheckedAfterMove(board, whiteToMove, doneMoveStack);
        if(!kingAttacked)
            return false;

	    // for each piece of the side to move: get its possible moves, and if it has any, it's not checkmate
	    for(int rank = 1; rank <= 8; rank++) {
	        for(int file = 1; file <= 8; file++) {
	            if(board[rank][file].teamColor == sideToMove) {
	                int[][] possibleMoves = getPossMoves(board, rank, file, doneMoveStack, whiteToMove);
	                for(int r = 1; r <= 8; r++) {
	                    for(int f = 1; f <= 8; f++) {
	                        if(possibleMoves[r][f] > 0) {
	                            return false;
                            }
                        }
                    }
                }
            }
        }

	    return true;
    }

    /**
     * Detects whether one side has stalemated
     * @param board The current board state
     * @param doneMoveStack All completed moves
     * @param whiteToMove Which side moves next
     * @return Whether one side has stalemated
     */
    static boolean isStalemate(Piece[][] board, ArrayDeque<State> doneMoveStack, boolean whiteToMove) {
        int sideToMove;
        if(whiteToMove)
            sideToMove = Piece.WHITE;
        else
            sideToMove = Piece.BLACK;

        boolean kingAttacked = isKingCheckedAfterMove(board, whiteToMove, doneMoveStack);
        if(kingAttacked)
            return false;

        // for each piece of the side to move: get its possible moves, and if it has any, it's not checkmate
        for(int rank = 1; rank <= 8; rank++) {
            for(int file = 1; file <= 8; file++) {
                if(board[rank][file].teamColor == sideToMove) {
                    int[][] possibleMoves = getPossMoves(board, rank, file, doneMoveStack, whiteToMove);
                    for(int r = 1; r <= 8; r++) {
                        for(int f = 1; f <= 8; f++) {
                            if(possibleMoves[r][f] > 0) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
