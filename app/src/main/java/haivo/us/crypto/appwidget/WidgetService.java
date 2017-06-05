package haivo.us.crypto.appwidget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.widget.RemoteViewsService;

@TargetApi(11)
public class WidgetService extends RemoteViewsService {
    @TargetApi(11)
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListProvider(getApplicationContext(), intent);
    }
}
