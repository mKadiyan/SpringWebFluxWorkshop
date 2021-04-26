package com.learn.springwebfluxworkshop.router;

import com.learn.springwebfluxworkshop.handler.MyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MyRouter {
    @Bean
    public RouterFunction<ServerResponse> route(MyHandler myHandler) {
        return RouterFunctions
                .route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)), myHandler::handleFluxRequest)
                .andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)), myHandler::handleMonoRequest);
    }
}