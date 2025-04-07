import javax.swing.ImageIcon;
import java.util.List;

/**
 * Represents a quiz question.
 * A question can have a text prompt, an image, or both,
 * along with a list of answer options.
 */
public class Question {
    private String questionText;
    private ImageIcon questionImage;
    private List<AnswerOption> answerOptions;

    /**
     * Constructs a question with text, an image, and answers.
     *
     * @param questionText the text of the question (may be null)
     * @param questionImage the image associated with the question (may be null)
     * @param answerOptions the list of answer options
     */
    public Question(String questionText, ImageIcon questionImage, List<AnswerOption> answerOptions) {
        this.questionText = questionText;
        this.questionImage = questionImage;
        this.answerOptions = answerOptions;
    }

    /**
     * Constructs a question with text only.
     *
     * @param questionText the text of the question
     * @param answerOptions the list of answer options
     */
    public Question(String questionText, List<AnswerOption> answerOptions) {
        this(questionText, null, answerOptions);
    }

    /**
     * Constructs a question with an image only.
     *
     * @param questionImage the image for the question
     * @param answerOptions the list of answer options
     */
    public Question(ImageIcon questionImage, List<AnswerOption> answerOptions) {
        this(null, questionImage, answerOptions);
    }

    public String getQuestionText() {
        return questionText;
    }

    public ImageIcon getQuestionImage() {
        return questionImage;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }
}
