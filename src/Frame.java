import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <code>Frame</code> class. This represents the game.
 * @author Chris W. Bao, Ben Megan
 * @since 4 APR 2020
 * @version 0.1.5
 */
class Frame extends JFrame implements ComponentListener {
    // CONSTANTS
    // TODO: make it automatically fullscreen,
    //  and add a button to toggle fullscreen/not fullscreen
    // TODO: draw one border around all the panels
    // TODO: constrain proportions so it looks like
    //  "not absolute dog shit" when resized to extremes 
    // Size of window to OS is (width, height + menu bar height)
    // Size of window to us is (width, height)
    final int MENU_BAR_HEIGHT = 22;
    final int DEFAULT_WIDTH = 900;
    final int DEFAULT_HEIGHT = 500;

    // FIELDS
    Dimension size = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    InfoPanel   infoPanel;  // General info panel
    BoardPanel  boardPanel; // Chessboard panel
    PlayerPanel blackPanel; // Black player info panel
    PlayerPanel whitePanel; // White player info panel

    // CONSTRUCTOR
    /**
     * Constructor. Initiates all the <code>JPanels</code>.
     */
    Frame() {
        // Resizing stuff
        addComponentListener(this);
        
        // Panel stuff
        this.setLayout(null);
        
        infoPanel  = new InfoPanel();
        infoPanel.setBackground(Color.GRAY);
        infoPanel.setBounds(0, 0, 200, 500);
        this.add(infoPanel);
        
        boardPanel = new BoardPanel();
        boardPanel.setBackground(Color.PINK);
        boardPanel.setBounds(200, 0, 500, 500);
        this.add(boardPanel);
    
        blackPanel = new PlayerPanel();
        blackPanel.setBackground(Color.BLACK);
        blackPanel.setBounds(700, 0, 200, 250);
        this.add(blackPanel);
        
        whitePanel = new PlayerPanel();
        whitePanel.setBackground(Color.WHITE);
        whitePanel.setBounds(700, 250, 200, 250);
        this.add(whitePanel);
        
        // Misc. setup
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT-MENU_BAR_HEIGHT));
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
        if(width <= height) { // Portrait orientation
            int outsideBoardLength = height-width;      // excess length outside boardPanel
            int sidePanelLength = outsideBoardLength/2; // height of infoPanels and playerPanels
            int playerPanelWidth = width/2;             // width of playerPanels
            infoPanel.changeSize(width, sidePanelLength);
            infoPanel.setBounds(0, 0, width, sidePanelLength);
            boardPanel.changeLength(width);
            //noinspection SuspiciousNameCombination
            boardPanel.setBounds(0, sidePanelLength, width, width);
            blackPanel.changeSize(playerPanelWidth, sidePanelLength);
            blackPanel.setBounds(0, sidePanelLength+width,
                    playerPanelWidth, sidePanelLength);
            whitePanel.changeSize(playerPanelWidth, sidePanelLength);
            whitePanel.setBounds(playerPanelWidth, sidePanelLength+width,
                    playerPanelWidth, sidePanelLength);
        } else {              // Landscape orientation
            int outsideBoardLength = width-height;      // excess length outside boardPanel
            int sidePanelLength = outsideBoardLength/2; // width of infoPanels and playerPanels
            int playerPanelHeight = height/2;           // height of playerPanels
            infoPanel.changeSize(sidePanelLength, height);
            infoPanel.setBounds(0, 0, sidePanelLength, height);
            boardPanel.changeLength(height);
            //noinspection SuspiciousNameCombination
            boardPanel.setBounds(sidePanelLength, 0, height, height);
            blackPanel.changeSize(sidePanelLength, playerPanelHeight);
            blackPanel.setBounds(sidePanelLength+height, 0,
                    sidePanelLength, playerPanelHeight);
            whitePanel.changeSize(sidePanelLength, playerPanelHeight);
            whitePanel.setBounds(sidePanelLength+height, playerPanelHeight,
                    sidePanelLength, playerPanelHeight);
        }
    }
    
    // COMPONENTLISTER METHODS
    /**
     * Changes the <code>size</code> field when the window gets resized
     * @param e the event passed to this methods on resizing
     */
    @Override
    public void componentResized(ComponentEvent e) {
        size = e.getComponent().getSize();
        size.height -= MENU_BAR_HEIGHT;
        resizePanels();
    }
    
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
}
