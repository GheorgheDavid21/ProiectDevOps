package loyalty.service;

import loyalty.dto.auth.AuthResponse;
import loyalty.dto.auth.LoginRequest;
import loyalty.dto.auth.RegisterRequest;
import loyalty.entity.User;
import loyalty.exception.EmailAlreadyUsedException;
import loyalty.repository.UserRepository;
import loyalty.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyUsedException("Email is already in use");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        userRepository.save(user);

        return new AuthResponse(tokenProvider.generateToken(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Wrong email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Wrong email or password");
        }

        return new AuthResponse(tokenProvider.generateToken(user));
    }
}
