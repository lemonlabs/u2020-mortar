package co.lemonlabs.mortar.example.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import co.lemonlabs.mortar.example.ui.screens.StubScreen;
import mortar.Mortar;

public class StubView extends FrameLayout {

    @Inject StubScreen.Presenter presenter;

    public StubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }
}

