import org.cryptoklineproject.service.KlineLoadService;
import org.cryptoklineproject.scheduler.KlineScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KlineSchedulerTest {
    @Mock
    private KlineLoadService klineLoadService;

    @InjectMocks
    private KlineScheduler klineScheduler;

    @Test
    public void testSchedulerLoad(){
        klineScheduler.scheduledLoad();
        verify(klineLoadService).loadDataBatch("BTCUSDT", null, null);
        verify(klineLoadService).loadDataBatch("ETHUSDT",  null, null);
        verify(klineLoadService).loadDataBatch("SOLUSDT", null, null);
    }

    @Test
    public void testCleanupDataInMemory(){
        klineScheduler.cleanupDataInMemory();
    }
}
