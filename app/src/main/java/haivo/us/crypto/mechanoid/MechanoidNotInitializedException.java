package haivo.us.crypto.mechanoid;

public class MechanoidNotInitializedException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public MechanoidNotInitializedException() {
        super("Mechanoid.init(Context) must be called before mechanoid can be used");
    }
}
