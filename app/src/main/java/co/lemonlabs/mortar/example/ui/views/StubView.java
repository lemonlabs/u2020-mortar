package co.lemonlabs.mortar.example.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.ui.screens.StubScreen;
import co.lemonlabs.mortar.example.ui.views.data.ExamplePopupData;
import mortar.Mortar;
import mortar.Popup;

public class StubView extends FrameLayout {

    @Inject StubScreen.Presenter presenter;

    @InjectView(R.id.stub_text) TextView textView;

    private Popup<ExamplePopupData, Boolean> examplePopup;

    public StubView(Context context, AttributeSet attrs) {
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

