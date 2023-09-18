package com.example.gateway.fillter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class CacheFilter extends AbstractGatewayFilterFactory<CustomDto> {

    private final Map<String,CacheData> map = new HashMap();


    @Override
    public GatewayFilter apply(CustomDto config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            request.getMethod();
            request.getPath();

            map.put(request.getPath().toString(),
                    map.getOrDefault(request.getPath().toString(),
                            new CacheData(request.getPath().toString()
                                    ,request.getMethod())));


            new CacheData(request.getPath().toString(),request.getMethod());

            HttpStatusCode statusCode = response.getStatusCode();
            System.out.println(statusCode);

            return chain.filter(exchange).then(
                   Mono.fromRunnable(()->{
                       System.out.println(map.get(request.getPath().toString()));
                   })
            );
        };
    }



    public CacheFilter(){
        super(CustomDto.class);
    }

    class CacheData{
        private Integer count;
        private final Date date;
        private final String api;
        private final HttpMethod method;

        public CacheData(String api, HttpMethod method) {
            this.count = 0;
            this.date = new Date();
            this.api = api;
            this.method = method;
        }

        public void plus(){
            this.count +=1;
        }

        @Override
        public String toString() {
            return
                    "count=" + count
                 ;
        }
    }

}
