package org.cryptoklineproject.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.cryptoklineproject.model.KlineData;
import org.cryptoklineproject.model.KlineInterval;
import org.cryptoklineproject.service.KlineDataFetchService;
import org.cryptoklineproject.service.ValidationInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/receive")
public class ReceiveController {
    private final KlineDataFetchService fetchService;
    private final ValidationInputService validationService;

    @Autowired
    public ReceiveController(KlineDataFetchService fetchService, ValidationInputService validationService){
        this.fetchService = fetchService;
        this.validationService = validationService;
    }

    @GetMapping("/range")
    public List<KlineData> getKlinesInRange(@RequestParam @NotBlank String symbol,
                                            @RequestParam @NotNull Long startTime,
                                            @RequestParam @NotNull Long endTime,
                                            @RequestParam @NotNull Long version,
                                            @RequestParam(defaultValue = "1m") String inputInterval){

        KlineInterval interval = validationService.validateInterval(inputInterval);
        validationService.validateSymbol(symbol);
        validationService.validateTimeRange(startTime, endTime);
        return fetchService.findKlinesInRange(symbol, startTime, endTime, version, interval);
    }
}
