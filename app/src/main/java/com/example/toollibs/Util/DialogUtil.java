package com.example.toollibs.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.example.toollibs.R;

public class DialogUtil {
    public static void showListDialog(Context context, String[] items, String title, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setItems(items, listener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show().setCanceledOnTouchOutside(true);
    }

    public static void showIntroDialog(Context context,String title,String message,String btnText){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton(btnText,mListener)
                .show();
    }

    private static DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
            }

        }
    };

    public static Dialog showViewDialog(Context context, String title, View view, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(title)
                .setView(view)
                .setNegativeButton(R.string.cancel,listener)
                .setPositiveButton(R.string.confirm,listener)
                .show();
    }

    public static Dialog showViewDialog2(Context context, String title, View view, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(title)
                .setView(view)
                .setNegativeButton(R.string.cancel,listener)
                .show();
    }

    public static Dialog showViewDialog(Context context, String title, int resId, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(title)
                .setView(resId)
                .setNegativeButton(R.string.cancel,listener)
                .setPositiveButton(R.string.confirm,listener)
                .show();
    }

    public static Dialog showMsgDialog(Context context, String title, String message, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel,listener)
                .setPositiveButton(R.string.confirm,listener)
                .show();
    }
}
