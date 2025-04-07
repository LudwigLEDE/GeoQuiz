import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the quiz game logic including loading questions,
 * tracking the current question, and advancing through the quiz.
 */
public class QuizGame {
    private List<Question> questions;
    private int currentQuestionIndex;

    /**
     * Creates a QuizGame with a specific question set.
     *
     * @param setName the identifier for the question set (e.g., "Set 1", "Set 2")
     */
    public QuizGame(String setName) {
        questions = new ArrayList<>();
        currentQuestionIndex = 0;
        loadQuestions(setName);
    }

    /**
     * Loads questions based on the given set name.
     * You can replace this with external loading (e.g., from a JSON file).
     *
     * @param setName the identifier of the question set to load
     */
    private void loadQuestions(String setName) {
        if ("Set 1".equals(setName)) {
            // Example Question Set 1: Text-based question with 4 text answers.
            List<AnswerOption> answers1 = new ArrayList<>();
            answers1.add(new AnswerOption("3", false));
            answers1.add(new AnswerOption("4", true));
            answers1.add(new AnswerOption("5", false));
            answers1.add(new AnswerOption("6", false));
            questions.add(new Question("What is 2 + 2?", answers1));

            // Add more questions for Set 1...
        } else if ("Set 2".equals(setName)) {
            // Example Question Set 2: Image-based question with 2 text answers.
            ImageIcon questionImage = new ImageIcon("images/question2.png"); // Ensure image exists
            List<AnswerOption> answers2 = new ArrayList<>();
            answers2.add(new AnswerOption("Answer A", false));
            answers2.add(new AnswerOption("Answer B", true));
            questions.add(new Question(questionImage, answers2));

            // Add more questions for Set 2...
        }
    }

    /**
     * Returns the current question.
     *
     * @return the current Question object, or null if no more questions.
     */
    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    /**
     * Advances to the next question.
     */
    public void nextQuestion() {
        currentQuestionIndex++;
    }

    /**
     * Resets the quiz to the beginning.
     */
    public void reset() {
        currentQuestionIndex = 0;
    }
}
