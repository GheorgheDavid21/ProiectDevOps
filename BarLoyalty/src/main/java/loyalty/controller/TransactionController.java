package loyalty.controller;

import loyalty.dto.transaction.TransactionRequest;
import loyalty.dto.transaction.TransactionResponse;
import loyalty.entity.Transaction;
import loyalty.entity.User;
import loyalty.exception.EntityNotFoundException;
import loyalty.repository.UserRepository;
import loyalty.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserRepository userRepository;

    public TransactionController(TransactionService transactionService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;

    }

    @PostMapping("/earn")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<TransactionResponse> earn(@AuthenticationPrincipal String email,
                                                    @RequestBody TransactionRequest request) {

        UUID userId = userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Transaction tr = transactionService.createEarnTransaction(userId, request.barId(), request.points());
        return ResponseEntity.ok(TransactionResponse.fromEntity(tr));
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('BAR_ADMIN')")
    public ResponseEntity<Void> validate(@RequestParam String qrCode) {
        transactionService.validateTransaction(qrCode);
        return ResponseEntity.ok().build();
    }

}
