package haivo.us.crypto.mechanoid.net;

public class UnexpectedHttpStatusException extends RuntimeException {
    private static final long serialVersionUID = 1;
    private int mActualStatus;
    private int mExpectedStatus;

    public int getActualStatus() {
        return this.mActualStatus;
    }

    public int getExpectedStatus() {
        return this.mExpectedStatus;
    }

    public UnexpectedHttpStatusException(int actualStatus, int expectedStatus) {
        super(String.format("Expected Status %d,  Actual Status %d", new Object[]{ Integer.valueOf(expectedStatus), Integer.valueOf(actualStatus)}));
        this.mActualStatus = actualStatus;
        this.mExpectedStatus = expectedStatus;
    }
}
