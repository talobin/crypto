package haivo.us.crypto.fragment;

import haivo.us.crypto.activity.CheckerAddActivity;
import haivo.us.crypto.adapter.CheckersListAdapter;
import android.annotation.TargetApi;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragmentLayout;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import haivo.us.crypto.appwidget.WidgetProvider;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.fragment.generic.ActionModeListFragment;
import haivo.us.crypto.R;
import java.util.ArrayList;
import haivo.us.crypto.mechanoid.Mechanoid;
import haivo.us.crypto.mechanoid.db.MechanoidContentProvider;
import haivo.us.crypto.receiver.MarketChecker;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh.SetupWizard;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import haivo.us.crypto.util.AsyncTaskCompat;
import haivo.us.crypto.util.CheckerRecordHelper;
import haivo.us.crypto.util.PreferencesUtils;

public class CheckersListFragment extends ActionModeListFragment<Cursor>
    implements LoaderCallbacks<Cursor>, OnRefreshListener {
    private static final int REFRESH_ALL_HINT_DURATION = 2000;
    public static final int SORT_MODE_CURRENCY = 1;
    public static final int SORT_MODE_EXCHANGE = 2;
    public static final int SORT_MODE_MANUALLY = 0;
    private CheckersListAdapter adapter;
    private AsyncTask<ArrayList<ContentProviderOperation>, Void, Void> dragAndDropUpateTask;
    private PullToRefreshLayout mPullToRefreshLayout;
    private Runnable refreshAllCompleteRunnable;
    private MenuItem sortByManuallyItem;

    /* renamed from: haivo.us.crypto.fragment.CheckersListFragment.2 */
    class C01762 implements Runnable {
        C01762() {
        }

        public void run() {
            if (CheckersListFragment.this.getActivity() != null
                && CheckersListFragment.this.mPullToRefreshLayout != null) {
                CheckersListFragment.this.mPullToRefreshLayout.setRefreshComplete();
            }
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckersListFragment.3 */
    class C01773 extends AsyncTask<ArrayList<ContentProviderOperation>, Void, Void> {
        final /* synthetic */ Context val$appContext;

        C01773(Context context) {
            this.val$appContext = context;
        }

        protected Void doInBackground(ArrayList<ContentProviderOperation>... ops) {
            try {
                Mechanoid.getContentResolver().applyBatch(MaindbContract.CONTENT_AUTHORITY, ops[0]);
                PreferencesUtils.setCheckersListSortMode(CheckersListFragment.this.getActivity(), 0);
                if (CheckersListFragment.this.sortByManuallyItem != null) {
                    CheckersListFragment.this.sortByManuallyItem.setChecked(true);
                }
                WidgetProvider.refreshWidget(this.val$appContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckersListFragment.1 */
    class C03671 extends CheckersListAdapter {
        C03671(Context context) {
            super(context);
        }

        public void drop(int from, int to) {
            super.drop(from, to);
            CheckersListFragment.this.onDrop(from, to);
        }
    }

    public CheckersListFragment() {
        this.refreshAllCompleteRunnable = new C01762();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkers_list_fragment, container, false);
        ListFragmentLayout.setupIds(view);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ViewGroup viewGroup = (ViewGroup) view;
        this.mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
        SetupWizard insertLayoutInto = ActionBarPullToRefresh.from(getActivity()).insertLayoutInto(viewGroup);
        int[] iArr = new int[SORT_MODE_CURRENCY];
        iArr[0] = getListView().getId();
        insertLayoutInto.theseChildrenArePullable(iArr)
                        .listener(this)
                        .options(Options.create().minimize(REFRESH_ALL_HINT_DURATION).build())
                        .setup(this.mPullToRefreshLayout);
        this.adapter = new C03671(getActivity());
        setListAdapter(this.adapter);
        setListShownNoAnimation(false);
        setEmptyText(getString(R.string.checkers_list_empty_text));
        getListView().setDividerHeight(0);
        getListView().setDivider(null);
        //hai
        getListView().setSelector(getContext().getDrawable(R.drawable.abc_list_selector_holo_light));
        enableActionModeOrContextMenu();
        getLoaderManager().restartLoader(0, null, this);
    }

    public void onDestroyView() {
        this.mPullToRefreshLayout.removeCallbacks(this.refreshAllCompleteRunnable);
        this.mPullToRefreshLayout = null;
        super.onDestroyView();
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        editCheckerRecord((Cursor) this.adapter.getItem(position));
    }

    private void editCheckerRecord(Cursor cursor) {
        CheckerAddActivity.startCheckerAddActivity(getActivity(), CheckerRecord.fromCursor(cursor));
    }

    private void deleteCheckerRecord(Cursor cursor, boolean refresh) {
        CheckerRecord checkerRecord = CheckerRecord.fromCursor(cursor);
        CheckerRecordHelper.doBeforeDelete(getActivity(), checkerRecord);
        CheckerRecord.fromCursor(cursor).delete(refresh);
        CheckerRecordHelper.doAfterDelete(getActivity(), checkerRecord);
    }

    private void onDrop(int from, int to) {
        if (from != to) {
            Context appContext = getActivity().getApplicationContext();
            ArrayList<ContentProviderOperation> ops = new ArrayList();
            Cursor cursor = this.adapter.getCursor();
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    long sortOrder = (long) this.adapter.getListPosition(cursor.getPosition());
                    ops.add(ContentProviderOperation.newUpdate(MaindbContract.Checker.CONTENT_URI.buildUpon()
                                                                                                 .appendPath(String.valueOf(
                                                                                                     cursor.getLong(0)))
                                                                                                 .appendQueryParameter(
                                                                                                     MechanoidContentProvider.PARAM_NOTIFY,
                                                                                                     String.valueOf(
                                                                                                         false))
                                                                                                 .build())
                                                    .withValue(MaindbContract.CheckerColumns.SORT_ORDER,
                                                               Long.valueOf(sortOrder))
                                                    .build());
                    cursor.moveToNext();
                }
            }
            if (this.dragAndDropUpateTask != null) {
                this.dragAndDropUpateTask.cancel(true);
            }
            this.dragAndDropUpateTask = new C01773(appContext);
            try {
                AsyncTask asyncTask = this.dragAndDropUpateTask;
                ArrayList[] arrayListArr = new ArrayList[SORT_MODE_CURRENCY];
                arrayListArr[0] = ops;
                AsyncTaskCompat.execute(asyncTask, arrayListArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int checkedSortMenuItem;
        inflater.inflate(R.menu.checkers_list_fragment, menu);
        this.sortByManuallyItem = menu.findItem(R.id.sortByManuallyItem);
        switch (PreferencesUtils.getCheckersListSortMode(getActivity())) {
            case SORT_MODE_CURRENCY /*1*/:
                checkedSortMenuItem = R.id.sortByCurrencyItem;
                break;
            case SORT_MODE_EXCHANGE /*2*/:
                checkedSortMenuItem = R.id.sortByExchangeItem;
                break;
            default:
                checkedSortMenuItem = R.id.sortByManuallyItem;
                break;
        }
        menu.findItem(checkedSortMenuItem).setChecked(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addCheckerItem) {
            CheckerAddActivity.startCheckerAddActivity(getActivity(), null);
            return true;
        }
        if (item.getItemId() == R.id.sortByManuallyItem) {
            PreferencesUtils.setCheckersListSortMode(getActivity(), 0);
            item.setChecked(true);
            getLoaderManager().restartLoader(0, null, this);
            WidgetProvider.refreshWidget(getActivity());
        } else if (item.getItemId() == R.id.sortByCurrencyItem) {
            PreferencesUtils.setCheckersListSortMode(getActivity(), SORT_MODE_CURRENCY);
            item.setChecked(true);
            getLoaderManager().restartLoader(0, null, this);
            WidgetProvider.refreshWidget(getActivity());
        } else if (item.getItemId() == R.id.sortByExchangeItem) {
            PreferencesUtils.setCheckersListSortMode(getActivity(), SORT_MODE_EXCHANGE);
            item.setChecked(true);
            getLoaderManager().restartLoader(0, null, this);
            WidgetProvider.refreshWidget(getActivity());
        } else if (item.getItemId() == R.id.refreshAllItem) {
            this.mPullToRefreshLayout.setRefreshing(true);
            onRefreshStarted(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRefreshStarted(View unused) {
        if (this.mPullToRefreshLayout != null && getActivity() != null) {
            MarketChecker.refreshAllEnabledCheckerRecords(getActivity());
            this.mPullToRefreshLayout.postDelayed(this.refreshAllCompleteRunnable, 2000);
        }
    }

    protected int getActionModeOrContextMenuResId() {
        return R.menu.checkers_list_fragment_cab;
    }

    @TargetApi(11)
    protected void onActionModeItemsCheckedCountChanged(ActionMode mode, int checkedItemCount) {
        boolean z = true;
        MenuItem findItem = mode.getMenu().findItem(R.id.editItem);
        if (checkedItemCount != SORT_MODE_CURRENCY) {
            z = false;
        }
        findItem.setVisible(z);
        super.onActionModeItemsCheckedCountChanged(mode, checkedItemCount);
    }

    protected boolean onActionModeOrContextMenuItemClicked(int menuItemId,
                                                           Cursor checkedItem,
                                                           int listItemPosition,
                                                           int checkedItemsCount,
                                                           boolean isForLastItem) {
        switch (menuItemId) {
            case R.id.deleteItem /*2131624099*/:
                deleteCheckerRecord(checkedItem, isForLastItem);
                return true;
            case R.id.editItem /*2131624107*/:
                editCheckerRecord(checkedItem);
                return true;
            default:
                return super.onActionModeOrContextMenuItemClicked(menuItemId,
                                                                  checkedItem,
                                                                  listItemPosition,
                                                                  checkedItemsCount,
                                                                  isForLastItem);
        }
    }

    protected void onActionModeActive(boolean active) {
        super.onActionModeActive(active);
        this.adapter.setActionModeActive(active);
    }

    private void setNewList(Cursor cursor) {
        if (getView() != null && getActivity() != null) {
            if (cursor != null) {
                this.adapter.swapCursor(cursor);
            }
            setListShown(true);
        }
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(getActivity(),
                                MaindbContract.Checker.CONTENT_URI,
                                CheckerRecord.PROJECTION,
                                null,
                                null,
                                getSortOrderString(getActivity()));
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        setNewList(cursor);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        setNewList(null);
    }

    public static String getSortOrderString(Context context) {
        switch (PreferencesUtils.getCheckersListSortMode(context)) {
            case SORT_MODE_CURRENCY /*1*/:
                return "currencySrc ASC, currencyDst ASC, marketKey ASC";
            case SORT_MODE_EXCHANGE /*2*/:
                return "marketKey ASC, currencySrc ASC, currencyDst ASC";
            default:
                return "sortOrder ASC";
        }
    }
}
