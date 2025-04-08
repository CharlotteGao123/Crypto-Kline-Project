import org.cryptoklineproject.controllers.LoadController;
import org.cryptoklineproject.service.KlineLoadService;
import org.cryptoklineproject.service.ValidationInputService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoadControllerTest {
    @Mock
    private ValidationInputService validationInputService;

    @Mock
    private KlineLoadService klineLoadService;

    @InjectMocks
    private LoadController controller;

    @Test
    public void testLoadData() {
        String symbol = "BTCUSDT";
        Long startTime = 123L;
        Long endTime = 456L;

        controller.loadData(symbol, startTime, endTime);

        Mockito.verify(validationInputService).validateSymbol(symbol);
        Mockito.verify(validationInputService).validateTimeRange(startTime, endTime);
        Mockito.verify(klineLoadService).loadDataBatch(symbol, startTime, endTime);
    }
}
