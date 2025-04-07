import javax.swing.ImageIcon;
import java.io.Serializable;

/**
 * Represents a single answer option for a quiz question.
 * It can have text, an image, or both.
 */
public class AnswerOption implements Serializable {
    private static final long serialVersionUID = 1L;

    private String text;
    private ImageIcon image;
    private boolean isCorrect;

    // Constructors
    public AnswerOption(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public AnswerOption(ImageIcon image, boolean isCorrect) {
        this.image = image;
        this.isCorrect = isCorrect;
    }

    public AnswerOption(String text, ImageIcon image, boolean isCorrect) {
        this.text = text;
        this.image = image;
        this.isCorrect = isCorrect;
    }

    // Getters
    public String getText() { return text; }
    public ImageIcon getImage() { return image; }
    public boolean isCorrect() { return isCorrect; }
}
