import javax.swing.*;
import java.awt.*;

/**
 * Ui creates a secondary window with simple panels and buttons.
 * This could serve as a menu, options panel, or for displaying game information.
 */
public class Ui extends JFrame {
    public Ui() {
        // Set the title and default close operation
        super("Secondary UI");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Use GridBagLayout for flexible component placement
        setLayout(new GridBagLayout());
        GridBagConstraints ugbc = new GridBagConstraints();

        // Create and add a panel for questions or instructions
        JPanel questionPanel = new JPanel();
        questionPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        add(questionPanel);

        // Configure constraints and add answer buttons panel
        ugbc.gridy = 1;
        JPanel answerButtonsPanel = new JPanel(new GridBagLayout());
        answerButtonsPanel.setBorder(BorderFactory.createTitledBorder("Answers"));

        // Create buttons for possible answers
        JButton answer1 = new JButton("1");
        JButton answer2 = new JButton("2");
        JButton answer3 = new JButton("3");
        JButton answer4 = new JButton("4");

        // Place the buttons using GridBagConstraints
        GridBagConstraints abp = new GridBagConstraints();
        answerButtonsPanel.add(answer1, abp);
        abp.gridx = 1;
        answerButtonsPanel.add(answer2, abp);
        abp.gridy = 1;
        answerButtonsPanel.add(answer4, abp);
        abp.gridx = 0;
        answerButtonsPanel.add(answer3, abp);

        add(answerButtonsPanel);

        // Set window size and make visible
        setSize(300, 200);
        setVisible(true);
    }
}
