package loyalty.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TransactionMetrics {
    private final Counter validatedTransactions;

    public TransactionMetrics(MeterRegistry registry) {
        this.validatedTransactions = registry.counter("transactions.validated.total");
    }

    public void incrementValidated() {
        validatedTransactions.increment();
    }
}
