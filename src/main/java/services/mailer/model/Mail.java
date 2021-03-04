package services.mailer.model;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;
import services.mailer.MailerService;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Mail{
    ObjectId _id;
    ArrayList<ObjectId> to;
    ArrayList<ObjectId> cc;
    ArrayList<ObjectId> bcc;
    String priority;
    String subject;
    String template;
    String token;
    Integer __v;

    public Mail(Document document) throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field: fields){
            field.set(this, document.get(field.getName()));
        }
    }

    public void setTo(ArrayList<ObjectId> to) {
        this.to = to;
    }

    public void setCc(ArrayList<ObjectId> cc) {
        this.cc = cc;
    }

    public void setBcc(ArrayList<ObjectId> bcc) {
        this.bcc = bcc;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPriority(String priority){
        this.priority = priority;
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getToken() {
        return token;
    }

    public String getPriority() {
        return priority;
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<ObjectId> getBcc() {
        return bcc;
    }

    public ArrayList<ObjectId> getCc() {
        return cc;
    }

    public ArrayList<ObjectId> getTo() {
        return to;
    }

    public String getTemplate() {
        return template;
    }

    public Integer get__v() {
        return __v;
    }
}
