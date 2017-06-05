package haivo.us.crypto.model.currency;

import java.util.HashMap;

public class CurrencySymbols {
    public static final HashMap<String, String> CURRENCY_SYMBOLS;

    static {
        CURRENCY_SYMBOLS = new HashMap();
        CURRENCY_SYMBOLS.put(Currency.USD, "$");
        CURRENCY_SYMBOLS.put(Currency.PLN, "z\u0142");
        CURRENCY_SYMBOLS.put(Currency.CNY, "\u00a5");
        CURRENCY_SYMBOLS.put(Currency.EUR, "\u20ac");
        CURRENCY_SYMBOLS.put(Currency.GBP, "\u00a3");
        CURRENCY_SYMBOLS.put(Currency.CHF, "Fr");
        CURRENCY_SYMBOLS.put(Currency.RUB, "\u0440.");
        CURRENCY_SYMBOLS.put(Currency.RUR, "\u0440.");
        CURRENCY_SYMBOLS.put(Currency.AUD, "$");
        CURRENCY_SYMBOLS.put(Currency.SEK, "kr");
        CURRENCY_SYMBOLS.put(Currency.DKK, "kr");
        CURRENCY_SYMBOLS.put(Currency.HKD, "$");
        CURRENCY_SYMBOLS.put(Currency.SGD, "$");
        CURRENCY_SYMBOLS.put(Currency.THB, "\u0e3f");
        CURRENCY_SYMBOLS.put(Currency.NZD, "$");
        CURRENCY_SYMBOLS.put(Currency.JPY, "\u00a5");
        CURRENCY_SYMBOLS.put(Currency.BRL, "R$");
        CURRENCY_SYMBOLS.put(Currency.KRW, "\u20a9");
        CURRENCY_SYMBOLS.put(Currency.AFN, "\u060b");
        CURRENCY_SYMBOLS.put(Currency.ALL, "L");
        CURRENCY_SYMBOLS.put(Currency.DZD, "\u062f.\u062c");
        CURRENCY_SYMBOLS.put(Currency.AOA, "Kz");
        CURRENCY_SYMBOLS.put(Currency.ARS, "$");
        CURRENCY_SYMBOLS.put(Currency.AMD, "\u0564\u0580.");
        CURRENCY_SYMBOLS.put(Currency.AWG, "\u0192");
        CURRENCY_SYMBOLS.put(Currency.AZN, "m");
        CURRENCY_SYMBOLS.put(Currency.BSD, "$");
        CURRENCY_SYMBOLS.put(Currency.BHD, "\u0628.\u062f");
        CURRENCY_SYMBOLS.put(Currency.BDT, "\u09f3");
        CURRENCY_SYMBOLS.put(Currency.BBD, "$");
        CURRENCY_SYMBOLS.put(Currency.BYR, "Br");
        CURRENCY_SYMBOLS.put(Currency.BZD, "$");
        CURRENCY_SYMBOLS.put(Currency.BMD, "$");
        CURRENCY_SYMBOLS.put(Currency.BTN, "Nu.");
        CURRENCY_SYMBOLS.put(Currency.BOB, "Bs.");
        CURRENCY_SYMBOLS.put(Currency.BAM, "\u041a\u041c");
        CURRENCY_SYMBOLS.put(Currency.BWP, "P");
        CURRENCY_SYMBOLS.put(Currency.BND, "$");
        CURRENCY_SYMBOLS.put(Currency.BGN, "\u043b\u0432");
        CURRENCY_SYMBOLS.put(Currency.BIF, "Fr");
        CURRENCY_SYMBOLS.put(Currency.TRY, "TL");
        CURRENCY_SYMBOLS.put(Currency.ZAR, "R");
        CURRENCY_SYMBOLS.put(Currency.IDR, "Rp");
    }
}
