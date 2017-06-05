package haivo.us.crypto.fragment;

import haivo.us.crypto.activity.CheckerAddActivity;
import haivo.us.crypto.activity.CheckerAddAlarmActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.R;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.util.AlarmRecordHelper;
import haivo.us.crypto.util.CurrencyUtils;
import haivo.us.crypto.util.TickerUtils;
import haivo.us.crypto.view.ViewAlarmCheckPointPreference;
import haivo.us.crypto.view.ViewAlarmTypeSpinnerPreference;
import haivo.us.crypto.view.ViewAlarmValuePickerPreference;
import haivo.us.crypto.view.generic.ViewSpinnerPreference;
import haivo.us.crypto.view.generic.ViewTwoStatePreference;

public class CheckerAddAlarmFragment extends Fragment {
    private static final String EXTRA_SHOULD_SAVE_STATE = "should_save_state";
    private static final String EXTRA_UNSAVED_CHANGES = "unsaved_changes";
    @BindView(R.id.alarmCheckPointView) ViewAlarmCheckPointPreference alarmCheckPointView;
    @BindView(R.id.alarmEnabledView) ViewTwoStatePreference alarmEnabledView;
    @BindView(R.id.alarmLedView) ViewTwoStatePreference alarmLedView;
    private AlarmRecord alarmRecord;
    @BindView(R.id.alarmSoundView) ViewTwoStatePreference alarmSoundView;
    @BindView(R.id.alarmTTSView) ViewTwoStatePreference alarmTTSView;
    @BindView(R.id.alarmTypeView) ViewAlarmTypeSpinnerPreference alarmTypeView;
    @BindView(R.id.alarmValueView) ViewAlarmValuePickerPreference alarmValueView;
    @BindView(R.id.alarmVibrateView) ViewTwoStatePreference alarmVibrateView;
    private CheckerRecord checkerRecord;
    private boolean unsavedChanges;

    /* renamed from: haivo.us.crypto.fragment.CheckerAddAlarmFragment.1 */
    class C03261 implements ViewTwoStatePreference.OnCheckChangedListener {
        C03261() {
        }

