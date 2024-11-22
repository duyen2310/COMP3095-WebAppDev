package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration
@Slf4j
public class Routes {
    @Value("${services.product-url}")
    private String productServiceUrl;

    @Value("${services.order-url}")
    private String orderServiceUrl;

    @Value("${services.inventory-url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute(){

        log.info("Initializing product-service route with the url: {}", productServiceUrl);

        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product/**"),request->{
                    log.info("Received Request for product-service: {}", request.uri());
                   try{
                       ServerResponse response = HandlerFunctions.http(productServiceUrl).handle(request);
                       log.info("Response status: {}", response.statusCode());
                       return response;
                   }catch (Exception e){
                       log.error("Error occurred while routing to: {}", e.getMessage(), e);
                       return ServerResponse.status(500).body("Error occurred when routing to product-service");
                   }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute(){

        log.info("Initializing order-service route with the url: {}", orderServiceUrl);

        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"),request->{
                    log.info("Received Request for order-service: {}", request.uri());
                    try{
                        ServerResponse response = HandlerFunctions.http(orderServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    }catch (Exception e){
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to order-service");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute(){

        log.info("Initializing inventory-service route with the url: {}", inventoryServiceUrl);

        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"),request->{
                    log.info("Received Request for inventory-service: {}", request.uri());
                    try{
                        ServerResponse response = HandlerFunctions.http(inventoryServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    }catch (Exception e){
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to inventory-service");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute(){

        return GatewayRouterFunctions.route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8084"))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute(){

        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8082"))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute(){

        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8083"))
                .filter(setPath("/api-docs"))
                .build();

    }
}