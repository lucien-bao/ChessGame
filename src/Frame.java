import javax.swing.*;
import java.awt.*;

/**
 * Frame class. This represents the game.
 * @author Chris W. Bao, Ben Megan
 * @since 28 MAR 2020
 * @version 0.1.1
 */
class Frame extends JFrame {
    // CONSTANTS
    final int WIDTH  = 800;
    final int HEIGHT = 800;

    // FIELDS
    BoardPanel boardPanel;
    InfoPanel infoPanel;
    PlayerPanel blackPanel;
    PlayerPanel whitePanel;

    // CONSTRUCTORS
    Frame() {
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
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    // METHODS
}
