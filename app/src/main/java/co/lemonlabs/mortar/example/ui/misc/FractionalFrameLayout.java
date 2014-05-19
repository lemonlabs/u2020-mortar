package co.lemonlabs.mortar.example.ui.misc;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

@TargetApi(11)
public class FractionalFrameLayout extends FrameLayout {

    private float yFraction;
    private float xFraction;

    private int screenHeight;
    private int screenWidth;

    public FractionalFrameLayout(Context context) {
        super(context);
    }

    public FractionalFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenHeight = h;
        screenWidth = w;
    }

    public void setYFraction(float fraction) {
        yFraction = fraction;
        setY(screenHeight > 0 ? (yFraction * screenHeight) : 0);
    }

    public float getYFraction() {
        return yFraction;
    }

    public void setXFraction(float fraction) {
        xFraction = fraction;
        setX(screenWidth > 0 ? (xFraction * screenWidth) : 0);
    }

    public float getXFraction() {
        return xFraction;
    }
}
