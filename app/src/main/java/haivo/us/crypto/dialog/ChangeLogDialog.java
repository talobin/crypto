package haivo.us.crypto.dialog;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import haivo.us.crypto.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;

public class ChangeLogDialog {
    private static final String CHANGELOG_XML = "changelog";
    private static final String TAG = "ChangeLogDialog";
    private Activity fActivity;

    /* renamed from: haivo.us.crypto.dialog.ChangeLogDialog.1 */
    class C01701 implements OnClickListener {
        C01701() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    public ChangeLogDialog(Activity context) {
        this.fActivity = context;
    }

    private String GetAppVersion() {
        try {
            return this.fActivity.getPackageManager().getPackageInfo(this.fActivity.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return BuildConfig.VERSION_NAME;
        }
    }

    private String ParseReleaseTag(XmlResourceParser aXml) throws XmlPullParserException, IOException {
        String _Result = "<h1>Release: " + aXml.getAttributeValue(null, "version") + "</h1><ul>";
        int eventType = aXml.getEventType();
        while (true) {
            if (eventType == 3 && !aXml.getName().equals("change")) {
                return _Result + "</ul>";
            }
            if (eventType == 2 && aXml.getName().equals("change")) {
                eventType = aXml.next();
                _Result = _Result + "<li>" + aXml.getText() + "</li>";
            }
            eventType = aXml.next();
        }
    }

    private String GetStyle() {
        return "<style type=\"text/css\">h1 { margin-left: 0px; font-size: 12pt; }li { margin-left: 0px; font-size: 9pt;}ul { padding-left: 30px;}</style>";
    }

    private String GetHTMLChangelog(int aResourceId, Resources aResource) {
        String _Result = "<html><head>" + GetStyle() + "</head><body>";
        XmlResourceParser _xml = aResource.getXml(aResourceId);
        try {
            for (int eventType = _xml.getEventType(); eventType != 1; eventType = _xml.next()) {
                if (eventType == 2 && _xml.getName().equals(BuildConfig.BUILD_TYPE)) {
                    _Result = _Result + ParseReleaseTag(_xml);
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e2) {
            Log.e(TAG, e2.getMessage(), e2);
        } finally {
            _xml.close();
        }
        return _Result + "</body></html>";
    }

    public void show() {
        String _PackageName = this.fActivity.getPackageName();
        try {
            Resources _Resource = this.fActivity.getPackageManager().getResourcesForApplication(_PackageName);
            String _Title =
                _Resource.getString(R.string.settings_about_changelog_dialog_title) + " v" + GetAppVersion();
            String _HTML = GetHTMLChangelog(_Resource.getIdentifier(CHANGELOG_XML, "xml", _PackageName), _Resource);
            String _Close = _Resource.getString(R.string.generic_close);
            if (_HTML.equals(BuildConfig.VERSION_NAME)) {
                Toast.makeText(this.fActivity, "Could not load change log", Toast.LENGTH_SHORT).show();
                return;
            }
            WebView _WebView = new WebView(this.fActivity);
            _WebView.loadDataWithBaseURL(null, _HTML, "text/html", "UTF-8", null);
            new Builder(this.fActivity).setTitle(_Title)
                                       .setView(_WebView)
                                       .setPositiveButton(_Close, new C01701())
                                       .create()
                                       .show();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
