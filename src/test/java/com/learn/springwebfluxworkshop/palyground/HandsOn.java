package com.learn.springwebfluxworkshop.palyground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class HandsOn {

    @Test
    public void testFlux() throws InterruptedException {
        Flux<String> just = Flux.just("A", "B", "C");
        just.subscribe(data -> System.out.println("Subs 1 == "+ data));
    }

    @Test
    public void testMono() throws InterruptedException {
        Mono<String> just = Mono.just("A");
        just.subscribe(data -> System.out.println("Subs 1 == "+ data));
    }

    @Test
    public void testMonoError() throws InterruptedException {
        Flux<Integer> just = Flux.range(1, 20);
        just.subscribe(data -> System.out.println("Subs 1 == "+ data)
                       ,data -> System.out.println("Error == "+ data)
                       ,() -> System.out.println("Complete===="),
                       (subscription -> subscription.request(10))
        );
        Thread.sleep(2000);

        ///need to check

    }


    @Test
    public void testFluxWithError() throws InterruptedException {
        Flux<String> just = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("new error"))).log();

        StepVerifier.create(just)
                .expectSubscription()
                .expectNext("A")
                .expectNext("B")
                .expectNext("C")
                .expectErrorMessage("new error")
                .verify();
    }
}
