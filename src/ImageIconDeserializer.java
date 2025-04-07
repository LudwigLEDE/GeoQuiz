import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import javax.swing.*;
import java.lang.reflect.Type;

public class ImageIconDeserializer implements JsonDeserializer<ImageIcon> {
    @Override
    public ImageIcon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // Get the URL string from the JSON element
        String url = json.getAsString();
        // Create and return a new ImageIcon using the URL
        return new ImageIcon(url);
    }
}
