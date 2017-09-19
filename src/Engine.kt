package com.Main

import java.util.Queue
import java.util.LinkedList

// The core of the chess AI
class ChessEngine {

    // Plays a game of chess, returns the color who won
    fun playGame() : ChessPieceColor {
        // Create a new chess board
        var chessBoard = ChessBoard()
        var currentPlayer = ChessPieceColor.White

        // While game has not finished
        while (true) {
            // Setup queue
        val queue = LinkedList<NegamaxNode>()
            queue.add(NegamaxNode(null, chessBoard, 0.0, currentPlayer))

            // Get the next board state
            chessBoard = negamax(queue, currentPlayer)

            // Is the game over?
            if (gameOver(chessBoard, currentPlayer)) {
                return currentPlayer
            }

            currentPlayer = if (currentPlayer == ChessPieceColor.White) ChessPieceColor.Black else ChessPieceColor.White
        }
    }

    private data class NegamaxNode(val parentNode: NegamaxNode?,
                                   val board: ChessBoard,
                                   val boardValue: Double,
                                   val forColor: ChessPieceColor,
                                   val childNodes: List<NegamaxNode> = ArrayList<NegamaxNode>())

    private fun negamax(queue: Queue<NegamaxNode>, forColor: ChessPieceColor, depth: Int = 0) : ChessBoard {
        if (depth < 3) {
            // Get the next layer
            val nextLayerQueue = LinkedList<NegamaxNode>()
            while (queue.peek() != null) {
                val node = queue.poll()
                for (board in node.board.makeAllValidMoves(forColor)) {
                    val newNode = NegamaxNode(node, board, board.evaluate(forColor), forColor)
                    nextLayerQueue.add(newNode)
                }
            }

            // Increase depth
            val newDepth = depth + 1

            // Get next player
            val nextPlayer =
                    if (forColor == ChessPieceColor.White) ChessPieceColor.Black
                    else ChessPieceColor.White

            // Process the next layer
            negamax(nextLayerQueue, nextPlayer, newDepth)
        } else {
            if (queue.peek() == null) {
                throw NoMoveFoundException()
            }

            // Get the root node of the tree
            var rootNode = queue.poll().parentNode
            while (rootNode.parentNode != null) {
                rootNode = rootNode.parentNode
            }

            for (node in rootNode.childNodes) {
                for (opponentNode in node.childNodes) {
                    for (myNode in opponentNode.childNodes) {
                        val
                    }
                }
            }


            var maxAlpha = 0.0
            var parentNode = queue.peek().parentNode

            while (queue.peek() != null) {
                val node = queue.poll()
                if (node.boardValue > maxAlpha) {
                    maxAlpha = node.boardValue
                }

                if (queue.peek() != null && queue.peek().parentNode != parentNode) {

                    // Ok we've reached end of a nodes children,

                    parentNode = queue.peek().parentNode
                    maxAlpha = 0.0
                }
            }
        }
        return ChessBoard()
    }

    // Works out if the game is won given the board state for the current player
    private fun gameOver(chessBoard: ChessBoard, forPlayer: ChessPieceColor) : Boolean {
        // TODO Works out if the game is won given the board state for the current player
        return true
    }
}

class NoMoveFoundException : Exception()