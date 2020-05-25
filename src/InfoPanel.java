import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <code>InfoPanel</code> class. This displays information relevant to the game.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.6
 * @since 4 APR 2020
 */
public class InfoPanel extends JPanel implements ActionListener, BoardStateListener {
	// CONSTANTS //
	final int BACKGROUND_OPAQUE     = Color.HSBtoRGB(0, 0, 0.5f);
	final float BACKGROUND_ALPHA    = 0.5f;
	final int BACKGROUND_RGBA       = (BACKGROUND_OPAQUE & 16777215) | ((int) (BACKGROUND_ALPHA*255) << 24);
	final Color BACKGROUND_COLOR    = new Color(BACKGROUND_RGBA, true);
	final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
	);
	JButton newGameButton;
	BoardPanel boardPanel;
	
	// FIELDS //
	int width, height;
	int checkmateStatus; // 0 is none, 1 is white, 2 is black
	int stalemateStatus;
	JLabel gameOverLabel;
	
	// CONSTRUCTOR //
	
	/**
	 * Default constructor. Recieves a <code>BoardPanel</code> instance to access info about the game.
	 * @param boardPanel The <code>BoardPanel</code> instance from <code>Frame</code>.
	 */
	InfoPanel(BoardPanel boardPanel) {
		this.setOpaque(false);
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(this);
		this.add(newGameButton);
		this.boardPanel = boardPanel;
		this.checkmateStatus = 0;
		this.stalemateStatus = 0;
		gameOverLabel = new JLabel("");
		this.add(gameOverLabel);
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
	 * Not used.
	 * @param e -
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHints(RENDERING_HINTS);
		
		graphics2d.setColor(BACKGROUND_COLOR);
		graphics2d.fillRect(0, 0, width, height);

		graphics2d.setColor(Color.BLACK);
		if(checkmateStatus == 1)
			gameOverLabel.setText("White wins");
		else if(checkmateStatus == 2)
			gameOverLabel.setText("Black wins");
		else if(stalemateStatus != 0)
			gameOverLabel.setText("Draw by stalemate");
	}
	
	/**
	 * Not used.
	 * @param e -
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newGameButton) {
			boardPanel.setBoard();
			boardPanel.checkmateStatus = 0;
			boardPanel.stalemateStatus = 0;
			checkmateStatus = 0;
			stalemateStatus = 0;
			gameOverLabel.setText("");
			boardPanel.repaint();
		}
	}
	
	/**
	 * Checks for check-/stalemate. Called by <code>BoardPanel</code>.
	 */
	@Override
	public void gameIsOver() {
		this.checkmateStatus = boardPanel.checkmateStatus;
		this.stalemateStatus = boardPanel.stalemateStatus;
		this.repaint();
	}
}
