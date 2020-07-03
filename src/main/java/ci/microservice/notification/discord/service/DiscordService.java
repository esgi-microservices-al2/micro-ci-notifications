package ci.microservice.notification.discord.service;

import ci.microservice.notification.discord.models.DiscordRequest;

import java.util.List;

public interface DiscordService {

    void addRequest(DiscordRequest request);
    List<DiscordRequest> getAllRequest();

}
