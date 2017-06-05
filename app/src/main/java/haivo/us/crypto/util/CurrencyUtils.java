package haivo.us.crypto.util;

import haivo.us.crypto.model.CurrencySubunit;
import haivo.us.crypto.model.CurrencySubunitsMap;
import haivo.us.crypto.model.currency.CurrenciesSubunits;
import haivo.us.crypto.model.currency.CurrencySymbols;

public class CurrencyUtils {
    public static String getCurrencySymbol(String currency) {
        return CurrencySymbols.CURRENCY_SYMBOLS.containsKey(currency) ? (String) CurrencySymbols.CURRENCY_SYMBOLS.get(
            currency) : currency;
    }

    public static CurrencySubunit getCurrencySubunit(String currency, long subunitToUnit) {
        if (CurrenciesSubunits.CURRENCIES_SUBUNITS.containsKey(currency)) {
            CurrencySubunitsMap subunits = (CurrencySubunitsMap) CurrenciesSubunits.CURRENCIES_SUBUNITS.get(currency);
            if (subunits.containsKey(Long.valueOf(subunitToUnit))) {
                return (CurrencySubunit) subunits.get(Long.valueOf(subunitToUnit));
            }
        }
        return new CurrencySubunit(currency, 1);
    }
}
