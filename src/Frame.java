import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Frame class. This represents the game.
 * @author Chris W. Bao, Ben Megan
 * @since 28 MAR 2020
 * @version 0.1.3
 */
class Frame extends JFrame implements ComponentListener {
    // CONSTANTS
    final int DEFAULT_WIDTH = 800;
    final int DEFAULT_HEIGHT = 800;
    Dimension size = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    // FIELDS
    BoardPanel boardPanel;  // Chessboard panel
    InfoPanel infoPanel;    // General info panel
    PlayerPanel blackPanel; // Black player info panel
    PlayerPanel whitePanel; // White player info panel

    // CONSTRUCTOR
    /**
     * Constructor. Initiates all the JPanels.
     */
    Frame() {
        // Resizing stuff
        addComponentListener(this);
        
        // Panel stuff
        boardPanel = new BoardPanel();
        boardPanel.setBackground(Color.PINK);
        infoPanel  = new InfoPanel();
        infoPanel.setBackground(Color.GRAY);
        blackPanel = new PlayerPanel();
        blackPanel.setBackground(Color.BLACK);
        whitePanel = new PlayerPanel();
        whitePanel.setBackground(Color.WHITE);
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(blackPanel, BorderLayout.WEST);
        this.add(whitePanel, BorderLayout.EAST);
        
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
