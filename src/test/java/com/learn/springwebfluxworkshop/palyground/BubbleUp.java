package com.learn.springwebfluxworkshop.palyground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class BubbleUp {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
//                .concatWith(Flux.error(new RuntimeException("error is here")))
                .concatWith(Flux.just("After Error"))
                .log();
        stringFlux.subscribe(System.out::println, (e) -> System.out.println(e), () -> System.out.println("Completed"));
    }

    @Test
    public void fluxTestWithoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring boot", "Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxTestWithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("error is here")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Reactive Spring")
//                .expectError(RuntimeException.class)
                .expectErrorMessage("error is here")
                .verify();
    }

    @Test
    public void fluxTestWithCount() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("error is here")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(3)
//                .expectError(RuntimeException.class)
                .expectErrorMessage("error is here")
                .verify();
    }

    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.just("Spring")
                .log();
        stringMono.subscribe(System.out::println, (e) -> System.out.println(e), () -> System.out.println("Completed"));
    }

    @Test
    public void fluxTestWithFilter() {
        Flux<Integer> rangeFlux = Flux.range(1, 100)
                .filter(integer -> integer % 10 == 0)
                .log();
        StepVerifier.create(rangeFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void fluxTestWithMap() {
        Flux<Integer> rangeFlux = Flux.range(1, 100)
                .filter(integer -> integer % 10 == 0)
                .map(integer -> integer - 1)
                .log();
        StepVerifier.create(rangeFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void fluxTestWithFlatMap() {
        Flux<String> rangeFlux = Flux.just("Hello", "There", "How", "Are", "You", "?")
                .window(2)
                .delayElements(Duration.ofSeconds(3))
                .flatMapSequential(value -> value.parallel())
                .flatMap(value -> Flux.fromArray(value.split("")))
                .log();
        StepVerifier.create(rangeFlux)
                .expectNextCount(20)
                .verifyComplete();
    }

    @Test
    public void fluxTestWithInfinite() {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .take(10)
                .log();
        StepVerifier.create(infiniteFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void fluxTestBackPressure() {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .log();
        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .thenRequest(2)
                .expectNext(0L)
                .expectNext(1L)
                .thenRequest(1)
                .expectNext(2L)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .log();
        infiniteFlux.subscribe(System.out::println,
                               (e) -> System.out.println(e),
                               () -> System.out.println("Completed"),
                               (subscription -> subscription.request(10))
        );
        Thread.sleep(3000);
    }

    @Test
    public void hotFlux() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100));
        ConnectableFlux<Long> publish = infiniteFlux.publish();
        publish.connect();
        publish.subscribe((val) -> System.out.println("Subscriber 1 = "+ val));
        Thread.sleep(1000);
        publish.subscribe((val) -> System.out.println("Subscriber 2 = "+ val));
        Thread.sleep(2000);
    }

    @Test
    public void virtualTime() {
        VirtualTimeScheduler.getOrSet();
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofSeconds(3))
                .take(3).log();

        StepVerifier.withVirtualTime(() -> infiniteFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0L)
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(1L)
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(2L)
                .verifyComplete();

    }
}
