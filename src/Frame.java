import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Frame class. This represents the game.
 * @author Chris W. Bao, Ben Megan
 * @since 28 MAR 2020
 * @version 0.1.4
 */
class Frame extends JFrame implements ComponentListener {
    // CONSTANTS
    // TODO: make it automatically fullscreen,
    //  and add a button to toggle fullscreen/not fullscreen
    final int MENU_BAR_HEIGHT = 22;
    final int DEFAULT_WIDTH = 500;
    final int DEFAULT_HEIGHT = 800 + MENU_BAR_HEIGHT;

    // FIELDS
    Dimension size = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    InfoPanel   infoPanel;  // General info panel
    PlayerPanel blackPanel; // Black player info panel
    BoardPanel  boardPanel; // Chessboard panel
    PlayerPanel whitePanel; // White player info panel
    Image       background; // Background for when the app is in fullscreen

    // CONSTRUCTOR
    /**
     * Constructor. Initiates all the JPanels.
     */
    Frame() {
        // Resizing stuff
        addComponentListener(this);
        
        // Panel stuff
        this.setLayout(null);
        
        infoPanel  = new InfoPanel();
        infoPanel.setBackground(Color.GRAY);
        infoPanel.setBounds(0, 0, 500, 100);
        this.add(infoPanel);
    
        blackPanel = new PlayerPanel();
        blackPanel.setBackground(Color.BLACK);
        blackPanel.setBounds(0, 100, 500, 100);
        this.add(blackPanel);
        
        boardPanel = new BoardPanel();
        boardPanel.setBackground(Color.PINK);
        boardPanel.setBounds(0, 200, 500, 500);
        this.add(boardPanel);
        
        whitePanel = new PlayerPanel();
        whitePanel.setBackground(Color.WHITE);
        whitePanel.setBounds(0, 700, 500, 100);
        this.add(whitePanel);
        
        // Misc. setup
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    // METHODS
    /**
     * Resizes the panels after the JFrame instance gets resized.
     */
    void resizePanels() {
        //TODO: implement
    }
    
    // COMPONENTLISTER METHODS
    /**
     * Changes the [size] field when the window gets resized
     * @param e the event passed to this methods on resizing
     */
    @Override
    public void componentResized(ComponentEvent e) {
        size = e.getComponent().getSize();
        resizePanels();
    }
    
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
}
