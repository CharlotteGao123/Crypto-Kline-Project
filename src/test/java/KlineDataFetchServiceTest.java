import org.cryptoklineproject.mapper.KlineDataMapper;
import org.cryptoklineproject.model.KlineData;
import org.cryptoklineproject.model.KlineInterval;
import org.cryptoklineproject.service.KlineDataFetchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KlineDataFetchServiceTest {

    @Mock
    private KlineDataMapper klineDataMapper;

    @InjectMocks
    private KlineDataFetchService fetchService;

    @Test
    public void testFindKlinesInRange() {
        String symbol = "BTCUSDT";
        Long startTime = 1000L;
        Long endTime = 2000L;
        Long version = 1L;
        String interval = "1m";

        KlineData kl1 = new KlineData();

        List<KlineData> expected = List.of(kl1);
        when(klineDataMapper.findKlinesInRange(symbol, startTime, endTime, version))
                .thenReturn(expected);

        List<KlineData> actual = fetchService.findKlinesInRange(symbol, startTime, endTime, version, KlineInterval.parseKlineinterval(interval));

        assertEquals(expected, actual);

        verify(klineDataMapper, times(1)).findKlinesInRange(symbol, startTime, endTime, version);
    }
}