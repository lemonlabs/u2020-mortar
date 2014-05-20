package co.lemonlabs.mortar.example.ui.screens;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.core.StateBlueprint;
import co.lemonlabs.mortar.example.core.TransitionScreen;
import co.lemonlabs.mortar.example.core.android.ActionBarPresenter;
import co.lemonlabs.mortar.example.core.android.DrawerPresenter;
import co.lemonlabs.mortar.example.core.anim.Transition;
import co.lemonlabs.mortar.example.ui.views.StubXView;
import co.lemonlabs.mortar.example.ui.views.data.ExamplePopupData;
import dagger.Provides;
import flow.Flow;
import flow.Layout;
import mortar.PopupPresenter;
import mortar.ViewPresenter;
import rx.functions.Action0;

@Layout(R.layout.stubx)
@Transition({R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right})
public class StubXScreen extends TransitionScreen implements StateBlueprint  {

    private final boolean hasDrawer;
    private final int     position;
    public StubXScreen(boolean hasDrawer, int position) {
        this.hasDrawer = hasDrawer;
        this.position = position;
    }

    @Override
    public String getMortarScopeName() {
        return getClass().getName() + position;
    }

    @Override
    public Object getDaggerModule() {
        return new Module(hasDrawer);
    }

    @Override public void setViewState(SparseArray<Parcelable> viewState) {
        // no view state to be stored
    }

    @dagger.Module(
        injects = {
            StubXView.class
        },
        addsTo = CorePresenter.Module.class,
        library = true
    )

    public static class Module {

        private final boolean hasDrawer;

        public Module(boolean hasDrawer) {
            this.hasDrawer = hasDrawer;
        }

        @Provides @Named("stub") boolean providesHasDrawer() {
            return hasDrawer;
        }
    }

    @Singleton
    public static class Presenter extends ViewPresenter<StubXView> {

        private final Flow                                      flow;
        private final ActionBarPresenter                        actionBar;
        private final DrawerPresenter                           drawer;
        private final boolean                                   hasDrawer;
        private final PopupPresenter<ExamplePopupData, Boolean> examplePopupPresenter;

        private AtomicBoolean transitioning = new AtomicBoolean();

        @Inject
        Presenter(Flow flow, ActionBarPresenter actionBar, DrawerPresenter drawer, @Named("stub") boolean hasDrawer) {
            this.flow = flow;
            this.actionBar = actionBar;
            this.drawer = drawer;
            this.hasDrawer = hasDrawer;
            this.examplePopupPresenter = new PopupPresenter<ExamplePopupData, Boolean>() {
                @Override protected void onPopupResult(Boolean confirmed) {
                    Presenter.this.getView().showToast(confirmed ? "Just did that!" : "If you say so...");
                }
            };
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;

            actionBar.setConfig(new ActionBarPresenter.Config(
                true,
                true,
                hasDrawer ? "Stub with drawer" : "Stub",
                new ActionBarPresenter.MenuAction("Alert", new Action0() {
                    @Override public void call() {
                        examplePopupPresenter.show(new ExamplePopupData("This is an example of a Popup Presenter"));
                    }
                })
            ));

            if (!hasDrawer) {
                drawer.setConfig(new DrawerPresenter.Config(false, DrawerLayout.LOCK_MODE_LOCKED_CLOSED));
            } else {
                drawer.setConfig(new DrawerPresenter.Config(true, DrawerLayout.LOCK_MODE_UNLOCKED));
            }

            getView().setStubText(R.string.stub_go_up);

            examplePopupPresenter.takeView(getView().getExamplePopup());
        }

        @Override
        public void dropView(StubXView view) {
            examplePopupPresenter.dropView(getView().getExamplePopup());
            super.dropView(view);
        }

        public void goToAnotherStub() {
            if (!transitioning.getAndSet(true)) {
                flow.goTo(new StubYScreen(false, new Random().nextInt(420)));
            }
        }
    }
}
