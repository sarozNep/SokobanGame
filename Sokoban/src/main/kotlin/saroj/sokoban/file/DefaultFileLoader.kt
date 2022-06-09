package saroj.sokoban.file

import saroj.sokoban.game.Coordinates
import saroj.sokoban.game.SokobanBoard
import saroj.sokoban.game.Forklift
import java.io.File

/**
 * Default Loader
 * Here we expect to read a file like:
 *
 * 12
 * 6
 * 7
 * #######
 * # #X X#
 * #E#   #
 * #  C#C#
 * #     #
 * #######
 *
 * Where:
 * The 1st row is the starting energy
 * The 2nd row is the number of rows
 * The 3rd row is the number of columns
 * The other lines represents the Game Board where the objects are:
 *
 * Blank = Empty Space
 * # = Wall
 * E = Forklift
 * C = Box
 * X = Target
 */
class DefaultFileLoader : FileLoader {

    /**
     * Return the Extension of the file
     * But is this needed?? We could have any text file with any extension
     */
    override val extension: String
        get() = TODO("Not yet implemented")

    /**
     * This method will read/parse the file and return the Board
     * The method signature is a bit different from the problem, but it does
     * make sense to encapsulate it returning the Board
     */
    override fun load(file: File): SokobanBoard {
        val lines = file.readLines()

        //Get the Basic data
        val maxEnergy = lines[0].toInt()
        val rows = lines[1].toInt()
        val columns = lines[2].toInt()

        //Create the Empty Board
        val sokobanBoard = SokobanBoard(rows, columns, Forklift(maxEnergy))

        //Now we are ready to parse the board
        //For now we assume the Board will reflect the size provided in the above lines
        val linesAlreadyConsumed = 3
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                //Get the char for the position
                val char = lines[row + linesAlreadyConsumed][column]

                //Get the Board object that represents the char
                val boardObject = getBoardObjectFromChar(char)

                //Encapsulate the Coordinates
                val coordinates = Coordinates(row, column)

                //Do the Validations for each new Object
                validate(sokobanBoard, coordinates, boardObject)

                //Add it into the Board
                sokobanBoard.set(
                    coordinates,
                    boardObject
                )
            }
        }

        //Do the final validations
        finalValidate(sokobanBoard)

        //Return the Result containing Board and Forklift objects
        return sokobanBoard
    }

    /**
     * Make validations when adding Board Objects like we want only one Forklift into the game
     */
    private fun validate(sokobanBoard: SokobanBoard, coordinates: Coordinates, boardObject: SokobanBoard.BoardObject) {
        if (boardObject == SokobanBoard.BoardObject.FORKLIFT) {
            if (sokobanBoard.forkliftPosition != null) {
                throw RuntimeException("More than one Forklift")
            }
        }
    }

    /**
     * Make the final validations like if we have a Forklift and if there are Targets and Boxes
     */
    private fun finalValidate(sokobanBoard: SokobanBoard) {
        if (sokobanBoard.forkliftPosition == null) {
            throw RuntimeException("No Forklift added")
        }

        val targets = sokobanBoard.getAllObjectsOfType(SokobanBoard.BoardObject.TARGET)
        val boxes = sokobanBoard.getAllObjectsOfType(SokobanBoard.BoardObject.BOX)

        if (boxes.size != targets.size) {
            throw RuntimeException("Boxes and Targes does not match")
        }
    }
}