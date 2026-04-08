package com.danilo.coupon_api.service;

import com.danilo.coupon_api.domain.Coupon;
import com.danilo.coupon_api.dto.CouponCreateRequest;
import com.danilo.coupon_api.dto.CouponResponse;
import com.danilo.coupon_api.exception.ResourceNotFoundException;
import com.danilo.coupon_api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public CouponResponse create(CouponCreateRequest request) {
        Coupon coupon = Coupon.create(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );

        Coupon savedCoupon = couponRepository.save(coupon);
        return CouponResponse.fromEntity(savedCoupon);
    }

    @Transactional(readOnly = true)
    public CouponResponse findById(UUID id) {
        Coupon coupon = couponRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));

        return CouponResponse.fromEntity(coupon);
    }

    @Transactional
    public void delete(UUID id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));

        coupon.delete();
        couponRepository.save(coupon);
    }
}