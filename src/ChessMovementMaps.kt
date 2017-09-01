package com.Main

import java.util.*

// Maps chess directions to straight line movement values
class StraightLineDirectionMapping {

    companion object {
        private val xMap = createXMap()
        private val yMap = createYMap()

        fun x(direction: ChessDirection) : Int {
            return xMap[direction]!!
        }

        fun y(direction: ChessDirection) : Int {
            return yMap[direction]!!
        }

        // Creates the mapping in the x axis
        fun createXMap() : Hashtable<ChessDirection, Int> {
            val map = Hashtable<ChessDirection, Int>()
            map.put(ChessDirection.BackLeft, -1)
            map.put(ChessDirection.Left, -1)
            map.put(ChessDirection.ForwardLeft, -1)
            map.put(ChessDirection.Forward, 0)
            map.put(ChessDirection.ForwardRight, 1)
            map.put(ChessDirection.Right, 1)
            map.put(ChessDirection.BackRight, 1)
            map.put(ChessDirection.Back, 0)
            return map
        }

        // Creates the mapping in y axis
        fun createYMap() : Hashtable<ChessDirection, Int> {
            val map = Hashtable<ChessDirection, Int>()
            map.put(ChessDirection.BackLeft, -1)
            map.put(ChessDirection.Left, 0)
            map.put(ChessDirection.ForwardLeft, 1)
            map.put(ChessDirection.Forward, 1)
            map.put(ChessDirection.ForwardRight, 1)
            map.put(ChessDirection.Right, 0)
            map.put(ChessDirection.BackRight, -1)
            map.put(ChessDirection.Back, -1)
            return map
        }
    }
}

// Maps chess directions to knight movement values
class KnightDirectionMapping {

    companion object {
        private val xMap = createXMap()
        private val yMap = createYMap()

        fun x(direction: ChessDirection) : Int {
            return xMap[direction]!!
        }

        fun y(direction: ChessDirection) : Int {
            return yMap[direction]!!
        }

        // Creates the mapping in the x axis
        fun createXMap() : Hashtable<ChessDirection, Int> {
            val map = Hashtable<ChessDirection, Int>()
            map.put(ChessDirection.BackLeft, -1)
            map.put(ChessDirection.Left, -2)
            map.put(ChessDirection.ForwardLeft, -2)
            map.put(ChessDirection.Forward, -1)
            map.put(ChessDirection.ForwardRight, 1)
            map.put(ChessDirection.Right, 2)
            map.put(ChessDirection.BackRight, 2)
            map.put(ChessDirection.Back, 1)
            return map
        }

        // Creates the mapping in y axis
        fun createYMap() : Hashtable<ChessDirection, Int> {
            val map = Hashtable<ChessDirection, Int>()
            map.put(ChessDirection.BackLeft, -2)
            map.put(ChessDirection.Left, -1)
            map.put(ChessDirection.ForwardLeft, 1)
            map.put(ChessDirection.Forward, 2)
            map.put(ChessDirection.ForwardRight, 2)
            map.put(ChessDirection.Right, 1)
            map.put(ChessDirection.BackRight, -1)
            map.put(ChessDirection.Back, -2)
            return map
        }
    }
}