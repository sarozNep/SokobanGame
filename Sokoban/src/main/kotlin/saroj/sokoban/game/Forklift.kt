package saroj.sokoban.game

/**
 * Class that will represent the Player Forklift
 */
data class Forklift(
    private var energy: Int
) {
    /**
     * Upon Movement, we lose energy
     */
    fun decreaseEnergy() {
        energy -= 1
    }

    /**
     * We might find a power up to add energy
     */
    fun increaseEnergy() {
        energy += 1
    }

    /**
     * Expose the currency energy
     */
    fun getEnergy() = energy
}