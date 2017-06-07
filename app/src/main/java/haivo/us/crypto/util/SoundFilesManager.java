package haivo.us.crypto.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SoundFilesManager {
    public static final String BITCOIN_CHECKER_DOWN_ALERT_FILENAME = "bitcoin_checker_down_alert";
    public static final String BITCOIN_CHECKER_UP_CHEERS_FILENAME = "bitcoin_checker_up_cheers";

    public static void installRingtonesIfNeeded(Context context) {
        installRingtoneFileIfNeeded(context, BITCOIN_CHECKER_UP_CHEERS_FILENAME, "Crypto Kit Up Cheers");
        installRingtoneFileIfNeeded(context, BITCOIN_CHECKER_DOWN_ALERT_FILENAME, "Crypto Kit Down Alert");
    }

    private static void installRingtoneFileIfNeeded(Context context, String fileName, String ringtoneTitle) {
        try {
            File file = copyRingtoneFromResources(context, fileName);
            if (file != null) {
                Uri contentUri = Media.getContentUriForPath(file.getAbsolutePath());
                if (checkRingtoneUri(context, contentUri, file.getAbsolutePath()) == null) {

                    context.getContentResolver().insert(contentUri, createRingtoneDatabaseEntry(file, ringtoneTitle));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static File getRingtonesDir() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/audio/notifications");
    }

    private static File getRingtonePath(File ringtonesDir, String fileName) {
        return new File(ringtonesDir, fileName + ".mp3");
    }

    private static File copyRingtoneFromResources(Context context, String fileName) throws Throwable {
        Exception e;
        Throwable th;
        InputStream fis = null;
        OutputStream outputStream = null;
        try {
            File path = getRingtonesDir();
            if (!path.exists()) {
                path.mkdirs();
            }
            File f = getRingtonePath(path, fileName);
            if (f.exists()) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (fis == null) {
                    return f;
                }
                try {
                    fis.close();
                    return f;
                } catch (IOException e22) {
                    e22.printStackTrace();
                    return f;
                }
            }
            fis = context.getContentResolver()
                         .openAssetFileDescriptor(Uri.parse("android.resource://"
                                                                + context.getPackageName()
                                                                + "/raw/"
                                                                + fileName), "r")
                         .createInputStream();
            if (fis == null) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e2222) {
                        e2222.printStackTrace();
                    }
                }
                return null;
            }
            OutputStream fos = new FileOutputStream(f);
            try {
                byte[] buf = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                while (true) {
                    int len = fis.read(buf);
                    if (len <= 0) {
                        break;
                    }
                    fos.write(buf, 0, len);
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e22222) {
                        e22222.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e222222) {
                        e222222.printStackTrace();
                    }
                }
                outputStream = fos;
                return f;
            } catch (Exception e3) {
                e = e3;
                outputStream = fos;
                try {
                    e.printStackTrace();
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e2222222) {
                            e2222222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e22222222) {
                            e22222222.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e222222222) {
                            e222222222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e2222222222) {
                            e2222222222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                outputStream = fos;
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (outputStream != null) {
                outputStream.close();
            }
            if (fis != null) {
                fis.close();
            }
            return null;
        }
    }

    private static ContentValues createRingtoneDatabaseEntry(File f, String ringtoneTitle) {
        ContentValues values = new ContentValues();
        values.put("_data", f.getAbsolutePath());
        values.put("title", ringtoneTitle);
        values.put("_size", Long.valueOf(f.length()));
        values.put("mime_type", "audio/mp3");
        values.put("is_ringtone", Boolean.valueOf(false));
        values.put("is_notification", Boolean.valueOf(true));
        values.put("is_alarm", Boolean.valueOf(false));
        values.put("is_music", Boolean.valueOf(false));
        return values;
    }

    public static Uri getUriForRingtone(Context context, String fileName) {
        File file = getRingtonePath(getRingtonesDir(), fileName).getAbsoluteFile();
        return checkRingtoneUri(context, Media.getContentUriForPath(file.getAbsolutePath()), file.getAbsolutePath());
    }

    private static Uri checkRingtoneUri(Context context, Uri contentUri, String filePath) {
        Uri uri = null;
        Cursor cursor = context.getContentResolver()
                               .query(contentUri, new String[] { "_id" }, "_data='" + filePath + "'", null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                uri = Uri.withAppendedPath(contentUri, String.valueOf(cursor.getInt(0)));
            }
            cursor.close();
        }
        return uri;
    }
}
