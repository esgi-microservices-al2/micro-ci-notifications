//package ci.microservice.notification.amqp;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class ReceiveMessageHandler {
//    @RabbitListener(queues = AmqpConfig.QUEUE_NAME)
//    public void consumeDefaultMessage(final EventModel eventModel) {
//        log.info("message receive: {}", eventModel.toString());
//    }
//}
