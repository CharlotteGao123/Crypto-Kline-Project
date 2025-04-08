package org.cryptoklineproject.model;

public enum KlineInterval {
    ONE_MINUTE("1m", 60 * 1000L),
    FIVE_MINUTES("5m", 5 * 60 * 1000L),
    FIFTEEN_MINUTES("15m", 15 * 60 * 1000L),
    ONE_HOUR("1h", 60 * 60 * 1000L),
    ONE_DAY("1d", 24 * 60 * 60 * 1000L);

    private final String code;
    private final long millis;

    KlineInterval(String code, Long millis) {
        this.code = code;
        this.millis = millis;
    }

    public String getCode() {
        return code;
    }

    public long getMillis() {
        return millis;
    }

    public static KlineInterval parseKlineinterval(String interval) {
        for (KlineInterval ki : values()) {
            if (ki.getCode().equals(interval)) {
                return ki;
            }
        }
        throw new IllegalArgumentException("Unsupported interval: " + interval);
    }
}


