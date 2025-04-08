package org.cryptoklineproject.mapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.*;
import org.cryptoklineproject.model.KlineData;

import java.util.List;

@Mapper
public interface KlineDataMapper{
    //batch insert
    @Insert({
            "<script>",
            "INSERT INTO kline_data (symbol, open_time, close_time, open_price, high_price, low_price, close_price,",
            " volume, quote_asset_volume, number_of_trades, version) ",
            "VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "   (#{item.symbol}, #{item.openTime}, #{item.closeTime}, #{item.openPrice},",
            "    #{item.highPrice}, #{item.lowPrice}, #{item.closePrice}, #{item.volume},",
            "    #{item.quoteAssetVolume}, #{item.numberOfTrades}, #{item.version})",
            "</foreach>",
            "</script>"
    })
    int insertKlineDataBatch(@Param("list") List<KlineData> klineDataList);

    //check in a range
    @Select({
            "<script>",
            "SELECT * FROM kline_data",
            "WHERE symbol = #{symbol}",
            "  AND version = #{version}",
            "  AND open_time &gt;= #{startTime}",
            "  AND close_time &lt;= #{endTime}",
            "</script>"
    })
    List<KlineData> findKlinesInRange(@Param("symbol") @NotBlank String symbol,
                                      @Param("startTime") @NotNull Long startTime,
                                      @Param("endTime") @NotNull Long endTime,
                                      @Param("version") @NotNull Long version);
}
