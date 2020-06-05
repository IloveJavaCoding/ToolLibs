package com.example.toollibs.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class DialogUtil {
    public static void showListDialog(Context context, String[] items, String title, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setItems(items, listener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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
                .setNegativeButton("cancel",listener)
                .setPositiveButton("confirm",listener)
                .show();
    }

    public static Dialog showMsgDialog(Context context, String title, String message, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton("cancel",listener)
                .setPositiveButton("confirm",listener)
                .show();
    }
}
