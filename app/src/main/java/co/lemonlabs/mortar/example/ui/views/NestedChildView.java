package co.lemonlabs.mortar.example.ui.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import co.lemonlabs.mortar.example.ui.screens.NestedScreen;
import mortar.Mortar;

public class NestedChildView extends FrameLayout {

    private NestedScreen.ChildPresenter presenter;

    public NestedChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    public void setPresenter(NestedScreen.ChildPresenter presenter) {
        this.presenter = presenter;
        presenter.takeView(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void startAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        animator.setDuration(3500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }
}
