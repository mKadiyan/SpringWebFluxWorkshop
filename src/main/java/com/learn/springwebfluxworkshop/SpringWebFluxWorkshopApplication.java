package com.learn.springwebfluxworkshop;

import com.learn.springwebfluxworkshop.persistence.models.Notification;
import com.learn.springwebfluxworkshop.persistence.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@Slf4j
public class SpringWebFluxWorkshopApplication implements CommandLineRunner {
    @Autowired
    private ReactiveMongoOperations mongoOperations;
    @Autowired
    private NotificationRepository notificationRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxWorkshopApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initalDataSetUp();
    }

    private void initalDataSetUp() {

        String[] topics = {"pmd", "otr", "sop"};
        CollectionOptions options = CollectionOptions.empty()
                .capped()
                .size(50000L)
                .maxDocuments(20L);
        mongoOperations.dropCollection(Notification.class)
                .then(mongoOperations.createCollection(Notification.class, options))
                .subscribe();

        Flux<Notification> itemCappedFlux = Flux.interval(Duration.ofSeconds(3))
                .map(i -> new Notification(null, topics[ThreadLocalRandom.current().nextInt(0, 3)], "Message "+i));

        notificationRepository
                .insert(itemCappedFlux)
                .subscribe((notification -> {
                    log.info("Inserted Notification is " + notification);
                }));
    }
}
