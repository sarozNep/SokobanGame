package saroj.sokoban.file

import saroj.sokoban.game.SokobanBoard
import java.io.File

interface FileLoader {

    val extension: String

    fun load(file: File): SokobanBoard

    fun getBoardObjectFromChar(char: Char): SokobanBoard.BoardObject{
        return when (char) {
            ' ' -> SokobanBoard.BoardObject.EMPTY
            '#' -> SokobanBoard.BoardObject.WALL
            'E' -> SokobanBoard.BoardObject.FORKLIFT
            'C' -> SokobanBoard.BoardObject.BOX
            'X' -> SokobanBoard.BoardObject.TARGET
            else -> throw RuntimeException("Board Object not valid: $char")
        }
    }
}