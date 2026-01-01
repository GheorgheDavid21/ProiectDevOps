package gateway.service;

import lombok.AllArgsConstructor;
import gateway.dto.reward.RewardRequest;
import gateway.dto.reward.RewardResponse;
import gateway.entity.Bar;
import gateway.entity.Reward;
import gateway.entity.Transaction;
import gateway.entity.User;
import gateway.repository.BarRepository;
import gateway.repository.RewardRepository;
import gateway.repository.TransactionRepository;
import gateway.repository.UserRepository;
import gateway.util.TransactionStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RewardService {
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final BarRepository barRepository;
    private final TransactionRepository transactionRepository;
    private final WebSocketEventService wsService;


    public RewardResponse createReward(UUID adminId, RewardRequest request) {
        Bar bar = barRepository.findByAdminId(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Bar not found"));

        Reward reward = new Reward();
        reward.setName(request.name());
        reward.setDescription(request.description());
        reward.setPointsRequired(request.pointsRequired());
        reward.setBar(bar);

        return RewardResponse.fromEntity(rewardRepository.save(reward));
    }

    public List<RewardResponse> listRewards(UUID barId) {
        return rewardRepository.findByBarIdAndActiveTrue(barId).stream()
                .map(RewardResponse::fromEntity)
                .toList();
    }

    public void redeemReward(UUID userId, UUID rewardId) {
        User user = userRepository.findById(userId).orElseThrow();
        Reward reward = rewardRepository.findById(rewardId).orElseThrow();

        Transaction tr = new Transaction();
        tr.setUser(user);
        tr.setBar(reward.getBar());
        tr.setType(gateway.util.TransactionType.REDEEM);
        tr.setPoints(reward.getPointsRequired());

        if(!reward.isActive() || user.getPoints() < reward.getPointsRequired()){
            tr.setStatus(TransactionStatus.REJECTED);
            transactionRepository.save(tr);
            wsService.sendEvent(userId, "REWARD_REDEEM_FAILED");
        }

        user.setPoints(user.getPoints() - reward.getPointsRequired());
        tr.setStatus(TransactionStatus.VALIDATED);

        userRepository.save(user);
        transactionRepository.save(tr);

        wsService.sendBalanceUpdate(userId, user.getPoints());
        wsService.sendEvent(userId, "REWARD_REDEEMED");
    }
}
