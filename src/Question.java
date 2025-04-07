import javax.swing.ImageIcon;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a quiz question.
 * A question can have text, an image, or both,
 * along with a list of answer options.
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    private String questionText;
    private ImageIcon questionImage;
    private List<AnswerOption> answerOptions;

    // Constructors
    public Question(String questionText, ImageIcon questionImage, List<AnswerOption> answerOptions) {
        this.questionText = questionText;
        this.questionImage = questionImage;
        this.answerOptions = answerOptions;
    }

    public Question(String questionText, List<AnswerOption> answerOptions) {
        this(questionText, null, answerOptions);
    }

    public Question(ImageIcon questionImage, List<AnswerOption> answerOptions) {
        this(null, questionImage, answerOptions);
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public ImageIcon getQuestionImage() { return questionImage; }
    public List<AnswerOption> getAnswerOptions() { return answerOptions; }
}
