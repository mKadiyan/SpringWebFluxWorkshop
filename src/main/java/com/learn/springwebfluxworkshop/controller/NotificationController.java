package com.learn.springwebfluxworkshop.controller;

import com.learn.springwebfluxworkshop.dto.NotificationDto;
import com.learn.springwebfluxworkshop.persistence.models.Notification;
import com.learn.springwebfluxworkshop.persistence.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping(value = "/notifications/{topic}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Notification> getNotificationsByTopic(@PathVariable String topic) {
        return notificationRepository.findAllByTopic(topic);
    }

    @PostMapping(value = "/notifications")
    public Mono<Notification> addNotifications(@RequestBody NotificationDto NotificationDto) {
        return notificationRepository.save(new Notification(null, NotificationDto.getTopic(), NotificationDto.getMessage()));
    }
}
