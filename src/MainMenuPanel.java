import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A main menu panel that allows the user to select a quiz question set.
 * The menu displays a central title that updates after a set is selected,
 * and a "Start Round" button appears at the bottom center.
 */
public class MainMenuPanel extends JPanel {
    private String selectedSetName = null;
    private JLabel titleLabel;
    private JButton startButton;
    private CardLayout cardLayout;
    private JPanel container;

    /**
     * Constructs the main menu.
     *
     * @param cardLayout the CardLayout used to swap between panels
     * @param container  the container panel using the CardLayout
     */
    public MainMenuPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        setLayout(new BorderLayout());

        // Create title label in the center
        titleLabel = new JLabel("Select a Quiz Question Set", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(titleLabel, BorderLayout.CENTER);

        // Create a panel for quiz set selection buttons at the top
        JPanel selectionPanel = new JPanel(new FlowLayout());
        JButton set1Button = new JButton("Quiz Set 1");
        JButton set2Button = new JButton("Quiz Set 2");
        selectionPanel.add(set1Button);
        selectionPanel.add(set2Button);
        add(selectionPanel, BorderLayout.NORTH);

        // Add action listeners for set buttons
        set1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSetName = "Set 1";
                titleLabel.setText("Quiz Set 1 Selected");
                startButton.setVisible(true);
            }
        });

        set2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSetName = "Set 2";
                titleLabel.setText("Quiz Set 2 Selected");
                startButton.setVisible(true);
            }
        });

        // Create "Start Round" button at the bottom center (initially hidden)
        startButton = new JButton("Start Round");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setVisible(false);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(startButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Start button listener: create quiz panel and switch to it
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSetName != null) {
                    // Create the quiz game using the selected set
                    QuizGame quizGame = new QuizGame(selectedSetName);
                    QuizPanel quizPanel = new QuizPanel(quizGame);

                    // Register controller events for answer selection
                    AutomatenController.getInstance().addListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            int keyCode = e.getKeyCode();
                            if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_5) {
                                int selectedIndex = keyCode - KeyEvent.VK_0;
                                quizPanel.selectAnswer(selectedIndex);
                            }
                        }
                    });

                    container.add(quizPanel, "QuizPanel");
                    cardLayout.show(container, "QuizPanel");
                }
            }
        });
    }
}
