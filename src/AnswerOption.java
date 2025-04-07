import javax.swing.ImageIcon;

/**
 * Represents a single answer option for a quiz question.
 * It can have text, an image, or both.
 */
public class AnswerOption {
    private String text;
    private ImageIcon image;
    private boolean isCorrect;

    /**
     * Constructs an answer option with text only.
     *
     * @param text the answer text
     * @param isCorrect whether this is the correct answer
     */
    public AnswerOption(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    /**
     * Constructs an answer option with an image only.
     *
     * @param image the answer image
     * @param isCorrect whether this is the correct answer
     */
    public AnswerOption(ImageIcon image, boolean isCorrect) {
        this.image = image;
        this.isCorrect = isCorrect;
    }

    /**
     * Constructs an answer option with both text and an image.
     *
     * @param text the answer text
     * @param image the answer image
     * @param isCorrect whether this is the correct answer
     */
    public AnswerOption(String text, ImageIcon image, boolean isCorrect) {
        this.text = text;
        this.image = image;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public ImageIcon getImage() {
        return image;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
