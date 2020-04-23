import javax.swing.*;
import java.awt.*;

/**
 * <code>InfoPanel</code> class. This displays information relevant to the game.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.3
 * @since 4 APR 2020
 */
public class InfoPanel extends JPanel {
	// CONSTANTS
	final int BACKGROUND_OPAQUE     = Color.HSBtoRGB(0, 0, 0.5f);
	final float BACKGROUND_ALPHA    = 0.5f;
	final int BACKGROUND_RGBA       = (BACKGROUND_OPAQUE & 16777215) | ((int) (BACKGROUND_ALPHA*255) << 24);
	final Color BACKGROUND_COLOR    = new Color(BACKGROUND_RGBA, true);
	final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
	);
	
	// FIELDS
	int width, height;
	
	// CONSTRUCTOR
	InfoPanel() {
		this.setOpaque(false);
		System.out.println(BACKGROUND_COLOR.getAlpha());
	}
	
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
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHints(RENDERING_HINTS);
		
		graphics2d.setColor(BACKGROUND_COLOR);
		graphics2d.fillRect(0, 0, width, height);
	}
}
