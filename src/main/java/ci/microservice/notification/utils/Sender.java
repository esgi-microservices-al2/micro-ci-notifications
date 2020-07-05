package ci.microservice.notification.utils;

import ci.microservice.notification.amqp.AmqpConfig;
import ci.microservice.notification.amqp.EventModel;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
public class Sender {
    private final Logger logger = Logger.getLogger(Sender.class);

    @Value("${microservice.notification.subjectForBuildStatus}")
    private String emailSubjectForEstimatedNavs;
    @Value("${microservice.notification.mail.comming.from}")
    private String emailCommingFrom;
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * @throws MessagingException the Exception
     */
    public void sendMail(String emailTo, String fileName, final EventModel eventModel) throws MessagingException {

        final String emailBodyForBuildStatus = "<font face=\"arial\">Please find in the mail the the build status.<br>" +
                "<br>"+ eventModel.getProjectId() +
                "<br>" + eventModel.getTest() +
                "<br>" + eventModel.getType() +
                "<br>" + eventModel.getDate() +
                "<br>" + eventModel.getBuild() +
                "Should you have any question regarding this mail, thank you to contact <a href = \"mailto: msouissi2@myges.fr\">" +
                ".<br>Best regards,<br><br><strong>The micro-service notification team</strong></font>";
        System.out.print("emailBodyForBuildStatus*************************" + eventModel.getBuild());
        logger.info("sending message to email address : " + emailTo + " in process ...");
        MimeMessage messenge = javaMailSender.createMimeMessage();
        MimeMessageHelper sendMail = new MimeMessageHelper(messenge, true, "UTF-8");
        sendMail.setTo(emailTo.split(";"));
        sendMail.setFrom(emailCommingFrom);
        sendMail.setSubject(emailSubjectForEstimatedNavs);
        sendMail.setText(emailBodyForBuildStatus, true);
        javaMailSender.send(messenge);
        logger.info("The message was sent succesfully to : " + emailTo);
    }
}
