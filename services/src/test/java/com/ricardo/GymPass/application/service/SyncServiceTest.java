package com.ricardo.GymPass.application.service;

import com.ricardo.GymPass.application.dto.ExternalMemberDto;
import com.ricardo.GymPass.application.dto.ExternalMembershipDto;
import com.ricardo.GymPass.application.dto.SyncResponse;
import com.ricardo.GymPass.application.dto.SyncResult;
import com.ricardo.GymPass.domain.entity.Gym;
import com.ricardo.GymPass.domain.entity.Membership;
import com.ricardo.GymPass.domain.entity.User;
import com.ricardo.GymPass.domain.enums.UserRole;
import com.ricardo.GymPass.domain.exception.ExternalApiException;
import com.ricardo.GymPass.domain.repository.GymRepository;
import com.ricardo.GymPass.domain.repository.MembershipRepository;
import com.ricardo.GymPass.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SyncServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private GymRepository gymRepository;

    @Mock
    private RestTemplate restTemplate;

    private SyncService syncService;

    @BeforeEach
    void setUp() {
        syncService = new SyncService(userRepository, membershipRepository, gymRepository, restTemplate);
        ReflectionTestUtils.setField(syncService, "externalApiBaseUrl", "http://localhost:8080");
    }

    @Test
    void sync_createsNewUsers_success() {
        List<ExternalMemberDto> members = List.of(new ExternalMemberDto("ext-001", null, null));
        List<ExternalMembershipDto> memberships = List.of();
        SyncResponse response = new SyncResponse(null, members, memberships);

        Gym gym = new Gym();
        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.of(gym));
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(response);
        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        SyncResult result = syncService.sync();

        assertNotNull(result);
        assertEquals(1, result.usersCreated());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void sync_updatesExistingUsers_success() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setExternalId("ext-001");
        existingUser.setPasswordHash("");
        existingUser.setRole(UserRole.MEMBER);
        existingUser.setImported(true);

        List<ExternalMemberDto> members = List.of(new ExternalMemberDto("ext-001", null, null));
        List<ExternalMembershipDto> memberships = List.of();
        SyncResponse response = new SyncResponse(null, members, memberships);

        Gym gym = new Gym();
        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.of(gym));
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(response);
        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        SyncResult result = syncService.sync();

        assertNotNull(result);
        assertEquals(1, result.usersUpdated());
    }

    @Test
    void sync_skipsUnknownUserMemberships() {
        ExternalMembershipDto membershipDto = new ExternalMembershipDto(
                "mem-001", "user-999", "ACTIVE", "BASIC",
                LocalDate.now(), LocalDate.now().plusMonths(1),
                null, null
        );

        List<ExternalMemberDto> members = List.of();
        List<ExternalMembershipDto> memberships = List.of(membershipDto);
        SyncResponse response = new SyncResponse(null, members, memberships);

        Gym gym = new Gym();
        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.of(gym));
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(response);

        SyncResult result = syncService.sync();

        assertNotNull(result);
        assertEquals(0, result.membershipsCreated());
        verify(membershipRepository, never()).save(any(Membership.class));
    }

    @Test
    void sync_noDataFromApi_returnsEmptyResult() {
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(null);

        SyncResult result = syncService.sync();

        assertNotNull(result);
        assertEquals(0, result.usersCreated());
    }

    @Test
    void sync_externalApiError_throwsException() {
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenThrow(new ExternalApiException("API error", 500));

        assertThrows(ExternalApiException.class, () -> syncService.sync());
    }

