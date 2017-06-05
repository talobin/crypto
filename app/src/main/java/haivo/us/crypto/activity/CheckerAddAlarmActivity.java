package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleFragmentSubActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.fragment.CheckerAddAlarmFragment;
import haivo.us.crypto.R;

public class CheckerAddAlarmActivity extends SimpleFragmentSubActivity<CheckerAddAlarmFragment> {
    public static final String EXTRA_ALARM_RECORD = "alarm_record";
    public static final String EXTRA_ALARM_RECORD_INDEX = "alarm_record_index";

    protected int getContentViewResId() {
        return R.layout.checker_add_alarm_activity;
    }

    protected CheckerAddAlarmFragment createChildFragment() {
        return CheckerAddAlarmFragment.newInstance((CheckerRecord) getIntent().getParcelableExtra(CheckerAddActivity.EXTRA_CHECKER_RECORD),
                                                   (AlarmRecord) getIntent().getParcelableExtra(EXTRA_ALARM_RECORD));
    }

    public void finish() {
        int i = -1;
        Intent data = new Intent();
        boolean unsavedChanges = false;
        CheckerAddAlarmFragment fragment = (CheckerAddAlarmFragment) getChildFragment();
        if (fragment != null) {
            unsavedChanges = fragment.getUnsavedChanges();
            data.putExtra(EXTRA_ALARM_RECORD, fragment.getAlarmRecord());
        }
        data.putExtra(EXTRA_ALARM_RECORD_INDEX, getIntent().getIntExtra(EXTRA_ALARM_RECORD_INDEX, -1));
        if (!unsavedChanges) {
            i = 0;
        }
        setResult(i, data);
        super.finish();
    }

    public static void startCheckerAddAlarmActivity(Fragment fragment,
                                                    int requestCode,
                                                    CheckerRecord checkerRecord,
                                                    AlarmRecord alarmRecord,
                                                    int alarmRecordIndex) {
        Intent intent = new Intent(fragment.getActivity(), CheckerAddAlarmActivity.class);
        intent.putExtra(CheckerAddActivity.EXTRA_CHECKER_RECORD, checkerRecord);
        intent.putExtra(EXTRA_ALARM_RECORD, alarmRecord);
        intent.putExtra(EXTRA_ALARM_RECORD_INDEX, alarmRecordIndex);
        fragment.startActivityForResult(intent, requestCode);
    }
}
