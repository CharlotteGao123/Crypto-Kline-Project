import org.cryptoklineproject.service.BinanceDataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;
import java.util.List;

@Profile("test")
@Configuration
public class TestBinanceConfig {

    @Bean
    public BinanceDataFetcher binanceDataFetcher() {
        return new BinanceDataFetcher() {
            public void init() {

            }


            public List<String> getAllowedSymbols() {

                return Collections.singletonList("BTCUSDT");
            }
        };
    }
}
