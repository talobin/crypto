package haivo.us.crypto.util;

import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.CurrencyPairsListWithDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CurrencyPairsMapHelper {
    private final HashMap<String, CharSequence[]> currencyPairs;
    private final HashMap<String, String> currencyPairsIds;
    private long date;
    private int pairsCount;

    public CurrencyPairsMapHelper(CurrencyPairsListWithDate currencyPairsListWithDate) {
        this.pairsCount = 0;
        this.currencyPairs = new LinkedHashMap();
        this.currencyPairsIds = new HashMap();
        if (currencyPairsListWithDate != null) {
            this.date = currencyPairsListWithDate.date;
            List<CurrencyPairInfo> sortedPairs = currencyPairsListWithDate.pairs;
            this.pairsCount = sortedPairs.size();
            HashMap<String, Integer> currencyGroupSizes = new HashMap();
            for (CurrencyPairInfo currencyPairInfo : sortedPairs) {
                Integer currentCurrencyGroupSize = (Integer) currencyGroupSizes.get(currencyPairInfo.getCurrencyBase());
                if (currentCurrencyGroupSize == null) {
                    currentCurrencyGroupSize = Integer.valueOf(1);
                } else {
                    currentCurrencyGroupSize = Integer.valueOf(currentCurrencyGroupSize.intValue() + 1);
                }
                currencyGroupSizes.put(currencyPairInfo.getCurrencyBase(), currentCurrencyGroupSize);
            }
            int currentGroupPositionToInsert = 0;
            for (CurrencyPairInfo currencyPairInfo2 : sortedPairs) {
                CharSequence[] currencyGroup = (CharSequence[]) this.currencyPairs.get(currencyPairInfo2.getCurrencyBase());
                if (currencyGroup == null) {
                    currencyGroup = new CharSequence[((Integer) currencyGroupSizes.get(currencyPairInfo2.getCurrencyBase())).intValue()];
                    this.currencyPairs.put(currencyPairInfo2.getCurrencyBase(), currencyGroup);
                    currentGroupPositionToInsert = 0;
                } else {
                    currentGroupPositionToInsert++;
                }
                currencyGroup[currentGroupPositionToInsert] = currencyPairInfo2.getCurrencyCounter();
                if (currencyPairInfo2.getCurrencyPairId() != null) {
                    this.currencyPairsIds.put(createCurrencyPairKey(currencyPairInfo2.getCurrencyBase(), currencyPairInfo2.getCurrencyCounter()), currencyPairInfo2.getCurrencyPairId());
                }
            }
        }
    }

    public long getDate() {
        return this.date;
    }

    public HashMap<String, CharSequence[]> getCurrencyPairs() {
        return this.currencyPairs;
    }

    public String getCurrencyPairId(String currencyBase, String currencyCounter) {
        return (String) this.currencyPairsIds.get(createCurrencyPairKey(currencyBase, currencyCounter));
    }

    public int getPairsCount() {
        return this.pairsCount;
    }

    private String createCurrencyPairKey(String currencyBase, String currencyCounter) {
        return String.format("%1$s_%2$s", new Object[]{currencyBase, currencyCounter});
    }
}
