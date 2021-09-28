package guru.springframework.mongo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * For streaming demonstration purpose
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteRunner  implements CommandLineRunner {

    private final QuoteGeneratorService quoteGeneratorService;
    private final QuoteHistoryService quoteHistoryService;

    @Override
    public void run(String... args) throws Exception {
        quoteGeneratorService
                .fetchQuoteStream(Duration.ofMillis(100L))
                .log("Got quotes.")
                .take(50)
                .flatMap(quoteHistoryService::saveQuoteToMongo)
                .subscribe(savedQuote -> log.debug("Saved quote: " + savedQuote),
                        throwable -> log.error("Error > ", throwable),
                        () -> log.debug("All done"));
    }
}
