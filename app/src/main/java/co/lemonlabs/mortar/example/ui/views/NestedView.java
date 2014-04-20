package co.lemonlabs.mortar.example.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.ui.screens.NestedScreen;
import mortar.Mortar;

public class NestedView extends FrameLayout {

    @Inject NestedScreen.Presenter presenter;

    @InjectView(R.id.nested_child) NestedChildView childView;

    public NestedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        presenter.takeView(this);
        childView.setPresenter(presenter.getChildPresenter());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }
}
