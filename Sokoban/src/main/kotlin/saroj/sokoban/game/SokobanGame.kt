package saroj.sokoban.game

/**
 * Class that will aggregate Game components
 */
class SokobanGame(
    val sokobanBoard: SokobanBoard,
    val movementManager: MovementManager
) {

    /**
     * Make an ingame Movement
     * Should delegate this to the Movement Manager and evaluate the Result
     */
    fun move(targetPosition: Coordinates): Boolean? {
        val movementResult = movementManager.move(
            sokobanBoard = sokobanBoard,
            startPosition = sokobanBoard.forkliftPosition ?: throw RuntimeException("No forklift position"),
            targetPosition = targetPosition
        )

        //After a valid movement, we should evaluate the game
        if (movementResult) {
            sokobanBoard.forklift.decreaseEnergy()

            //Check if game has ended
            return if (isGameWon()) {
                true
            } else if (sokobanBoard.forklift.getEnergy() < 0) {
                false
            } else {
                null
            }
        } else {
            return null
        }
    }

    /**
     * Decide if the Player won the game
     * In other words, the Boxes and Targets should be in the same Position and Energy should be >= 0
     */
    private fun isGameWon(): Boolean {
        val boxes = sokobanBoard.getAllObjectsOfType(SokobanBoard.BoardObject.BOX)
        val targets = sokobanBoard.getAllObjectsOfType(SokobanBoard.BoardObject.TARGET)
        return boxes.containsAll(targets) && sokobanBoard.forklift.getEnergy() >= 0
    }
}