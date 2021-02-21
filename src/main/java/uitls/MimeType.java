package uitls;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.FileReader;
import java.io.IOException;

public class MimeType {
    public JsonObject mimeTypeObject;
    public MimeType() throws IOException {
        String currentLocation = (System.getProperty("user.dir"));
        JsonReader reader = Json.createReader(
                new FileReader(
                        currentLocation+"\\src\\main\\resources\\mime_type.json"
                )
        );
        mimeTypeObject = reader.readObject();
        reader.close();
    }

    public JsonValue getMimeType(String mime){
        return mimeTypeObject.get(mime);
    }
}
