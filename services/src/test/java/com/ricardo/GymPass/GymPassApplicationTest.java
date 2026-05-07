package com.ricardo.GymPass;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GymPassApplicationTest {

    @Test
    void main_runsWithoutException() {
        assertDoesNotThrow(() -> {
            GymPassApplication.main(new String[] {});
        });
    }

    @Test
    void applicationStarts() {
        assertNotNull(GymPassApplication.class);
        assertTrue(GymPassApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}