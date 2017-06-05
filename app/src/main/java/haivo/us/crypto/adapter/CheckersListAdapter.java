package haivo.us.crypto.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mobeta.android.dslv.DragSortCursorAdapter;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.R;
import java.util.List;
import haivo.us.crypto.model.CurrencySubunit;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;
import haivo.us.crypto.util.AlarmRecordHelper;
import haivo.us.crypto.util.CheckerRecordHelper;
import haivo.us.crypto.util.CurrencyUtils;
import haivo.us.crypto.util.FormatUtils;
import haivo.us.crypto.util.FormatUtilsBase;
import haivo.us.crypto.util.MarketsConfigUtils;
import haivo.us.crypto.util.NotificationUtils;
import haivo.us.crypto.util.SpannableUtils;
import haivo.us.crypto.util.TickerUtils;

public class CheckersListAdapter extends DragSortCursorAdapter {
    private boolean actionModeActive;
    private final Context context;

    /* renamed from: haivo.us.crypto.adapter.CheckersListAdapter.1 */
    class C01611 implements OnCheckedChangeListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ CheckerRecord val$item;

        C01611(CheckerRecord checkerRecord, Context context) {
            this.val$item = checkerRecord;
            this.val$context = context;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MaindbContract.Checker.newBuilder().setEnabled(isChecked).update(this.val$item.getId(), true);
            this.val$item.setEnabled(isChecked);
            CheckerRecordHelper.doAfterEdit(this.val$context, this.val$item, true);
        }
    }

    static class ViewHolder {
        @BindView(R.id.alarmView) TextView alarmView;
        @BindView(R.id.currencyView) TextView currencyView;
        @BindView(R.id.dragHandle) View dragHandle;
        @BindView(R.id.lastCheckTimeView) TextView lastCheckTimeView;
        @BindView(R.id.lastCheckValueView) TextView lastCheckValueView;
        @BindView(R.id.lastCheckView) TextView lastCheckView;
        @BindView(R.id.marketView) TextView marketView;
        @BindView(R.id.separator) View separator;
        @BindView(R.id.switchView) CompoundButton switchView;

        public ViewHolder(View view) {
            ButterKnife.bind((Object) this, view);
        }
    }

    public CheckersListAdapter(Context context) {
        super(context, null, false);
        this.context = context;
    }

    public void setActionModeActive(boolean actionModeActive) {
        this.actionModeActive = actionModeActive;
        notifyDataSetChanged();
    }

    public View newView(Context arg0, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.checkers_list_fragment_list_item, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    public void bindView(View convertView, Context context, Cursor cursor) {
        int lastCheckValueViewVisibility;
        ViewHolder holder = (ViewHolder) convertView.getTag();
        CheckerRecord item = CheckerRecord.fromCursor(cursor);
        CurrencySubunit subunitDst =
            CurrencyUtils.getCurrencySubunit(item.getCurrencyDst(), item.getCurrencySubunitDst());
        Market market = MarketsConfigUtils.getMarketByKey(item.getMarketKey());
        holder.marketView.setText(market.name);
        TextView textView = holder.currencyView;
        Object[] objArr = new Object[2];
        objArr[0] =
            FormatUtils.getCurrencySrcWithContractType(item.getCurrencySrc(), market, (int) item.getContractType());
        objArr[1] = item.getCurrencyDst();
        textView.setText(context.getString(R.string.generic_currency_pair, objArr));
        Ticker lastCheckTicker = TickerUtils.fromJson(item.getLastCheckTicker());
        if (item.getErrorMsg() != null) {
            holder.lastCheckView.setText(context.getString(R.string.check_error_generic_prefix,
                                                           new Object[] { BuildConfig.VERSION_NAME }));
            holder.lastCheckValueView.setText(Html.fromHtml("<small>" + item.getErrorMsg() + "</small>"));
            holder.lastCheckValueView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            lastCheckValueViewVisibility = 0;
            holder.lastCheckTimeView.setText(FormatUtilsBase.formatSameDayTimeOrDate(context, item.getLastCheckDate()));
        } else if (lastCheckTicker != null) {
            holder.lastCheckView.setText(R.string.checkers_list_item_last_check);
            holder.lastCheckValueView.setText(" " + FormatUtilsBase.formatPriceWithCurrency(lastCheckTicker.last,
                                                                                            subunitDst));
            holder.lastCheckValueView.setCompoundDrawablesWithIntrinsicBounds(0,
                                                                              0,
                                                                              NotificationUtils.getIconResIdForTickers(
                                                                                  TickerUtils.fromJson(item.getPreviousCheckTicker()),
                                                                                  lastCheckTicker,
                                                                                  true),
                                                                              0);
            lastCheckValueViewVisibility = 0;
            holder.lastCheckTimeView.setText(FormatUtilsBase.formatSameDayTimeOrDate(context,
                                                                                     lastCheckTicker.timestamp));
        } else {
            holder.lastCheckView.setText(R.string.checkers_list_item_last_check);
            holder.lastCheckValueView.setText(null);
            lastCheckValueViewVisibility = 8;
        }
        holder.lastCheckView.setVisibility(lastCheckValueViewVisibility);
        holder.lastCheckValueView.setVisibility(holder.lastCheckView.getVisibility());
        holder.lastCheckTimeView.setVisibility(holder.lastCheckView.getVisibility());
        holder.separator.setVisibility(holder.lastCheckView.getVisibility());
        List<AlarmRecord> alarmRecords = CheckerRecordHelper.getAlarmsForCheckerRecord(item, true);
        if (alarmRecords == null || alarmRecords.size() == 0) {
            holder.alarmView.setText(context.getString(R.string.checkers_list_item_alarm, new Object[] {
                " " + context.getString(R.string.checkers_list_item_alarm_none)
            }));
        } else {
            String alarmString = BuildConfig.VERSION_NAME;
            for (AlarmRecord alarmRecord : alarmRecords) {
                if (!TextUtils.isEmpty(alarmString)) {
                    alarmString = alarmString + ",  ";
                }
                alarmString =
                    alarmString + context.getString(R.string.checkers_list_item_alarm_value_format, new Object[] {
                        AlarmRecordHelper.getPrefixForAlarmType(item, alarmRecord),
                        AlarmRecordHelper.getValueForAlarmType(subunitDst, alarmRecord),
                        AlarmRecordHelper.getSufixForAlarmType(item, alarmRecord)
                    });
            }
            holder.alarmView.setText(SpannableUtils.formatWithSpans(context.getString(R.string.checkers_list_item_alarm),
                                                                    " " + alarmString,
                                                                    new RelativeSizeSpan(1.25f)));
        }
        holder.dragHandle.setVisibility(this.actionModeActive ? View.VISIBLE : View.GONE);
        holder.switchView.setVisibility(this.actionModeActive ? View.INVISIBLE : View.VISIBLE);
        holder.switchView.setOnCheckedChangeListener(null);
        holder.switchView.setChecked(item.getEnabled());
        holder.switchView.setOnCheckedChangeListener(new C01611(item, context));
    }
}
