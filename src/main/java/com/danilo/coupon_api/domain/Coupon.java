package com.danilo.coupon_api.domain;

import com.danilo.coupon_api.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private Boolean published;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    private Boolean redeemed;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static Coupon create(
            String code,
            String description,
            BigDecimal discountValue,
            LocalDateTime expirationDate,
            Boolean published
    ) {
        validateRequiredFields(code, description, discountValue, expirationDate);

        String sanitizedCode = sanitizeCode(code);
        validateCode(sanitizedCode);
        validateDiscountValue(discountValue);
        validateExpirationDate(expirationDate);

        return new Coupon(
                null,
                sanitizedCode,
                description,
                discountValue,
                expirationDate,
                published != null ? published : false,
                false,
                false,
                LocalDateTime.now()
        );
    }

    public void delete() {
        if (Boolean.TRUE.equals(this.deleted)) {
            throw new BusinessException("Coupon already deleted");
        }

        this.deleted = true;
    }

    private static void validateRequiredFields(
            String code,
            String description,
            BigDecimal discountValue,
            LocalDateTime expirationDate
    ) {
        if (code == null || code.isBlank()) {
            throw new BusinessException("Code is required");
        }

        if (description == null || description.isBlank()) {
            throw new BusinessException("Description is required");
        }

        if (discountValue == null) {
            throw new BusinessException("Discount value is required");
        }

        if (expirationDate == null) {
            throw new BusinessException("Expiration date is required");
        }
    }

    private static String sanitizeCode(String code) {
        return code.replaceAll("[^a-zA-Z0-9]", "");
    }

    private static void validateCode(String code) {
        if (code.length() != 6) {
            throw new BusinessException("Coupon code must contain exactly 6 alphanumeric characters");
        }
    }

    private static void validateDiscountValue(BigDecimal discountValue) {
        if (discountValue.compareTo(BigDecimal.valueOf(0.5)) < 0) {
            throw new BusinessException("Discount value must be at least 0.5");
        }
    }

    private static void validateExpirationDate(LocalDateTime expirationDate) {
        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Expiration date cannot be in the past");
        }
    }
}