        public boolean onCheckChanged(ViewTwoStatePreference viewCheckBoxPreference, boolean checked) {
            CheckerAddAlarmFragment.this.alarmRecord.setEnabled(checked);
            CheckerAddAlarmFragment.this.unsavedChanges = true;
            CheckerAddAlarmFragment.this.refreshAlarmToggles(checked);
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddAlarmFragment.2 */
    class C03272 implements ViewSpinnerPreference.OnItemSelectedListener {
        C03272() {
        }

        public boolean onItemSelected(ViewSpinnerPreference viewSpinnerPreference, int position) {
            CheckerAddAlarmFragment.this.alarmRecord.setType((long) AlarmRecordHelper.getAlarmTypeForPosition(
                CheckerAddAlarmFragment.this.getActivity(),
                position));
            CheckerAddAlarmFragment.this.unsavedChanges = true;
            CheckerAddAlarmFragment.this.refreshAlarmValueView();
            CheckerAddAlarmFragment.this.refreshLastCheckPointVisibility();
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddAlarmFragment.3 */
    class C03283 implements ViewAlarmValuePickerPreference.OnValueChangedListener {
        C03283() {
        }

        public boolean onValueChanged(ViewAlarmValuePickerPreference viewAlarmValuePickerPreference, double number) {
            CheckerAddAlarmFragment.this.alarmRecord.setValue(number);
            CheckerAddAlarmFragment.this.unsavedChanges = true;
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddAlarmFragment.4 */
    class C03294 implements ViewAlarmCheckPointPreference.OnCheckpointChangedListener {
        C03294() {
        }

        public boolean onCheckpointChanged(ViewAlarmCheckPointPreference viewAlarmCheckpointPreference,
                                           Ticker lastCheckPointTicker) {
            CheckerAddAlarmFragment.this.alarmRecord.setLastCheckPointTicker(lastCheckPointTicker != null
                                                                             ? TickerUtils.toJson(lastCheckPointTicker)
                                                                             : null);
            CheckerAddAlarmFragment.this.unsavedChanges = true;
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddAlarmFragment.5 */
    class C03305 implements ViewTwoStatePreference.OnCheckChangedListener {
        C03305() {
        }

        public boolean onCheckChanged(ViewTwoStatePreference viewCheckBoxPreference, boolean checked) {
            CheckerAddAlarmFragment.this.alarmRecord.setSound(checked);
            CheckerAddAlarmFragment.this.unsavedChanges = true;
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddAlarmFragment.6 */
    class C03316 implements ViewTwoStatePreference.OnCheckChangedListener {
        C03316() {
        }

        public boolean onCheckChanged(ViewTwoStatePreference viewCheckBoxPreference, boolean checked) {
            CheckerAddAlarmFragment.this.alarmRecord.setVibrate(checked);
            CheckerAddAlarmFragment.this.unsavedChanges = true;
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddAlarmFragment.7 */
    class C03327 implements ViewTwoStatePreference.OnCheckChangedListener {
        C03327() {
        }

        public boolean onCheckChanged(ViewTwoStatePreference viewCheckBoxPreference, boolean checked) {
            CheckerAddAlarmFragment.this.alarmRecord.setLed(checked);
            CheckerAddAlarmFragment.this.unsavedChanges = true;
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.CheckerAddAlarmFragment.8 */
    class C03338 implements ViewTwoStatePreference.OnCheckChangedListener {
        C03338() {
        }

        public boolean onCheckChanged(ViewTwoStatePreference viewCheckBoxPreference, boolean checked) {
            CheckerAddAlarmFragment.this.alarmRecord.setTtsEnabled(checked);
            CheckerAddAlarmFragment.this.unsavedChanges = true;
            return true;
        }
    }

    public static final CheckerAddAlarmFragment newInstance(CheckerRecord checkerRecord, AlarmRecord alarmRecord) {
        CheckerAddAlarmFragment fragment = new CheckerAddAlarmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CheckerAddActivity.EXTRA_CHECKER_RECORD, checkerRecord);
        bundle.putParcelable(CheckerAddAlarmActivity.EXTRA_ALARM_RECORD, alarmRecord);
        bundle.putBoolean(EXTRA_SHOULD_SAVE_STATE, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checker_add_alarm_fragment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (shouldSaveState()) {
            if (savedInstanceState != null) {
                this.alarmRecord =
                    (AlarmRecord) savedInstanceState.getParcelable(CheckerAddAlarmActivity.EXTRA_ALARM_RECORD);
                this.unsavedChanges = savedInstanceState.getBoolean(EXTRA_UNSAVED_CHANGES);
            } else if (getArguments() != null) {
                this.alarmRecord =
                    (AlarmRecord) getArguments().getParcelable(CheckerAddAlarmActivity.EXTRA_ALARM_RECORD);
            }
            if (getArguments() != null) {
                this.checkerRecord =
                    (CheckerRecord) getArguments().getParcelable(CheckerAddActivity.EXTRA_CHECKER_RECORD);
            }
        } else if (savedInstanceState != null) {
            this.unsavedChanges = savedInstanceState.getBoolean(EXTRA_UNSAVED_CHANGES);
        }
        setCheckerAndAlarmRecord(this.checkerRecord, this.alarmRecord);
    }

    public void setCheckerAndAlarmRecord(CheckerRecord checkerRecord, AlarmRecord alarmRecord) {
        if (alarmRecord != null && checkerRecord != null) {
            this.checkerRecord = checkerRecord;
            this.alarmRecord = alarmRecord;
            refreshUI();
        }
    }

    private boolean shouldSaveState() {
        return getArguments() != null && getArguments().getBoolean(EXTRA_SHOULD_SAVE_STATE);
    }

    private void refreshUI() {
        refreshAlarmToggles(this.alarmRecord.getEnabled());
        this.alarmEnabledView.setOnCheckChangedListener(null);
        this.alarmEnabledView.setChecked(this.alarmRecord.getEnabled());
        this.alarmEnabledView.setOnCheckChangedListener(new C03261());
        this.alarmTypeView.setOnItemSelectedListener(null);
        this.alarmTypeView.setSelection(AlarmRecordHelper.getPositionForAlarmType(getActivity(), this.alarmRecord));
        this.alarmTypeView.setOnItemSelectedListener(new C03272());
        this.alarmValueView.setOnValueChangedListener(null);
        this.alarmValueView.setCheckerAndAlarmRecord(this.checkerRecord, this.alarmRecord);
        refreshAlarmValueView();
        this.alarmValueView.setOnValueChangedListener(new C03283());
        this.alarmCheckPointView.setOnCheckpointChangedListener(null);
        refreshLastCheckPoint();
        this.alarmCheckPointView.setOnCheckpointChangedListener(new C03294());
        this.alarmSoundView.setOnCheckChangedListener(null);
        this.alarmSoundView.setChecked(this.alarmRecord.getSound());
        this.alarmSoundView.setOnCheckChangedListener(new C03305());
        this.alarmVibrateView.setOnCheckChangedListener(null);
        this.alarmVibrateView.setChecked(this.alarmRecord.getVibrate());
        this.alarmVibrateView.setOnCheckChangedListener(new C03316());
        this.alarmLedView.setOnCheckChangedListener(null);
        this.alarmLedView.setChecked(this.alarmRecord.getLed());
        this.alarmLedView.setOnCheckChangedListener(new C03327());
        this.alarmTTSView.setOnCheckChangedListener(null);
        this.alarmTTSView.setChecked(this.alarmRecord.getTtsEnabled());
        this.alarmTTSView.setOnCheckChangedListener(new C03338());
    }

    public void onSaveInstanceState(Bundle outState) {
        if (shouldSaveState()) {
            outState.putParcelable(CheckerAddAlarmActivity.EXTRA_ALARM_RECORD, this.alarmRecord);
        }
        outState.putBoolean(EXTRA_UNSAVED_CHANGES, this.unsavedChanges);
        super.onSaveInstanceState(outState);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public AlarmRecord getAlarmRecord() {
        return this.alarmRecord;
    }

    public boolean getUnsavedChanges() {
        return this.unsavedChanges;
    }

    private void refreshLastCheckPoint() {
        this.alarmCheckPointView.setSufix(CurrencyUtils.getCurrencySymbol(CurrencyUtils.getCurrencySubunit(this.checkerRecord
                                                                                                               .getCurrencyDst(),
                                                                                                           this.checkerRecord
                                                                                                               .getCurrencySubunitDst()).name));
        this.alarmCheckPointView.setCheckerAndAlarmRecord(this.checkerRecord, this.alarmRecord);
        refreshLastCheckPointVisibility();
    }

    private void refreshLastCheckPointVisibility() {
        this.alarmCheckPointView.setVisibility(AlarmRecordHelper.isCheckPointAvailableForAlarmType(this.alarmRecord)
                                               ? View.VISIBLE
                                               : View.GONE);
    }

    private void refreshAlarmToggles(boolean isAlarmEnabled) {
        this.alarmTypeView.setEnabled(isAlarmEnabled);
        this.alarmValueView.setEnabled(isAlarmEnabled);
        this.alarmCheckPointView.setEnabled(isAlarmEnabled);
        this.alarmSoundView.setEnabled(isAlarmEnabled);
        this.alarmVibrateView.setEnabled(isAlarmEnabled);
        this.alarmLedView.setEnabled(isAlarmEnabled);
        this.alarmTTSView.setEnabled(isAlarmEnabled);
    }

    private void refreshAlarmValueView() {
        this.alarmValueView.setPrefix(AlarmRecordHelper.getPrefixForAlarmType(this.checkerRecord, this.alarmRecord));
        this.alarmValueView.setSufix(AlarmRecordHelper.getSufixForAlarmType(this.checkerRecord, this.alarmRecord));
        this.alarmValueView.setValue(this.alarmRecord.getValue());
    }
}
