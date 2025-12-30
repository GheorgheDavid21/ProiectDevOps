package loyalty.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class WebSocketEventService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendBalanceUpdate(UUID userId, int newBalance) {
        messagingTemplate.convertAndSend(
                "/topic/balance/" + userId,
                (Object) Map.of("points", newBalance)
        );
    }
    public void sendEvent(UUID userId, String eventType) {
        messagingTemplate.convertAndSend(
                "/topic/events/" + userId,
                (Object) Map.of("event", eventType)
        );
    }
}
