package ci.microservice.notification;

import ci.microservice.notification.adresseMail.dao.AdresseMailRepository;
import ci.microservice.notification.adresseMail.models.AdresseMail;

import ci.microservice.notification.discord.models.DiscordRequest;
import ci.microservice.notification.discord.repository.DiscordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@SpringBootApplication
@Configuration
public class NotificationApplication implements CommandLineRunner {
    @Autowired
    AdresseMailRepository adresseMailRepository;

    @Autowired
    DiscordRepository discordRepository;

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        adresseMailRepository.deleteAll();
//        adresseMailRepository.saveAll(
//                List.of(
//                        new AdresseMail("mouna89@gmail.com")
//                )
//        );

        discordRepository.deleteAll();
        discordRepository.saveAll(
                List.of(
                        new DiscordRequest("710847217648402522-aaaaaaaaaaaaaa")
                )
        );
    }

//    @Bean
//    public ApplicationRunner runner() {
//        return new Runner();
//    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        //  replace ** with the url of the front-side one deployed on azure
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
