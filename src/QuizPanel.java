import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * A panel that displays a quiz question along with its answer options.
 * It includes a counter for the number of correct answers and, when finished,
 * shows a button to return to the main menu.
 */
public class QuizPanel extends JPanel {
    private QuizGame quizGame;
    private JLabel questionLabel;
    private JLabel scoreLabel; // Label to display the score
    private JLabel questionImageLabel;
    private JPanel answersPanel;
    private int correctCount; // Counter for correct answers
    private QuizFinishedListener finishedListener; // Callback for when the quiz ends

    /**
     * Constructs the QuizPanel using the provided QuizGame instance and a finished callback.
     *
     * @param quizGame         the game manager containing quiz questions
     * @param finishedListener the callback to call when the quiz is finished
     */
    public QuizPanel(QuizGame quizGame, QuizFinishedListener finishedListener) {
        this.quizGame = quizGame;
        this.finishedListener = finishedListener;
        correctCount = 0; // initialize counter to zero
        setLayout(new BorderLayout());
        initializeComponents();
        displayCurrentQuestion();
    }

    /**
     * Initializes UI components including the question text, image, score label, and answers panel.
     */
    private void initializeComponents() {
        // Create the question label
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Create the score label
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a top panel to hold both question text and score
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(questionLabel, BorderLayout.CENTER);
        topPanel.add(scoreLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Create the question image label
        questionImageLabel = new JLabel("", SwingConstants.CENTER);
        add(questionImageLabel, BorderLayout.CENTER);

        // Create the answers panel with GridLayout (supports 2 or 4 answers)
        answersPanel = new JPanel();
        answersPanel.setLayout(new GridLayout(0, 2, 10, 10));
        add(answersPanel, BorderLayout.SOUTH);
    }

    /**
     * Displays the current question and updates the UI accordingly.
     */
    public void displayCurrentQuestion() {
        Question question = quizGame.getCurrentQuestion();
        if (question == null) {
            // No more questions: show final score and a return button
            removeAll();
            JPanel finishPanel = new JPanel(new BorderLayout());
            JLabel finishLabel = new JLabel("Quiz Completed! Final Score: " + correctCount, SwingConstants.CENTER);
            finishLabel.setFont(new Font("Arial", Font.BOLD, 20));
            finishPanel.add(finishLabel, BorderLayout.CENTER);

            JButton returnButton = new JButton("Return to Main Menu");
            returnButton.setFont(new Font("Arial", Font.BOLD, 18));
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Notify that the quiz is finished
                    finishedListener.quizFinished();
                }
            });
            finishPanel.add(returnButton, BorderLayout.SOUTH);

            add(finishPanel, BorderLayout.CENTER);
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
     * Handles the selection of an answer and updates the score if correct.
     *
     * @param selectedIndex the index of the selected answer
     */
    private void handleAnswerSelection(int selectedIndex) {
        Question currentQuestion = quizGame.getCurrentQuestion();
        if (selectedIndex >= currentQuestion.getAnswerOptions().size()) {
            return;
        }
        AnswerOption selectedAnswer = currentQuestion.getAnswerOptions().get(selectedIndex);
        if (selectedAnswer.isCorrect()) {
            JOptionPane.showMessageDialog(this, "Correct!");
            correctCount++;
            scoreLabel.setText("Score: " + correctCount);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect!");
        }
        quizGame.nextQuestion();
        displayCurrentQuestion();
    }

    /**
     * Public method to allow external selection of an answer (e.g., via controller input).
     *
     * @param index the index of the answer to select
     */
    public void selectAnswer(int index) {
        handleAnswerSelection(index);
    }
}
