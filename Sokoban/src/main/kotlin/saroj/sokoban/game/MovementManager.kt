package saroj.sokoban.game

/**
 * Class to Manage the Movement within the Sokoban Board
 * Here goes the logic that tell what happens when elements collide and so on.
 * The exercise suggests use of inheritance for game objects,
 * But from architectural perspective, does not make sense the Board object classes
 * to know what happens when one collide to another, so all this behavior is implemented here.
 */
class MovementManager {

    /**
     * Encapsulate Movement Directions
     */
    enum class MovementDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    //Expose the Last Movement, so the Renderer may change the Forklift icon
    var lastMovementDirection: MovementDirection? = null
        private set

    /**
     * Since we are collecting User Input from Mouse, we only allow movement if the User taps
     * on the adjacent cells from the current forklift position (UP, DOWN, LEFT, RIGHT)
     * Of course we cannot move through walls and, depending on the board object,
     * we might push the Boxes and apply custom behavior for other objects
     */
    fun move(
        sokobanBoard: SokobanBoard,
        startPosition: Coordinates,
        targetPosition: Coordinates
    ): Boolean {
        //Lets calculate the Direction
        val movementDirection = getMovementDirectionForTargetCoordinates(targetPosition, startPosition)

        //Invoke the other function with movement direction
        return move(sokobanBoard, startPosition, movementDirection)
    }

    /**
     * Core movement function.
     * Since we might push other objects, we want a recursive function to make move chains.
     * So, for this we take the Start Position and the Movement direction.
     * With this approach, we can chain box movements and also make custom behaviors for new
     * Board Objects, like holes and others.
     */
    private fun move(
        sokobanBoard: SokobanBoard,
        startPosition: Coordinates,
        movementDirection: MovementDirection
    ): Boolean {
        //Result flag
        var isMovementValid = false

        //Get the new desired Coordinates based on the Movement Direction
        val newPosition = when (movementDirection) {
            MovementDirection.UP -> startPosition.copy(row = startPosition.row - 1)
            MovementDirection.DOWN -> startPosition.copy(row = startPosition.row + 1)
            MovementDirection.LEFT -> startPosition.copy(column = startPosition.column - 1)
            MovementDirection.RIGHT -> startPosition.copy(column = startPosition.column + 1)
        }

        //Check what exists in the new position
        when (sokobanBoard.get(newPosition)) {
            //Empty stuff, so we just move to the new Position
            SokobanBoard.BoardObject.EMPTY,
            SokobanBoard.BoardObject.TARGET -> {
                sokobanBoard.move(startPosition, newPosition)
                isMovementValid = true
            }

            //For Boxes, we must push it, so lets make it recursive
            SokobanBoard.BoardObject.BOX -> {
                //Try to move the box
                isMovementValid = move(sokobanBoard, newPosition, movementDirection)

                //If we moved it, move our starter object also
                if (isMovementValid) {
                    sokobanBoard.move(startPosition, newPosition)
                }
            }

            //Cannot move through walls, so nothing to do
            SokobanBoard.BoardObject.WALL -> {}
            else -> {}
        }

        if (isMovementValid) {
            lastMovementDirection = movementDirection
        }

        //Return if we moved or not
        return isMovementValid
    }

    /**
     * Get the Movement Direction based on the target Coordinate.
     * Note that we allow only movements that are adjacents to the starting point.
     */
    private fun getMovementDirectionForTargetCoordinates(
        target: Coordinates,
        currentForkliftPosition: Coordinates
    ): MovementDirection {
        //Get the Coordinate Differences. We allow movement only of one of these are +1 or -1
        val diffColumn = target.column - currentForkliftPosition.column
        val diffRow = target.row - currentForkliftPosition.row

        //Lets calculate the Direction
        return when {
            diffColumn == 1 && diffRow == 0 -> MovementDirection.RIGHT
            diffColumn == -1 && diffRow == 0 -> MovementDirection.LEFT
            diffRow == -1 && diffColumn == 0 -> MovementDirection.UP
            diffRow == 1 && diffColumn == 0 -> MovementDirection.DOWN
            else -> throw RuntimeException("Movement not allowed")
        }
    }
}