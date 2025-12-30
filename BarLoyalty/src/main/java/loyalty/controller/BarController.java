package loyalty.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import loyalty.dto.bar.BarRequest;
import loyalty.dto.bar.BarResponse;
import loyalty.entity.Bar;
import loyalty.entity.User;
import loyalty.repository.BarRepository;
import loyalty.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/bars")
@PreAuthorize("hasRole('BAR_ADMIN')")
@AllArgsConstructor
public class BarController {
    private final BarRepository barRepository;
    private final UserRepository userRepository;

    @PostMapping
    public BarResponse createBar(@AuthenticationPrincipal String email, @RequestBody BarRequest barRequest) {
        User admin = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Bar bar = new Bar();
        bar.setName(barRequest.name());
        bar.setAddress(barRequest.address());
        bar.setAdmin(admin);

        barRepository.save(bar);
        return BarResponse.fromEntity(bar);
    }

    @GetMapping("/my")
    public BarResponse myBar(@AuthenticationPrincipal String email) {
        User admin = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Bar bar = barRepository.findByAdminId(admin.getId()).orElseThrow();
        return BarResponse.fromEntity(bar);
    }
}
