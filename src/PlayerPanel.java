import javax.swing.*;
import java.awt.*;

/**
 * <code>PlayerPanel</code> class. This displays information about a specific player.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.4
 * @since 4 APR 2020
 */
public class PlayerPanel extends JPanel {
	// FIELDS //
	int width, height;
	boolean isWhite;
	int BACKGROUND_OPAQUE;
	final float BACKGROUND_ALPHA = 0.5f;
	int BACKGROUND_RGBA;
	Color BACKGROUND_COLOR;
	final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
	);
	
	// CONSTRUCTOR //
	
	/**
	 * Creates a <code>PlayerPanel</code> based on color.
	 * @param isWhite The color to use.
	 */
	PlayerPanel(boolean isWhite) {
		this.setOpaque(false);
		this.isWhite = isWhite;
		if(isWhite) {
			BACKGROUND_OPAQUE   = Color.HSBtoRGB(0, 0, 1);
		} else {
			BACKGROUND_OPAQUE   = Color.HSBtoRGB(0, 0, 0);
		}
		BACKGROUND_RGBA     = (BACKGROUND_OPAQUE & 16777215) | ((int) (BACKGROUND_ALPHA*255) << 24);
		BACKGROUND_COLOR    = new Color(BACKGROUND_RGBA, true);
	}
	
	// METHODS //
	
	/**
	 * Resizes the panel to the specified size.
	 *
	 * @param width The new width to use.
	 * @param height The new height to use.
	 */
	void changeSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Repaints this instance.
	 * @param graphics The <code>Graphics</code> instance used to draw.
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHints(RENDERING_HINTS);
		
		graphics2d.setColor(BACKGROUND_COLOR);
		graphics2d.fillRect(0, 0, width, height);
	}
}
