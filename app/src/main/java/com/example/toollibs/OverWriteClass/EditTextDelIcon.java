package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.toollibs.R;

public class EditTextDelIcon extends android.support.v7.widget.AppCompatEditText {
    private Context context;
    private Drawable icon;

    public EditTextDelIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Init();
    }

    private void Init() {
        //set delete icon;
        icon = context.getResources().getDrawable(R.drawable.icon_del);
        //set text change listener;
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SetDrawable();
            }
        });

    }

    private void SetDrawable() {
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (icon != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 30;//size
            if (rect.contains(eventX, eventY)) {
                setText("");//clear contents
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
