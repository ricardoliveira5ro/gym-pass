package com.ricardo.GymPass.application.service;

import com.ricardo.GymPass.application.dto.ExternalMemberDto;
import com.ricardo.GymPass.application.dto.ExternalMembershipDto;
import com.ricardo.GymPass.application.dto.SyncResponse;
import com.ricardo.GymPass.application.dto.SyncResult;
import com.ricardo.GymPass.domain.entity.Gym;
import com.ricardo.GymPass.domain.entity.Membership;
import com.ricardo.GymPass.domain.entity.User;
import com.ricardo.GymPass.domain.enums.MembershipStatus;
import com.ricardo.GymPass.domain.enums.MembershipTier;
import com.ricardo.GymPass.domain.enums.UserRole;
import com.ricardo.GymPass.domain.exception.ExternalApiException;
import com.ricardo.GymPass.domain.exception.ResourceNotFoundException;
import com.ricardo.GymPass.domain.repository.GymRepository;
import com.ricardo.GymPass.domain.repository.MembershipRepository;
import com.ricardo.GymPass.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class SyncService {

    private static final Logger logger = LoggerFactory.getLogger(SyncService.class);

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final GymRepository gymRepository;
    private final RestTemplate restTemplate;

    @Value("${external.api.base-url}")
    private String externalApiBaseUrl;

    public SyncService(UserRepository userRepository, MembershipRepository membershipRepository, GymRepository gymRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.gymRepository = gymRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public SyncResult sync() {
        logger.info("Starting data sync from external API: {}", externalApiBaseUrl);

        SyncResponse response = fetchFromExternalApi();

        if (response == null) {
            logger.warn("No data received from external API");
            return new SyncResult(0, 0, 0, 0, LocalDate.now().toString());
        }

        logger.info("Received {} members and {} memberships from external API",
                    response.members().size(), response.memberships().size());

        int usersCreated = 0;
        int usersUpdated = 0;
        int membershipsCreated = 0;
        int membershipsUpdated = 0;

        Gym gym = getDefaultGym();

        for (ExternalMemberDto member : response.members()) {
            Optional<User> existingUser = userRepository.findByExternalId(member.id());

            if (existingUser.isPresent()) {
                User user = existingUser.get();
                userRepository.save(user);

                usersUpdated++;
                logger.debug("Updated user: {}", user.getExternalId());
            } else {
                User newUser = new User();

                newUser.setExternalId(member.id());
                newUser.setPasswordHash("");
                newUser.setRole(UserRole.MEMBER);
                newUser.setImported(true);

                userRepository.save(newUser);

                usersCreated++;
                logger.debug("Created user: {}", newUser.getExternalId());
            }
        }

        for (ExternalMembershipDto membershipDto : response.memberships()) {
            Optional<User> userOpt = userRepository.findByExternalId(membershipDto.memberId());
            if (userOpt.isEmpty()) {
                logger.warn("Membership {} references unknown user {}, skipping",
                            membershipDto.id(), membershipDto.memberId());
                continue;
            }

            User user = userOpt.get();
            Optional<Membership> existingMembership = membershipRepository.findByExternalId(membershipDto.id());

            if (existingMembership.isPresent()) {
                Membership membership = existingMembership.get();
                membership.setStatus(parseStatus(membershipDto.status()));
                membership.setTier(parseTier(membershipDto.tier()));
                membership.setStartDate(membershipDto.startDate());
                membership.setEndDate(membershipDto.endDate());

                membershipRepository.save(membership);

                membershipsUpdated++;
                logger.debug("Updated membership: {}", membership.getExternalId());
            } else {
                Membership newMembership = new Membership();
                newMembership.setExternalId(membershipDto.id());
                newMembership.setUser(user);
                newMembership.setGym(gym);
                newMembership.setStatus(parseStatus(membershipDto.status()));
                newMembership.setTier(parseTier(membershipDto.tier()));
                newMembership.setStartDate(membershipDto.startDate());
                newMembership.setEndDate(membershipDto.endDate());

                membershipRepository.save(newMembership);

                membershipsCreated++;
                logger.debug("Created membership: {}", newMembership.getExternalId());
            }
        }

        String syncDate = response.date() != null ? response.date().toString() : LocalDate.now().toString();
        logger.info("Sync completed: {} users created, {} users updated, {} memberships created, {} memberships updated",
                     usersCreated, usersUpdated, membershipsCreated, membershipsUpdated);

        return new SyncResult(usersCreated, usersUpdated, membershipsCreated, membershipsUpdated, syncDate);
    }

    private SyncResponse fetchFromExternalApi() {
        String url = externalApiBaseUrl + "/api/v1/sync/today";
        try {
            return restTemplate.getForObject(url, SyncResponse.class);
        } catch (HttpClientErrorException e) {
            logger.error("Client error from external API: {}", e.getMessage());
            throw new ExternalApiException("External API returned client error: " + e.getMessage(), e.getStatusCode().value());
        } catch (HttpServerErrorException e) {
            logger.error("Server error from external API: {}", e.getMessage());
            throw new ExternalApiException("External API returned server error: " + e.getMessage(), e.getStatusCode().value());
        } catch (Exception e) {
            logger.error("Failed to connect to external API: {}", e.getMessage());
            throw new ExternalApiException("Failed to connect to external API: " + e.getMessage());
        }
    }

    private Gym getDefaultGym() {
        return gymRepository.findFirstByActiveTrue()
                .orElseGet(() -> {
                    logger.warn("No active gym found, using first available gym or creating default");
                    return gymRepository.findAll().stream().findFirst()
                            .orElseGet(() -> {
                                logger.error("No gym found in database. Please create a gym before syncing.");
                                throw new ResourceNotFoundException("Gym", "default");
                            });
                });
    }

    private MembershipStatus parseStatus(String status) {
        if (status == null) {
            return MembershipStatus.ACTIVE;
        }
        try {
            return MembershipStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Unknown membership status: {}, defaulting to ACTIVE", status);
            return MembershipStatus.ACTIVE;
        }
    }

    private MembershipTier parseTier(String tier) {
        if (tier == null) {
            return MembershipTier.BASIC;
        }
        try {
            return MembershipTier.valueOf(tier.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Unknown membership tier: {}, defaulting to BASIC", tier);
            return MembershipTier.BASIC;
        }
    }
}