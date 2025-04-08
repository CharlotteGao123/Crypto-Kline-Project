package org.cryptoklineproject.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cryptoklineproject.mapper.KlineDataMapper;
import org.cryptoklineproject.model.KlineData;
import org.cryptoklineproject.model.KlineInterval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.LongStream;

@Slf4j
@Service
@Validated
public class KlineLoadService {
    private final BinanceDataFetcher binanceDataFetcher;
    private final KlineDataMapper klineDataMapper;

    @Value("${binance.limit: 500}")
    private int defaultLimit;
    @Value("${app.kline.defaultNeed:1440}")
    private int defaultNeed;

    @Value("${binance.interval}")
    private String interval;

    public KlineLoadService(BinanceDataFetcher binanceDataFetcher,
                            KlineDataMapper klineDataMapper) {
        this.binanceDataFetcher = binanceDataFetcher;
        this.klineDataMapper = klineDataMapper;
    }

    //If the user does not pass startTime/endTime, the defaultNeed most recent data will be pulled by default.
    public void loadDataBatch(@NotBlank String symbol,
                             @NotNull Long startTime,
                             @NotNull Long endTime) {

        var version = System.currentTimeMillis();
        // todo use enum covert
        long intervalMillis = KlineInterval.parseKlineinterval(interval).getMillis();
        long gap = defaultLimit * intervalMillis;
        log.info("Start loadDataBatch: symbol={}, startTime={}, endTime={}, gap={}",
                symbol, startTime, endTime, gap);

        // stream parallel
        LongStream.range(startTime, endTime)
                .parallel()
                .filter( s -> (s-startTime)% gap ==0)
                .forEach( s-> processChunk(symbol, s, Math.min(s + gap, endTime), defaultLimit, version));
                    // todo create another function
    }
    private void processChunk(String symbol, long chunkStart, long chunkEnd, int limit, long version) {
        List<KlineData> data = binanceDataFetcher.fetchKlines(symbol, chunkStart, chunkEnd, limit);
        data.forEach(d -> d.setVersion(version));
        log.info("Fetched {} klines for symbol={}, chunkStart={}, chunkEnd={}, gap={}",
                data.size(), symbol, chunkStart, chunkEnd, (chunkEnd - chunkStart));
        if (!data.isEmpty()) {
            klineDataMapper.insertKlineDataBatch(data);
        }
    }
}


    // todo multiprocessing and multithreading
    // todo clean up code.

    //todo merge into one.


