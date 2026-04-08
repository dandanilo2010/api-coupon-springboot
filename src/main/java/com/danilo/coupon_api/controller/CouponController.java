package com.danilo.coupon_api.controller;

import com.danilo.coupon_api.dto.CouponCreateRequest;
import com.danilo.coupon_api.dto.CouponResponse;
import com.danilo.coupon_api.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CouponCreateRequest request) {
        CouponResponse response = couponService.create(request);

        return ResponseEntity
                .created(URI.create("/coupon/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(couponService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }
}