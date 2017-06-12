package haivo.us.crypto.fragment;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.linearlistview.LinearListView;
import com.linearlistview.LinearListView.OnItemClickListener;
import haivo.us.crypto.R;
import haivo.us.crypto.activity.CheckerAddActivity;
import haivo.us.crypto.activity.CheckerAddAlarmActivity;
import haivo.us.crypto.activity.MarketPickerListActivity;
import haivo.us.crypto.adapter.CheckerAlarmsListAdapter;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.dialog.DynamicCurrencyPairsDialog;
import haivo.us.crypto.mechanoid.Mechanoid;
import haivo.us.crypto.mechanoid.db.SQuery;
import haivo.us.crypto.mechanoid.db.SQuery.Op;
import haivo.us.crypto.model.CurrencySubunit;
import haivo.us.crypto.model.FuturesMarket;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.currency.CurrenciesSubunits;
import haivo.us.crypto.model.market.Unknown;
import haivo.us.crypto.receiver.MarketChecker;
import haivo.us.crypto.util.AlarmRecordHelper;
import haivo.us.crypto.util.CheckerRecordHelper;
import haivo.us.crypto.util.CurrencyPairsMapHelper;
import haivo.us.crypto.util.FormatUtils;
import haivo.us.crypto.util.MarketCurrencyPairsStore;
import haivo.us.crypto.util.MarketsConfigUtils;
import haivo.us.crypto.util.NotificationUtils;
import haivo.us.crypto.util.PreferencesUtils;
import haivo.us.crypto.util.Utils;
import haivo.us.crypto.view.ViewCurrencySpinnerPreference;
import haivo.us.crypto.view.generic.ViewFrequencyPickerPreference;
import haivo.us.crypto.view.generic.ViewPreference;
import haivo.us.crypto.view.generic.ViewSpinnerPreference;
import haivo.us.crypto.view.generic.ViewTwoStatePreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;

public class CheckerAddFragment extends Fragment {
    private static final String EXTRA_ALARM_RECORDS = "alarm_records";
    private static final String EXTRA_REMOVED_ALARM_RECORD_IDS = "removed_alarm_record_ids";
    private static final String EXTRA_UNSAVED_CHANGES = "unsaved_changes";
    private static final int REQ_EDIT_ALARM = 200;
    private static final int REQ_MARKET_PICKER = 199;
    private ArrayList<AlarmRecord> alarmRecords;
    @BindView(R.id.alarmSectionHeader)
    TextView alarmSectionHeader;
    @BindView(R.id.alarmSectionWrapper)
    View alarmSectionWrapper;
    private CheckerAlarmsListAdapter alarmsAdapter;
    @BindView(R.id.alarmsListView)
    LinearListView alarmsListView;
    @BindView(R.id.checkSectionWrapper)
    View checkSectionWrapper;
    @BindView(R.id.checkTTSView)
    ViewTwoStatePreference checkTTSView;
    private CheckerAddAlarmFragment checkerAddAlarmFragment;
    @BindView(R.id.checkerAddAlarmFragmentWrapper)
    View checkerAddAlarmFragmentWrapper;
    private CheckerRecord checkerRecord;
    @BindView(R.id.currencyDstSpinner)
    ViewCurrencySpinnerPreference currencyDstSpinner;
    @BindView(R.id.currencyDstSubunitSpinner)
    ViewSpinnerPreference currencyDstSubunitSpinner;
    private CurrencyPairsMapHelper currencyPairsMapHelper;
    @BindView(R.id.currencySpinnersAndDynamicPairsWrapper)
    View currencySpinnersAndDynamicPairsWrapper;
    @BindView(R.id.currencySpinnersWrapper)
    View currencySpinnersWrapper;
    @BindView(R.id.currencySrcSpinner)
    ViewCurrencySpinnerPreference currencySrcSpinner;
    @BindView(R.id.currencySrcSubunitSpinner)
    ViewSpinnerPreference currencySrcSubunitSpinner;
    @BindView(R.id.dynamicCurrencyPairsInfoView)
    View dynamicCurrencyPairsInfoView;
    @BindView(R.id.dynamicCurrencyPairsNoSyncYetView)
    View dynamicCurrencyPairsNoSyncYetView;
    @BindView(R.id.dynamicCurrencyPairsWarningView)
    View dynamicCurrencyPairsWarningView;
    @BindView(R.id.enabledView)
    ViewTwoStatePreference enabledView;
    @BindView(R.id.frequencySpinner)
    ViewFrequencyPickerPreference frequencySpinner;
    @BindView(R.id.futuresContractTypeSpinner)
    ViewSpinnerPreference futuresContractTypeSpinner;
    @BindView(R.id.marketCautionView)
    TextView marketCautionView;
    @BindView(R.id.marketSpinner)
    ViewPreference marketSpinner;
    @BindView(R.id.notificationPriorityView)
    ViewTwoStatePreference notificationPriorityView;
    private ArrayList<Long> removedAlarmRecordIds;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private boolean unsavedChanges;

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.4 */
    class C01734 implements OnClickListener {
        C01734() {
        }

