package org.cryptoklineproject.service;

import jakarta.validation.constraints.NotNull;
import org.cryptoklineproject.model.KlineInterval;
import org.cryptoklineproject.model.exception.InputParamInvalidException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@Validated
public class ValidationInputService {
    private static final Set<String> ALLOWED_INTERVALS = new HashSet<>(Arrays.asList("1m", "5m", "15m", "30m", "1h", "1d"));
    private final BinanceDataFetcher binanceDataFetcher;

    @Autowired
    public ValidationInputService(BinanceDataFetcher binanceDataFetcher){
        this.binanceDataFetcher = binanceDataFetcher;
    }
    private Set<String> getAllowedSymbols() {
        String json = binanceDataFetcher.fetchSymbolInfo();
        Set<String> allowedSymbols = new HashSet<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray symbolsArray = jsonObject.getJSONArray("symbols");
        for (int i = 0; i < symbolsArray.length(); i++) {
            JSONObject symbolObj = symbolsArray.getJSONObject(i);
            allowedSymbols.add(symbolObj.getString("symbol").trim().toUpperCase());
        }
        return allowedSymbols;
    }

    // todo return enum
    public void validateSymbol(String symbol) {
        Set<String> allowedSymbols = getAllowedSymbols();
        if (!allowedSymbols.contains(symbol)) {
            throw new InputParamInvalidException("symbol=" + symbol + " is invalid. Allowed: " + allowedSymbols);
        }
    }

    public KlineInterval validateInterval(String interval) {
        try{
            return KlineInterval.parseKlineinterval(interval);
        } catch (Exception e) {
            throw new InputParamInvalidException("interval=" + interval + " is invalid. Allowed: " + ALLOWED_INTERVALS);
        }

    }

    public void validateTimeRange(@NotNull Long startTime, @NotNull Long endTime) {
        if (startTime >= endTime) {
            throw new InputParamInvalidException("startTime must be < endTime, but got: " + startTime + " >= " + endTime);
        }
    }
}
