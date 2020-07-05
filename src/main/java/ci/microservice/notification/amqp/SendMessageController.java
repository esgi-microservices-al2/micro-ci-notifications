package ci.microservice.notification.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class SendMessageController {

    private final RabbitTemplate rabbitTemplate;

    public SendMessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/message")
    public String sendEvents(@RequestBody EventModel eventModel) {
        rabbitTemplate.convertAndSend(AmqpConfig.QUEUE_NAME, eventModel);
        return "Goooooooooooooooooooooooooood !!!!!!!!!!!!!!!!!!!!!!!!!";
    }
}