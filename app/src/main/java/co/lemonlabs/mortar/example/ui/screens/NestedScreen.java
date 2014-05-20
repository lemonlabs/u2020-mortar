package co.lemonlabs.mortar.example.ui.screens;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.core.android.ActionBarPresenter;
import co.lemonlabs.mortar.example.ui.views.NestedChildView;
import co.lemonlabs.mortar.example.ui.views.NestedView;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import rx.functions.Action0;

@Layout(R.layout.nested)
public class NestedScreen implements Blueprint {

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
            NestedView.class,
            NestedChildView.class
        },
        addsTo = CorePresenter.Module.class,
        library = true
    )

    public static class Module {

        public Module() {}
    }

    @Singleton
    public static class Presenter extends ViewPresenter<NestedView> {

        @Inject ChildPresenter childPresenter;

        private final ActionBarPresenter actionBar;

        @Inject Presenter(ActionBarPresenter actionBar) {
            this.actionBar = actionBar;
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;

            actionBar.setConfig(new ActionBarPresenter.Config(
                true,
                true,
                "Nested Presenters",
                new ActionBarPresenter.MenuAction("Animate", new Action0() {
                    @Override public void call() {
                        if (getView() != null) {
                            toggleChildAnimation();
                        }
                    }
                })
            ));
        }

        public void toggleChildAnimation() {
            childPresenter.toggleAnimation();
        }

        public ChildPresenter getChildPresenter() {
            return childPresenter;
        }
    }

    @Singleton
    public static class ChildPresenter extends ViewPresenter<NestedChildView> {

        @Inject ChildPresenter() {
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;
            toggleAnimation();
        }

        public void toggleAnimation() {
            getView().toggleAnimation();
        }
    }
}
