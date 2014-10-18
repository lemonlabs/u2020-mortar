package co.lemonlabs.mortar.example.ui.screens;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.ui.views.DrawerView;
import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.drawer)
public class DrawerScreen implements Blueprint {
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
            DrawerView.class
        },
        addsTo = CorePresenter.Module.class,
        library = true
    )
    public static class Module {
    }

    @Singleton
    public static class Presenter extends ViewPresenter<DrawerView> {

        private final Flow flow;

        @Inject Presenter(Flow flow) {
            this.flow = flow;
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;
            getView().setListAdapter();
        }

        public void goToScreenAtPosition(int position) {
            switch (position) {
                case 0:
                    flow.replaceTo(new GalleryScreen());
                    break;
                case 1:
                    flow.replaceTo(new NestedScreen());
                    break;
                case 2:
                    flow.replaceTo(new StubXScreen(true, position - 1));
                    break;
                case 3:
                    flow.replaceTo(new ViewStateScreen(1));
                    break;
            }
        }
    }
}
