package gateway.config;

import gateway.entity.Bar;
import gateway.entity.User;
import gateway.repository.BarRepository;
import gateway.repository.UserRepository;
import gateway.util.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BarRepository barRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.passwords}")
    private String seedPassword;

    public DataSeeder(UserRepository userRepository, BarRepository barRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.barRepository = barRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        System.out.println("Seeding database with initial data...");

        String encodedPassword = passwordEncoder.encode(seedPassword);

        User client1 = User.builder()
                .email("client1@test.com")
                .passwordHash(encodedPassword)
                .role(Role.CLIENT)
                .points(100)
                .build();

        User client2 = User.builder()
                .email("client2@test.com")
                .passwordHash(encodedPassword)
                .role(Role.CLIENT)
                .points(50)
                .build();

        userRepository.saveAll(List.of(client1, client2));

        User admin1 = User.builder()
                .email("admin1@bar.com")
                .passwordHash(encodedPassword)
                .role(Role.BAR_ADMIN)
                .points(0)
                .build();

        User admin2 = User.builder()
                .email("admin2@bar.com")
                .passwordHash(encodedPassword)
                .role(Role.BAR_ADMIN)
                .points(0)
                .build();

        userRepository.saveAll(List.of(admin1, admin2));

        Bar bar1 = Bar.builder()
                .name("The Rusty Spoon")
                .address("Strada Principala nr. 1")
                .admin(admin1)
                .build();

        Bar bar2 = Bar.builder()
                .name("Temple")
                .address("Bulevardul Eroilor nr. 404")
                .admin(admin2)
                .build();

        barRepository.saveAll(List.of(bar1, bar2));

        System.out.println("Database seeded successfully: 2 Clients, 2 Admins, 2 Bars created.");
    }
}
