package gateway.service;

import gateway.entity.Bar;
import gateway.entity.Transaction;
import gateway.entity.User;
import gateway.metrics.TransactionMetrics;
import gateway.repository.BarRepository;
import gateway.repository.TransactionRepository;
import gateway.repository.UserRepository;
import gateway.util.TransactionStatus;
import gateway.util.TransactionType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BarRepository barRepository;
    private final QrServiceClient qrServiceClient;
    private final WebSocketEventService wsService;
    private final TransactionMetrics transactionMetrics;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              BarRepository barRepository,
                              QrServiceClient qrServiceClient,
                              WebSocketEventService wsService, TransactionMetrics transactionMetrics) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.barRepository = barRepository;
        this.qrServiceClient = qrServiceClient;
        this.wsService = wsService;
        this.transactionMetrics = transactionMetrics;
    }

    public List<Transaction> findAllByUserId(UUID userId) {
        return transactionRepository.findByUserId(userId);
    }

    public Transaction createEarnTransaction(UUID userId, UUID barId, int points) {
        User user = userRepository.findById(userId).orElseThrow();
        Bar bar = barRepository.findById(barId).orElseThrow();

        Transaction tr = new Transaction();
        tr.setUser(user);
        tr.setBar(bar);
        tr.setType(TransactionType.EARN);
        tr.setPoints(points);
        tr.setStatus(TransactionStatus.PENDING);

        tr = transactionRepository.save(tr);

        String qrCode = qrServiceClient.generateQrCode(tr.getId());
        tr.setQrCode(qrCode);

        return transactionRepository.save(tr);
    }

    public void validateTransaction(String qrCode) {
        Transaction tr = transactionRepository
                .findByQrCode(qrCode)
                .orElseThrow();

        boolean valid = qrServiceClient.validateQrCode(qrCode);
        if (!valid) {
            tr.setStatus(TransactionStatus.REJECTED);
            transactionRepository.save(tr);
            wsService.sendEvent(tr.getUser().getId(), "TRANSACTION_REJECTED");
            return;
        }
        tr.setStatus(TransactionStatus.VALIDATED);
        transactionMetrics.incrementValidated();
        User user = tr.getUser();
        user.setPoints(user.getPoints() + tr.getPoints());

        userRepository.save(user);
        transactionRepository.save(tr);

        wsService.sendBalanceUpdate(user.getId(), user.getPoints());
        wsService.sendEvent(user.getId(), "TRANSACTION_VALIDATED");
    }

}
