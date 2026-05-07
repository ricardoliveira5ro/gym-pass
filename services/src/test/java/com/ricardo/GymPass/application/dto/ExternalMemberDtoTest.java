package com.ricardo.GymPass.application.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExternalMemberDtoTest {

    @Test
    void externalMemberDto_record() {
        Instant now = Instant.now();
        ExternalMemberDto dto = new ExternalMemberDto("ext-001", now, now);

        assertEquals("ext-001", dto.id());
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }

    @Test
    void externalMembershipDto_record() {
        Instant now = Instant.now();
        LocalDate today = LocalDate.now();
        ExternalMembershipDto dto = new ExternalMembershipDto("mem-001", "user-001", "ACTIVE", "PREMIUM", today, today, now, now);

        assertEquals("mem-001", dto.id());
        assertEquals("user-001", dto.memberId());
        assertEquals("ACTIVE", dto.status());
        assertEquals("PREMIUM", dto.tier());
        assertEquals(today, dto.startDate());
        assertEquals(today, dto.endDate());
    }
}