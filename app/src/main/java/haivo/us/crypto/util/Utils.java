package haivo.us.crypto.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.ListPreference;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import haivo.us.crypto.R;

public class Utils {
    public static void showToast(Context context, int textResId) {
        showToast(context, textResId, false);
    }

    public static void showToast(Context context, int textResId, boolean isLong) {
        showToast(context, context.getString(textResId), isLong);
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, false);
    }

    public static void showToast(Context context, String text, boolean isLong) {
        Toast.makeText(context.getApplicationContext(), text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static void setViewError(TextView textView, int errorMsgResId) {
        setViewError(textView, textView.getContext().getString(errorMsgResId));
    }

    public static void setViewError(TextView textView, CharSequence errorMsg) {
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        textView.setError(errorMsg);
    }

    public static int getPixels(Context context, int dp) {
        return (int) (getPixelsF(context, (float) dp) + 0.5f);
    }

    public static float getPixelsF(Context context, float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }

    public static int getSpPixels(Context context, int sp) {
        return (int) (TypedValue.applyDimension(2, (float) sp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static void setSelectionAfterLastLetter(EditText editTextView) {
        editTextView.setSelection(editTextView.getText().length());
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        NetworkInfo activeNetwork =
            ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public static void sendEmail(Context context, String emailAddress, String subject) {
        try {
            Intent emailIntent =
                new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", emailAddress, null));
            emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.generic_send_email)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean goToGooglePlay(Context context, String packageName) {
        return goToWebPage(context, "market://details?id=" + packageName);
    }

    public static boolean goToWebPage(Context context, String address) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(address)));
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public static void hideKeyboard(Context context, View editText) {
        try {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                editText.getWindowToken(),
                0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context, View editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 1);
        imm.restartInput(editText);
    }

    public static boolean handleIntListOnPreferenceChange(ListPreference listPreference, Object newValue) {
        try {
            CharSequence[] entries = listPreference.getEntries();
            int index = listPreference.findIndexOfValue((String) newValue);
            if (index < 0 || index >= entries.length) {
                index = 0;
            }
            listPreference.setSummary(listPreference.getEntries()[index]);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
