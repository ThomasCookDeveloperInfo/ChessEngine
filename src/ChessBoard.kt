package com.Main

class MyApp {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val chessEngine = ChessEngine()
            chessEngine.playGame()
        }
    }
}

// Represents the chess board for a game of chess
// Exposes functions for evaluating the board and making moves
class ChessBoard(private val squares: Array<ChessSquare>
                 = Array(64, { index -> ChessSquare(coordFromIndex(index), startingPieceFromIndex(index))})) {

    // Makes all possible valid moves for the given color
    // returns a set of all new board states produced by those moves
    fun makeAllValidMoves(forColor: ChessPieceColor) : List<ChessBoard> {
        val newBoards = ArrayList<ChessBoard>()
        for (square in squares) {
            val piece = square.piece
            if (piece != null) {
                if (piece.color == forColor) {
                    if (piece.name == ChessPieceName.Pawn) {
                        val forwardCoord: Pair<Int, Int>
                        val forwardLeftCoord: Pair<Int, Int>
                        val forwardRightCoord: Pair<Int, Int>
                        if (forColor == ChessPieceColor.White) {
                            forwardCoord = Pair(square.coord.first, square.coord.second + 1)
                            forwardLeftCoord = Pair(square.coord.first - 1, square.coord.second + 1)
                            forwardRightCoord = Pair(square.coord.first + 1, square.coord.second + 1)
                        } else {
                            forwardCoord = Pair(square.coord.first, square.coord.second - 1)
                            forwardLeftCoord = Pair(square.coord.first - 1, square.coord.second - 1)
                            forwardRightCoord = Pair(square.coord.first + 1, square.coord.second - 1)
                        }
                        if (forwardCoord.second < 8 && forwardCoord.second > 0) {
                            newBoards.add(makeMove(square.coord, forwardCoord))
                        }
                        if (forwardLeftCoord.first > 0 && forwardLeftCoord.first > 0 &&
                                forwardLeftCoord.second > 0 && forwardLeftCoord.second < 8) {
                            newBoards.add(makeMove(square.coord, forwardLeftCoord))
                        }
                        if (forwardRightCoord.first > 0 && forwardRightCoord.first > 0 &&
                                forwardRightCoord.second > 0 && forwardRightCoord.second < 8) {
                            newBoards.add(makeMove(square.coord, forwardRightCoord))
                        }
                    }
                }
            }
        }
        return newBoards
    }

    // Moves the piece on square from -> to
    private fun makeMove(from: Pair<Int, Int>, to: Pair<Int, Int>) : ChessBoard {
        // Copy squares
        val newSquares = squares

        // Get ref to from square and to square
        val fromSquare = newSquares[indexFromCoord(from)]
        val toSquare = newSquares[indexFromCoord(to)]

        // Get ref to piece
        val piece = fromSquare.piece

        // Move piece from -> to
        toSquare.piece = piece
        fromSquare.piece = null

        // Return new chessboard
        return ChessBoard(newSquares)
    }

    // Evaluates the passed board state for the specified player
    fun evaluate(forColor: ChessPieceColor) : Double {
        val upsides = calculateBoardValue(forColor)
        var downsides: Double
        if (forColor == ChessPieceColor.White) {
            downsides = calculateBoardValue(ChessPieceColor.Black)
        } else {
            downsides = calculateBoardValue(ChessPieceColor.White)
        }
        return upsides - downsides
    }

    // Calculates the board value for the given color
    private fun calculateBoardValue(forColor: ChessPieceColor) : Double {
        // Count the concrete values (the material on the board)
        val concreteValues = countMaterial(forColor)

        // Count the heuristic values (the things about the board state that are helpful but not concrete)
        val heuristicValues = squares.filter { it.piece?.color == forColor }
                                     .sumByDouble { calculateValueOfSquare(it, forColor) }

        // Return the sum of concrete and heuristic values
        return concreteValues + heuristicValues
    }

    // Counts all the material on the board
    // Positive for the specified color
    // Negative for the opposite color
    fun countMaterial(forColor: ChessPieceColor) : Int {
        var materialValue = 0
        for (square in squares) {
            if (square.piece != null && square.piece?.color == forColor) {
                materialValue += square.piece!!.value
            } else if (square.piece != null && square.piece?.color != forColor) {
                materialValue -= square.piece!!.value
            }
        }
        return materialValue
    }

    // Calculates the value at the coord for the given color
    private fun valueAtCoord(coord: Pair<Int, Int>, forColor: ChessPieceColor) : Double {
        val indexFromCoord = indexFromCoord(coord)
        if (indexFromCoord in 0..63 && squares[indexFromCoord].piece != null) {
            if (squares[indexFromCoord].piece?.color != forColor) {
                return squares[indexFromCoord].piece!!.value.toDouble()
            }
        }
        return 0.0
    }

    // Returns the next coordinate in the specified direction, null if there isn't one
    private fun nextCoordInDirection(from: ChessSquare, direction: ChessDirection) : Pair<Int, Int> {
        return Pair(from.coord.first + StraightLineDirectionMapping.x(direction),
                    from.coord.second + StraightLineDirectionMapping.y(direction))
    }

    // Calculates the value obtainable in a straight line
    private fun straightLineValue(square: ChessSquare, forColor: ChessPieceColor,
                                   direction: ChessDirection, upside: Double = 0.0) : Double {
        val nextCoord = nextCoordInDirection(square, direction)
        if (isCoordOnBoard(nextCoord)) {
            val nextSquare = squares[indexFromCoord(nextCoord)]
            return straightLineValue(nextSquare, forColor, direction, valueAtCoord(nextCoord, forColor))
        } else {
            return upside
        }
    }

    // Calculates the total value of pieces obtainable by a knight on the given square
    private fun knightValue(square: ChessSquare, forColor: ChessPieceColor) : Double {
        var value = 0.0
        ChessDirection.values().forEach { direction ->
            value += valueAtCoord(Pair(square.coord.first  + KnightDirectionMapping.x(direction),
                                       square.coord.second + KnightDirectionMapping.y(direction)),
                                       forColor)
        }
        return value
    }

    // Returns true if the passed coord is on the board, false if not
    private fun isCoordOnBoard(coord: Pair<Int, Int>) : Boolean {
        return coord.first in 1..8 && coord.second in 1..8
    }

    // Calculates the value of all pieces that could be taken by the piece on this square
    private fun calculateValueOfSquare(square: ChessSquare, forColor: ChessPieceColor) : Double {
        // Keeps track of the upside
        var upside = 0.0

        // If the piece is a pawn
        if (square.piece?.name == ChessPieceName.Pawn) {
            // Look at the two squares diagonally in front
            val forwardLeft = Pair(square.coord.first + pawnDirection(forColor), square.coord.second - 1)
            var indexFromCoord = indexFromCoord(forwardLeft)
            if (indexFromCoord in 0..63) {
                val possiblePiece = squares[indexFromCoord]
                if (possiblePiece.piece != null && possiblePiece.piece?.color != forColor) {
                    upside += possiblePiece.piece!!.value
                }
            }
            val forwardRight = Pair(square.coord.first + pawnDirection(forColor), square.coord.second + 1)
            indexFromCoord = indexFromCoord(forwardRight)
            if (indexFromCoord in 0..63) {
                val possiblePiece = squares[indexFromCoord]
                if (possiblePiece.piece != null && possiblePiece.piece?.color != forColor) {
                    upside += possiblePiece.piece!!.value
                }
            }
        }
        else if (square.piece?.name == ChessPieceName.Rook) {
            upside += straightLineValue(square, forColor, ChessDirection.Left) +
                      straightLineValue(square, forColor, ChessDirection.Forward) +
                      straightLineValue(square, forColor, ChessDirection.Right) +
                      straightLineValue(square, forColor, ChessDirection.Back)
        }
        else if (square.piece?.name == ChessPieceName.Knight) {
            upside += knightValue(square, forColor)
        }
        else if (square.piece?.name == ChessPieceName.Bishop) {
            upside += straightLineValue(square, forColor, ChessDirection.BackLeft) +
                      straightLineValue(square, forColor, ChessDirection.ForwardLeft) +
                      straightLineValue(square, forColor, ChessDirection.ForwardRight) +
                      straightLineValue(square, forColor, ChessDirection.BackRight)
        }
        else if (square.piece?.name == ChessPieceName.Queen) {
            upside += straightLineValue(square, forColor, ChessDirection.BackLeft) +
                      straightLineValue(square, forColor, ChessDirection.Left) +
                      straightLineValue(square, forColor, ChessDirection.ForwardLeft) +
                      straightLineValue(square, forColor, ChessDirection.Forward) +
                      straightLineValue(square, forColor, ChessDirection.ForwardRight) +
                      straightLineValue(square, forColor, ChessDirection.Right) +
                      straightLineValue(square, forColor, ChessDirection.BackRight) +
                      straightLineValue(square, forColor, ChessDirection.Back)
        }
        return upside
    }

    // Returns the direction that a given colors pawns move in
    private fun pawnDirection(forColor: ChessPieceColor) : Int {
        if (forColor == ChessPieceColor.White) {
            return 1
        }
        return -1
    }

    companion object {
        // Maps an index to a coordinate
        private fun coordFromIndex(boardIndex: Int) : Pair<Int, Int> {
            val row: Int = (boardIndex / 8) + 1
            val column: Int = (boardIndex % 8) + 1
            return Pair(row, column)
        }

        // Maps a coordinate to an index
        private fun indexFromCoord(coord: Pair<Int, Int>) : Int {
            return ((coord.first - 1) * 8) + (coord.second - 1)
        }

        // Returns the chess piece that would belong on the index
        // TODO Refactor as this is shit
        private fun startingPieceFromIndex(boardIndex: Int) : ChessPiece? {
            // Convert board index to starting piece
            val row: Int = (boardIndex / 8) + 1
            val column: Int = (boardIndex % 8) + 1

            // White
            if (row == 1) {
                if (column == 1 || column == 8) {
                    return ChessPiece(ChessPieceName.Rook, ChessPieceColor.White, 5)
                }
                else if (column == 2 || column == 7) {
                    return ChessPiece(ChessPieceName.Knight, ChessPieceColor.White, 3)
                }
                else if (column == 3 || column == 6) {
                    return ChessPiece(ChessPieceName.Bishop, ChessPieceColor.White, 3)
                }
                else if (column == 4) {
                    return ChessPiece(ChessPieceName.King, ChessPieceColor.White, 4)
                }
                else {
                    return ChessPiece(ChessPieceName.Queen, ChessPieceColor.White, 9)
                }
            }
            else if (row == 2) {
                return ChessPiece(ChessPieceName.Pawn, ChessPieceColor.White, 1)
            }
            // Black
            else if (row == 7) {
                return ChessPiece(ChessPieceName.Pawn, ChessPieceColor.Black, 1)
            }
            else if (row == 8) {
                if (column == 1 || column == 8) {
                    return ChessPiece(ChessPieceName.Rook, ChessPieceColor.Black, 5)
                }
                else if (column == 2 || column == 7) {
                    return ChessPiece(ChessPieceName.Knight, ChessPieceColor.Black, 3)
                }
                else if (column == 3 || column == 6) {
                    return ChessPiece(ChessPieceName.Bishop, ChessPieceColor.Black, 3)
                }
                else if (column == 5) {
                    return ChessPiece(ChessPieceName.King, ChessPieceColor.Black, 4)
                }
                else {
                    return ChessPiece(ChessPieceName.Queen, ChessPieceColor.Black, 9)
                }
            }
            return null
        }
    }
}