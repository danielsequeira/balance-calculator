package enumeration;

import java.math.BigDecimal;

public enum Currency {
    EUR,
    USD;

    private static BigDecimal EUR_USD_CONVERSION_RATE = new BigDecimal("1.5");

    public static BigDecimal getEUR_USD_CONVERSION_RATE() {
        return EUR_USD_CONVERSION_RATE;
    }
}
