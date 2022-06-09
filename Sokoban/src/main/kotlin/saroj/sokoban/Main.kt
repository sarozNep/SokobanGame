package saroj.sokoban

import saroj.sokoban.file.DefaultFileLoader
import saroj.sokoban.game.Coordinates
import saroj.sokoban.game.MovementManager
import saroj.sokoban.game.SokobanGame
import saroj.sokoban.ui.GameUI
import java.io.File

//Filename, for testing purposes
const val filename = "board.txt"

//Get the Sokoban Game Instance
var sokobanGame = loadSokobanGameFromFile(filename)

//Restart Callback
val restartCallback: () -> Unit = {
    sokobanGame = loadSokobanGameFromFile(filename)
}

val userInputCallback: (Int, Int) -> Unit = { row, column ->
    try {
        val targetCoordinates = Coordinates(row, column)
        when (sokobanGame.move(targetCoordinates)) {
            true -> gameUI.displayMessage("You won the game!")
            false -> gameUI.displayMessage("You lose the game!")
        }
    } catch (ex: Exception) {
        println(ex)
    }
}

//Create the UI Component
var gameUI = createGameUI()

fun main() {
    gameUI.render()
}

/**
 * Parse the Game File
 */
private fun loadSokobanGameFromFile(filename: String): SokobanGame {
    //Open Java file
    val file = File(filename)

    //Go get the FileLoader... We might want to use Dependency Injection
    val fileLoader = DefaultFileLoader()

    //Parse the File into the Board
    val board = fileLoader.load(file)

    //Create the Game object
    return SokobanGame(
        sokobanBoard = board,
        movementManager = MovementManager()
    )
}

private fun createGameUI() =
    GameUI(
        sokobanGame = sokobanGame,
        userInputCallback = userInputCallback,
        restartCallback
    )