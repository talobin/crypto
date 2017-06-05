package haivo.us.crypto.view.generic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import haivo.us.crypto.R;

public class ViewPreference extends FrameLayout implements OnClickListener {
    private CharSequence summary;
    @Nullable @BindView(R.id.summaryView) TextView summaryView;
    private CharSequence title;
    @Nullable @BindView(R.id.titleAndSummaryContainer) ViewGroup titleAndSummaryContainer;
    @Nullable @BindView(R.id.titleView) TextView titleView;
    @Nullable @BindView(R.id.widgetFrame) ViewGroup widgetFrame;

    public ViewPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ViewPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(11)
    public ViewPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            LayoutInflater.from(getContext()).inflate(R.layout.view_preference, this);
        }
        ButterKnife.bind((View) this);
        setTitle(this.title);
        setSummary(this.summary);
    }

    protected void init(Context context, AttributeSet attrs) {
        TypedValue typedValue = new TypedValue();
        ((Activity) context).getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        setBackgroundResource(typedValue.resourceId);
        setOnClickListener(this);
        setFocusable(true);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPreference);
            this.title = a.getString(R.styleable.ViewPreference_title);
            this.summary = a.getString(R.styleable.ViewPreference_summary);
            a.recycle();
        }
    }

    public ViewGroup getTitleAndSummaryContainer() {
        return this.titleAndSummaryContainer;
    }

    public ViewGroup getWidgetFrame() {
        return this.widgetFrame;
    }

    protected void setWidget(View view) {
        if (this.widgetFrame != null) {
            this.widgetFrame.addView(view);
            this.widgetFrame.setVisibility(VISIBLE);
        }
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        if (this.titleView != null) {
            this.titleView.setText(title);
        }
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public void setSummary(CharSequence summary) {
        this.summary = summary;
        if (this.summaryView != null) {
            this.summaryView.setText(summary);
            this.summaryView.setVisibility(TextUtils.isEmpty(summary) ? GONE : VISIBLE);
        }
    }

    public CharSequence getSummary() {
        return this.summary;
    }

    public void onClick(View v) {
    }
}
