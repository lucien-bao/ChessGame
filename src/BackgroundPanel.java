import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * <code>BackgroundPanel</code> class. Displays the background image.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.1
 * @since 22 APR 2020
 */
public class BackgroundPanel extends JPanel {
	// TODO: Change it so that, when resized, the image is displayed like this: 
	//  +--+----------+--+
	//  |//|# # # # # |//|
	//  |//| # # # # #|//|
	//  +--+----------+--+ < right edge of image (not visible)
	//               ^
	//  right edge of panel
	//  (i.e. the image is "cropped" to fit the panel)
	//  instead of:
	//     +----------+
	//     |##########|
	//     |##########|
	//     +----------+ < right edge of image
	//               ^
	//  right edge of panel
	//  (i.e. the image is scaled to fit the panel)
	// CONSTANTS
	final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
	);
	
	// IMAGES
	BufferedImage background;
	
	// FIELDS
	int width, height;
	
	// CONSTRUCTOR
	BackgroundPanel() {
		try {
			File backgroundFile = new File("img/background.png");
			background = ImageIO.read(backgroundFile);
		} catch(IOException e) {
			System.out.println("Background image could not be loaded.");
			e.printStackTrace();
		}
	}
	
	// METHODS
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHints(RENDERING_HINTS);
		
		graphics2d.drawImage(background, 0, 0, width, height, this);
	}
	
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
