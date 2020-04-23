import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

/**
 * <code>Frame</code> class. This represents the game.
 *
 * @author Chris W. Bao, Ben C. Megan
 * @version 0.1.10
 * @since 4 APR 2020
 */
class Frame extends JFrame implements ComponentListener/*, MouseListener*/ {
	// CONSTANTS
	// TODO: make it automatically fullscreen,
	//  and add a button to toggle fullscreen/not fullscreen
	// TODO: draw one border around all the panels
	// Size of window to OS is (width, height + menu bar height)
	// Size of window to us is (width, height)
	final int MENU_BAR_HEIGHT = 22;
	final int MIN_WIDTH = 300;
	final int MIN_HEIGHT = 200;
	final int DEFAULT_WIDTH = 800;
	final int DEFAULT_HEIGHT = 500;
	final float IDEAL_PROPORTION = (float) DEFAULT_WIDTH / DEFAULT_HEIGHT;
	// Proportions within this distance from the ideal proportion are acceptable
	final float PROPORTION_BUFFER = 0.1f;
	
	// FIELDS
	Dimension size = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	InfoPanel infoPanel;  // General info panel
	BoardPanel boardPanel; // Chessboard panel
	PlayerPanel blackPanel; // Black player info panel
	PlayerPanel whitePanel; // White player info panel
	BackgroundPanel backgroundPanel; // Panel that displays background image
	
	// CONSTRUCTOR
	
