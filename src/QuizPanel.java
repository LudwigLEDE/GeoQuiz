import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * A panel that displays a quiz question and its answer options.
 * Supports both text and image-based questions and answers.
 */
public class QuizPanel extends JPanel {
    private QuizGame quizGame;
    private JLabel questionLabel;
    private JLabel questionImageLabel;
    private JPanel answersPanel;

    /**
     * Constructs the QuizPanel with a reference to the QuizGame.
     *
     * @param quizGame the game manager containing quiz questions
     */
    public QuizPanel(QuizGame quizGame) {
        this.quizGame = quizGame;
        setLayout(new BorderLayout());
        initializeComponents();
        displayCurrentQuestion();
    }

    /**
     * Initializes UI components.
     */
    private void initializeComponents() {
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        questionImageLabel = new JLabel("", SwingConstants.CENTER);
        add(questionImageLabel, BorderLayout.CENTER);

        answersPanel = new JPanel();
        answersPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Supports 2 or 4 answers
        add(answersPanel, BorderLayout.SOUTH);
    }

    /**
     * Displays the current question on the panel.
     */
    public void displayCurrentQuestion() {
        Question question = quizGame.getCurrentQuestion();
        if (question == null) {
            // End of quiz message
            removeAll();
            add(new JLabel("Quiz Completed!", SwingConstants.CENTER), BorderLayout.CENTER);
            revalidate();
            repaint();
            return;
        }

        // Display question text (if available)
        questionLabel.setText(question.getQuestionText() != null ? question.getQuestionText() : "");

        // Display question image (if available)
        questionImageLabel.setIcon(question.getQuestionImage());

        // Clear previous answers and add new answer buttons
        answersPanel.removeAll();
        List<AnswerOption> answers = question.getAnswerOptions();
        for (int i = 0; i < answers.size(); i++) {
            final int index = i;
            AnswerOption answer = answers.get(i);
            JButton answerButton = new JButton();
            if (answer.getText() != null) {
                answerButton.setText(answer.getText());
            }
            if (answer.getImage() != null) {
                answerButton.setIcon(answer.getImage());
            }
            answerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleAnswerSelection(index);
                }
            });
            answersPanel.add(answerButton);
        }

        revalidate();
        repaint();
    }

    /**
     * Handles the event when an answer is selected.
     *
     * @param selectedIndex the index of the selected answer
     */
    private void handleAnswerSelection(int selectedIndex) {
        Question currentQuestion = quizGame.getCurrentQuestion();
        if (selectedIndex >= currentQuestion.getAnswerOptions().size()) {
            // Ignore if the selected index is out of range
            return;
        }
        AnswerOption selectedAnswer = currentQuestion.getAnswerOptions().get(selectedIndex);
        if (selectedAnswer.isCorrect()) {
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect!");
        }
        quizGame.nextQuestion();
        displayCurrentQuestion();
    }

    /**
     * Public method to allow selection of an answer via the controller.
     *
     * @param index the index of the answer to select
     */
    public void selectAnswer(int index) {
        handleAnswerSelection(index);
    }
}
