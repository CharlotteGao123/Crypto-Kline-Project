package org.cryptoklineproject.service;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.cryptoklineproject.model.KlineData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BinanceDataFetcher {
    private static final Logger logger = LoggerFactory.getLogger(BinanceDataFetcher.class);

    @Value("${binance.base_url}")
    private String BASE_URL;

    @Value("${binance.kline_api}")
    private String klineApi;

    @Value("${binance.symbol_api}")
    private String symbolApi;

    @Value("${binance.limit}")
    private Integer defaultLimit;

    @Value("${binance.interval}")
    private String interval;

    @Autowired
    private RestTemplate restTemplate;

    private Set<String> validSymbols;

    @PostConstruct
    private void getAllowedSymbols() {
        String json = fetchSymbolInfo();
        Set<String> allowedSymbols = new HashSet<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray symbolsArray = jsonObject.getJSONArray("symbols");
        for (int i = 0; i < symbolsArray.length(); i++) {
            JSONObject symbolObj = symbolsArray.getJSONObject(i);
            allowedSymbols.add(symbolObj.getString("symbol").trim().toUpperCase());
        }
        this.validSymbols= allowedSymbols;
    }

    public Boolean validSymbol(String symbol) {
        symbol = symbol.trim().toUpperCase();
        if (this.validSymbols == null || this.validSymbols.isEmpty()) {
            getAllowedSymbols();
        }
        return this.validSymbols.contains(symbol);
    }


    public List<KlineData> fetchRecentKlines(String symbol) {
        // create API get URL -kline
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(klineApi)
                .queryParam("symbol", symbol)
                .queryParam("interval", this.interval)
                .queryParam("limit", this.defaultLimit)
                .build(true).encode().toUriString();
        return doFetch(url, symbol);
    }

    //-symbol
    public String fetchSymbolInfo(){
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(symbolApi)
                .toUriString();
        String json = restTemplate.getForObject(url, String.class);
        logger.info("fetchSymbolInfo returns: {}", json);
        return json;
    }

    /**
     *
     * @param symbol
     * @param startTime
     * @param endTime
     * @param limit
     * @return
     *
     *  In recursive logic, query forward step by step through the time range
     */
    public List<KlineData> fetchKlines(String symbol,
                                       Long startTime, Long endTime, Integer limit) {

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(klineApi)
                .queryParam("symbol", symbol)
                .queryParam("interval", this.interval)
                .queryParam("limit", limit)
                .queryParam("startTime", startTime)
                .queryParam("endTime", endTime)
                .toUriString();

        return doFetch(url, symbol);
    }

    private List<KlineData> doFetch(String url, String symbol) {

        // get response
        String[][] response = restTemplate.getForObject(url, String[][].class);
        return Arrays.stream(response)
                .parallel()
                .map(kline -> this.convert(symbol, kline))
                .toList();

    }

    private KlineData convert(String symbol, String[] kline){
        KlineData data = new KlineData();
        // Parse fields in order
        data.setSymbol(symbol);
        data.setOpenTime(Long.valueOf(kline[0]));
        data.setOpenPrice(new BigDecimal(kline[1]));
        data.setHighPrice(new BigDecimal(kline[2]));
        data.setLowPrice(new BigDecimal(kline[3]));
        data.setClosePrice(new BigDecimal(kline[4]));
        data.setVolume(new BigDecimal(kline[5]));
        data.setCloseTime(Long.valueOf(kline[6]));
        data.setQuoteAssetVolume(new BigDecimal(kline[7]));
        data.setNumberOfTrades(Long.valueOf(kline[8]));
        return data;
    }
}
