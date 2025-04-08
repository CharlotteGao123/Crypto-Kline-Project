import lombok.Value;
import org.cryptoklineproject.mapper.KlineDataMapper;
import org.cryptoklineproject.model.KlineData;
import org.cryptoklineproject.service.BinanceDataFetcher;
import org.cryptoklineproject.service.KlineLoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class KlineLoadServiceTest {
    @Mock
    private BinanceDataFetcher binanceDataFetcher;

    @Mock
    private KlineDataMapper klineDataMapper;

    @InjectMocks
    private KlineLoadService klineLoadService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(klineLoadService, "defaultLimit", 500);
        ReflectionTestUtils.setField(klineLoadService, "interval", "1m");
    }

    @Test
    public void testLoadDataBatch_singleChunk() {
        String symbol = "BTCUSDT";

        long startTime = 1_000_000L;
        long endTime = 1_000_000L + 9000L;

        KlineData testKlineData = new KlineData();
        testKlineData.setSymbol(symbol);
        testKlineData.setOpenTime(1_000_000L);
        testKlineData.setOpenPrice(new BigDecimal("50000"));
        testKlineData.setVersion(0L);
        List<KlineData> testList = List.of(testKlineData);

        when(binanceDataFetcher.fetchKlines(eq(symbol), anyLong(), anyLong(), eq(500)))
                .thenReturn(testList);

        klineLoadService.loadDataBatch(symbol, startTime, endTime);

        verify(binanceDataFetcher, times(1))
                .fetchKlines(eq(symbol), eq(startTime), eq(endTime), eq(500));

        verify(klineDataMapper, times(1)).insertKlineDataBatch(testList);

        assertTrue(testKlineData.getVersion() > 0);
    }
}
