package com.Main

// Represents a square on the chess board
class ChessSquare(val coord: Pair<Int, Int>, var piece: ChessPiece?)

// Represents a chess piece
class ChessPiece(val name: ChessPieceName, val color: ChessPieceColor, val value: Int)

// Enumerates the names of all chess pieces
enum class ChessPieceName {
    Rook,
    Knight,
    Bishop,
    Queen,
    King,
    Pawn
}

// Enumerates the chess colors
enum class ChessPieceColor {
    Black,
    White
}

// Enumerates the possible directions pieces can move in chess
enum class ChessDirection {
    Forward,
    ForwardRight,
    Right,
    BackRight,
    Back,
    BackLeft,
    Left,
    ForwardLeft
}