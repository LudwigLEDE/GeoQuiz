import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A main menu panel that lets the user select the quiz question set.
 */
public class MainMenuPanel extends JPanel {
    public MainMenuPanel(CardLayout cardLayout, JPanel container) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Select Quiz Question Set");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Button for Quiz Set 1
        JButton set1Button = new JButton("Quiz Set 1");
        set1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuizGame quizGame = new QuizGame("Set 1");
                QuizPanel quizPanel = new QuizPanel(quizGame);
                // Register the controller listener for answer selection
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
        });
        add(set1Button, gbc);

        // Button for Quiz Set 2
        gbc.gridx = 1;
        JButton set2Button = new JButton("Quiz Set 2");
        set2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuizGame quizGame = new QuizGame("Set 2");
                QuizPanel quizPanel = new QuizPanel(quizGame);
                // Register controller listener for answer selection
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
        });
        add(set2Button, gbc);
    }
}
