package haivo.us.crypto.model;

import java.util.HashMap;

public abstract class FuturesMarket extends Market {
    public final int[] contractTypes;

    protected abstract String getUrl(int i, CheckerInfo checkerInfo, int i2);

    public FuturesMarket(String name, String ttsName, HashMap<String, CharSequence[]> currencyPairs, int[] contractTypes) {
        super(name, ttsName, currencyPairs);
        this.contractTypes = contractTypes;
    }

    public final String getUrl(int requestId, CheckerInfo checkerInfo) {
        return getUrl(requestId, checkerInfo, checkerInfo.getContractType());
    }
}
