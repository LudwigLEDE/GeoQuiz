import java.util.List;

/**
 * Manages the quiz game logic.
 */
public class QuizGame {
    private List<Question> questions;
    private int currentQuestionIndex;

    /**
     * Constructs a QuizGame using the provided QuizSet.
     *
     * @param quizSet the loaded quiz set containing questions
     */
    public QuizGame(QuizSet quizSet) {
        this.questions = quizSet.getQuestions();
        this.currentQuestionIndex = 0;
    }

    /**
     * Returns the current question.
     *
     * @return the current Question or null if finished.
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
