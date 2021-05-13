package services.mailer;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import databases.MongoConnection;
import models.MongoModels.User;
import org.apache.commons.mail.HtmlEmail;
import org.bson.Document;
import services.mailer.model.Mail;
import uitls.MongoUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static com.mongodb.client.model.Filters.eq;

public class MailerService {
    MongoConnection mongoConnection = new MongoConnection("erp_v1");
    MongoUtil mongoUtil = new MongoUtil();
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

    public Runnable emailRun() {
        return () -> {
            while (true){
                try {
                    int mailSent = 0;
                    MongoCollection mailCollection = mongoConnection.getCollection("emails");
                    FindIterable<Document> mails = mailCollection.find(Filters.eq("isSend", false)).sort(Sorts.descending("priority"));
                    MongoCursor<Document> cursor = mails.iterator();
                    while (cursor.hasNext()) {
                        Mail toSend = new Mail(cursor.next());
                        List<String> including = setProjections(new String[]{"fullname", "email", "unique"});
                        String htmlContent = readFileAsString(System.getProperty("user.dir") + "\\src\\main\\resources\\template\\" + toSend.getTemplate() + ".html");
                        MongoCollection userCollection = mongoConnection.getCollection("users");
                        for (int i = 0; i < toSend.getTo().size(); i++) {
                            System.out.println(toSend.getTo().size());
                            FindIterable<Document> user = userCollection.find(eq("_id", toSend.getTo().get(i))).projection(Projections.include(including));
                            MongoCursor<Document> userCursor = user.iterator();
                            Transporter transporter = new Transporter();
                            User userModel = new User();
                            while (userCursor.hasNext()) {
                                mongoUtil.documentToObject(userCursor.next(), userModel);
                                HtmlEmail email = transporter.Transporter();
                                transporter.compose(email, userModel.getEmail(), toSend.getSubject(), htmlContent);
                                transporter.send(email);
                                mailSent++;
                            }
                        }
                        mailCollection.updateOne(Filters.eq("_id", toSend.get_id()), Updates.set("isSend", true));
                    }
                    if(mailSent > 0)System.out.println("Email sent : "+mailSent);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
    }
}
