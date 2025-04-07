import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;

public class QuizSetLoader {
    public static QuizSet loadQuizSet(String filePath) {
        // Register the custom deserializer for ImageIcon
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ImageIcon.class, new ImageIconDeserializer())
                .create();
        try (Reader reader = new FileReader(filePath)) {
            Type quizSetType = new TypeToken<QuizSet>() {}.getType();
            return gson.fromJson(reader, quizSetType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
