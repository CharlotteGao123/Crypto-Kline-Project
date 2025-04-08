import org.cryptoklineproject.controllers.LoadController;
import org.cryptoklineproject.controllers.ReceiveController;
import org.cryptoklineproject.model.KlineData;
import org.cryptoklineproject.model.KlineInterval;
import org.cryptoklineproject.service.KlineDataFetchService;
import org.cryptoklineproject.service.ValidationInputService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReceiveControllerTest {

    @Mock
    private ValidationInputService validationInputService;

    @Mock
    private KlineDataFetchService fetchService;

    @InjectMocks
    private ReceiveController controller;

    @Test
    public void testGetKlinesInRange(){
        String symbol = "BTCUSDT";
        Long startTime = 1000L;
        Long endTime = 2000L;
        Long version = 1L;
        String interval = "1m";

        List<KlineData> expect = List.of(new KlineData());
        when(validationInputService.validateInterval(anyString()))
                .thenReturn(KlineInterval.ONE_MINUTE);

        when(fetchService.findKlinesInRange(symbol, startTime, endTime, version, KlineInterval.parseKlineinterval(interval)))
                .thenReturn(expect);

        List<KlineData> result = controller.getKlinesInRange(symbol, startTime, endTime, version, interval);
        assertEquals(expect, result);

        Mockito.verify(validationInputService).validateInterval(interval);
        Mockito.verify(validationInputService).validateSymbol(symbol);
        Mockito.verify(validationInputService).validateTimeRange(startTime, endTime);

        verify(fetchService).findKlinesInRange(symbol, startTime, endTime, version, KlineInterval.parseKlineinterval(interval));
    }
}
