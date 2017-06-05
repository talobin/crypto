package haivo.us.crypto.model;

public class CurrencyPairInfo implements Comparable<CurrencyPairInfo> {
    protected final String currencyBase;
    protected final String currencyCounter;
    protected final String currencyPairId;

    public CurrencyPairInfo(String currencyBase, String currencyCounter, String currencyPairId) {
        this.currencyBase = currencyBase;
        this.currencyCounter = currencyCounter;
        this.currencyPairId = currencyPairId;
    }

    public String getCurrencyBase() {
        return this.currencyBase;
    }

    public String getCurrencyCounter() {
        return this.currencyCounter;
    }

    public String getCurrencyPairId() {
        return this.currencyPairId;
    }

    public int compareTo(CurrencyPairInfo another) throws NullPointerException {
        if (this.currencyBase == null || another.currencyBase == null || this.currencyCounter == null || another.currencyCounter == null) {
            throw new NullPointerException();
        }
        int compBase = this.currencyBase.compareToIgnoreCase(another.currencyBase);
        return compBase != 0 ? compBase : this.currencyCounter.compareToIgnoreCase(another.currencyCounter);
    }
}
