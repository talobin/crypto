package haivo.us.crypto.model.currency;

import java.util.HashMap;
import haivo.us.crypto.model.CurrencySubunit;
import haivo.us.crypto.model.CurrencySubunitsMap;

public class CurrenciesSubunits {
    public static final HashMap<String, CurrencySubunitsMap> CURRENCIES_SUBUNITS;

    static {
        CURRENCIES_SUBUNITS = new HashMap();
        CURRENCIES_SUBUNITS.put(VirtualCurrency.BTC,
                                new CurrencySubunitsMap(new CurrencySubunit(VirtualCurrency.BTC, 1),
                                                        new CurrencySubunit(VirtualCurrency.mBTC, 1000),
                                                        new CurrencySubunit(VirtualCurrency.uBTC, 1000000),
                                                        new CurrencySubunit(VirtualCurrency.Satoshi,
                                                                            100000000,
                                                                            false)));
        CURRENCIES_SUBUNITS.put(VirtualCurrency.LTC,
                                new CurrencySubunitsMap(new CurrencySubunit(VirtualCurrency.LTC, 1),
                                                        new CurrencySubunit(VirtualCurrency.mLTC, 1000)));
    }
}
