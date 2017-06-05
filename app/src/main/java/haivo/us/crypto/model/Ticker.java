package haivo.us.crypto.model;

public class Ticker {
    public static final int NO_DATA = -1;
    public double ask;
    public double bid;
    public double high;
    public double last;
    public double low;
    public long timestamp;
    public double vol;

    public Ticker() {
        this.bid = -1.0d;
        this.ask = -1.0d;
        this.vol = -1.0d;
        this.high = -1.0d;
        this.low = -1.0d;
        this.last = -1.0d;
        this.timestamp = -1;
    }
}
