package gateway.controller;

import gateway.dto.auth.UserResponse;
import gateway.entity.User;
import gateway.exception.EntityNotFoundException;
import gateway.repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
@PreAuthorize("hasRole('CLIENT')")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public UserResponse me(@Parameter(hidden = true) @AuthenticationPrincipal String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserResponse.fromEntity(user);
    }

    @GetMapping("/points")
    public int points(@Parameter(hidden = true) @AuthenticationPrincipal String email) {
        return userRepository.findByEmail(email)
                .orElseThrow()
                .getPoints();
    }
}
