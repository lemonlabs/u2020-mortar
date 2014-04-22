package co.lemonlabs.mortar.example.ui.screens;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.core.android.ActionBarPresenter;
import co.lemonlabs.mortar.example.core.android.DrawerPresenter;
import co.lemonlabs.mortar.example.ui.views.StubView;
import co.lemonlabs.mortar.example.ui.views.data.ExamplePopupData;
import dagger.Provides;
import flow.Layout;
import mortar.Blueprint;
import mortar.PopupPresenter;
import mortar.ViewPresenter;
import rx.util.functions.Action0;

@Layout(R.layout.stub)
public class StubScreen implements Blueprint {

    private final boolean hasDrawer;
    private final int     position;

    public StubScreen(boolean hasDrawer, int position) {
        this.hasDrawer = hasDrawer;
        this.position = position;
    }

    @Override
    public String getMortarScopeName() {
        return getClass().getName() + position;
    }

    @Override
    public Object getDaggerModule() {
        return new Module(hasDrawer, position);
    }

    @dagger.Module(
        injects = {
            StubView.class
        },
        addsTo = CorePresenter.Module.class,
        library = true
    )

    public static class Module {

        private final boolean hasDrawer;
        private final int     position;

        public Module(boolean hasDrawer, int position) {
            this.hasDrawer = hasDrawer;
            this.position = position;
        }

        @Provides @Named("stub") boolean providesHasDrawer() {
            return hasDrawer;
        }

        @Provides @Named("stub") String providesStubText() {
            return String.format("STUB SCREEN %d", position);
        }
    }

    @Singleton
    public static class Presenter extends ViewPresenter<StubView> {

        private final ActionBarPresenter                        actionBar;
        private final DrawerPresenter                           drawer;
        private final boolean                                   hasDrawer;
        private final String                                    stubText;
        private final PopupPresenter<ExamplePopupData, Boolean> examplePopupPresenter;

        @Inject
        Presenter(ActionBarPresenter actionBar, DrawerPresenter drawer, @Named("stub") boolean hasDrawer, @Named("stub") String stubText) {
            this.actionBar = actionBar;
            this.drawer = drawer;
            this.hasDrawer = hasDrawer;
            this.stubText = stubText;
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
            }

            getView().setStubText(stubText);

            examplePopupPresenter.takeView(getView().getExamplePopup());
        }

        @Override
        public void dropView(StubView view) {
            examplePopupPresenter.dropView(getView().getExamplePopup());
            super.dropView(view);
        }
    }
}
