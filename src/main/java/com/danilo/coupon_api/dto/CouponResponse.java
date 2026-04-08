package com.danilo.coupon_api.dto;

import com.danilo.coupon_api.domain.Coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDateTime expirationDate,
        String status,
        Boolean published,
        Boolean redeemed
) {

    public static CouponResponse fromEntity(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscountValue(),
                coupon.getExpirationDate(),
                "ACTIVE",
                coupon.getPublished(),
                coupon.getRedeemed()
        );
    }
}