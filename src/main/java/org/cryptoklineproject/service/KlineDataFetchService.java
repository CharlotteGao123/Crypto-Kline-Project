package org.cryptoklineproject.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cryptoklineproject.mapper.KlineDataMapper;
import org.cryptoklineproject.model.KlineData;
import org.cryptoklineproject.model.KlineInterval;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Validated
public class KlineDataFetchService {
    private final KlineDataMapper klineDataMapper;

    public KlineDataFetchService(KlineDataMapper klineDataMapper) {
        this.klineDataMapper = klineDataMapper;
    }

    public @NotEmpty List<@NotNull @Valid KlineData> findKlinesInRange(@NotNull String symbol,
                                                                       @NotNull Long startTime,
                                                                       @NotNull Long endTime,
                                                                       @NotNull Long version,
                                                                       @NotBlank KlineInterval interval) {


        log.info("Fetching klines for symbol={}, startTime={}, endTime={}, version={}, interval={}",
                symbol, startTime, endTime, version, interval);
        List<KlineData> data1m = klineDataMapper.findKlinesInRange(symbol, startTime, endTime, version);
        return this.covert(data1m, interval);
    }



    private List<KlineData> covert(List<KlineData> data1M, KlineInterval interval){
        // todo fix find klines in range by interval based on we only stored 1m data
        //  how to aggregate
        if (data1M == null || data1M.isEmpty()) {
            return List.of();
        }

        if (interval == KlineInterval.ONE_MINUTE) {
            return data1M;
        }

        long intervalMillis = interval.getMillis();
        List<KlineData> aggregated = new ArrayList<>();

        data1M.sort(Comparator.comparingLong(KlineData::getOpenTime));

        KlineData currentAggregate = null;
        long currentGroupStartTime = 0;

        for (KlineData kline : data1M) {

            long groupStart = (kline.getOpenTime() / intervalMillis) * intervalMillis;
            if (currentAggregate == null || groupStart != currentGroupStartTime) {
                if (currentAggregate != null) {
                    aggregated.add(currentAggregate);
                }
                currentAggregate = new KlineData();
                currentAggregate.setSymbol(kline.getSymbol());
                currentAggregate.setOpenTime(kline.getOpenTime());
                currentAggregate.setOpenPrice(kline.getOpenPrice());
                currentAggregate.setHighPrice(kline.getHighPrice());
                currentAggregate.setLowPrice(kline.getLowPrice());
                currentAggregate.setVolume(kline.getVolume());
                currentAggregate.setQuoteAssetVolume(kline.getQuoteAssetVolume());
                currentAggregate.setNumberOfTrades(kline.getNumberOfTrades());
                currentAggregate.setCloseTime(kline.getCloseTime());
                currentAggregate.setClosePrice(kline.getClosePrice());
                currentGroupStartTime = groupStart;
            } else {
                if (kline.getHighPrice().compareTo(currentAggregate.getHighPrice()) > 0) {
                    currentAggregate.setHighPrice(kline.getHighPrice());
                }
                if (kline.getLowPrice().compareTo(currentAggregate.getLowPrice()) < 0) {
                    currentAggregate.setLowPrice(kline.getLowPrice());
                }
                currentAggregate.setVolume(currentAggregate.getVolume().add(kline.getVolume()));
                currentAggregate.setQuoteAssetVolume(currentAggregate.getQuoteAssetVolume().add(kline.getQuoteAssetVolume()));
                currentAggregate.setNumberOfTrades(currentAggregate.getNumberOfTrades() + kline.getNumberOfTrades());
                // new record
                currentAggregate.setCloseTime(kline.getCloseTime());
                currentAggregate.setClosePrice(kline.getClosePrice());
            }
        }

        if (currentAggregate != null) {
            aggregated.add(currentAggregate);
        }
        return aggregated;
    }
}
