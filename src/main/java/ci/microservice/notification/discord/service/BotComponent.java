package ci.microservice.notification.discord.service;

import ci.microservice.notification.amqp.AmqpConfig;
import ci.microservice.notification.amqp.EventModel;
import ci.microservice.notification.discord.models.DiscordRequest;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Service
@Slf4j
public class BotComponent implements CommandLineRunner {

    @Value("${microservice.notification.discord.token}")
    private String discordToken;

    private DiscordApi api;

    @PostConstruct
    public void init() {
        api = new DiscordApiBuilder().setToken(discordToken).login().join();
    }

    @Autowired
    private DiscordService discordService;

    public boolean subscribeRequest(String channelId, String subscriberMessage){
        String subscriberCode = subscriberMessage.replace("!subscribeHere", "").trim();
        DiscordRequest t = new  DiscordRequest(channelId + "-" + subscriberCode);
        System.out.println(t.getId());
        discordService.addRequest(t);
        return true;
    }

    public int countAllSubscribeRequest() {
        return discordService.getAllRequest().size();
    }


    private void notify(String buildId, String message){
        List<DiscordRequest> discordRequest = new ArrayList<DiscordRequest>(discordService.getAllRequest());
        if(discordRequest.size() == 0) return;
        discordRequest = discordRequest.stream().filter(a->a.getId().split("-")[1].equals(buildId)).collect(Collectors.toList());
        discordRequest.forEach(a->sendMessageOnChannel(a.getId().split("-")[0], message));
    }

    @RabbitListener(queues = AmqpConfig.QUEUE_NAME)
    public void notify(final EventModel eventModel){
        String buildMessage = "Le Build : " + eventModel.getProjectId() + " / "  + eventModel.getTest()
                + " / " + eventModel.getType()
                + " / " + eventModel.getDate()
                + " / " + eventModel.getBuild() + " \n Finalisé !";
        notify(eventModel.getProjectId(), buildMessage);
    }

    private void sendMessageOnChannel(String channelId, Message msg){
       sendMessageOnChannel(channelId, msg.getContent());
    }

    public void sendMessageOnChannel(String channelId, String msg){
        api.getTextChannelById(channelId).ifPresent(action-> {
            action.sendMessage(msg);
        });
    }

    private boolean isDuplicateMessageOnCache(String message){
        boolean isDuplicateMessageOnCache = false;

        for(Message m : api.getCachedMessages()){
            int creationDateDiff = (int)((new Date().getTime() - Date.from(m.getCreationTimestamp()).getTime())/1000);
            if(creationDateDiff < 2){
                if(m.getContent().equals(message)){
                    isDuplicateMessageOnCache = true;
                    break;
                }
            }

        }
        return isDuplicateMessageOnCache;
    }

    private void deleteDuplicateMessage(MessageCreateEvent event){
        int count = 0;

        int creationDateDiff = (int)((new Date().getTime() - Date.from(event.getMessage().getCreationTimestamp()).getTime())/1000);

        for (Message m : event.getChannel().getMessagesAfterAsStream(event.getMessage()).collect(Collectors.toList())){
            if(creationDateDiff < 3) {
                if (count > 0) {
                    event.getChannel().deleteMessages(m);
                }
            }
            count += 1;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        api.addMessageCreateListener(event -> {

            if(event.getChannel().getMessagesAfterAsStream(event.getMessage()).count() > 0) return;

            String msg = event.getMessageContent();

            String response = null;

            String channelId = event.getMessage().getChannel().getIdAsString();

            if (msg.equalsIgnoreCase("!help")) {
                response = "Entrez : \n - !sendMeMessage pour ouvrir un cannal privé entre le bot et vous. \n - !subscribeHere <codebuild> pour souscrire à un cannal de build";
            }

            if (msg.equalsIgnoreCase("!ping")) {
                response = "Ponge! " + event.getChannel().getIdAsString() + " " + discordService.getAllRequest().size();
            }

            if (msg.equalsIgnoreCase("!sendMeMessage")) {
                event.getMessage().getUserAuthor().ifPresent(user->user.sendMessage("Hello ! :)"));
                response = "C'est fait !";
            }

            if (msg.contains("!subscribeHere")) {
                if(subscribeRequest(channelId, msg)){
                    response = "Tu seras bien notifié lors des prochains builds ! \n - channel : " + channelId;
                }
                else{
                    sendMessageOnChannel(channelId,"Ton numéro de build n'est pas correct");
                    response = "Ton numéro de build n'est pas correct";
                }
            }

            if(response != null){
                if(!this.isDuplicateMessageOnCache(response)){
                    sendMessageOnChannel(channelId, response);
                }
            }

            deleteDuplicateMessage(event);
        });
    }
}
