import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <code>Frame</code> class. This represents the game.
 * @author Chris W. Bao, Ben Megan
 * @since 4 APR 2020
 * @version 0.1.7
 */
class Frame extends JFrame implements ComponentListener, MouseListener {
    // CONSTANTS
    // TODO: make it automatically fullscreen,
    //  and add a button to toggle fullscreen/not fullscreen
    // TODO: draw one border around all the panels
    // TODO: constrain proportions so it looks like
    //  "not absolute dog shit" when resized to extremes 
    // Size of window to OS is (width, height + menu bar height)
    // Size of window to us is (width, height)
    final int MENU_BAR_HEIGHT = 22;
    final int DEFAULT_WIDTH = 800;
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
        super("JChess");
        
        // Resizing stuff
        addComponentListener(this);
        addMouseListener(this);
        
        // Panel stuff
        this.setLayout(null);
        
        infoPanel  = new InfoPanel();
        infoPanel.setBackground(Color.GRAY);
        infoPanel.setBounds(0, 0, 150, 500);
        this.add(infoPanel);
        
        boardPanel = new BoardPanel();
        boardPanel.setBackground(Color.PINK);
        boardPanel.setBounds(150, 0, 500, 500);
        this.add(boardPanel);
    
        blackPanel = new PlayerPanel();
        blackPanel.setBackground(Color.BLACK);
        blackPanel.setBounds(650, 0, 150, 250);
        this.add(blackPanel);
        
        whitePanel = new PlayerPanel();
        whitePanel.setBackground(Color.WHITE);
        whitePanel.setBounds(650, 250, 150, 250);
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
        int width    = size.width;
        int height   = size.height;
        int horShift = 0;
        int verShift = 0;
        int effectiveWidth  = width;
        int effectiveHeight = height;
        if(effectiveWidth <= effectiveHeight) { // Portrait orientation
            if(Math.abs((float)height / width - 1.6) > 0.2) { // proportions are bad
                if((float)height / width > 1.6) { // too tall
                    effectiveHeight = (int)(1.8 * width);
                    verShift = (height - effectiveHeight) / 2;
                }
                else { // too wide
                    effectiveWidth  = (int)(height / 1.8);
                    horShift = (width - effectiveWidth) / 2;
                }
            }
            int outsideBoardLength = effectiveHeight-effectiveWidth;      // excess length outside boardPanel
            int sidePanelLength = outsideBoardLength/2; // height of infoPanels and playerPanels
            int playerPanelWidth = effectiveWidth/2;             // width of playerPanels
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
        } else {              // Landscape orientation
            if(Math.abs((float)width / height - 1.6) > 0.2) { // proportions are bad
                if((float)width / height > 1.6) { // too wide
                    effectiveWidth  = (int)(1.8 * height);
                    horShift = (width - effectiveWidth) / 2;
                }
                else { // too tall
                    effectiveHeight = (int)(width / 1.8);
                    verShift = (height - effectiveHeight) / 2;
                }
            }
            int outsideBoardLength = effectiveWidth-effectiveHeight;      // excess length outside boardPanel
            int sidePanelLength = outsideBoardLength/2; // width of infoPanels and playerPanels
            int playerPanelHeight = effectiveHeight/2;           // height of playerPanels
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
        boardPanel.repaint(); // redraws everything
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

    // MOUSELISTENER METHODS
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println(size.width + ", " + size.height);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}
