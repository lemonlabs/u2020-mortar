package co.lemonlabs.mortar.example.ui.screens;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.BetterViewPresenter;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.core.TransitionScreen;
import co.lemonlabs.mortar.example.core.android.ActionBarPresenter;
import co.lemonlabs.mortar.example.core.android.DrawerPresenter;
import co.lemonlabs.mortar.example.core.anim.Transition;
import co.lemonlabs.mortar.example.ui.views.ViewStateView;
import dagger.Provides;
import flow.Flow;
import flow.Layout;
import rx.functions.Action0;

import static java.lang.Integer.MAX_VALUE;

@Layout(R.layout.view_state)
@Transition({R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right})
public class ViewStateScreen extends TransitionScreen {

    private final int position;

    public ViewStateScreen(int position) {
        this.position = position;
    }

    @Override
    public String getMortarScopeName() {
        // always a difference screen
        return getClass().getName() + position;
    }

    @Override
    public Object getDaggerModule() {
        return new Module(getViewState());
    }

    @Override
    protected void doWriteToParcel(Parcel parcel, int flags) {
        parcel.writeInt(position);
    }

    public static final Creator<ViewStateScreen> CREATOR = new ScreenCreator<ViewStateScreen>() {

        @Override protected ViewStateScreen doCreateFromParcel(Parcel source) {
            int position = source.readInt();
            return new ViewStateScreen(position);
        }

        @Override public ViewStateScreen[] newArray(int size) {
            return new ViewStateScreen[size];
        }
    };

    @dagger.Module(
        injects = {
            ViewStateView.class
        },
        addsTo = CorePresenter.Module.class,
        library = true
    )

    public static class Module {

        private final SparseArray<Parcelable> viewState;

        public Module(SparseArray<Parcelable> viewState) {

            this.viewState = viewState;
        }

        @Provides SparseArray<Parcelable> providesViewState() {
            return viewState;
        }
    }

    @Singleton
    public static class Presenter extends BetterViewPresenter<ViewStateView> {

        private final Flow               flow;
        private final ActionBarPresenter actionBar;
        private final DrawerPresenter    drawer;

        @Inject
        Presenter(SparseArray<Parcelable> viewState, Flow flow, ActionBarPresenter actionBar, DrawerPresenter drawer) {
            super(viewState);
            this.flow = flow;
            this.actionBar = actionBar;
            this.drawer = drawer;
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            actionBar.setConfig(new ActionBarPresenter.Config(
                true,
                true,
                "View State Parcer  Example",
                new ActionBarPresenter.MenuAction("Go away", new Action0() {
                    @Override public void call() {
                        flow.goTo(new ViewStateScreen(new Random().nextInt(MAX_VALUE)));
                    }
                })
            ));

            drawer.setConfig(new DrawerPresenter.Config(true, DrawerLayout.LOCK_MODE_UNLOCKED));
            restoreViewState();
        }
    }
}
