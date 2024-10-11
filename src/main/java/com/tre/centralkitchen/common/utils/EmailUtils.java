package com.tre.centralkitchen.common.utils;

import com.tre.jdevtemplateboot.common.util.JasyptUtils;
import jodd.mail.Email;
import jodd.mail.MailServer;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailUtils {


    private final String host = "cn.tre-inc.com";


    private final String username = "10004406song_yingying@cn.tre-inc.com";


    private final String password = "1HUOs4mbR7Pm18U7dL8N9Tu6lXKo57QP";


    private final String smtp = "cn.tre-inc.com";

    public void sendEmail(String[] toEmail, String subject, String message){


        String password1 = JasyptUtils.decyptPwd(password);

        Email email = Email.create()
                .from(username)
                .to(toEmail)
                .subject(subject)
                .textMessage(message);

        SmtpServer smtpServer = MailServer.create()
                .host(host)
                .auth(username, password1)
                .port(587)
                .buildSmtpMailServer();
        SendMailSession session = smtpServer.createSession();
        session.open();
        session.sendMail(email);
        session.close();
        System.out.println("email = " + email);
    }

    public void sendEmail( String[]toEmail,String[] ccEmail,String subject,String message){

        String password1 = JasyptUtils.decyptPwd(password);

        Email email = Email.create()
                .from(username)
                .to(toEmail)
                .subject(subject)
                .textMessage(message)
                .cc(ccEmail);
        SmtpServer smtpServer = MailServer.create()
                .host(host)
                .auth(username, password1)
                .port(587)
                .buildSmtpMailServer();
        SendMailSession session = smtpServer.createSession();
        session.open();
        session.sendMail(email);
        session.close();
        System.out.println("email = " + email);
    }

    public void sendEmail( String[] toEmail,String[] ccEmail,String[] bccEmail,String subject,String message){


        String password1 = JasyptUtils.decyptPwd(password);

        Email email = Email.create()
                .from(username)
                .to(toEmail)
                .subject(subject)
                .textMessage(message)
                .cc(ccEmail)
                .bcc(bccEmail);

        SmtpServer smtpServer = MailServer.create()
                .host(host)
                .auth(username, password1)
                .port(587)
                .buildSmtpMailServer();
        SendMailSession session = smtpServer.createSession();
        session.open();
        session.sendMail(email);
        session.close();
        System.out.println("email = " + email);
    }

}
