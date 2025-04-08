package org.cryptoklineproject.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.cryptoklineproject.service.KlineLoadService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableScheduling
public class KlineScheduler {
    private final KlineLoadService klineLoadService;

    public KlineScheduler(KlineLoadService klineLoadService) {
        this.klineLoadService = klineLoadService;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void scheduledLoad() {
        // TODO database store
        String[] symbols = {"BTCUSDT", "ETHUSDT", "SOLUSDT"};
        for(String sym: symbols) {
            try {
                log.info("[KlineScheduler] scheduledLoad -> auto loadDataBatch(BTCUSDT,1m,100)");
                klineLoadService.loadDataBatch(sym, null, null);
            }catch (Exception e){
                log.error("[KlineScheduler] Error storing data for symbol " + sym, e);
            }
        }
    }
    public void cleanupDataInMemory() {
        log.info("cleanupDataInMemory() - do some memory-level cleanup, no DB delete");
    }
}
