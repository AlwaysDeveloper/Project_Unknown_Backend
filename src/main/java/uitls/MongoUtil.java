package uitls;

import org.bson.Document;

import java.lang.reflect.Field;

public class MongoUtil {
    public void documentToObject(Document document, Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields){
            field.set(
                    object,
                    document.get(field.getName())
            );
        }
    }

    public void objectToDocument(Object object, Document document) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields){
            document.put(field.getName(), field.get(object));
        }
    }
}
