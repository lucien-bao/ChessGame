import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <code>InfoPanel</code> class. This displays information relevant to the game.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.9.9
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
	final double BUTTON_WIDTH_RATIO = 1;
	final double BUTTON_HEIGHT_RATIO = 0.05;
	
	// FIELDS //
	int width, height;
	int gameStatus; // 0 -> playing, 1 -> white, 2 -> black, -1 -> stalemate
	JButton newGameButton;
	JButton undoMoveButton;
	JButton redoMoveButton;
	BoardPanel boardPanel;
	JLabel gameOverLabel;
	
	// CONSTRUCTOR //
	
	/**
	 * Default constructor. Recieves a <code>BoardPanel</code> instance to access info about the game.
	 * @param boardPanel The <code>BoardPanel</code> instance from <code>Frame</code>.
	 */
	InfoPanel(BoardPanel boardPanel) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.setOpaque(false);
		this.boardPanel = boardPanel;
		this.gameStatus = 0;
		
		this.newGameButton = new JButton("New Game");
		newGameButton.addActionListener(this);

		this.undoMoveButton = new JButton("Undo Move");
		undoMoveButton.addActionListener(this);

		this.redoMoveButton = new JButton("Redo Move");
		redoMoveButton.addActionListener(this);

		setButtonSize();
		this.add(newGameButton);
		this.add(undoMoveButton);
		this.add(redoMoveButton);

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
		setButtonSize();
	}

	void setButtonSize() {
		newGameButton.setMinimumSize(new Dimension((int)(width * BUTTON_WIDTH_RATIO), (int)(height * BUTTON_HEIGHT_RATIO)));
		newGameButton.setMaximumSize(new Dimension((int)(width * BUTTON_WIDTH_RATIO), (int)(height * BUTTON_HEIGHT_RATIO)));

		undoMoveButton.setMinimumSize(new Dimension((int)(width * BUTTON_WIDTH_RATIO), (int)(height * BUTTON_HEIGHT_RATIO)));
		undoMoveButton.setMaximumSize(new Dimension((int)(width * BUTTON_WIDTH_RATIO), (int)(height * BUTTON_HEIGHT_RATIO)));

		redoMoveButton.setMinimumSize(new Dimension((int)(width * BUTTON_WIDTH_RATIO), (int)(height * BUTTON_HEIGHT_RATIO)));
		redoMoveButton.setMaximumSize(new Dimension((int)(width * BUTTON_WIDTH_RATIO), (int)(height * BUTTON_HEIGHT_RATIO)));
	}
	
	/**
	 * Redraws stuff.
	 * @param graphics The <code>Graphics</code> instance used to draw.
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHints(RENDERING_HINTS);
		
		graphics2d.setColor(BACKGROUND_COLOR);
		graphics2d.fillRect(0, 0, width, height);

		graphics2d.setColor(Color.BLACK);
		if(gameStatus == 1)
			gameOverLabel.setText("White wins");
		else if(gameStatus == 2)
			gameOverLabel.setText("Black wins");
		else if(gameStatus == -1)
			gameOverLabel.setText("Draw by stalemate");
	}
	
	/**
	 * Listens for actions.
	 * @param e The <code>ActionEvent</code> passed from the event thread.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newGameButton) {
			boardPanel.setBoard();
			boardPanel.gameStatus = 0;
			this.gameStatus = 0;
			this.gameOverLabel.setText("");
		} else if(e.getSource() == undoMoveButton) {
			if(boardPanel.doneMoveStack.size() > 0)
				boardPanel.undoMove();
		} else if(e.getSource() == redoMoveButton) {
			if(boardPanel.undoneMoveStack.size() > 0) {
				boardPanel.redoMove();
			}
		}
		boardPanel.repaint();
	}
	
	/**
	 * Checks for check-/stalemate. Called by <code>BoardPanel</code>.
	 */
	@Override
	public void gameIsOver() {
		this.gameStatus = boardPanel.gameStatus;
		this.repaint();
	}
}
