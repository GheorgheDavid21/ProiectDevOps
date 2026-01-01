package gateway.controller;

import gateway.dto.transaction.TransactionRequest;
import gateway.dto.transaction.TransactionResponse;
import gateway.entity.Transaction;
import gateway.entity.User;
import gateway.exception.EntityNotFoundException;
import gateway.repository.UserRepository;
import gateway.service.TransactionService;
import io.swagger.v3.oas.annotations.Parameter;
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
    public ResponseEntity<TransactionResponse> earn(@Parameter(hidden = true) @AuthenticationPrincipal String email,
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
