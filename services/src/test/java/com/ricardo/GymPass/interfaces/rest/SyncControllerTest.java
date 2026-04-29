package com.ricardo.GymPass.interfaces.rest;

import com.ricardo.GymPass.application.dto.SyncResult;
import com.ricardo.GymPass.application.service.SyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncControllerTest {

    @Mock
    private SyncService syncService;

    @InjectMocks
    private SyncController syncController;

    @Test
    void refresh_returns200() {
        SyncResult syncResult = new SyncResult(1, 0, 5, 0, "2026-04-29");
        when(syncService.sync()).thenReturn(syncResult);

        ResponseEntity<SyncResult> response = syncController.refresh();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().usersCreated());
    }
}