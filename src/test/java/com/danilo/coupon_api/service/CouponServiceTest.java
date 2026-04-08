package com.danilo.coupon_api.service;

import com.danilo.coupon_api.domain.Coupon;
import com.danilo.coupon_api.dto.CouponCreateRequest;
import com.danilo.coupon_api.dto.CouponResponse;
import com.danilo.coupon_api.exception.ResourceNotFoundException;
import com.danilo.coupon_api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    void shouldCreateCouponSuccessfully() {
        CouponCreateRequest request = new CouponCreateRequest(
                "ABC-12@3",
                "Cupom de teste",
                BigDecimal.valueOf(10.5),
                LocalDateTime.now().plusDays(1),
                true
        );

        UUID id = UUID.randomUUID();

        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> {
            Coupon coupon = invocation.getArgument(0);
            return new Coupon(
                    id,
                    coupon.getCode(),
                    coupon.getDescription(),
                    coupon.getDiscountValue(),
                    coupon.getExpirationDate(),
                    coupon.getPublished(),
                    coupon.getDeleted(),
                    coupon.getRedeemed(),
                    coupon.getCreatedAt()
            );
        });

        CouponResponse response = couponService.create(request);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("ABC123", response.code());
        assertEquals("Cupom de teste", response.description());
        assertEquals(BigDecimal.valueOf(10.5), response.discountValue());
        assertEquals("ACTIVE", response.status());
        assertTrue(response.published());
        assertFalse(response.redeemed());

        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void shouldFindCouponByIdSuccessfully() {
        UUID id = UUID.randomUUID();

        Coupon coupon = new Coupon(
                id,
                "ABC123",
                "Cupom encontrado",
                BigDecimal.valueOf(15.0),
                LocalDateTime.now().plusDays(2),
                true,
                false,
                false,
                LocalDateTime.now()
        );

        when(couponRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(coupon));

        CouponResponse response = couponService.findById(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("ABC123", response.code());
        assertEquals("Cupom encontrado", response.description());

        verify(couponRepository, times(1)).findByIdAndDeletedFalse(id);
    }

    @Test
    void shouldThrowExceptionWhenCouponIsNotFoundById() {
        UUID id = UUID.randomUUID();

        when(couponRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> couponService.findById(id)
        );

        assertEquals("Coupon not found", exception.getMessage());

        verify(couponRepository, times(1)).findByIdAndDeletedFalse(id);
    }

    @Test
    void shouldThrowExceptionWhenCouponIsDeletedInFindById() {
        UUID id = UUID.randomUUID();

        when(couponRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> couponService.findById(id)
        );

        assertEquals("Coupon not found", exception.getMessage());

        verify(couponRepository, times(1)).findByIdAndDeletedFalse(id);
    }

    @Test
    void shouldDeleteCouponSuccessfully() {
        UUID id = UUID.randomUUID();

        Coupon coupon = new Coupon(
                id,
                "ABC123",
                "Cupom para deletar",
                BigDecimal.valueOf(20.0),
                LocalDateTime.now().plusDays(3),
                false,
                false,
                false,
                LocalDateTime.now()
        );

        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        couponService.delete(id);

        assertTrue(coupon.getDeleted());

        verify(couponRepository, times(1)).findById(id);
        verify(couponRepository, times(1)).save(coupon);
    }

    @Test
    void shouldThrowExceptionWhenDeletingCouponThatDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(couponRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> couponService.delete(id)
        );

        assertEquals("Coupon not found", exception.getMessage());

        verify(couponRepository, times(1)).findById(id);
        verify(couponRepository, never()).save(any(Coupon.class));
    }
}