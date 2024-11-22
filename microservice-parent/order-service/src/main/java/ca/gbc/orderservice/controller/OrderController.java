package ca.gbc.orderservice.controller;

import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.dto.OrderResponse;
import ca.gbc.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest){
//        orderService.placeOrder(orderRequest);
       try{
        OrderResponse placedOrder = orderService.placeOrder(orderRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/order" + placedOrder);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Order placed successfully");}
       catch (RuntimeException runtimeException){
           return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .contentType(MediaType.TEXT_PLAIN)
                   .body("The item is not in stock");}
       }
    }
