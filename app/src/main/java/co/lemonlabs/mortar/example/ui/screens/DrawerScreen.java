package co.lemonlabs.mortar.example.ui.screens;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.ui.views.DrawerView;
import dagger.Provides;
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

        @Provides ArrayAdapter<String> provideAdapter(Context context) {
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

        private final ArrayAdapter<String> adapter;

        @Inject Presenter(ArrayAdapter<String> adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;
            getView().setListAdapter(adapter);
        }
    }
}
