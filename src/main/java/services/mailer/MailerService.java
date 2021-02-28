package services.mailer;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
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
        FindIterable<Document> mails = mongoDatabase.getCollection("emails").find();
        MongoCursor<Document> cursor = mails.iterator();
        List<Mail> mailList = new ArrayList<Mail>();
        while(cursor.hasNext()){
            mailList.add(new Mail(cursor.next()));
        }
        List <String> excluding = new ArrayList<String>();
        excluding.add("photo");
        excluding.add("address");
        excluding.add("password");
        excluding.add("dob");
        excluding.add("_id");
        excluding.add("__v");

        String htmlContent = readFileAsString(System.getProperty("user.dir")+"\\src\\main\\resources\\template\\"+mailList.get(0).getTemplate()+".html");
        for(int i = 0; i < mailList.get(0).getTo().size(); i++) {
            FindIterable<Document> user = mongoDatabase.getCollection("users").find(eq("_id", mailList.get(0).getTo().get(i))).projection(Projections.exclude(excluding));
            MongoCursor<Document> cursor1  = user.iterator();
            while (cursor1.hasNext()){
                HtmlEmail htmlEmail = new HtmlEmail();
                htmlEmail.setHostName("smtp.mailtrap.io");
                htmlEmail.setSmtpPort(2525);
                htmlEmail.setAuthenticator(
                        new DefaultAuthenticator(
                                "3f0c1aa509b5de",
                                "23059002ac92a8"
                        )
                );
                htmlEmail.setFrom("testTeam@unknown.com", "From");
                htmlEmail.addTo((String) cursor1.next().get("email"), "To");
                htmlEmail.setSubject(mailList.get(0).getSubject());
                htmlEmail.setHtmlMsg(htmlContent);
                htmlEmail.send();
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
}
