package haivo.us.crypto.util;

import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.util.Pair;

public class SpannableUtils {

    public static class StringAndSpan extends Pair<String, CharacterStyle> {
        public StringAndSpan(String first, CharacterStyle second) {
            super(first, second);
        }
    }

    public static CharSequence formatWithSpans(String text, String span1text, CharacterStyle span1) {
        return formatWithSpans(text, new StringAndSpan(span1text, span1));
    }

    public static CharSequence formatWithSpans(String text, String span1text, CharacterStyle span1, String span2text, CharacterStyle span2) {
        return formatWithSpans(text, new StringAndSpan(span1text, span1), new StringAndSpan(span2text, span2));
    }

    public static CharSequence formatWithSpans(String text, String span1text, CharacterStyle span1, String span2text, CharacterStyle span2, String span3text, CharacterStyle span3) {
        return formatWithSpans(text, new StringAndSpan(span1text, span1), new StringAndSpan(span2text, span2), new StringAndSpan(span3text, span3));
    }

    public static CharSequence formatWithSpans(String text, String span1text, CharacterStyle span1, String span2text, CharacterStyle span2, String span3text, CharacterStyle span3, String span4text, CharacterStyle span4) {
        return formatWithSpans(text, new StringAndSpan(span1text, span1), new StringAndSpan(span2text, span2), new StringAndSpan(span3text, span3), new StringAndSpan(span4text, span4));
    }

    public static CharSequence formatWithSpans(String text, StringAndSpan... args) {
        int i;
        int i2;
        int[] argsStartPoints = new int[args.length];
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        for (i = 0; i < args.length; i++) {
            argsStartPoints[i] = text.indexOf("%" + (i + 1) + "$s");
            if (argsStartPoints[i] >= 0) {
                if (i < 9) {
                    i2 = 1;
                } else {
                    i2 = 2;
                }
                ssb.setSpan(args[i].second, argsStartPoints[i], argsStartPoints[i] + (i2 + 3), 33);
            }
        }
        for (i = args.length - 1; i >= 0; i--) {
            if (argsStartPoints[i] >= 0) {
                if (i < 9) {
                    i2 = 1;
                } else {
                    i2 = 2;
                }
                ssb.replace(argsStartPoints[i], argsStartPoints[i] + (i2 + 3), (CharSequence) args[i].first);
            }
        }
        return ssb;
    }
}
