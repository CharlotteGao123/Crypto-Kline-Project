package org.cryptoklineproject.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KlineData {
    @NotNull
    private Long openTime;
    @NotBlank
    private String symbol;
    @NotNull
    private Long closeTime;
    @NotNull
    private BigDecimal openPrice;
    @NotNull
    private BigDecimal highPrice;
    @NotNull
    private BigDecimal lowPrice;
    @NotNull
    private BigDecimal closePrice;
    @Min(0) @NotNull
    private BigDecimal volume;
    @Min(value = 1, message = "Quote asset volume must be greater than 0") @NotNull
    private BigDecimal quoteAssetVolume;
    @Min(value = 1, message = "Number of trades must be greater than 0") @NotNull
    private Long numberOfTrades;
    @NotNull
    private Long version;
}

