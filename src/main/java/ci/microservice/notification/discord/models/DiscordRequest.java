package ci.microservice.notification.discord.models;

import org.springframework.data.annotation.Id;

public class DiscordRequest {
    @Id
    public String id;


    public DiscordRequest(String id) {
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public String getChannelId(){
        return id.split("-")[0];
    }

    public String getBuildId(){
        return id.split("-")[1];
    }
}
