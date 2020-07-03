package ci.microservice.notification.discord.service;

import ci.microservice.notification.discord.models.DiscordRequest;
import ci.microservice.notification.discord.repository.DiscordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscordServiceImpl implements DiscordService {
    @Autowired
    DiscordRepository requestRepository;

    @Override
    public void addRequest(DiscordRequest request) {
        requestRepository.save(request);
    }

    @Override
    public List<DiscordRequest> getAllRequest() {
        return requestRepository.findAll();
    }
}
