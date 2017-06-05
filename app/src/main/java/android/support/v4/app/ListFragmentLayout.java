package android.support.v4.app;

import android.view.View;
import haivo.us.crypto.R;

public class ListFragmentLayout {
    public static void setupIds(View view) {
        view.findViewById(R.id.empty_id).setId(ListFragment.INTERNAL_EMPTY_ID);
        view.findViewById(R.id.progress_container_id).setId(ListFragment.INTERNAL_PROGRESS_CONTAINER_ID);
        view.findViewById(R.id.list_container_id).setId(ListFragment.INTERNAL_LIST_CONTAINER_ID);
    }
}
