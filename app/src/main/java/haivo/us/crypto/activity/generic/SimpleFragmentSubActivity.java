package haivo.us.crypto.activity.generic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

public abstract class SimpleFragmentSubActivity<T extends Fragment> extends SimpleFragmentActivity<T> {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }
}
