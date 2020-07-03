package ci.microservice.notification.discord.repository;

import ci.microservice.notification.discord.models.DiscordRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscordRepository extends MongoRepository<DiscordRequest, String> {
}
