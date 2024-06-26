package com.mymarket.contoller;

import com.mymarket.dto.CartQuantityDto;
import com.mymarket.dto.CartRequestDto;
import com.mymarket.entity.Cart;
import com.mymarket.security.UserDetailsImpl;
import com.mymarket.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @Valid @RequestBody CartRequestDto requestDto) {
        try {
            Long userId = userDetails.getUser().getId(); // 인증된 사용자의 userId 가져오기
            Cart addToCart = cartService.addToCart(userId, requestDto);
            return ResponseEntity.ok(addToCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        List<Cart> cartList = cartService.getCartListByUserId(userId);
        return ResponseEntity.ok(cartList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartProduct(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable Long id,
                                               @Valid @RequestBody CartQuantityDto requestDto) {
        try {
            Long userId = userDetails.getUser().getId();
            Cart updatedCart = cartService.updateCartProduct(userId, id, requestDto);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
