import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainMenuPanel extends JPanel {
    private String selectedSetName = null;
    private JLabel titleLabel;
    private JButton startButton;

    private CardLayout cardLayout;
    private JPanel container;

    public MainMenuPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        setLayout(new GridBagLayout());
        setBackground(AppColors.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label (centered)
        titleLabel = new JLabel("Select a Quiz Question Set", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(AppColors.COPY);
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Panel for selection buttons (quiz set buttons)
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        selectionPanel.setBackground(AppColors.BACKGROUND);
        JButton set1Button = new JButton("GeoQuiz Set 1");
        JButton set2Button = new JButton("GeoQuiz Set 2");
        styleButton(set1Button, AppColors.PRIMARY, AppColors.PRIMARY_CONTENT);
        styleButton(set2Button, AppColors.PRIMARY, AppColors.PRIMARY_CONTENT);
        selectionPanel.add(set1Button);
        selectionPanel.add(set2Button);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(selectionPanel, gbc);

        // "Start Round" button at the bottom (initially hidden)
        startButton = new JButton("Start Round");
        styleButton(startButton, AppColors.SECONDARY, AppColors.SECONDARY_CONTENT);
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setVisible(false);
        gbc.gridy = 2;
        gbc.gridwidth = 2;  //TODO: Button Größe ist nicht richitg
        add(startButton, gbc);

        // Action listeners for quiz set buttons:
        set1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedSetName = "GeoQuiz Set 1";
                titleLabel.setText("GeoQuiz Set 1 Selected");
                startButton.setVisible(true);
                System.out.println("Selected Quiz Set: " + selectedSetName);
            }
        });

        set2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedSetName = "GeoQuiz Set 2";
                titleLabel.setText("GeoQuiz Set 2 Selected");
                startButton.setVisible(true);
                System.out.println("Selected Quiz Set: " + selectedSetName);
            }
        });

        // Action listener for the "Start Round" button:
        startButton.addActionListener(new ActionListener() {
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
                    // Create QuizPanel with a callback to return to the main menu
                    QuizPanel quizPanel = new QuizPanel(new QuizGame(quizSet), new QuizFinishedListener() {
                        public void quizFinished() {
                            cardLayout.show(container, "MainMenu");
                        }
                    });

                    // Register controller events (if using controller input)
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

    // Helper method to style buttons with global colors.
    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(AppColors.BORDER));
        button.setFont(new Font("Arial", Font.BOLD, 18));
    }
}
