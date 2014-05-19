package co.lemonlabs.mortar.example.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.ui.misc.FractionalFrameLayout;
import co.lemonlabs.mortar.example.ui.screens.StubYScreen;
import co.lemonlabs.mortar.example.ui.views.data.ExamplePopupData;
import mortar.Mortar;
import mortar.Popup;

public class StubYView extends FractionalFrameLayout {

    @Inject StubYScreen.Presenter presenter;

    @InjectView(R.id.stub_text) Button textView;

    private Popup<ExamplePopupData, Boolean> examplePopup;

    public StubYView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
        examplePopup = new ExamplePopup(context);
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

    @OnClick(R.id.stub_text)
    public void clickStub() {
        presenter.goToAnotherStub();
    }

    public void setStubText(String text) {
        textView.setText(text);
    }


    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public Popup<ExamplePopupData, Boolean> getExamplePopup() {
        return examplePopup;
    }
}

