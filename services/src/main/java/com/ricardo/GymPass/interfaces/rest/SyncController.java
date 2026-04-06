package com.ricardo.GymPass.interfaces.rest;

import com.ricardo.GymPass.application.dto.SyncResult;
import com.ricardo.GymPass.application.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sync")
public class SyncController {

    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<SyncResult> refresh() {
        logger.info("Received sync refresh request");
        SyncResult result = syncService.sync();
        logger.info("Sync completed: usersCreated={}, usersUpdated={}, membershipsCreated={}, membershipsUpdated={}",
                    result.usersCreated(), result.usersUpdated(), 
                    result.membershipsCreated(), result.membershipsUpdated());
        return ResponseEntity.ok(result);
    }
}
