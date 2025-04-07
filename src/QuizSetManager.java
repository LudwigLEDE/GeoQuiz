import java.io.*;

/**
 * Manages saving and loading of quiz sets using Java Serialization.
 */
public class QuizSetManager {
    /**
     * Saves the provided QuizSet to a file.
     * @param filename The file path to save the quiz set.
     * @param quizSet The QuizSet object to save.
     */
    public static void saveQuizSet(String filename, QuizSet quizSet) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(quizSet);
            System.out.println("QuizSet saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a QuizSet from a file.
     * @param filename The file path from which to load the quiz set.
     * @return The loaded QuizSet, or null if an error occurred.
     */
    public static QuizSet loadQuizSet(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            QuizSet quizSet = (QuizSet) ois.readObject();
            System.out.println("QuizSet loaded from " + filename);
            return quizSet;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
