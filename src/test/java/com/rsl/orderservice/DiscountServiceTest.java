package com.rsl.orderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.rsl.orderservice.model.Coupon;
import com.rsl.orderservice.model.Customer;
import com.rsl.orderservice.repository.CouponRepository;
import com.rsl.orderservice.service.DiscountService;
import com.rsl.orderservice.service.PricingService;

class DiscountServiceTest {

    private DiscountService newDiscountService() {
        CouponRepository coupons = new CouponRepository();
        coupons.save(new Coupon("SAVE10", 10));
        return new DiscountService(coupons, new PricingService());
    }

    @Test
    void noCouponAndNonMemberMeansNoDiscount() {
        DiscountService discounts = newDiscountService();
        Customer bob = new Customer("C-2", "Bob", false);

        assertEquals(0, discounts.discountCents(1000, bob, null));
    }

    @Test
    void unknownCouponCodeThrowsException() {
        DiscountService discounts = newDiscountService();
        Customer bob = new Customer("C-2", "Bob", false);

        // A coupon code the customer mistyped must fail the transaction explicitly.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> discounts.discountCents(1000, bob, "BLACKFRIDAY"));
        assertEquals("Invalid coupon code: BLACKFRIDAY", exception.getMessage());
    }
}
