package haivo.us.crypto.appwidget;

import haivo.us.crypto.activity.generic.SimpleFragmentSubActivity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

public class WidgetConfigureActivity extends SimpleFragmentSubActivity<WidgetConfigureFragment> {
    private int appWidgetId;

    public WidgetConfigureActivity() {
        this.appWidgetId = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.appWidgetId = extras.getInt("appWidgetId", 0);
        }
        super.onCreate(savedInstanceState);
        WidgetProvider.updateWdgetWithId(this, AppWidgetManager.getInstance(this), this.appWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra("appWidgetId", this.appWidgetId);
        setResult(-1, resultValue);
    }

    protected WidgetConfigureFragment createChildFragment() {
        return WidgetConfigureFragment.newInstance(this.appWidgetId);
    }

    public void finish() {
        WidgetProvider.updateWdgetWithId(this, AppWidgetManager.getInstance(this), this.appWidgetId);
        super.finish();
    }
}
