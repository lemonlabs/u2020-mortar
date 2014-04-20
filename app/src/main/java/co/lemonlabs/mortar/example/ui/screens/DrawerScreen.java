package co.lemonlabs.mortar.example.ui.screens;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.ui.views.DrawerView;
import dagger.Provides;
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

        public Module() {}

        @Provides @Named("drawer") ArrayAdapter<String> provideAdapter(Context context) {
            return new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                context.getResources().getStringArray(R.array.drawer_items)
            );
        }
    }


    @Singleton
    public static class Presenter extends ViewPresenter<DrawerView> {

        private final Flow                 flow;
        private final ArrayAdapter<String> adapter;

        @Inject Presenter(Flow flow, @Named("drawer") ArrayAdapter<String> adapter) {
            this.flow = flow;
            this.adapter = adapter;
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;
            getView().setListAdapter(adapter);
        }

        public void goToScreenAtPosition(int position) {
            switch (position) {
                case 0:
                    flow.replaceTo(new GalleryScreen());
                    break;
                case 1:
                    flow.replaceTo(new NestedScreen());
                    break;
                default:
                    flow.replaceTo(new StubScreen(true, position - 1));
                    break;
            }
        }
    }
}
