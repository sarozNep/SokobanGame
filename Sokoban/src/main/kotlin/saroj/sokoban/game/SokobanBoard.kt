package saroj.sokoban.game

/**
 * Class to represent the Board
 * Note that the coordinate system starts from the Upper Left corner:
 * (0,0)..........(0,columns)
 * ...               .....
 * (rows,0).......(rows,columns)
 *
 * Due to practical reasons, we will not have a Matrix containing the all of the Objects,
 * mainly because we want to keep track of the initial state of the Board.
 *
 * For example: When we move the Forklift, we may end up walking over the Target cell,
 * but if we move out of it, we want the UI to be restored without loss of state.
 *
 * So, we will have a matrix containing the static part of the Board, like walls, empty floor and the target cells
 * and we will have a Map structure that will contain the Movable objects
 */
data class SokobanBoard(
    val rows: Int,
    val columns: Int,
    val forklift: Forklift
) {
    /**
     * Possible Objects that can fit in the Board cells
     */
    enum class BoardObject {
        EMPTY,
        WALL,
        BOX,
        TARGET,
        FORKLIFT
    }

    /**
     * Matrix that will represent the Static part of the Board
     */
    private val matrix: Array<Array<BoardObject>> = Array(rows) {
        Array(columns) {
            BoardObject.EMPTY
        }
    }

    /**
     * Map containing the movable objects
     */
    private val movableObjects = mutableMapOf<Coordinates, BoardObject>()

    /**
     * Convenience to return the Forklift Position
     */
    val forkliftPosition: Coordinates?
        get() {
            movableObjects.forEach { (coordinates, boardObject) ->
                if (boardObject == BoardObject.FORKLIFT) {
                    return coordinates
                }
            }

            return null
        }

    /**
     * Put a given Board object into the Board Position
     * In here we decide if we add into the Static Matrix or the Movable Map
     * This should be called only when defining the Board at start
     *
     * For ingame movement, use the move function instead
     */
    fun set(coordinates: Coordinates, boardObject: BoardObject) {
        if (isFixedObject(boardObject)) {
            matrix[coordinates.row][coordinates.column] = boardObject
        } else {
            matrix[coordinates.row][coordinates.column] = BoardObject.EMPTY
            movableObjects[coordinates] = boardObject
        }
    }

    /**
     * List of Fixed objects
     */
    private fun isFixedObject(boardObject: BoardObject) =
        boardObject == BoardObject.WALL
                || boardObject == BoardObject.EMPTY
                || boardObject == BoardObject.TARGET

    /**
     * Function to implement movement of the Movable Objects
     */
    fun move(startPosition: Coordinates, endPosition: Coordinates) {
        val obj = get(startPosition)
        movableObjects.remove(startPosition)
        movableObjects[endPosition] = obj
    }

    /**
     * Get what exists in the given position
     * First we go to the Movable Map and then in the Matrix
     */
    fun get(coordinates: Coordinates): BoardObject {
        return movableObjects[coordinates] ?: matrix[coordinates.row][coordinates.column]
    }

    /**
     * Get all coordinates for the Board Objects of a given type
     * Since this is a heavy operation, should be used with care
     */
    fun getAllObjectsOfType(boardObject: BoardObject): List<Coordinates> {
        val result = mutableListOf<Coordinates>()

        //Find in the Matrix
        matrix.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, obj ->
                if (obj == boardObject) {
                    result.add(Coordinates(rowIndex, columnIndex))
                }
            }
        }

        //Find in the Movables
        movableObjects.forEach { (coordinates, obj) ->
            if (obj == boardObject) {
                result.add(coordinates)
            }
        }

        return result
    }


}