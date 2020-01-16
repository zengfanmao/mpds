package com.aimissu.basemvp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.aimissu.basemvp.R;
/**

 */
public class WaitingProgressDialog extends Dialog {

    private WaitingProgressDialog(Context context) {
        super(context);
    }

    private WaitingProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static WaitingProgressDialog create(Context context, int layoutId, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        WaitingProgressDialog dialog = new WaitingProgressDialog(context, R.style.waitingDialogStyle);
        dialog.setTitle("");
        dialog.setContentView(layoutId);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    public void setContentText(String msg) {
        if (msg == null || msg.length() == 0) {
            findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = findViewById(R.id.message);
            txt.setText(msg);
        }
    }

    public static WaitingProgressDialog create(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        return create(context, R.layout.view_custom_progress, message, cancelable, cancelListener);
    }

    public static WaitingProgressDialog show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        WaitingProgressDialog dialog = create(context, message, cancelable, cancelListener);
        dialog.show();
        return dialog;
    }
}
