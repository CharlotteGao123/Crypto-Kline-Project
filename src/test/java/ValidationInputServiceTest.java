import org.cryptoklineproject.model.exception.InputParamInvalidException;
import org.cryptoklineproject.service.BinanceDataFetcher;
import org.cryptoklineproject.service.ValidationInputService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationInputServiceTest {
    @Mock
    private BinanceDataFetcher binanceDataFetcher;

    @InjectMocks
    private ValidationInputService validationInputService;

    @Test
    public void test_validationSymbol_invalid() {
        when(binanceDataFetcher.fetchSymbolInfo()).thenReturn("{\"symbols\": [{\"symbol\": \"BTCUSDT\"}, {\"symbol\": \"ETHUSDT\"}, {\"symbol\": \"BNBUSDT\"}]}");
        InputParamInvalidException ex = assertThrows(InputParamInvalidException.class, () -> {
            validationInputService.validateSymbol("a");
        });
        assertThat(ex.getMessage(), containsString("a is invalid"));
    }

    @Test
    public void test_validateSymbol_valid() {
        when(binanceDataFetcher.fetchSymbolInfo()).thenReturn("{\"symbols\": [{\"symbol\": \"BTCUSDT\"}, {\"symbol\": \"ETHUSDT\"}, {\"symbol\": \"BNBUSDT\"}]}");

        validationInputService.validateSymbol("BTCUSDT");
    }

    @Test
    public void test_validateInterval_invalid() {
        InputParamInvalidException ex = assertThrows(InputParamInvalidException.class, () -> {
            validationInputService.validateInterval("2m");
        });
        assertThat(ex.getMessage(), containsString("interval=2m is invalid"));
    }

    @Test
    public void test_validateInterval_valid() {
        validationInputService.validateInterval("1m");
    }

    @Test
    public void test_validateTimeRange_invalid() {
        InputParamInvalidException ex = assertThrows(InputParamInvalidException.class, () -> {
            validationInputService.validateTimeRange(2000L, 1000L);
        });
        assertThat(ex.getMessage(), containsString("startTime must be < endTime"));
    }

    @Test
    public void test_validateTimeRange_valid() {
        validationInputService.validateTimeRange(1000L, 2000L);
    }
}
