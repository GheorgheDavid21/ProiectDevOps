package gateway.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import gateway.dto.reward.RewardRequest;
import gateway.dto.reward.RewardResponse;
import gateway.entity.User;
import gateway.exception.EntityNotFoundException;
import gateway.repository.UserRepository;
import gateway.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('BAR_ADMIN')")
    public ResponseEntity<RewardResponse> create(@Parameter(hidden = true) @AuthenticationPrincipal String email,
                                                 @RequestBody RewardRequest rewardRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return ResponseEntity.ok(rewardService.createReward(user.getId(), rewardRequest));
    }

    @GetMapping("/bar/{barId}")
    public ResponseEntity<List<RewardResponse>> list(@PathVariable UUID barId) {
        return ResponseEntity.ok(rewardService.listRewards(barId));
    }

    @GetMapping("/{rewardId}/redeem")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> redeem(@Parameter(hidden = true) @AuthenticationPrincipal String email,
                                               @PathVariable UUID rewardId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        rewardService.redeemReward(user.getId(), rewardId);
        return ResponseEntity.ok().build();
    }
}
