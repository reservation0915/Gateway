package com.example.gateway.fillter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter
        extends AbstractGatewayFilterFactory<CustomDto> {


    @Value("${jwt.secret}")
    private String secret;

    public CustomGlobalFilter() {
        super(CustomDto.class);
    }

    @Override
    public GatewayFilter apply(CustomDto config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            System.out.println("zzzzzzz"+secret);
            if(true){
                System.out.println("req : " + request.getId() +
                        ", " + request.getMethod() + " : "
                        + request.getPath() +
                        ", " + request.getRemoteAddress()
                );
            }
            return chain.filter(exchange).then(
                    Mono.fromRunnable(()->{
                        if(true){
                            System.out.println("res : " + request.getId() +
                                    ", " + response.getStatusCode()
                            );
                        }
                    })
            );


        };
    }
}