package haivo.us.crypto.model;

public class Futures {
    public static final int CONTRACT_TYPE_BIMONTHLY = 3;
    public static final int CONTRACT_TYPE_BIWEEKLY = 1;
    public static final int CONTRACT_TYPE_MONTHLY = 2;
    public static final int CONTRACT_TYPE_QUARTERLY = 4;
    private static final String[] CONTRACT_TYPE_SHORT_NAMES;
    public static final int CONTRACT_TYPE_WEEKLY = 0;

    static {
        CONTRACT_TYPE_SHORT_NAMES = new String[]{"1W", "2W", "1M", "2M", "3M"};
    }

    public static String getContractTypeShortName(int contractType) {
        if (contractType < 0 || contractType >= CONTRACT_TYPE_SHORT_NAMES.length) {
            return null;
        }
        return CONTRACT_TYPE_SHORT_NAMES[contractType];
    }
}
