package saroj.sokoban.ui

import pt.iscte.guitoo.board.Board
import saroj.sokoban.game.Coordinates
import saroj.sokoban.game.MovementManager
import saroj.sokoban.game.SokobanBoard
import saroj.sokoban.game.SokobanGame

/**
 * Class that will control the Game UI
 * This will render the SokobanGame to the User
 * and also collect User Input
 */
class GameUI(
    private val sokobanGame: SokobanGame,
    private val userInputCallback: (Int, Int) -> Unit,
    private val restartCallback: () -> Unit
) {

    /**
     * The Library Board will render the squares of the Lines x Columns
     * and each Square will have the specific side
     */
    private val guiBoard: Board = Board(
        "Title",
        sokobanGame.sokobanBoard.rows,
        sokobanGame.sokobanBoard.columns,
        50
    )

    /**
     * Render the Game UI
     */
    fun render() {
        //For each Row/Column, check what we have in the Board and Render the correct image
        renderSquares(guiBoard)

        //Define the User inputs
        guiBoard.addMouseListener(userInputCallback)

        //Add the Label about remaining energy
        guiBoard.addLabel { "Energy Remaining: ${sokobanGame.sokobanBoard.forklift.getEnergy()}" }

        //Action to restart the game
        guiBoard.addAction("Restart") {
            val clear = guiBoard.promptText("Clear? Y/N")
            if (clear.equals("Y", ignoreCase = true)) {
                restartCallback()
            }
        }

        //Display the Board
        guiBoard.open()
    }

    /**
     * Display an Alert Message
     */
    fun displayMessage(message: String) = guiBoard.showMessage(message)

    fun refresh() = guiBoard.refresh()

    /**
     * Based on Sokoban Board cells, render the correct images
     */
    private fun renderSquares(guiBoard: Board) {
        guiBoard.setIconProvider { row, column ->
            when (sokobanGame.sokobanBoard.get(Coordinates(row, column))) {
                SokobanBoard.BoardObject.EMPTY -> "images/Chao.png"
                SokobanBoard.BoardObject.BOX -> "images/Caixote.png"
                SokobanBoard.BoardObject.TARGET -> "images/Alvo.png"
                SokobanBoard.BoardObject.FORKLIFT -> renderForkliftIcon()
                SokobanBoard.BoardObject.WALL -> "images/Parede.png"
            }
        }
    }

    /**
     * Decide which Forklift Icon to apply based on last valid movement
     */
    private fun renderForkliftIcon() =
        when (sokobanGame.movementManager.lastMovementDirection) {
            MovementManager.MovementDirection.DOWN -> "images/Empilhadora_D.png"
            MovementManager.MovementDirection.UP -> "images/Empilhadora_U.png"
            MovementManager.MovementDirection.LEFT -> "images/Empilhadora_L.png"
            MovementManager.MovementDirection.RIGHT -> "images/Empilhadora_R.png"
            else -> "images/Empilhadora_R.png"
        }
}