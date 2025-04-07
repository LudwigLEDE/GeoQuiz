import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A panel that displays a quiz question and its answer options using GridBagLayout.
 * The answer options are randomized each time a question is displayed.
 * When an answer is selected, the buttons are colored (green for correct, red for incorrect)
 * in a game-show style, then after a delay, the next question is loaded.
 * When finished, a "Return to Main Menu" button appears.
 */
public class QuizPanel extends JPanel {
    private QuizGame quizGame;
    private JLabel scoreLabel;
    private JLabel questionLabel;
    private JLabel questionImageLabel;
    private JPanel answersPanel;
    private int correctCount;
    private List<JButton> answerButtons;
    // New list to hold the randomized answer options for the current question
    private List<AnswerOption> currentAnswerOptions;
    private QuizFinishedListener finishedListener;

    /**
     * Constructs the QuizPanel.
     *
     * @param quizGame         the game manager containing quiz questions
     * @param finishedListener callback to be invoked when the quiz is finished
     */
    public QuizPanel(QuizGame quizGame, QuizFinishedListener finishedListener) {
        this.quizGame = quizGame;
        this.finishedListener = finishedListener;
        correctCount = 0;
        answerButtons = new ArrayList<>();
        setLayout(new GridBagLayout());
        setBackground(AppColors.BACKGROUND);
        initializeComponents();
        displayCurrentQuestion();
    }

    /**
     * Initializes UI components using GridBagLayout.
     */
    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Score label at the top, spanning two columns.
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(AppColors.COPY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(scoreLabel, gbc);

        // Question label
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 22));
        questionLabel.setForeground(AppColors.COPY);
        gbc.gridy = 1;
        add(questionLabel, gbc);

        // Question image label
        questionImageLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 2;
        add(questionImageLabel, gbc);

        // Answers panel using GridBagLayout for answer buttons.
        answersPanel = new JPanel(new GridBagLayout());
        answersPanel.setBackground(AppColors.FOREGROUND);
        gbc.gridy = 3;
        add(answersPanel, gbc);
    }

    /**
     * Displays the current question. Randomizes the answer order for each question.
     */
    public void displayCurrentQuestion() {
        Question question = quizGame.getCurrentQuestion();
        if (question == null) {
            // No more questions: show final score and a return button.
            removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel finishLabel = new JLabel("Quiz Completed! Final Score: " + correctCount, SwingConstants.CENTER);
            finishLabel.setFont(new Font("Arial", Font.BOLD, 24));
            finishLabel.setForeground(AppColors.COPY);
            add(finishLabel, gbc);

            gbc.gridy = 1;
            JButton returnButton = new JButton("Return to Main Menu");
            returnButton.setFont(new Font("Arial", Font.BOLD, 20));
            returnButton.setBackground(AppColors.SECONDARY);
            returnButton.setForeground(AppColors.SECONDARY_CONTENT);
            returnButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    finishedListener.quizFinished();
                }
            });
            add(returnButton, gbc);

            revalidate();
            repaint();
            return;
        }

        // Set question text and image.
        questionLabel.setText(question.getQuestionText() != null ? question.getQuestionText() : "");
        questionImageLabel.setIcon(question.getQuestionImage());

        // Randomize the answer options.
        List<AnswerOption> answers = question.getAnswerOptions();
        currentAnswerOptions = new ArrayList<>(answers);
        Collections.shuffle(currentAnswerOptions);

        // Clear previous answers and build new answer buttons.
        answersPanel.removeAll();
        answerButtons.clear();

        int numAnswers = currentAnswerOptions.size();
        GridBagConstraints gbcAns = new GridBagConstraints();
        gbcAns.insets = new Insets(5, 5, 5, 5);
        gbcAns.fill = GridBagConstraints.BOTH;
        for (int i = 0; i < numAnswers; i++) {
            AnswerOption answer = currentAnswerOptions.get(i);
            JButton btn = new JButton();
            btn.setFont(new Font("Arial", Font.PLAIN, 18));
            // Default button style using primary light background.
            btn.setBackground(AppColors.PRIMARY_LIGHT);
            btn.setForeground(AppColors.PRIMARY_CONTENT);
            if (answer.getText() != null) {
                btn.setText(answer.getText());
            }
            if (answer.getImage() != null) {
                btn.setIcon(answer.getImage());
            }
            final int index = i;
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleAnswerSelection(index);
                }
            });
            answerButtons.add(btn);
            gbcAns.gridx = i % 2;
            gbcAns.gridy = i / 2;
            answersPanel.add(btn, gbcAns);
        }
        revalidate();
        repaint();
    }

    /**
     * Handles answer selection: disables buttons, sets colors based on correctness,
     * and after a delay advances to the next question.
     *
     * @param selectedIndex the index of the selected answer from the randomized list
     */
    private void handleAnswerSelection(int selectedIndex) {
        if (selectedIndex >= currentAnswerOptions.size()) {
            return;
        }
        // Disable all answer buttons.
        for (JButton btn : answerButtons) {
            btn.setEnabled(false);
        }
        AnswerOption selectedAnswer = currentAnswerOptions.get(selectedIndex);
        int correctIndex = -1;
        for (int i = 0; i < currentAnswerOptions.size(); i++) {
            if (currentAnswerOptions.get(i).isCorrect()) {
                correctIndex = i;
                break;
            }
        }
        // Apply color feedback: correct button green, others red.
        if (selectedAnswer.isCorrect()) {
            answerButtons.get(selectedIndex).setBackground(AppColors.SUCCESS);
            answerButtons.get(selectedIndex).setForeground(AppColors.SUCCESS_CONTENT);
            for (int i = 0; i < answerButtons.size(); i++) {
                if (i != selectedIndex) {
                    answerButtons.get(i).setBackground(AppColors.ERROR);
                    answerButtons.get(i).setForeground(AppColors.ERROR_CONTENT);
                }
            }
            correctCount++;
            scoreLabel.setText("Score: " + correctCount);
        } else {
            answerButtons.get(selectedIndex).setBackground(AppColors.ERROR);
            answerButtons.get(selectedIndex).setForeground(AppColors.ERROR_CONTENT);
            if (correctIndex != -1) {
                answerButtons.get(correctIndex).setBackground(AppColors.SUCCESS);
                answerButtons.get(correctIndex).setForeground(AppColors.SUCCESS_CONTENT);
            }
            for (int i = 0; i < answerButtons.size(); i++) {
                if (i != selectedIndex && i != correctIndex) {
                    answerButtons.get(i).setBackground(AppColors.ERROR);
                    answerButtons.get(i).setForeground(AppColors.ERROR_CONTENT);
                }
            }
        }
        // Delay transition to the next question.
        Timer timer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quizGame.nextQuestion();
                displayCurrentQuestion();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Allows external components (e.g., controller input) to select an answer.
     *
     * @param index the index of the answer to select from the randomized list
     */
    public void selectAnswer(int index) {
        handleAnswerSelection(index);
    }
}