	/**
	 * Constructor. Initiates all the <code>JPanels</code>.
	 */
	public Frame() {
		super("JChess");
		
		// Piece images
		Piece.loadImages(this);
		
		// Resizing stuff
		addComponentListener(this);
//		addMouseListener(this);
		
		// Panel stuff
		this.setLayout(null);
		
		infoPanel = new InfoPanel();
		infoPanel.setBackground(Color.GRAY);
		infoPanel.setBounds(0, 0, 150, 500);
		this.add(infoPanel);
		
		boardPanel = new BoardPanel();
		boardPanel.setBounds(150, 0, 500, 500);
		this.add(boardPanel);
		
		blackPanel = new PlayerPanel(false);
		blackPanel.setBackground(Color.BLACK);
		blackPanel.setBounds(650, 0, 150, 250);
		this.add(blackPanel);
		
		whitePanel = new PlayerPanel(true);
		whitePanel.setBackground(Color.WHITE);
		whitePanel.setBounds(650, 250, 150, 250);
		this.add(whitePanel);
		
		backgroundPanel = new BackgroundPanel();
		backgroundPanel.setBounds(0, 0, 800, 500);
		this.add(backgroundPanel);
		
		// Misc. setup
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT + MENU_BAR_HEIGHT));
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT + MENU_BAR_HEIGHT));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	// METHODS
	
	/**
	 * Resizes the panels after the <code>JFrame</code> instance gets resized.
	 */
	void resizePanels() {
		int width = size.width;
		int height = size.height;
        int effectiveWidth = size.width;
        int effectiveHeight = size.height;
		int horShift = 0;
		int verShift = 0;
		
		backgroundPanel.changeSize(width, height);
		backgroundPanel.setBounds(0, 0, width, height);
		
        // Portrait orientation
		if(effectiveWidth <= effectiveHeight) {
            // Check if proportions more than 0.2 away from ideal proportions
			if(Math.abs((float) height / width - IDEAL_PROPORTION) > 0.1) {
				if((float) height / width > IDEAL_PROPORTION) { // Too tall
					effectiveHeight = (int) ((IDEAL_PROPORTION + PROPORTION_BUFFER) * width);
					verShift = (height - effectiveHeight) / 2;
				} else {                                        // Too wide
					effectiveWidth = (int) (height / (IDEAL_PROPORTION - PROPORTION_BUFFER));
					horShift = (width - effectiveWidth) / 2;
				}
			}
			
			// Actual resizing of panels
            // excess length outside boardPanel
			int outsideBoardLength = effectiveHeight - effectiveWidth;
            // height of infoPanels and playerPanels
			int sidePanelLength = outsideBoardLength / 2;
            // width of playerPanels
			int playerPanelWidth = effectiveWidth / 2;
			infoPanel.changeSize(effectiveWidth, sidePanelLength);
			infoPanel.setBounds(horShift, verShift, effectiveWidth, sidePanelLength);
			boardPanel.changeLength(effectiveWidth);
			//noinspection SuspiciousNameCombination
			boardPanel.setBounds(horShift, sidePanelLength + verShift, effectiveWidth, effectiveWidth);
			blackPanel.changeSize(playerPanelWidth, sidePanelLength);
			blackPanel.setBounds(horShift, sidePanelLength + effectiveWidth + verShift,
					playerPanelWidth, sidePanelLength);
			whitePanel.changeSize(playerPanelWidth, sidePanelLength);
			whitePanel.setBounds(playerPanelWidth + horShift, sidePanelLength + effectiveWidth + verShift,
					playerPanelWidth, sidePanelLength);
		}
		
        // Landscape orientation
		else {
            // Check if proportions more than 0.1 away from ideal proportions
			if(Math.abs((float) width / height - IDEAL_PROPORTION) > 0.1) {
				if((float) width / height > IDEAL_PROPORTION) { // Too wide
					effectiveWidth = (int) ((IDEAL_PROPORTION + PROPORTION_BUFFER) * height);
					horShift = (width - effectiveWidth) / 2;
				} else {                                        // Too tall
					effectiveHeight = (int) (width / (IDEAL_PROPORTION - PROPORTION_BUFFER));
					verShift = (height - effectiveHeight) / 2;
				}
			}
			
            // Actual resizing of panels
            // excess length outside boardPanel
			int outsideBoardLength = effectiveWidth - effectiveHeight;
            // width of infoPanels and playerPanels
			int sidePanelLength = outsideBoardLength / 2;
            // height of playerPanels
			int playerPanelHeight = effectiveHeight / 2;
			infoPanel.changeSize(sidePanelLength, effectiveHeight);
			infoPanel.setBounds(horShift, verShift, sidePanelLength, effectiveHeight);
			boardPanel.changeLength(effectiveHeight);
			//noinspection SuspiciousNameCombination
			boardPanel.setBounds(sidePanelLength + horShift, verShift, effectiveHeight, effectiveHeight);
			blackPanel.changeSize(sidePanelLength, playerPanelHeight);
			blackPanel.setBounds(sidePanelLength + effectiveHeight + horShift, verShift,
					sidePanelLength, playerPanelHeight);
			whitePanel.changeSize(sidePanelLength, playerPanelHeight);
			whitePanel.setBounds(sidePanelLength + effectiveHeight + horShift, playerPanelHeight + verShift,
					sidePanelLength, playerPanelHeight);
		}
		// Redraw everything
		backgroundPanel.repaint();
		boardPanel.repaint();
		whitePanel.repaint();
		blackPanel.repaint();
		infoPanel.repaint();
	}
	
	// COMPONENTLISTER METHODS
	
	/**
	 * Changes the <code>size</code> field when the window gets resized
	 *
	 * @param e the event passed to this methods on resizing
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		size = e.getComponent().getSize();
		size.height -= MENU_BAR_HEIGHT;
		resizePanels();
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
	}
	
//	// MOUSELISTENER METHODS
//	@Override
//	public void mouseClicked(MouseEvent mouseEvent) {
//	}
//	
//	@Override
//	public void mousePressed(MouseEvent mouseEvent) {
//	}
//	
//	@Override
//	public void mouseReleased(MouseEvent mouseEvent) {
//	}
//	
//	@Override
//	public void mouseEntered(MouseEvent mouseEvent) {
//	}
//	
//	@Override
//	public void mouseExited(MouseEvent mouseEvent) {
//	}
}
