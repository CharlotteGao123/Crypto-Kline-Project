import org.cryptoklineproject.model.KlineData;
import org.cryptoklineproject.service.BinanceDataFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//TODO put each test file under the same package
@ExtendWith(MockitoExtension.class)
public class BinanceDataFetcherTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BinanceDataFetcher binanceDataFetcher;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(binanceDataFetcher, "BASE_URL", "https://test-url.com/api/v3");
        ReflectionTestUtils.setField(binanceDataFetcher, "symbolApi", "/exchangeInfo");
        ReflectionTestUtils.setField(binanceDataFetcher, "klineApi", "/klines");
        ReflectionTestUtils.setField(binanceDataFetcher, "defaultLimit", 500);
        ReflectionTestUtils.setField(binanceDataFetcher, "interval", "1m");
    }

    @Test
    public void testValidSymbol_valid() {
        String jsonResponse = "{\n" +
                "  \"timezone\": \"UTC\",\n" +
                "  \"serverTime\": 1665059971359,\n" +
                "  \"symbols\": [\n" +
                "    {\"symbol\": \"BTCUSDT\"},\n" +
                "    {\"symbol\": \"ETHUSDT\"},\n" +
                "    {\"symbol\": \"BNBUSDT\"}\n" +
                "  ]\n" +
                "}";

        String expectedUrl = "https://test-url.com/api/v3/exchangeInfo";
        when(restTemplate.getForObject(expectedUrl, String.class))
                .thenReturn(jsonResponse);

        Boolean valid = binanceDataFetcher.validSymbol("BTCUSDT");
        assertTrue(valid, "BTCUSDT valid");
    }

    @Test
    public void testValidSymbol_invalid() {
        String jsonResponse = "{\n" +
                "  \"timezone\": \"UTC\",\n" +
                "  \"serverTime\": 1665059971359,\n" +
                "  \"symbols\": [\n" +
                "    {\"symbol\": \"BTCUSDT\"},\n" +
                "    {\"symbol\": \"ETHUSDT\"},\n" +
                "    {\"symbol\": \"BNBUSDT\"}\n" +
                "  ]\n" +
                "}";

        String expectedUrl = "https://test-url.com/api/v3/exchangeInfo";
        lenient().when(restTemplate.getForObject(expectedUrl, String.class))
                .thenReturn(jsonResponse);


        Boolean valid = binanceDataFetcher.validSymbol("INVALID");
        assertFalse(valid, "INVALID");
    }

    @Test
    public void testValidSymbol_caching() {
        String jsonResponse = "{\n" +
                "  \"timezone\": \"UTC\",\n" +
                "  \"serverTime\": 1665059971359,\n" +
                "  \"symbols\": [\n" +
                "    {\"symbol\": \"BTCUSDT\"},\n" +
                "    {\"symbol\": \"ETHUSDT\"},\n" +
                "    {\"symbol\": \"BNBUSDT\"}\n" +
                "  ]\n" +
                "}";
        String expectedUrl = "https://test-url.com/api/v3/exchangeInfo";
        when(restTemplate.getForObject(expectedUrl, String.class))
                .thenReturn(jsonResponse);

        assertTrue(binanceDataFetcher.validSymbol("BTCUSDT"));
        assertTrue(binanceDataFetcher.validSymbol("BTCUSDT"));

        verify(restTemplate, times(1)).getForObject(expectedUrl, String.class);
    }

    @Test
    public void testFetchRecentKlines() {
        String symbol = "BTCUSDT";

        String[][] sampleResponse = new String[][] {
                { "1000", "50000", "51000", "49000", "50500", "100.0", "1010", "200.0", "150" }
        };

        String expectedUrl = "https://test-url.com/api/v3/klines?symbol=" + symbol
                + "&interval=1m&limit=500";
        when(restTemplate.getForObject(expectedUrl, String[][].class))
                .thenReturn(sampleResponse);

        List<KlineData> list = binanceDataFetcher.fetchRecentKlines(symbol);
        assertNotNull(list);
        assertEquals(1, list.size());
        KlineData data = list.get(0);

        assertEquals(symbol, data.getSymbol());
        assertEquals(1000L, data.getOpenTime());
        assertEquals(new BigDecimal("50000"), data.getOpenPrice());
        assertEquals(new BigDecimal("51000"), data.getHighPrice());
        assertEquals(new BigDecimal("49000"), data.getLowPrice());
        assertEquals(new BigDecimal("50500"), data.getClosePrice());
    }

    @Test
    public void testFetchKlines() {
        String symbol = "BTCUSDT";
        long startTime = 2000L;
        long endTime = 3000L;
        int limit = 50;

        String[][] sampleResponse = new String[][] {
                { "2000", "60000", "61000", "59000", "60500", "150.0", "3010", "250.0", "200" },
                { "2500", "60500", "61500", "60000", "61000", "120.0", "3500", "220.0", "180" }
        };
        String expectedUrl = "https://test-url.com/api/v3/klines?symbol=" + symbol
                + "&interval=1m&limit=" + limit
                + "&startTime=" + startTime + "&endTime=" + endTime;
        when(restTemplate.getForObject(expectedUrl, String[][].class))
                .thenReturn(sampleResponse);

        List<KlineData> list = binanceDataFetcher.fetchKlines(symbol, startTime, endTime, limit);
        assertNotNull(list);
        assertEquals(2, list.size());
        // check first one
        KlineData data1 = list.get(0);
        assertEquals(symbol, data1.getSymbol());
        assertEquals(2000L, data1.getOpenTime());
        assertEquals(new BigDecimal("60000"), data1.getOpenPrice());
        // check second one
        KlineData data2 = list.get(1);
        assertEquals(symbol, data2.getSymbol());
        assertEquals(2500L, data2.getOpenTime());
        assertEquals(new BigDecimal("60500"), data2.getOpenPrice());
    }
}
