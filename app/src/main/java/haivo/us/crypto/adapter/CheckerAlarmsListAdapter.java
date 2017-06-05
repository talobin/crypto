package haivo.us.crypto.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nineoldandroids.view.ViewHelper;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.fragment.CheckerAddFragment;
import haivo.us.crypto.R;
import java.util.List;
import haivo.us.crypto.util.AlarmRecordHelper;

public abstract class CheckerAlarmsListAdapter extends BaseAdapter {
    private static final float ALPHA_OFF = 0.1f;
    private static final float ALPHA_ON = 1.0f;
    private final CheckerAddFragment checkerAddFragment;
    private final CheckerRecord checkerRecord;
    private final Context context;
    private final List<AlarmRecord> items;
    private final int verticalPadding;

    /* renamed from: haivo.us.crypto.adapter.CheckerAlarmsListAdapter.1 */
    class C01581 implements OnCheckedChangeListener {
        final /* synthetic */ AlarmRecord val$alarmRecord;

        C01581(AlarmRecord alarmRecord) {
            this.val$alarmRecord = alarmRecord;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            this.val$alarmRecord.setEnabled(isChecked);
            CheckerAlarmsListAdapter.this.checkerAddFragment.makeUnsavedChanges();
        }
    }

    /* renamed from: haivo.us.crypto.adapter.CheckerAlarmsListAdapter.2 */
    class C01592 implements OnClickListener {
        final /* synthetic */ ViewHolder val$holder;

        C01592(ViewHolder viewHolder) {
            this.val$holder = viewHolder;
        }

        public void onClick(View v) {
            this.val$holder.checkBox.toggle();
        }
    }

    /* renamed from: haivo.us.crypto.adapter.CheckerAlarmsListAdapter.3 */
    class C01603 implements OnLongClickListener {
        final /* synthetic */ AlarmRecord val$alarmRecord;
        final /* synthetic */ int val$position;

        C01603(AlarmRecord alarmRecord, int i) {
            this.val$alarmRecord = alarmRecord;
            this.val$position = i;
        }

        public boolean onLongClick(View v) {
            CheckerAlarmsListAdapter.this.onItemLongClick(this.val$alarmRecord, this.val$position);
            return true;
        }
    }

    static class ViewHolder {
        @BindView(R.id.alarmLedView) View alarmLedView;
        @BindView(R.id.alarmSoundView) View alarmSoundView;
        @BindView(R.id.alarmTtsView) View alarmTtsView;
        @BindView(R.id.alarmVibrateView) View alarmVibrateView;
        @BindView(R.id.alarmView) TextView alarmView;
        @BindView(R.id.checkBox) CompoundButton checkBox;
        @BindView(R.id.checkBoxWrapper) FrameLayout checkBoxWrapper;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public abstract void onItemLongClick(AlarmRecord alarmRecord, int i);

    public CheckerAlarmsListAdapter(Context context,
                                    CheckerAddFragment checkerAddFragment,
                                    CheckerRecord checkerRecord,
                                    List<AlarmRecord> items) {
        this.context = context;
        this.checkerAddFragment = checkerAddFragment;
        this.checkerRecord = checkerRecord;
        this.items = items;
        this.verticalPadding = context.getResources().getDimensionPixelSize(R.dimen.list_item_vertical_padding_card);
    }

    public int getCount() {
        if (this.items == null || this.items.size() <= 1) {
            return 1;
        }
        return this.items.size() + 1;
    }

    public AlarmRecord getItem(int position) {
        return (position < 0 || position >= this.items.size()) ? null : (AlarmRecord) this.items.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        return position == getCount() + -1 ? 1 : 0;
    }

    @TargetApi(11)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            int i;
            convertView =
                LayoutInflater.from(this.context).inflate(R.layout.checker_add_alarm_add_list_item, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            if (getCount() == 1) {
                i = R.string.checker_add_alarm_list_add_alarm_more;
            } else {
                i = R.string.checker_add_alarm_list_add_alarm;
            }
            textView.setText(i);
            return convertView;
        }
        if (convertView == null) {
            convertView =
                LayoutInflater.from(this.context).inflate(R.layout.checker_add_alarm_list_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        convertView.setPadding(convertView.getPaddingLeft(),
                               (position == 0 ? 2 : 1) * this.verticalPadding,
                               convertView.getPaddingRight(),
                               convertView.getPaddingBottom());
        AlarmRecord alarmRecord = getItem(position);
        if (alarmRecord != null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.alarmView.setText(this.context.getString(R.string.checker_add_alarm_type_value_format, new Object[] {
                AlarmRecordHelper.getPrefixForAlarmType(this.checkerRecord, alarmRecord),
                AlarmRecordHelper.getValueForAlarmType(this.checkerRecord, alarmRecord),
                AlarmRecordHelper.getSufixForAlarmType(this.checkerRecord, alarmRecord)
            }));
            ViewHelper.setAlpha(holder.alarmSoundView, alarmRecord.getSound() ? ALPHA_ON : ALPHA_OFF);
            ViewHelper.setAlpha(holder.alarmVibrateView, alarmRecord.getVibrate() ? ALPHA_ON : ALPHA_OFF);
            ViewHelper.setAlpha(holder.alarmLedView, alarmRecord.getLed() ? ALPHA_ON : ALPHA_OFF);
            ViewHelper.setAlpha(holder.alarmTtsView, alarmRecord.getTtsEnabled() ? ALPHA_ON : ALPHA_OFF);
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(alarmRecord.getEnabled());
            holder.checkBox.setOnCheckedChangeListener(new C01581(alarmRecord));
            holder.checkBoxWrapper.setOnClickListener(new C01592(holder));
        }
        convertView.setOnLongClickListener(new C01603(alarmRecord, position));
        return convertView;
    }
}