        public void onClick(DialogInterface dialog, int which) {
            PreferencesUtils.setExchangeTutorialDone(CheckerAddFragment.this.getActivity());
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.5 */
    class C01745 implements View.OnClickListener {
        C01745() {
        }

        public void onClick(View v) {
            MarketPickerListActivity.startActivityForResult(CheckerAddFragment.this,
                                                            CheckerAddFragment.REQ_MARKET_PICKER,
                                                            CheckerAddFragment.this.checkerRecord.getMarketKey());
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.6 */
    class C01756 implements View.OnClickListener {

        /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.6.1 */
        class C03371 extends DynamicCurrencyPairsDialog {
            C03371(Context context, Market market, CurrencyPairsMapHelper currencyPairsMapHelper) {
                super(context, market, currencyPairsMapHelper);
            }

            public void onPairsUpdated(Market market, CurrencyPairsMapHelper currencyPairsMapHelper) {
                CheckerAddFragment.this.currencyPairsMapHelper = currencyPairsMapHelper;
                if (CheckerAddFragment.this.getView() != null && CheckerAddFragment.this.getActivity() != null) {
                    CheckerAddFragment.this.refreshDynamicCurrencyPairsView(market);
                    CheckerAddFragment.this.refreshCurrencySpinnersForMarket(market, true);
                }
            }
        }

        C01756() {
        }

        public void onClick(View v) {
            new C03371(CheckerAddFragment.this.getActivity(),
                       CheckerAddFragment.this.getSelectedMarket(),
                       CheckerAddFragment.this.currencyPairsMapHelper).show();
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.1 */
    class C03341 extends CheckerAlarmsListAdapter {

        /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.1.1 */
        class C01721 implements OnClickListener {
            final /* synthetic */ int val$position;

            C01721(int i) {
                this.val$position = i;
            }

            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    AlarmRecord removedAlarmRecord =
                        (AlarmRecord) CheckerAddFragment.this.alarmRecords.remove(this.val$position);
                    if (removedAlarmRecord != null && removedAlarmRecord.getId() > 0) {
                        CheckerAddFragment.this.removedAlarmRecordIds.add(Long.valueOf(removedAlarmRecord.getId()));
                    }
                    CheckerAddFragment.this.unsavedChanges = true;
                    CheckerAddFragment.this.refreshAlarms();
                }
            }
        }

        C03341(Context context, CheckerAddFragment checkerAddFragment, CheckerRecord checkerRecord, List items) {
            super(context, checkerAddFragment, checkerRecord, items);
        }

        public void onItemLongClick(AlarmRecord alarmRecord, int position) {
            new Builder(CheckerAddFragment.this.getActivity()).setItems(R.array.checker_add_alarm_context_menu,
                                                                        new C01721(position)).show();
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.2 */
    class C03352 implements OnItemClickListener {
        C03352() {
        }

        public void onItemClick(LinearListView parent, View view, int position, long id) {
            if (position >= CheckerAddFragment.this.alarmsAdapter.getCount() - 1) {
                AlarmRecord newAlarmRecord = AlarmRecordHelper.generateDefaultAlarmRecord(true);
                CheckerAddFragment.this.alarmRecords.add(newAlarmRecord);
                CheckerAddFragment.this.unsavedChanges = true;
                CheckerAddFragment.this.refreshAlarms();
                CheckerAddAlarmActivity.startCheckerAddAlarmActivity(CheckerAddFragment.this,
                                                                     CheckerAddFragment.REQ_EDIT_ALARM,
                                                                     CheckerAddFragment.this.checkerRecord,
                                                                     newAlarmRecord,
                                                                     CheckerAddFragment.this.alarmRecords.indexOf(
                                                                         newAlarmRecord));
                return;
            }
            CheckerAddAlarmActivity.startCheckerAddAlarmActivity(CheckerAddFragment.this,
                                                                 CheckerAddFragment.REQ_EDIT_ALARM,
                                                                 CheckerAddFragment.this.checkerRecord,
                                                                 (AlarmRecord) CheckerAddFragment.this.alarmRecords.get(
                                                                     position),
                                                                 position);
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.3 */
    class C03363 implements ViewTwoStatePreference.OnCheckChangedListener {
        C03363() {
        }

        public boolean onCheckChanged(ViewTwoStatePreference viewCheckBoxPreference, boolean checked) {
            CheckerAddFragment.this.checkerRecord.setEnabled(checked);
            CheckerAddFragment.this.unsavedChanges = true;
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.7 */
    class C03387 implements ViewSpinnerPreference.OnItemSelectedListener {
        C03387() {
        }

        public boolean onItemSelected(ViewSpinnerPreference viewSpinnerPreference, int position) {
            String newCurrency = viewSpinnerPreference.getEntries() != null
                                 ? String.valueOf(viewSpinnerPreference.getEntries()[position])
                                 : null;
            if (newCurrency == null || !newCurrency.equals(CheckerAddFragment.this.checkerRecord.getCurrencySrc())) {
                CheckerAddFragment.this.checkerRecord.setCurrencySrc(newCurrency);
                CheckerAddFragment.this.checkerRecord.setCurrencySubunitSrc(1);
                CheckerAddFragment.this.unsavedChanges = true;
                CheckerAddFragment.this.clearLastAndPreviousCheck();
                CheckerAddFragment.this.clearAlarmsLastCheckPoint();
                CheckerAddFragment.this.refreshCurrencySrcSubunits();
                CheckerAddFragment.this.refreshCurrencyDstSpinnerForMarket(CheckerAddFragment.this.getSelectedMarket(),
                                                                           true);
            }
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.8 */
    class C03398 implements ViewSpinnerPreference.OnItemSelectedListener {
        C03398() {
        }

        public boolean onItemSelected(ViewSpinnerPreference viewSpinnerPreference, int position) {
            String newCurrency = viewSpinnerPreference.getEntries() != null
                                 ? String.valueOf(viewSpinnerPreference.getEntries()[position])
                                 : null;
            if (newCurrency == null || !newCurrency.equals(CheckerAddFragment.this.checkerRecord.getCurrencyDst())) {
                CheckerAddFragment.this.checkerRecord.setCurrencyDst(newCurrency);
                CheckerAddFragment.this.checkerRecord.setCurrencySubunitDst(1);
                CheckerAddFragment.this.unsavedChanges = true;
                CheckerAddFragment.this.clearLastAndPreviousCheck();
                CheckerAddFragment.this.clearAlarmsLastCheckPoint();
                CheckerAddFragment.this.refreshCurrencyDstSubunits();
                CheckerAddFragment.this.refreshAlarms();
            }
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddFragment.9 */
    class C03409 implements ViewSpinnerPreference.OnItemSelectedListener {
        C03409() {
        }

        public boolean onItemSelected(ViewSpinnerPreference viewSpinnerPreference, int position) {
            Market market = CheckerAddFragment.this.getSelectedMarket();
            if (market instanceof FuturesMarket) {
                FuturesMarket futuresMarket = (FuturesMarket) market;
                if (position >= 0 && position < futuresMarket.contractTypes.length) {
                    CheckerAddFragment.this.checkerRecord.setContractType((long) futuresMarket.contractTypes[position]);
                    CheckerAddFragment.this.unsavedChanges = true;
                    CheckerAddFragment.this.clearLastAndPreviousCheck();
                    CheckerAddFragment.this.clearAlarmsLastCheckPoint();
                }
            }
            return true;
        }
    }

    public CheckerAddFragment() {
        this.unsavedChanges = false;
    }

    public static final CheckerAddFragment newInstance(CheckerRecord checkerRecord, long alarmRecordId) {
        CheckerAddFragment fragment = new CheckerAddFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CheckerAddActivity.EXTRA_CHECKER_RECORD, checkerRecord);
        bundle.putLong(MarketChecker.EXTRA_ALARM_RECORD_ID, alarmRecordId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checker_add_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            this.checkerRecord =
                (CheckerRecord) savedInstanceState.getParcelable(CheckerAddActivity.EXTRA_CHECKER_RECORD);
            List<AlarmRecord> savedAlarmRecords = (List) savedInstanceState.getSerializable(EXTRA_ALARM_RECORDS);
            if (savedAlarmRecords != null) {
                this.alarmRecords = new ArrayList(savedAlarmRecords);
            }
            this.removedAlarmRecordIds = (ArrayList) savedInstanceState.getSerializable(EXTRA_REMOVED_ALARM_RECORD_IDS);
            this.unsavedChanges = savedInstanceState.getBoolean(EXTRA_UNSAVED_CHANGES);
        } else if (getArguments() != null) {
            this.checkerRecord = (CheckerRecord) getArguments().getParcelable(CheckerAddActivity.EXTRA_CHECKER_RECORD);
        }
        if (this.checkerRecord == null) {
            this.checkerRecord = new CheckerRecord();
            this.checkerRecord.setEnabled(true);
            this.checkerRecord.setMarketKey(PreferencesUtils.getMarketDefault(getActivity()));
            this.checkerRecord.setCurrencySubunitSrc(1);
            this.checkerRecord.setCurrencySubunitDst(1);
            this.checkerRecord.setFrequency(-1);
            this.checkerRecord.setNotificationPriority(-2);
            this.checkerRecord.setTtsEnabled(false);
            this.checkerRecord.setSortOrder(Long.MAX_VALUE);
            this.alarmRecords = new ArrayList();
            this.alarmRecords.add(AlarmRecordHelper.generateDefaultAlarmRecord(false));
        } else if (this.alarmRecords == null) {
            this.alarmRecords = new ArrayList(CheckerRecordHelper.getAlarmsForCheckerRecord(this.checkerRecord, false));
        }
        NotificationUtils.clearAlarmNotificationForCheckerRecord(getActivity(), this.checkerRecord);
        if (this.removedAlarmRecordIds == null) {
            this.removedAlarmRecordIds = new ArrayList();
        }
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                                           .setTitle(this.checkerRecord.getId() > 0
                                                     ? R.string.checker_add_title_edit
                                                     : R.string.checker_add_title_new);
        this.checkerAddAlarmFragment =
            (CheckerAddAlarmFragment) getChildFragmentManager().findFragmentById(R.id.checkerAddAlarmFragment);
        this.alarmsAdapter = new C03341(getActivity(), this, this.checkerRecord, this.alarmRecords);
        this.alarmsListView.setAdapter(this.alarmsAdapter);
        this.alarmsListView.setOnItemClickListener(new C03352());
        this.enabledView.setChecked(this.checkerRecord.getEnabled());
        this.enabledView.setOnCheckChangedListener(new C03363());
        Market selectedMarket = MarketsConfigUtils.getMarketByKey(this.checkerRecord.getMarketKey());
        if (selectedMarket instanceof Unknown) {
            selectedMarket = MarketsConfigUtils.getMarketById(0);
            PreferencesUtils.setMarketDefault(getActivity(), selectedMarket.key);
            this.checkerRecord.setMarketKey(selectedMarket.key);
            new Builder(getActivity()).setCancelable(false)
                                      .setTitle(R.string.checker_add_exchange_unknown_title)
                                      .setMessage(R.string.checker_add_exchange_unknown_text)
                                      .setPositiveButton(R.string.ok, null)
                                      .show();
        } else if (!PreferencesUtils.isExchangeTutorialDone(getActivity())
            && SQuery.newQuery().count(MaindbContract.Checker.CONTENT_URI) == 0) {
            new Builder(getActivity()).setCancelable(false)
                                      .setTitle(R.string.checker_add_exchange_tutorial_title)
                                      .setMessage(R.string.checker_add_exchange_tutorial_text)
                                      .setPositiveButton(R.string.ok, new C01734())
                                      .show();
        }
        this.currencyPairsMapHelper =
            new CurrencyPairsMapHelper(MarketCurrencyPairsStore.getPairsForMarket(getActivity(), selectedMarket.key));
        this.marketSpinner.setSummary(selectedMarket.name);
        refreshMarketCautionView(selectedMarket);
        this.marketSpinner.setOnClickListener(new C01745());
        refreshDynamicCurrencyPairsView(selectedMarket);
        View.OnClickListener dynamicCurrencyPairsOnClickListener = new C01756();
        this.currencySpinnersAndDynamicPairsWrapper.setOnClickListener(dynamicCurrencyPairsOnClickListener);
        this.currencySpinnersAndDynamicPairsWrapper.setClickable(false);
        this.currencySpinnersAndDynamicPairsWrapper.setFocusable(false);
        this.dynamicCurrencyPairsInfoView.setOnClickListener(dynamicCurrencyPairsOnClickListener);
        refreshCurrencySpinnersForMarket(selectedMarket, false);
        this.currencySrcSpinner.setOnItemSelectedListener(new C03387());
        this.currencyDstSpinner.setOnItemSelectedListener(new C03398());
        this.currencySrcSpinner.setOnSyncClickedListener(dynamicCurrencyPairsOnClickListener);
        this.currencyDstSpinner.setOnSyncClickedListener(dynamicCurrencyPairsOnClickListener);
        refreshFuturesContractTypes(selectedMarket);
        this.futuresContractTypeSpinner.setOnItemSelectedListener(new C03409());
        refreshCurrencySrcSubunits();
        this.currencySrcSubunitSpinner.setOnItemSelectedListener(new ViewSpinnerPreference.OnItemSelectedListener() {
            public boolean onItemSelected(ViewSpinnerPreference viewSpinnerPreference, int position) {
                try {
                    if (CurrenciesSubunits.CURRENCIES_SUBUNITS.containsKey(checkerRecord.getCurrencySrc())) {
                        checkerRecord.setCurrencySubunitSrc(((CurrencySubunit) CurrenciesSubunits.CURRENCIES_SUBUNITS.get(
                            checkerRecord.getCurrencySrc()).values().toArray()[position]).subunitToUnit);
                    } else {
                        CheckerAddFragment.this.checkerRecord.setCurrencySubunitSrc(1);
                    }
                } catch (Exception e) {
                    CheckerAddFragment.this.checkerRecord.setCurrencySubunitSrc(1);
                    e.printStackTrace();
                }
                CheckerAddFragment.this.unsavedChanges = true;
                CheckerAddFragment.this.refreshAlarms();
                return true;
            }
        });
        refreshCurrencyDstSubunits();
        this.currencyDstSubunitSpinner.setOnItemSelectedListener(new ViewSpinnerPreference.OnItemSelectedListener() {
            public boolean onItemSelected(ViewSpinnerPreference viewSpinnerPreference, int position) {
                try {
                    if (CurrenciesSubunits.CURRENCIES_SUBUNITS.containsKey(CheckerAddFragment.this.checkerRecord.getCurrencyDst())) {
                        CheckerAddFragment.this.checkerRecord.setCurrencySubunitDst(((CurrencySubunit) CurrenciesSubunits.CURRENCIES_SUBUNITS
                            .get(CheckerAddFragment.this.checkerRecord.getCurrencyDst())
                            .values()
                            .toArray()[position]).subunitToUnit);
                    } else {
                        CheckerAddFragment.this.checkerRecord.setCurrencySubunitDst(1);
                    }
                } catch (Exception e) {
                    CheckerAddFragment.this.checkerRecord.setCurrencySubunitDst(1);
                    e.printStackTrace();
                }
                CheckerAddFragment.this.unsavedChanges = true;
                CheckerAddFragment.this.refreshAlarms();
                return true;
            }
        });
        this.frequencySpinner.setFrequency(this.checkerRecord.getFrequency());
        this.frequencySpinner.setOnFrequencySelectedListener(new ViewFrequencyPickerPreference.OnFrequencySelectedListener() {
            public boolean onFrequencySelected(ViewFrequencyPickerPreference viewSpinnerPreference, long frequency) {
                CheckerAddFragment.this.checkerRecord.setFrequency(frequency);
                CheckerAddFragment.this.unsavedChanges = true;
                return true;
            }
        });
        this.notificationPriorityView.setChecked(this.checkerRecord.getNotificationPriority() >= -2);
        this.notificationPriorityView.setOnCheckChangedListener(new ViewTwoStatePreference.OnCheckChangedListener() {
            public boolean onCheckChanged(ViewTwoStatePreference viewCheckBoxPreference, boolean checked) {
                CheckerAddFragment.this.checkerRecord.setNotificationPriority(checked ? -2 : -100);
                CheckerAddFragment.this.unsavedChanges = true;
                return true;
            }
        });
        this.checkTTSView.setChecked(this.checkerRecord.getTtsEnabled());
        this.checkTTSView.setOnCheckChangedListener(new ViewTwoStatePreference.OnCheckChangedListener() {
            public boolean onCheckChanged(ViewTwoStatePreference viewCheckBoxPreference, boolean checked) {
                CheckerAddFragment.this.checkerRecord.setTtsEnabled(checked);
                CheckerAddFragment.this.unsavedChanges = true;
                return true;
            }
        });
        this.alarmSectionHeader.setText(R.string.checker_add_alarm_category_title);
        if (getArguments() != null && savedInstanceState == null) {
            long alarmRecordId = getArguments().getLong(MarketChecker.EXTRA_ALARM_RECORD_ID, -1);
            if (alarmRecordId > 0 && this.alarmRecords != null) {
                int i = 0;
                while (i < this.alarmRecords.size()) {
                    AlarmRecord alarmRecord = (AlarmRecord) this.alarmRecords.get(i);
                    if (alarmRecord == null || alarmRecord.getId() != alarmRecordId) {
                        i++;
                    } else {
                        getArguments().remove(MarketChecker.EXTRA_ALARM_RECORD_ID);
                        if (this.alarmRecords.size() == 1) {
                            this.scrollView.post(new Runnable() {
                                public void run() {
                                    CheckerAddFragment.this.scrollView.scrollTo(0,
                                                                                CheckerAddFragment.this.alarmSectionHeader
                                                                                    .getBottom());
                                }
                            });
                            return;
                        } else {
                            CheckerAddAlarmActivity.startCheckerAddAlarmActivity(this,
                                                                                 REQ_EDIT_ALARM,
                                                                                 this.checkerRecord,
                                                                                 alarmRecord,
                                                                                 i);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CheckerAddActivity.EXTRA_CHECKER_RECORD, this.checkerRecord);
        outState.putSerializable(EXTRA_ALARM_RECORDS, this.alarmRecords);
        outState.putSerializable(EXTRA_REMOVED_ALARM_RECORD_IDS, this.removedAlarmRecordIds);
        outState.putBoolean(EXTRA_UNSAVED_CHANGES, this.unsavedChanges);
        super.onSaveInstanceState(outState);
    }

    private void onMarketChanged(Market market) {
        if (market != null) {
            this.marketSpinner.setSummary(market.name);
            refreshMarketCautionView(market);
            this.currencyPairsMapHelper =
                new CurrencyPairsMapHelper(MarketCurrencyPairsStore.getPairsForMarket(getActivity(), market.key));
            this.checkerRecord.setMarketKey(market.key);
            this.checkerRecord.setContractType(0);
            this.unsavedChanges = true;
            clearLastAndPreviousCheck();
            clearAlarmsLastCheckPoint();
            refreshDynamicCurrencyPairsView(market);
            refreshCurrencySpinnersForMarket(market, true);
            refreshFuturesContractTypes(market);
        }
    }

    private void refreshMarketCautionView(Market market) {
        int textResId;
        int i = 0;
        if (market != null) {
            textResId = market.getCautionResId();
        } else {
            textResId = 0;
        }
        String cautionText = textResId > 0 ? getString(textResId) : BuildConfig.VERSION_NAME;
        this.marketCautionView.setText(cautionText);
        TextView textView = this.marketCautionView;
        if (TextUtils.isEmpty(cautionText)) {
            i = 8;
        }
        textView.setVisibility(i);
    }

    private Market getSelectedMarket() {
        return MarketsConfigUtils.getMarketByKey(this.checkerRecord.getMarketKey());
    }

    private void refreshDynamicCurrencyPairsView(Market market) {
        boolean dynamicPairsEnabled;
        int i = 0;
        if (market.getCurrencyPairsUrl(0) != null) {
            dynamicPairsEnabled = true;
        } else {
            dynamicPairsEnabled = false;
        }
        this.dynamicCurrencyPairsInfoView.setEnabled(dynamicPairsEnabled);
        View view = this.dynamicCurrencyPairsNoSyncYetView;
        if (!dynamicPairsEnabled
            || market.currencyPairs == null
            || market.currencyPairs.isEmpty()
            || (this.currencyPairsMapHelper != null && this.currencyPairsMapHelper.getDate() > 0)) {
            i = 8;
        }
        view.setVisibility(i);
        this.currencySrcSpinner.setShowSyncButton(dynamicPairsEnabled);
        this.currencyDstSpinner.setShowSyncButton(dynamicPairsEnabled);
    }

    private void refreshCurrencySpinnersForMarket(Market market, boolean considerAsChange) {
        boolean isCurrencyEmpty;
        int i;
        int i2 = 8;
        refreshCurrencySrcSpinnerForMarket(market, considerAsChange);
        refreshCurrencyDstSpinnerForMarket(market, considerAsChange);
        if (this.checkerRecord.getCurrencySrc() == null || this.checkerRecord.getCurrencyDst() == null) {
            isCurrencyEmpty = true;
        } else {
            isCurrencyEmpty = false;
        }
        View view = this.dynamicCurrencyPairsWarningView;
        if (isCurrencyEmpty) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        this.currencySpinnersAndDynamicPairsWrapper.setClickable(isCurrencyEmpty);
        this.currencySpinnersAndDynamicPairsWrapper.setFocusable(isCurrencyEmpty);
        view = this.currencySpinnersWrapper;
        if (isCurrencyEmpty) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        view = this.checkSectionWrapper;
        if (isCurrencyEmpty) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        View view2 = this.alarmSectionWrapper;
        if (!isCurrencyEmpty) {
            i2 = 0;
        }
        view2.setVisibility(i2);
    }

    private HashMap<String, CharSequence[]> getProperCurrencyPairs(Market market) {
        if (this.currencyPairsMapHelper == null
            || this.currencyPairsMapHelper.getCurrencyPairs() == null
            || this.currencyPairsMapHelper.getCurrencyPairs().size() <= 0) {
            return market.currencyPairs;
        }
        return this.currencyPairsMapHelper.getCurrencyPairs();
    }

    private void refreshCurrencySrcSpinnerForMarket(Market market, boolean considerAsChange) {
        HashMap<String, CharSequence[]> currencyPairs = getProperCurrencyPairs(market);
        CharSequence oldSelectedValue = this.checkerRecord.getCurrencySrc();
        String newCurrency = null;
        if (currencyPairs == null || currencyPairs.size() <= 0) {
            this.currencySrcSpinner.setEntriesAndSelection(null, 0);
        } else {
            int selection = 0;
            CharSequence[] srcEntries = new CharSequence[currencyPairs.size()];
            int i = 0;
            for (String currency : currencyPairs.keySet()) {
                srcEntries[i] = currency;
                if (currency.equals(oldSelectedValue)) {
                    selection = i;
                }
                i++;
            }
            this.currencySrcSpinner.setEntriesAndSelection(srcEntries, selection);
            if (this.currencySrcSpinner.getEntry() != null) {
                newCurrency = String.valueOf(this.currencySrcSpinner.getEntry());
                if (!newCurrency.equals(this.checkerRecord.getCurrencySrc())) {
                    this.checkerRecord.setCurrencySubunitSrc(1);
                    if (this.checkerRecord.getCurrencySrc() != null) {
                        this.unsavedChanges = true;
                    }
                }
            }
        }
        this.checkerRecord.setCurrencySrc(newCurrency);
        refreshCurrencySrcSubunits();
        refreshAlarms();
    }

    private void refreshCurrencyDstSpinnerForMarket(Market market, boolean considerAsChange) {
        int i = 0;
        HashMap<String, CharSequence[]> currencyPairs = getProperCurrencyPairs(market);
        String oldSelectedValue = this.checkerRecord.getCurrencyDst();
        String newCurrency = null;
        if (currencyPairs == null || currencyPairs.size() <= 0 || this.checkerRecord.getCurrencySrc() == null) {
            this.currencyDstSpinner.setEntriesAndSelection(null, 0);
        } else {
            int selection = 0;
            CharSequence[] dstEntries =
                new CharSequence[((CharSequence[]) currencyPairs.get(this.checkerRecord.getCurrencySrc())).length];
            int i2 = 0;
            CharSequence[] charSequenceArr = (CharSequence[]) currencyPairs.get(this.checkerRecord.getCurrencySrc());
            int length = charSequenceArr.length;
            while (i < length) {
                CharSequence currency = charSequenceArr[i];
                dstEntries[i2] = currency;
                if (currency.equals(oldSelectedValue)) {
                    selection = i2;
                }
                i2++;
                i++;
            }
            this.currencyDstSpinner.setEntriesAndSelection(dstEntries, selection);
            if (this.currencyDstSpinner.getEntry() != null) {
                newCurrency = String.valueOf(this.currencyDstSpinner.getEntry());
                if (!newCurrency.equals(this.checkerRecord.getCurrencyDst())) {
                    this.checkerRecord.setCurrencySubunitDst(1);
                    if (this.checkerRecord.getCurrencyDst() != null) {
                        this.unsavedChanges = true;
                    }
                }
            }
        }
        this.checkerRecord.setCurrencyDst(newCurrency);
        refreshCurrencyDstSubunits();
        refreshAlarms();
    }

    private void refreshFuturesContractTypes(Market market) {
        CharSequence[] entries = null;
        int selection = 0;
        if (market instanceof FuturesMarket) {
            int oldContractType = (int) this.checkerRecord.getContractType();
            FuturesMarket futuresMarket = (FuturesMarket) market;
            entries = new CharSequence[futuresMarket.contractTypes.length];
            for (int i = 0; i < futuresMarket.contractTypes.length; i++) {
                int contractType = futuresMarket.contractTypes[i];
                entries[i] = FormatUtils.getContractTypeName(getActivity(), contractType);
                if (oldContractType == contractType) {
                    selection = i;
                }
            }
        }
        this.futuresContractTypeSpinner.setEntriesAndSelection(entries, selection);
        this.futuresContractTypeSpinner.setVisibility(entries != null ? View.VISIBLE : View.GONE);
    }

    private void refreshCurrencySrcSubunits() {
        this.currencySrcSubunitSpinner.setVisibility(View.GONE);
    }

    private void refreshCurrencyDstSubunits() {
        if (!refreshCurrencySubunits(this.checkerRecord.getCurrencyDst(),
                                     this.currencyDstSubunitSpinner,
                                     this.checkerRecord.getCurrencySubunitDst())) {
            if (this.checkerRecord.getCurrencySubunitDst() != 1) {
                this.unsavedChanges = true;
            }
            this.checkerRecord.setCurrencySubunitDst(1);
        }
    }

    private boolean refreshCurrencySubunits(String currency,
                                            ViewSpinnerPreference viewSpinnerPreference,
                                            long subunitToUnit) {
        CharSequence[] entries;
        boolean hasSubunits = true;
        int i = 0;
        int selection = 0;
        if (CurrenciesSubunits.CURRENCIES_SUBUNITS.containsKey(currency)) {
            LinkedHashMap<Long, CurrencySubunit> subunits =
                (LinkedHashMap) CurrenciesSubunits.CURRENCIES_SUBUNITS.get(currency);
            entries = new CharSequence[subunits.size()];
            int i2 = 0;
            for (CurrencySubunit currencySubunit : subunits.values()) {
                entries[i2] = currencySubunit.name;
                if (currencySubunit.subunitToUnit == subunitToUnit) {
                    selection = i2;
                }
                i2++;
            }
            if (entries == null || entries.length <= 0) {
                hasSubunits = false;
            }
        } else {
            entries = new CharSequence[] { currency };
            selection = 0;
            hasSubunits = false;
        }
        viewSpinnerPreference.setEntriesAndSelection(entries, selection);
        if (currency == null) {
            i = 8;
        }
        viewSpinnerPreference.setVisibility(i);
        viewSpinnerPreference.setEnabled(hasSubunits);
        return hasSubunits;
    }

    private void clearLastAndPreviousCheck() {
        if (!(TextUtils.isEmpty(this.checkerRecord.getLastCheckTicker())
            && TextUtils.isEmpty(this.checkerRecord.getPreviousCheckTicker()))) {
            this.unsavedChanges = true;
        }
        this.checkerRecord.setLastCheckTicker(null);
        this.checkerRecord.setPreviousCheckTicker(null);
    }

    private void clearAlarmsLastCheckPoint() {
        Iterator it = this.alarmRecords.iterator();
        while (it.hasNext()) {
            AlarmRecord alarmRecord = (AlarmRecord) it.next();
            if (!TextUtils.isEmpty(alarmRecord.getLastCheckPointTicker())) {
                this.unsavedChanges = true;
            }
            alarmRecord.setLastCheckPointTicker(null);
        }
    }

    private void refreshAlarms() {
        if (this.alarmRecords.size() == 1) {
            this.checkerAddAlarmFragment.setCheckerAndAlarmRecord(this.checkerRecord, this.alarmRecords.get(0));
            this.checkerAddAlarmFragmentWrapper.setVisibility(View.VISIBLE);
        } else {
            this.checkerAddAlarmFragmentWrapper.setVisibility(View.GONE);
        }
        this.alarmsAdapter.notifyDataSetChanged();
    }

    public void makeUnsavedChanges() {
        this.unsavedChanges = true;
    }

    public boolean onBackPressed() {
        if (!this.unsavedChanges && (this.checkerAddAlarmFragment == null
            || !this.checkerAddAlarmFragment.getUnsavedChanges())) {
            return false;
        }
        new Builder(getActivity()).setTitle(R.string.checker_add_unsaved_changes_dialog_title)
                                  .setMessage(R.string.checker_add_unsaved_changes_dialog_message)
                                  .setNegativeButton(R.string.generic_quit, new OnClickListener() {
                                      public void onClick(DialogInterface dialog, int which) {
                                          CheckerAddFragment.this.getActivity().finish();
                                      }
                                  })
                                  .setNeutralButton(R.string.cancel, null)
                                  .setPositiveButton(R.string.generic_save, new OnClickListener() {
                                      public void onClick(DialogInterface dialog, int which) {
                                          CheckerAddFragment.this.save();
                                      }
                                  })
                                  .show();
        return true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.checker_add_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveItem) {
            save();
            return true;
        } else if (item.getItemId() != R.id.deleteItem) {
            return super.onOptionsItemSelected(item);
        } else {
            delete();
            return true;
        }
    }

    private void save() {
        if (this.checkerRecord.getCurrencySrc() == null || this.checkerRecord.getCurrencyDst() == null) {
            Utils.showToast(getActivity(), (int) R.string.checker_add_save_error_no_currency_pair, true);
            return;
        }
        String currencyPairId;
        CheckerRecord checkerRecord = this.checkerRecord;
        if (this.currencyPairsMapHelper != null) {
            currencyPairId = this.currencyPairsMapHelper.getCurrencyPairId(this.checkerRecord.getCurrencySrc(),
                                                                           this.checkerRecord.getCurrencyDst());
        } else {
            currencyPairId = null;
        }
        checkerRecord.setCurrencyPairId(currencyPairId);
        try {
            this.checkerRecord.save(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (this.checkerRecord.getId() > 0) {
            Iterator it = this.removedAlarmRecordIds.iterator();
            while (it.hasNext()) {
                SQuery.newQuery()
                      .expr("_id", Op.EQ, ((Long) it.next()).longValue())
                      .delete(MaindbContract.Alarm.CONTENT_URI, false);
            }
            Iterator it2 = this.alarmRecords.iterator();
            while (it2.hasNext()) {
                AlarmRecord alarmRecord = (AlarmRecord) it2.next();
                alarmRecord.setCheckerId(this.checkerRecord.getId());
                alarmRecord.setEnabled(alarmRecord.getEnabled());
                alarmRecord.save(false);
            }
        }
        Mechanoid.getContentResolver().notifyChange(MaindbContract.Checker.CONTENT_URI, null);
        Mechanoid.getContentResolver().notifyChange(MaindbContract.Alarm.CONTENT_URI, null);
        CheckerRecordHelper.doAfterEdit(getActivity(), this.checkerRecord, true);
        getActivity().setResult(-1);
        getActivity().finish();
    }

    private void delete() {
        if (this.checkerRecord.getId() > 0) {
            CheckerRecordHelper.doBeforeDelete(getActivity(), this.checkerRecord);
            try {
                this.checkerRecord.delete(true);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            CheckerRecordHelper.doAfterDelete(getActivity(), this.checkerRecord);
        }
        getActivity().setResult(-1);
        getActivity().finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == REQ_EDIT_ALARM) {
                int alarmRecordPosition = data.getIntExtra(CheckerAddAlarmActivity.EXTRA_ALARM_RECORD_INDEX, -1);
                AlarmRecord alarmRecord = data.getParcelableExtra(CheckerAddAlarmActivity.EXTRA_ALARM_RECORD);
                if (alarmRecordPosition < 0 || alarmRecordPosition >= this.alarmRecords.size()) {
                    if (alarmRecord != null) {
                        this.alarmRecords.add(alarmRecord);
                    }
                } else if (alarmRecord != null) {
                    this.alarmRecords.set(alarmRecordPosition, alarmRecord);
                } else {
                    this.alarmRecords.remove(alarmRecordPosition);
                }
                this.unsavedChanges = true;
                refreshAlarms();
            } else if (requestCode == REQ_MARKET_PICKER) {
                onMarketChanged(MarketsConfigUtils.getMarketByKey(data.getStringExtra(MarketPickerListActivity.EXTRA_MARKET_KEY)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
