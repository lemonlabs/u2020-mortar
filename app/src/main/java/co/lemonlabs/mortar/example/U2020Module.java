package co.lemonlabs.mortar.example;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import co.lemonlabs.mortar.example.core.android.AndroidModule;
import co.lemonlabs.mortar.example.core.util.ScreenParcer;
import co.lemonlabs.mortar.example.data.DataModule;
import co.lemonlabs.mortar.example.ui.UiModule;
import dagger.Module;
import dagger.Provides;
import flow.Parcer;

@Module(
    includes = {
        UiModule.class,
        DataModule.class,
        AndroidModule.class
    },
    injects = {
        U2020App.class
    },
    library = true
)
public final class U2020Module {
    private final U2020App app;

    public U2020Module(U2020App app) {
        this.app = app;
    }

    @Provides @Singleton Application provideApplication() {
        return app;
    }

    @Provides @Singleton Gson provideGson() {
        return new GsonBuilder()
            .create();
    }

    @Provides @Singleton Parcer<Object> provideParcer() {
        return new ScreenParcer<>();
    }
}
