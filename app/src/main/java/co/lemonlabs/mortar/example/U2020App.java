package co.lemonlabs.mortar.example;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.lemonlabs.mortar.example.ui.ActivityHierarchyServer;
import dagger.ObjectGraph;
import mortar.Mortar;
import mortar.MortarScope;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public class U2020App extends Application {

    private MortarScope applicationScope;

    @Inject ActivityHierarchyServer activityHierarchyServer;

    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            // TODO Crashlytics.start(this);
            // TODO Timber.plant(new CrashlyticsTree());
        }

        buildObjectGraphAndInject();

        registerActivityLifecycleCallbacks(activityHierarchyServer);
    }

    private void buildObjectGraphAndInject() {
        long start = SystemClock.elapsedRealtime();

        ObjectGraph objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);
        applicationScope = Mortar.createRootScope(BuildConfig.DEBUG, objectGraph);

        long diff = TimeUnit.NANOSECONDS.toMillis(SystemClock.elapsedRealtime() - start);
        Timber.i("Global object graph creation took %sms", diff);
    }

    public void rebuildObjectGraphAndInject() {
        Mortar.destroyRootScope(applicationScope);
        buildObjectGraphAndInject();
    }

    @Override
    public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return applicationScope;
        }
        return super.getSystemService(name);
    }

    public static U2020App get(Context context) {
        return (U2020App) context.getApplicationContext();
    }
}
