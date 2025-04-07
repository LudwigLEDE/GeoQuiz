import javax.swing.*;
import java.awt.*;

/**
 * Main is the entry point of the quiz game application.
 * It creates a full-screen window with a main menu for selecting the quiz set.
 */
public class Main {
    public static void main(String[] args) {
        // Create a full-screen window
        JFrame frame = new JFrame("Quiz Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0, 0, screenSize.width, screenSize.height);

        // Use CardLayout to swap between main menu and quiz panels
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        // Create and add the main menu panel
        MainMenuPanel mainMenuPanel = new MainMenuPanel(cardLayout, container);
        container.add(mainMenuPanel, "MainMenu");

        frame.add(container);
        frame.setVisible(true);
    }
}
