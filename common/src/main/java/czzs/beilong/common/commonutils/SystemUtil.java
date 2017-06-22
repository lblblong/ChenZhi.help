package czzs.beilong.common.commonutils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by LBL on 2016/11/5.
 */

public class SystemUtil {
    /**
     * 关闭键盘
     * @param context
     */
    public static void shutKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }
}
