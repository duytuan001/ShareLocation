package com.example.tuanpd.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by tuanpd on 10/2/15.
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
        setCursorDrawable();
    }


    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCursorDrawable();

    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCursorDrawable();
    }

//    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void setCursorDrawable() {
        //set cursor drawable
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this, R.drawable.aa);
        } catch (Exception ignored) {
        }
    }
}
