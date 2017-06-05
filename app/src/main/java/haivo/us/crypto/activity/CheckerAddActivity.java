package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleFragmentSubActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.fragment.CheckerAddFragment;
import haivo.us.crypto.receiver.MarketChecker;

public class CheckerAddActivity extends SimpleFragmentSubActivity<CheckerAddFragment> {
    public static final String EXTRA_CHECKER_RECORD = "checker_record";

    protected CheckerAddFragment createChildFragment() {
        CheckerRecord checkerRecord = (CheckerRecord) getIntent().getParcelableExtra(EXTRA_CHECKER_RECORD);
        if (checkerRecord == null) {
            long checkerRecordId = getIntent().getLongExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, 0);
            if (checkerRecordId > 0) {
                checkerRecord = CheckerRecord.get(checkerRecordId);
            }
        }
        return CheckerAddFragment.newInstance(checkerRecord,
                                              getIntent().getLongExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, 0));
    }

    public void onBackPressed() {
        Fragment childFragment = getChildFragment();
        if (!(childFragment instanceof CheckerAddFragment) || !((CheckerAddFragment) childFragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    public static void startCheckerAddActivity(Context context, CheckerRecord checkerRecord) {
        startCheckerAddActivity(context, checkerRecord, -1, false);
    }

    public static void startCheckerAddActivity(Context context,
                                               CheckerRecord checkerRecord,
                                               long alarmRecordId,
                                               boolean newTask) {
        Intent intent = new Intent(context, CheckerAddActivity.class);
        if (newTask) {
            intent.addFlags(268435456);
        }
        intent.putExtra(EXTRA_CHECKER_RECORD, checkerRecord);
        intent.putExtra("TEST",true);
        intent.putExtra("TEST2",23);
        intent.putExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, alarmRecordId);

        context.startActivity(intent);
    }
}
