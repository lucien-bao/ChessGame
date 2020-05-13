import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * <code>BackgroundPanel</code> class. Displays the background image.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.2
 * @since 22 APR 2020
 */
public class BackgroundPanel extends JPanel {
	// CONSTANTS
	double imageRatio = 1440 / 900.0;
	final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
	);
	
	// IMAGES
	BufferedImage background;
	
	// FIELDS
	int imageWidth, imageHeight;
	int panelWidth, panelHeight;
	
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
		int x = 0;
		int y = 0;
		if((double)panelWidth / panelHeight > imageRatio)
			y = imageHeight / 2 - panelHeight / 2;
		else
			x = imageWidth / 2 - panelWidth / 2;
		graphics2d.drawImage(background, -x, -y, imageWidth, imageHeight, this);
	}
	
	/**
	 * Resizes the panel to the specified size
	 *
	 * @param width  the new width to use
	 * @param height the new height to use
	 */
	void changeSize(int width, int height) {
		this.panelHeight = height;
		this.panelWidth  = width;
		this.imageWidth = width;
		this.imageHeight = height;
		if(this.imageWidth > this.imageHeight)
			this.imageHeight = (int)(this.imageWidth / imageRatio);
		else
			this.imageWidth = (int)(this.imageHeight * imageRatio);
	}
}
