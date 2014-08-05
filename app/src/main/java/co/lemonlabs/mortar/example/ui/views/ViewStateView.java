package co.lemonlabs.mortar.example.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import co.lemonlabs.mortar.example.ui.misc.FractionalFrameLayout;
import co.lemonlabs.mortar.example.ui.screens.ViewStateScreen;
import mortar.Mortar;

public class ViewStateView extends FractionalFrameLayout {

    @Inject ViewStateScreen.Presenter presenter;

    public ViewStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }
}
