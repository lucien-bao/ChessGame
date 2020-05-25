/**
 * <code>BoardStateListener</code> interface. Used to detect check-/stalemate.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.2
 * @since 25 MAY 2020
 */
interface BoardStateListener {
    /**
     * Checks for game over and updates internal state.
     */
    void gameIsOver();
}
