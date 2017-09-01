package com.Main

// The core of the chess AI
class ChessEngine {

    // Plays a game of chess, returns the color who won
    fun playGame() : ChessPieceColor {
        // Create a new chess board
        var chessBoard = ChessBoard()
        var currentPlayer = ChessPieceColor.White

        // While game has not finished
        while (true) {
            // Get the next board state
            val chessBoard = negamax(chessBoard, currentPlayer, 3)

            // Is the game over?
            if (gameOver(chessBoard, currentPlayer)) {
                return currentPlayer
            }

            currentPlayer = if (currentPlayer == ChessPieceColor.White) ChessPieceColor.Black else ChessPieceColor.White
        }
    }

    // Recursive function to find best move given the board state and the current player, returns the new board
    private fun negamax(forBoard: ChessBoard, forColor: ChessPieceColor, depth: Int) : ChessBoard {
        // TODO Finds the best root node for a given board state and a given player by applying negamax to D = depth

        val initialBoardValue = forBoard.evaluate(forColor)
        System.out.println("Initial Board Value: " + initialBoardValue)
        // While d <= depth
        // For each piece owned by current player
        // For each valid move for the piece
        // Create a copy of the board with that move madeS
        // Evaluate the board

        return ChessBoard()
    }

    // Works out if the game is won given the board state for the current player
    private fun gameOver(chessBoard: ChessBoard, forPlayer: ChessPieceColor) : Boolean {
        // TODO Works out if the game is won given the board state for the current player
        return true
    }
}