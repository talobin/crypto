package haivo.us.crypto.model;

public class CurrencySubunit {
    public final boolean allowDecimal;
    public final String name;
    public final long subunitToUnit;

    public CurrencySubunit(String name, long subunitToUnit) {
        this(name, subunitToUnit, true);
    }

    public CurrencySubunit(String name, long subunitToUnit, boolean allowDecimal) {
        this.name = name;
        this.subunitToUnit = subunitToUnit;
        this.allowDecimal = allowDecimal;
    }
}
