package loyalty.dto.bar;

import loyalty.entity.Bar;

import java.util.UUID;

public record BarResponse(
        UUID id,
        String name,
        String address,
        UUID adminID
) {
    public static BarResponse fromEntity(Bar bar) {
        return new BarResponse(
                bar.getId(),
                bar.getName(),
                bar.getAddress(),
                bar.getAdmin().getId() != null ? bar.getAdmin().getId() : null
        );
    }
}
