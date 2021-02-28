package services.mailer;


import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import databases.MongoConnection;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.bson.Document;
import org.bson.types.ObjectId;
import services.mailer.model.Mail;

import javax.print.Doc;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.eq;

public class MailerService {
    MongoDatabase mongoDatabase = new MongoConnection().MongoConnection().getDatabase("erp_v1");
    public MailerService() throws IllegalAccessException, EmailException, IOException {
        FindIterable<Document> mails = mongoDatabase.getCollection("emails").find(eq("isSend", false));
        MongoCursor<Document> cursor = mails.iterator();
        while(cursor.hasNext()){
            Mail toSend = new Mail(cursor.next());
            List <String> excluding = setProjections(new String[]{"photo", "address", "password", "dob", "__id", "__v"});
            String htmlContent = readFileAsString(System.getProperty("user.dir")+"\\src\\main\\resources\\template\\"+toSend.getTemplate()+".html");
            for(int i = 0; i < toSend.getTo().size(); i++) {
                FindIterable<Document> user = mongoDatabase.getCollection("users").find(eq("_id", toSend.getTo().get(i))).projection(Projections.exclude(excluding));
                MongoCursor<Document> userCursor  = user.iterator();
                Transporter transporter = new Transporter();
                while (userCursor.hasNext()){
                    HtmlEmail email = transporter.Transporter();
                    transporter.compose(email, userCursor.next().get("email").toString(), toSend.getSubject(), htmlContent);
                    transporter.send(email);
                }
            }
        }
    }

    private String readFileAsString(String filePath) throws IOException {
        StringBuilder builder = new StringBuilder();
        Stream<String> stream = Files.lines(
                Paths.get(filePath),
                StandardCharsets.UTF_8
        );
        stream.forEach(s -> builder.append(s).append("\n"));
        return builder.toString();
    }

    private List<String> setProjections(String[] fields) {
        List<String> projections = new ArrayList<String>();
        for(String field : fields){
            projections.add(field);
        }
        return projections;
    }
}
