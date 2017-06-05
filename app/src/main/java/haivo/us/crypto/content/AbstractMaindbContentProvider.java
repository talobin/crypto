package haivo.us.crypto.content;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import haivo.us.crypto.content.AbstractMaindbOpenHelper.Sources;
import haivo.us.crypto.content.MaindbContract.Alarm;
import haivo.us.crypto.content.MaindbContract.Checker;
import java.util.Set;
import haivo.us.crypto.mechanoid.db.ContentProviderActions;
import haivo.us.crypto.mechanoid.db.DefaultContentProviderActions;
import haivo.us.crypto.mechanoid.db.MechanoidContentProvider;
import haivo.us.crypto.mechanoid.db.MechanoidSQLiteOpenHelper;

public abstract class AbstractMaindbContentProvider extends MechanoidContentProvider {
    protected static final int ALARM = 2;
    protected static final int ALARM_ID = 3;
    protected static final int CHECKER = 0;
    protected static final int CHECKER_ID = 1;
    public static final int NUM_URI_MATCHERS = 4;

    protected UriMatcher createUriMatcher() {
        UriMatcher matcher = new UriMatcher(-1);
        String authority = MaindbContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, Sources.CHECKER, CHECKER);
        matcher.addURI(authority, "checker/#", CHECKER_ID);
        matcher.addURI(authority, Sources.ALARM, ALARM);
        matcher.addURI(authority, "alarm/#", ALARM_ID);
        return matcher;
    }

    protected String[] createContentTypes() {
        String[] contentTypes = new String[NUM_URI_MATCHERS];
        contentTypes[CHECKER] = Checker.CONTENT_TYPE;
        contentTypes[CHECKER_ID] = Checker.ITEM_CONTENT_TYPE;
        contentTypes[ALARM] = Alarm.CONTENT_TYPE;
        contentTypes[ALARM_ID] = Alarm.ITEM_CONTENT_TYPE;
        return contentTypes;
    }

    protected MechanoidSQLiteOpenHelper createOpenHelper(Context context) {
        return new MaindbOpenHelper(context);
    }

    protected Set<Uri> getRelatedUris(Uri uri) {
        return (Set) MaindbContract.REFERENCING_VIEWS.get(uri);
    }

    protected ContentProviderActions createActions(int id) {
        switch (id) {
            case CHECKER /*0*/:
                return createCheckerActions();
            case CHECKER_ID /*1*/:
                return createCheckerByIdActions();
            case ALARM /*2*/:
                return createAlarmActions();
            case ALARM_ID /*3*/:
                return createAlarmByIdActions();
            default:
                throw new UnsupportedOperationException("Unknown id: " + id);
        }
    }

    protected ContentProviderActions createCheckerByIdActions() {
        return new DefaultContentProviderActions(Sources.CHECKER, true, CheckerRecord.getFactory());
    }

    protected ContentProviderActions createCheckerActions() {
        return new DefaultContentProviderActions(Sources.CHECKER, false, CheckerRecord.getFactory());
    }

    protected ContentProviderActions createAlarmByIdActions() {
        return new DefaultContentProviderActions(Sources.ALARM, true, AlarmRecord.getFactory());
    }

    protected ContentProviderActions createAlarmActions() {
        return new DefaultContentProviderActions(Sources.ALARM, false, AlarmRecord.getFactory());
    }
}
