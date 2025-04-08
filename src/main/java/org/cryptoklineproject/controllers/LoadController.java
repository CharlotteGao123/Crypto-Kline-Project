package org.cryptoklineproject.controllers;

import org.cryptoklineproject.service.KlineLoadService;
import org.cryptoklineproject.service.ValidationInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/load")
public class LoadController {

    private final KlineLoadService klineLoadService;
    private final ValidationInputService validationInputService;

    @Autowired
    public LoadController(KlineLoadService klineLoadService, ValidationInputService validationInputService) {
        this.klineLoadService = klineLoadService;
        this.validationInputService = validationInputService;
    }

    // POST /load/data?symbol=BTCUSDT&interval=1m&limit=10
    @PostMapping("/data")
    @ResponseStatus(HttpStatus.CREATED)
    public void loadData(@RequestParam String symbol,
                         @RequestParam(required = false) Long startTime,
                         @RequestParam(required = false) Long endTime) {

        validationInputService.validateSymbol(symbol);
        validationInputService.validateTimeRange(startTime, endTime);
        klineLoadService.loadDataBatch(symbol, startTime, endTime);
    }
}