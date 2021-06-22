package com.example.youtubesearcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YoutubeSearcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeSearcherApplication.class, args);
    }

}