@Test
    void sync_handlesNullMemberResponse_date() {
        List<ExternalMemberDto> members = List.of();
        List<ExternalMembershipDto> memberships = List.of();
        SyncResponse response = new SyncResponse(null, members, memberships);

        Gym gym = new Gym();
        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.of(gym));
        when(gymRepository.findAll()).thenReturn(java.util.List.of(gym));

        SyncResult result = syncService.sync();

        assertNotNull(result);
    }

    @Test
    void sync_handlesNullResponseDate() {
        List<ExternalMemberDto> members = List.of();
        List<ExternalMembershipDto> memberships = List.of();
        SyncResponse response = new SyncResponse(null, members, memberships);

        Gym gym = new Gym();
        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.of(gym));
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(response);

        SyncResult result = syncService.sync();

        assertNotNull(result);
    }

    @Test
    void sync_updatesMembership_success() {
        User user = new User();
        user.setId(1L);
        user.setExternalId("user-001");

        Membership existingMembership = new Membership();
        existingMembership.setId(1L);
        existingMembership.setExternalId("mem-001");
        existingMembership.setUser(user);

        ExternalMembershipDto membershipDto = new ExternalMembershipDto(
                "mem-001", "user-001", "ACTIVE", "PREMIUM",
                LocalDate.now(), LocalDate.now().plusMonths(1),
                Instant.now(), Instant.now()
        );

        List<ExternalMemberDto> members = List.of();
        List<ExternalMembershipDto> memberships = List.of(membershipDto);
        SyncResponse response = new SyncResponse(null, members, memberships);

        Gym gym = new Gym();
        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.of(gym));
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(response);
        when(userRepository.findByExternalId("user-001")).thenReturn(Optional.of(user));
        when(membershipRepository.findByExternalId("mem-001")).thenReturn(Optional.of(existingMembership));
        when(membershipRepository.save(any(Membership.class))).thenAnswer(i -> i.getArgument(0));

        SyncResult result = syncService.sync();

        assertNotNull(result);
        assertEquals(1, result.membershipsUpdated());
    }

    @Test
    void sync_createsMembership_success() {
        User user = new User();
        user.setId(1L);
        user.setExternalId("user-001");

        ExternalMembershipDto membershipDto = new ExternalMembershipDto(
                "mem-001", "user-001", "ACTIVE", "PREMIUM",
                LocalDate.now(), LocalDate.now().plusMonths(1),
                Instant.now(), Instant.now()
        );

        List<ExternalMemberDto> members = List.of();
        List<ExternalMembershipDto> memberships = List.of(membershipDto);
        SyncResponse response = new SyncResponse(null, members, memberships);

        Gym gym = new Gym();
        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.of(gym));
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(response);
        when(userRepository.findByExternalId("user-001")).thenReturn(Optional.of(user));
        when(membershipRepository.findByExternalId("mem-001")).thenReturn(Optional.empty());
        when(membershipRepository.save(any(Membership.class))).thenAnswer(i -> i.getArgument(0));

        SyncResult result = syncService.sync();

        assertNotNull(result);
        assertEquals(1, result.membershipsCreated());
    }

    @Test
    void sync_clientError_throwsExternalApiException() {
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenThrow(new org.springframework.web.client.HttpClientErrorException(
                        org.springframework.http.HttpStatus.BAD_REQUEST, "Bad request"));

        assertThrows(ExternalApiException.class, () -> syncService.sync());
    }

    @Test
    void sync_serverError_throwsExternalApiException() {
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenThrow(new org.springframework.web.client.HttpServerErrorException(
                        org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Server error"));

        assertThrows(ExternalApiException.class, () -> syncService.sync());
    }

    @Test
    void sync_noActiveGym_fallsBackToFirstGym() {
        List<ExternalMemberDto> members = List.of();
        List<ExternalMembershipDto> memberships = List.of();
        SyncResponse response = new SyncResponse(null, members, memberships);

        Gym gym = new Gym();
        gym.setActive(true);
        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.empty());
        when(gymRepository.findAll()).thenReturn(List.of(gym));
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(response);

        SyncResult result = syncService.sync();

        assertNotNull(result);
    }

    @Test
    void sync_noGymInDatabase_throwsException() {
        List<ExternalMemberDto> members = List.of();
        List<ExternalMembershipDto> memberships = List.of();
        SyncResponse response = new SyncResponse(null, members, memberships);

        when(gymRepository.findFirstByActiveTrue()).thenReturn(Optional.empty());
        when(gymRepository.findAll()).thenReturn(List.of());
        when(restTemplate.getForObject(contains("/api/v1/sync/today"), eq(SyncResponse.class)))
                .thenReturn(response);

        assertThrows(com.ricardo.GymPass.domain.exception.ResourceNotFoundException.class, () -> syncService.sync());
    }
}