package haivo.us.crypto.fragment;

import haivo.us.crypto.adapter.CheckerAlarmsListAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.MaindbContract.Alarm;
import haivo.us.crypto.fragment.generic.ActionModeListFragment;
import haivo.us.crypto.R;

public class CheckerAlarmsListFragment extends ActionModeListFragment<Cursor> implements LoaderCallbacks<Cursor> {
    private CheckerAlarmsListAdapter adapter;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setListAdapter(this.adapter);
        setListShownNoAnimation(false);
        setEmptyText(getString(R.string.checker_alarms_list_empty_text));
        getListView().setDividerHeight(0);
        getListView().setDivider(null);
        // hai
        getListView().setSelector(getContext().getDrawable(R.drawable.abc_list_selector_holo_light));
        enableActionModeOrContextMenu();
        getLoaderManager().initLoader(0, null, this);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
    }

    private void deleteCheckerAlarmRecord(Cursor cursor, boolean refresh) {
        AlarmRecord.fromCursor(cursor).delete(refresh);
    }

    protected int getActionModeOrContextMenuResId() {
        return R.menu.checker_alarms_list_fragment_cab;
    }

    protected boolean onActionModeOrContextMenuItemClicked(int menuItemId,
                                                           Cursor checkedItem,
                                                           int listItemPosition,
                                                           int checkedItemsCount,
                                                           boolean isForLastItem) {
        switch (menuItemId) {
            case R.id.deleteItem /*2131624099*/:
                deleteCheckerAlarmRecord(checkedItem, isForLastItem);
                return true;
            default:
                return super.onActionModeOrContextMenuItemClicked(menuItemId,
                                                                  checkedItem,
                                                                  listItemPosition,
                                                                  checkedItemsCount,
                                                                  isForLastItem);
        }
    }

    private void setNewList(Cursor cursor) {
        if (getView() != null && getActivity() != null) {
            setListShown(true);
        }
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(getActivity(), Alarm.CONTENT_URI, AlarmRecord.PROJECTION, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        setNewList(cursor);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        setNewList(null);
    }
}
