package com.ricardo.GymPass.domain.entity;

import com.ricardo.GymPass.domain.enums.MembershipStatus;
import com.ricardo.GymPass.domain.enums.MembershipTier;
import com.ricardo.GymPass.domain.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void user_settersGetters() {
        User user = new User();
        user.setId(1L);
        user.setExternalId("ext-001");
        user.setEmail("test@example.com");
        user.setPasswordHash("hash");
        user.setName("Test User");
        user.setRole(UserRole.MEMBER);
        user.setImported(true);

        assertEquals(1L, user.getId());
        assertEquals("ext-001", user.getExternalId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hash", user.getPasswordHash());
        assertEquals("Test User", user.getName());
        assertEquals(UserRole.MEMBER, user.getRole());
        assertTrue(user.isImported());
    }

    @Test
    void user_defaults() {
        User user = new User();

        assertFalse(user.isImported());
    }

    @Test
    void gym_settersGetters() {
        User owner = new User();
        owner.setId(1L);

        Gym gym = new Gym();
        gym.setId(1L);
        gym.setName("Test Gym");
        gym.setAddress("123 Main St");
        gym.setCity("Lisbon");
        gym.setState("Lisbon");
        gym.setPostalCode("1000");
        gym.setCountry("Portugal");
        gym.setPhone("+351123456789");
        gym.setEmail("gym@test.com");
        gym.setTimezone("Europe/Lisbon");
        gym.setActive(true);
        gym.setMaxCapacity(100);
        gym.setOwner(owner);

        assertEquals(1L, gym.getId());
        assertEquals("Test Gym", gym.getName());
        assertEquals("123 Main St", gym.getAddress());
        assertEquals("Lisbon", gym.getCity());
        assertEquals("Lisbon", gym.getState());
        assertEquals("1000", gym.getPostalCode());
        assertEquals("Portugal", gym.getCountry());
        assertEquals("+351123456789", gym.getPhone());
        assertEquals("gym@test.com", gym.getEmail());
        assertEquals("Europe/Lisbon", gym.getTimezone());
        assertTrue(gym.isActive());
        assertEquals(100, gym.getMaxCapacity());
        assertEquals(owner, gym.getOwner());
    }

    @Test
    void gym_defaults() {
        Gym gym = new Gym();

        assertEquals("Portugal", gym.getCountry());
        assertEquals("Europe/Lisbon", gym.getTimezone());
        assertTrue(gym.isActive());
    }

    @Test
    void membership_settersGetters() {
        User user = new User();
        user.setId(1L);

        Gym gym = new Gym();
        gym.setId(1L);

        Membership membership = new Membership();
        membership.setId(1L);
        membership.setExternalId("mem-001");
        membership.setUser(user);
        membership.setGym(gym);
        membership.setStatus(MembershipStatus.ACTIVE);
        membership.setTier(MembershipTier.PREMIUM);
        membership.setStartDate(LocalDate.now());
        membership.setEndDate(LocalDate.now().plusMonths(1));

        assertEquals(1L, membership.getId());
        assertEquals("mem-001", membership.getExternalId());
        assertEquals(user, membership.getUser());
        assertEquals(gym, membership.getGym());
        assertEquals(MembershipStatus.ACTIVE, membership.getStatus());
        assertEquals(MembershipTier.PREMIUM, membership.getTier());
        assertNotNull(membership.getStartDate());
        assertNotNull(membership.getEndDate());
    }
}