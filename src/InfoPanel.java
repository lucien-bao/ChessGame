import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <code>InfoPanel</code> class. This displays information relevant to the game.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.4
 * @since 4 APR 2020
 */
public class InfoPanel extends JPanel implements ActionListener {
	// CONSTANTS
	final int BACKGROUND_OPAQUE     = Color.HSBtoRGB(0, 0, 0.5f);
	final float BACKGROUND_ALPHA    = 0.5f;
	final int BACKGROUND_RGBA       = (BACKGROUND_OPAQUE & 16777215) | ((int) (BACKGROUND_ALPHA*255) << 24);
	final Color BACKGROUND_COLOR    = new Color(BACKGROUND_RGBA, true);
	final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
	);
	JButton newGame;
	BoardPanel boardPanel;
	
	// FIELDS
	int width, height;
	
	// CONSTRUCTOR
	InfoPanel(BoardPanel boardPanel) {
		this.setOpaque(false);
		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		newGame.setActionCommand("new game");
		this.add(newGame);
		this.boardPanel = boardPanel;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("new game")) {
			boardPanel.setBoard();
			boardPanel.checkmateStatus = 0;
			boardPanel.repaint();
		}
	}
}
