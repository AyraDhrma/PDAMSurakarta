package com.ayra.pdamsurakarta.manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ayra.pdamsurakarta.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LibraryManager {

    private static LibraryManager mInstance;
    public String TAG_SUCCESS = "TAG SIPOINT SUCCESS";
    private SweetAlertDialog loading, error, success;

    public static synchronized LibraryManager getInstance() {
        if (mInstance == null) {
            mInstance = new LibraryManager();
        }
        return mInstance;
    }

    public void loadingShow(String title, String content, Context context) {
        loading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loading.getProgressHelper().setBarColor(Color.parseColor("#4787C2"));
        loading.setCancelable(false);
        loading.setTitleText(title);
        loading.setContentText(content);
        loading.show();
    }

    public void loadingDismiss() {
        loading.dismiss();
    }

    public void errorShow(String content, Context context) {
        error = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        error.setCancelable(false);
        error.setContentText(content);
        error.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                error.dismiss();
            }
        });
        error.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(error.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        error.getWindow().setAttributes(lp);
    }

    public void successShow(String title, String content, Context context) {
        success = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        success.setCancelable(false);
        success.setTitleText(title);
        success.setContentText(content);
        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                success.dismiss();
            }
        });
        success.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(success.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        success.getWindow().setAttributes(lp);
    }

    public void alertDialog(Context context, String title, String message, String txtPositive, DialogInterface.OnClickListener clickPositive) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(txtPositive, clickPositive)
                .create()
                .show();
    }

    public void onError(VolleyError error, final Context context) {
        String TAG_ERROR = "TAG SIPOINT ERROR";
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Log.d(TAG_ERROR, "Network Error1 = " + error.getMessage());
            alertDialog(context, context.getResources().getString(R.string.no_internet_connection), context.getResources().getString(R.string.refresh_info), "Coba Lagi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                    context.startActivity(((Activity) context).getIntent());
                }
            });
        } else if (error instanceof AuthFailureError) {
            Log.d(TAG_ERROR, "Network Error2 = " + error.getMessage());
            errorShow(error.getMessage() == null ? "Cek koneksi internet anda" : error.getMessage(), context);
        } else if (error instanceof ServerError) {
            Log.d(TAG_ERROR, "Server Error = " + error.getMessage());
            errorShow(error.getMessage() == null ? "Cek koneksi internet anda" : error.getMessage(), context);
        } else if (error instanceof NetworkError) {
            Log.d(TAG_ERROR, "Network Error3 = " + error.getMessage());
            errorShow(error.getMessage() == null ? "Cek koneksi internet anda" : error.getMessage(), context);
        } else if (error instanceof ParseError) {
            Log.d(TAG_ERROR, "Data Error = " + error.getMessage());
            errorShow(error.getMessage() == null ? "Cek koneksi internet anda" : error.getMessage(), context);
        } else {
            Log.d(TAG_ERROR, error.getMessage());
            errorShow(error.getMessage() == null ? "Cek koneksi internet anda" : error.getMessage(), context);
        }
    }

}
