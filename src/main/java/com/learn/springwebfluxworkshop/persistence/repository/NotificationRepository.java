package com.learn.springwebfluxworkshop.persistence.repository;

import com.learn.springwebfluxworkshop.persistence.models.Notification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {

    @Tailable
    Flux<Notification> findAllByTopic(String topic);
}
