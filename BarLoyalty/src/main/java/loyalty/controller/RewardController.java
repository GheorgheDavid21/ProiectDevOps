package loyalty.controller;

import lombok.RequiredArgsConstructor;
import loyalty.dto.reward.RewardRequest;
import loyalty.dto.reward.RewardResponse;
import loyalty.entity.User;
import loyalty.exception.EntityNotFoundException;
import loyalty.repository.RewardRepository;
import loyalty.repository.UserRepository;
import loyalty.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('BAR_ADMIN')")
    public ResponseEntity<RewardResponse> create(@AuthenticationPrincipal String email,
                                                 @RequestBody RewardRequest rewardRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return ResponseEntity.ok(rewardService.createReward(user.getId(), rewardRequest));
    }

    @GetMapping("/bar/{barId}")
    @PreAuthorize("hasRole('Client')")
    public ResponseEntity<Void> redeem(@AuthenticationPrincipal String email,
                                               @PathVariable UUID rewardId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        rewardService.redeemReward(user.getId(), rewardId);
        return ResponseEntity.ok().build();
    }
}
