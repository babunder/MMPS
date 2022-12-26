package com.mmps.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.mmps.R;

/**
 * @author Aditya Kulkarni
 */

public class ProgressDialogUtil {

    public static ProgressDialog config(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
