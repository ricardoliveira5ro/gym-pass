package com.ricardo.GymPass.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumTest {

    @Test
    void userRole_values() {
        assertNotNull(UserRole.values());
        assertEquals(3, UserRole.values().length);
        assertNotNull(UserRole.valueOf("MEMBER"));
        assertNotNull(UserRole.valueOf("ADMIN"));
        assertNotNull(UserRole.valueOf("MANAGER"));
    }

    @Test
    void membershipStatus_values() {
        assertNotNull(MembershipStatus.values());
        assertEquals(4, MembershipStatus.values().length);
        assertNotNull(MembershipStatus.valueOf("ACTIVE"));
        assertNotNull(MembershipStatus.valueOf("EXPIRED"));
        assertNotNull(MembershipStatus.valueOf("CANCELLED"));
        assertNotNull(MembershipStatus.valueOf("SUSPENDED"));
    }

    @Test
    void membershipTier_values() {
        assertNotNull(MembershipTier.values());
        assertEquals(3, MembershipTier.values().length);
        assertNotNull(MembershipTier.valueOf("BASIC"));
        assertNotNull(MembershipTier.valueOf("PREMIUM"));
        assertNotNull(MembershipTier.valueOf("VIP"));
    }
}