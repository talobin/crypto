package haivo.us.crypto.mechanoid.db;

import android.database.Cursor;
import haivo.us.crypto.mechanoid.util.Closeables;

public abstract class CursorWalker {
    protected abstract Cursor createCursor();

    protected abstract void step(Cursor cursor);

    public int walk() {
        Cursor cursor = null;
        try {
            cursor = createCursor();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                step(cursor);
            }
            int count = cursor.getCount();
            return count;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }
}
