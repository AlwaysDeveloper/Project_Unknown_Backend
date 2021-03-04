package services.mailer;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Transporter {
     public HtmlEmail Transporter() {
         HtmlEmail email = new HtmlEmail();
         email.setHostName("smtp.mailtrap.io");
         email.setSmtpPort(2525);
         email.setAuthenticator(
                 new DefaultAuthenticator(
                         "3f0c1aa509b5de",
                         "23059002ac92a8"
                 )
         );
         return email;
    }

    public void compose(HtmlEmail email, String to, String subject, String content) throws EmailException {
        email.setFrom("testTeam@unknown.com", "From");
        email.addTo(to, "to");
        email.setSubject(subject);
        email.setHtmlMsg(content);
    }

    public void send(HtmlEmail email) throws EmailException {
        email.send();
    }
}
