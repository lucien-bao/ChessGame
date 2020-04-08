import javax.swing.*;

/**
 * <code>PlayerPanel</code> class. This displays information for a specific player.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.2
 * @since 4 APR 2020
 */
public class PlayerPanel extends JPanel {
	// FIELDS
	int width, height;
	
	// METHODS
	
	/**
	 * Resizes the panel to the specified size
	 *
	 * @param width  the new width to use
	 * @param height the new height to use
	 */
	void changeSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
