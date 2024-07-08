package com.zerobase.order.controller;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.order.application.CartApplication;
import com.zerobase.order.application.OrderApplication;
import com.zerobase.order.domain.product.AddProductCartForm;
import com.zerobase.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomerCartController {

    private final CartApplication cartApplication;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final OrderApplication orderApplication;

    @PostMapping
    public ResponseEntity<Cart> addCart(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                        @RequestBody AddProductCartForm form) {
        return ResponseEntity.ok(cartApplication.addCart(jwtAuthenticationProvider.getUserVo(token).getId(), form));
    }

    @GetMapping
    public ResponseEntity<Cart> showCart(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(cartApplication.getCart(jwtAuthenticationProvider.getUserVo(token).getId()));
    }

    @PutMapping
    public ResponseEntity<Cart> updateCart(@RequestHeader(name = "X-AUTH-TOKEN") String token, @RequestBody Cart cart) {
        return ResponseEntity.ok(cartApplication.updateCart(jwtAuthenticationProvider.getUserVo(token).getId(), cart));
    }

    @PostMapping("/order")
    public ResponseEntity<Cart> order(@RequestHeader(name = "X-AUTH-TOKEN") String token, @RequestBody Cart cart) {
        orderApplication.order(token, cart);
        return ResponseEntity.ok().build();
    }
}
