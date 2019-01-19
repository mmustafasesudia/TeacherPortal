package com.android.tutorapp.Utills;

import android.content.Context;

import com.android.tutorapp.R;

import dmax.dialog.SpotsDialog;

/**
 * Created by Muhammad Mustafa on 09/12/2017.
 */

public class ProgressDialogClass {

    public static  SpotsDialog  progressDialog ;

    public static void showProgress(Context context)
    {
        progressDialog = new SpotsDialog(context, R.style.CustomProgressDialog);
        progressDialog.show();
    }

    public static void hideProgress()
    {
        progressDialog.dismiss();
    }
}
