import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A main menu panel that allows the user to select a quiz set.
 * When a set is selected, it logs the selection, updates the title,
 * and shows a "Start Round" button that loads the quiz questions.
 */
public class MainMenuPanel extends JPanel {
    private String selectedSetName = null;
    private JLabel titleLabel;
    private JButton startButton;
    private CardLayout cardLayout;
    private JPanel container;

    public MainMenuPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        setLayout(new BorderLayout());

        // Title label in the center
        titleLabel = new JLabel("Select a Quiz Question Set", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(titleLabel, BorderLayout.CENTER);

        // Panel for selection buttons at the top
        JPanel selectionPanel = new JPanel(new FlowLayout());
        JButton set1Button = new JButton("GeoQuiz Set 1");
        JButton set2Button = new JButton("GeoQuiz Set 2");
        selectionPanel.add(set1Button);
        selectionPanel.add(set2Button);
        add(selectionPanel, BorderLayout.NORTH);

        // Set button action listeners
        set1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSetName = "GeoQuiz Set 1";
                titleLabel.setText("GeoQuiz Set 1 Selected");
                startButton.setVisible(true);
                System.out.println("Selected Quiz Set: " + selectedSetName);
            }
        });

        set2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSetName = "GeoQuiz Set 2";
                titleLabel.setText("GeoQuiz Set 2 Selected");
                startButton.setVisible(true);
                System.out.println("Selected Quiz Set: " + selectedSetName);
            }
        });

        // "Start Round" button at the bottom center
        startButton = new JButton("Start Round");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setVisible(false);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(startButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Start button listener: load the quiz set and switch panels
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSetName != null) {
                    String filePath = "";
                    if (selectedSetName.equals("GeoQuiz Set 1")) {
                        filePath = "data/quizsets/geoquiz_set1.json";
                    } else if (selectedSetName.equals("GeoQuiz Set 2")) {
                        filePath = "data/quizsets/geoquiz_set2.json";
                    }
                    QuizSet quizSet = QuizSetLoader.loadQuizSet(filePath);
                    if (quizSet == null) {
                        System.out.println("Failed to load quiz set from: " + filePath);
                        return;
                    }
                    // Create a QuizPanel and pass a finished callback that shows the main menu again.
                    QuizPanel quizPanel = new QuizPanel( new QuizGame(quizSet), new QuizFinishedListener() {
                        @Override
                        public void quizFinished() {
                            // Remove the quiz panel and show the main menu
                            cardLayout.show(container, "MainMenu");
                        }
                    });

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
