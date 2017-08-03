package com.fizz.JuAoProject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.fizz.JuAoProject.R;

/**
 * Created by fizz on 2017/6/5.
 */
public class CustomerRadiobutton extends RadioButton {

    private int mDrawableSize;// xml文件中设置的大小

    public CustomerRadiobutton(Context context) {
        this(context, null, 0);
    }

    public CustomerRadiobutton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerRadiobutton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomerRadiobutton);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            Log.i("CustomerRadiobutton", "attr:" + attr);
            switch (attr) {
                case R.styleable.CustomerRadiobutton_imgSize:
                    mDrawableSize = a.getDimensionPixelSize(R.styleable.CustomerRadiobutton_imgSize, 50);
                    Log.i("CustomerRadiobutton", "mDrawableSize:" + mDrawableSize);
                    break;
                case R.styleable.CustomerRadiobutton_drawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.CustomerRadiobutton_drawableBottom:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.CustomerRadiobutton_drawableRight:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.CustomerRadiobutton_drawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default:
                    break;
            }
        }
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);

    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
                                                        Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

}
