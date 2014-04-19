package co.lemonlabs.mortar.example.ui.screens;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.core.android.ActionBarPresenter;
import co.lemonlabs.mortar.example.core.android.DrawerPresenter;
import co.lemonlabs.mortar.example.ui.views.StubView;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.stub)
public class StubScreen implements Blueprint {
    @Override
    public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
        return new Module();
    }

    @dagger.Module(
        injects = {
            StubView.class
        },
        addsTo = CorePresenter.Module.class,
        library = true
    )

    public static class Module {

        public Module() {}
    }

    @Singleton
    public static class Presenter extends ViewPresenter<StubView> {

        private final ActionBarPresenter actionBar;
        private final DrawerPresenter    drawer;

        @Inject Presenter(ActionBarPresenter actionBar, DrawerPresenter drawer) {
            this.actionBar = actionBar;
            this.drawer = drawer;
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;

            actionBar.setConfig(new ActionBarPresenter.Config(true, true, "Stub", null));
            drawer.setConfig(new DrawerPresenter.Config(false, DrawerLayout.LOCK_MODE_LOCKED_CLOSED));
        }
    }
}
