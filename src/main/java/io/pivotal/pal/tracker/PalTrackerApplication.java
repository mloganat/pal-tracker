package io.pivotal.pal.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PalTrackerApplication {



    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }


    @Bean
    public InMemoryTimeEntryRepository repoMethod()
    {
        InMemoryTimeEntryRepository rep = new InMemoryTimeEntryRepository();
        return rep;
    }

//    @Bean
//    RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        return restTemplate;
//    }
}
