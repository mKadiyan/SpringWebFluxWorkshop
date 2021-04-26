package com.learn.springwebfluxworkshop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@RestController
public class PerformanceController {

    @GetMapping(value="/perf/val")
    public Mono<Integer> getMonoResult(){
        Random random = new Random();
        return Mono.just(random.nextInt())
                .delayElement(Duration.ofMillis(100));
    }
}
