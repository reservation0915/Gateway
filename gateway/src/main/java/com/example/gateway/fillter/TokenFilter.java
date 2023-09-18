package com.example.gateway.fillter;

import com.example.gateway.jwt.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TokenFilter
        extends AbstractGatewayFilterFactory<CustomDto> {
    public TokenFilter(JwtService jwtService) {
        super(CustomDto.class);
        this.jwtService = jwtService;
    }
    private final JwtService jwtService;
    @Override
    public GatewayFilter apply(CustomDto config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            if(request.getMethod() == HttpMethod.POST
                    && !isValidToken(request)){

                return onError(response,"401", HttpStatus.UNAUTHORIZED);

            }
                return chain.filter(exchange);
        };
    }
    public boolean isValidToken(ServerHttpRequest request){
        List<String> authorization =
                request.getHeaders().get("Authorization");
        System.out.println(authorization);
        if(authorization == null || authorization.size() == 0) return false;
        String bearerToken = authorization.get(0);
        if( bearerToken.startsWith("Bearer ")){

            return jwtService
                    .parseToken(
                            bearerToken.replace("Bearer ",""));
        }
        return false;
    }
    public Mono<Void> onError(ServerHttpResponse response,
                              String msg, HttpStatus code){
        response.setStatusCode(code);
        System.out.println(msg);
        return response.setComplete();
    }


}