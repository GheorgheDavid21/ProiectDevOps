package loyalty.service;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class QrServiceHealthIndicator implements HealthIndicator {
    private final QrServiceClient qrServiceClient;

    public QrServiceHealthIndicator(QrServiceClient qrServiceClient) {
        this.qrServiceClient = qrServiceClient;
    }

    @Override
    public Health health() {
        try {
            boolean up = qrServiceClient.ping();
            if (up) {
                return Health.up().withDetail("qr-service", "available").build();
            }
            return Health.down().withDetail("qr-service", "not responding").build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
