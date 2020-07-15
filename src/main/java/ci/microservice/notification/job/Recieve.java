package ci.microservice.notification.job;

import ci.microservice.notification.adresseMail.models.AdresseMail;
import ci.microservice.notification.adresseMail.services.AdresseMailService;
import ci.microservice.notification.amqp.AmqpConfig;
import ci.microservice.notification.amqp.EventModel;
import ci.microservice.notification.discord.service.BotComponent;
import ci.microservice.notification.utils.Sender;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@Service
@Slf4j
public class Recieve {

    @Autowired
    AdresseMailService adresseMailService;
    private final Logger logger = Logger.getLogger(Sender.class);
    @Value("${microservice.notification.build.status.to}")
    private String emailBuildStatusTo; // mail to
    @Resource
    private Sender sender;
    @Autowired
    BotComponent botComponent;

//    public void run(ApplicationArguments args) throws Exception {
//        logger.info("The beginning to launch the job");
//        exportDetailsOfBuild();
//        logger.info("finish to launch the job");
//    }

    @RabbitListener(queues = AmqpConfig.QUEUE_NAME)
    public void exportDetailsOfBuild(final EventModel eventModel) throws MessagingException {


        System.out.println("SALLLLLUUUUUUTTTTTTTT");

        String buildMessage = "Le Build : " + eventModel.getProjectId() + " / "  + eventModel.getTest()
                + " / " + eventModel.getType()
                + " / " + eventModel.getDate()
                + " / " + eventModel.getBuild() + " \n Finalisé !";

        System.out.println(buildMessage);


        List<AdresseMail> mails = adresseMailService.getAdressesMail();
        logger.info("The beginning to export the build status");
        for (int i = 0; i < mails.size(); i++){
            sender.sendMail(mails.get(i).getAdresse(), "Build status",eventModel);
            //logger.info("Finish to export the build status");
        }
        //sender.sendMail(emailBuildStatusTo, "Build status",eventModel);
        botComponent.notify(eventModel);

        logger.info("Finish to export the build status");


    }

}

