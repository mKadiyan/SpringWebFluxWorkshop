package com.learn.springwebfluxworkshop.controller;

        import org.springframework.http.MediaType;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RestController;
        import reactor.core.publisher.Flux;

        import java.lang.management.MemoryType;
        import java.time.Duration;

@RestController
public class FluxController {

    @GetMapping(value = "/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long>  getLongValues(){
        return Flux.interval(Duration.ofMillis(1000))
                .log();
    }

    @GetMapping(value = "/flux1")
    public Flux<Long>  getLongValues1(){
        return Flux.just(1L,2L,3L,4L,5L)
                .log();
    }
}