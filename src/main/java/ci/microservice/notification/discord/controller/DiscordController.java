package ci.microservice.notification.discord.controller;

import ci.microservice.notification.discord.service.BotComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/discord")
public class DiscordController {

    @Autowired
    BotComponent botComponent;

    @GetMapping("/test")
    public String addDataTest() {
        return "o";
    }

    @GetMapping("/ttt")
    @ResponseBody
    public String getFoos(@RequestParam String buildId) {
        return "hi";
    }



}
