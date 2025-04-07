import java.io.Serializable;
import java.util.List;

/**
 * Represents a quiz set containing a name and a list of questions.
 */
public class QuizSet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String setName;
    private List<Question> questions;

    public QuizSet(String setName, List<Question> questions) {
        this.setName = setName;
        this.questions = questions;
    }

    public String getSetName() { return setName; }
    public List<Question> getQuestions() { return questions; }
}
