package gateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class QrServiceClient {
    private final RestTemplate restTemplate;
    private final String qrServiceUrl;

    public QrServiceClient(RestTemplate restTemplate, @Value("${qr.service.url}") String qrServiceUrl) {
        this.restTemplate = restTemplate;
        this.qrServiceUrl = qrServiceUrl;
    }

    public String generateQrCode(UUID transactionId) {
        Map<String, String> request = Map.of("transaction_id", transactionId.toString());

        Map response = restTemplate.postForObject(
                qrServiceUrl + "/qr/generate",
                request,
                Map.class
        );
        if (response == null || !response.containsKey("qr_code")) {
            throw new RuntimeException("Invalid response from QR Service");
        }
        return response.get("qr_code").toString();
    }

    public boolean validateQrCode(String qrCode) {
        Map<String, String> request = Map.of("qr_code", qrCode);

        try {
            Map response = restTemplate.postForObject(
                    qrServiceUrl + "/qr/validate",
                    request,
                    Map.class
            );

            return response != null && "VALIDATED".equals(response.get("status"));
        } catch (Exception e){
            return false;
        }
//        return Boolean.TRUE.equals(response.get("qrCode"));
    }

    public boolean ping() {
        return restTemplate
                .getForEntity(qrServiceUrl + "/health", String.class)
                .getStatusCode()
                .is2xxSuccessful();
    }
}