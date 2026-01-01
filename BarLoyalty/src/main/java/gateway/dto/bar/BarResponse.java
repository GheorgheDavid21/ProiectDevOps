package gateway.dto.bar;

import gateway.entity.Bar;

import java.util.UUID;

public record BarResponse(
        UUID id,
        String name,
        String address,
        String adminEmail
) {
    public static BarResponse fromEntity(Bar bar) {
        return new BarResponse(
                bar.getId(),
                bar.getName(),
                bar.getAddress(),
                bar.getAdmin() != null ? bar.getAdmin().getEmail() : null
        );
    }
}
