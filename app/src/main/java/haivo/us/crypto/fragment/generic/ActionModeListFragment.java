package haivo.us.crypto.fragment.generic;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ActionModeListFragment<T> extends ListFragment {
    private ListAdapter adapter;
    private int contextMenuSelectenItemPosition;
    private ActionMode currentActionMode;

    @TargetApi(11)
    protected class MyMultiChoiceModeListener implements MultiChoiceModeListener {
        protected MyMultiChoiceModeListener() {
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            ActionModeListFragment.this.onActionModeActive(true);
            ActionModeListFragment.this.currentActionMode = mode;
            int menuResId = ActionModeListFragment.this.getActionModeOrContextMenuResId();
            if (menuResId <= 0) {
                return false;
            }
            mode.getMenuInflater().inflate(menuResId, menu);
            refreshIconsAndTitle(mode);
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            boolean ret = false;
            SparseBooleanArray checkedItemsArray = ActionModeListFragment.this.getListView().getCheckedItemPositions();
            if (checkedItemsArray != null) {
                int checkedItemsCount = checkedItemsArray.size();
                int i = 0;
                while (i < checkedItemsCount) {
                    try {
                        if (checkedItemsArray.valueAt(i)) {
                            if (ActionModeListFragment.this.onActionModeOrContextMenuItemClicked(item.getItemId(),
                                                                                                 (T) ActionModeListFragment.this.adapter
                                                                                                     .getItem(
                                                                                                         checkedItemsArray
                                                                                                             .keyAt(i)),
                                                                                                 checkedItemsArray.keyAt(
                                                                                                     i),
                                                                                                 checkedItemsCount,
                                                                                                 i
                                                                                                     ==
                                                                                                     checkedItemsCount
                                                                                                         + -1)) {
                                ret = true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
            if (ret) {
                mode.finish();
            }
            return ret;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            refreshIconsAndTitle(mode);
        }

        private void refreshIconsAndTitle(ActionMode mode) {
            int checkedItemCount = ActionModeListFragment.this.getListView().getCheckedItemCount();
            mode.setTitle(String.valueOf(checkedItemCount));
            ActionModeListFragment.this.onActionModeItemsCheckedCountChanged(mode, checkedItemCount);
        }

        public void onDestroyActionMode(ActionMode mode) {
            ActionModeListFragment.this.onActionModeActive(false);
            ActionModeListFragment.this.currentActionMode = null;
        }
    }

    public ActionModeListFragment() {
        this.contextMenuSelectenItemPosition = -1;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            onListItemClick(l, v, position, (T) this.adapter.getItem(position));
        } catch (Exception e) {
        }
    }

    public void onListItemClick(ListView l, View v, int position, T t) {
    }

    public void setListAdapter(ListAdapter adapter) {
        this.adapter = adapter;
        super.setListAdapter(adapter);
    }

    @TargetApi(11)
    public void cancelActionModeOrContextMenu() {
        if (VERSION.SDK_INT < 11) {
            getActivity().closeContextMenu();
        } else if (this.currentActionMode != null) {
            this.currentActionMode.finish();
        }
    }

    @TargetApi(11)
    protected void enableActionModeOrContextMenu() {
        if (VERSION.SDK_INT >= 11) {
            getListView().setChoiceMode(3);
            getListView().setMultiChoiceModeListener(new MyMultiChoiceModeListener());
            return;
        }
        registerForContextMenu(getListView());
    }

    protected void onActionModeActive(boolean active) {
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        int menuResId = getActionModeOrContextMenuResId();
        if (menuResId > 0) {
            this.contextMenuSelectenItemPosition = info.position;
            getActivity().getMenuInflater().inflate(menuResId, menu);
            super.onCreateContextMenu(menu, v, menuInfo);
            return;
        }
        this.contextMenuSelectenItemPosition = -1;
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (this.contextMenuSelectenItemPosition > -1) {
            try {
                return onActionModeOrContextMenuItemClicked(item.getItemId(),
                                                            (T) this.adapter.getItem(this.contextMenuSelectenItemPosition),
                                                            this.contextMenuSelectenItemPosition,
                                                            1,
                                                            true);
            } catch (Exception e) {
                e.printStackTrace();
                this.contextMenuSelectenItemPosition = -1;
            }
        }
        return false;
    }

    protected int getActionModeOrContextMenuResId() {
        return 0;
    }

    @TargetApi(11)
    protected void onActionModeItemsCheckedCountChanged(ActionMode mode, int checkedItemCount) {
    }

    protected boolean onActionModeOrContextMenuItemClicked(int menuItemId,
                                                           T t,
                                                           int listItemPosition,
                                                           int checkedItemsCount,
                                                           boolean isForLastItem) {
        return false;
    }
}
