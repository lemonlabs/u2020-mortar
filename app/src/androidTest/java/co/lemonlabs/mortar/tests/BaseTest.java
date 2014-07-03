package co.lemonlabs.mortar.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.squareup.spoon.Spoon;

import javax.inject.Inject;

import co.lemonlabs.mortar.example.core.android.ActionBarPresenter;
import co.lemonlabs.mortar.example.core.android.DrawerPresenter;
import co.lemonlabs.mortar.example.data.IdlingDownloaderWrapper;
import co.lemonlabs.mortar.example.data.api.IdlingGalleryServiceWrapper;
import co.lemonlabs.mortar.example.ui.MainActivity;
import flow.Flow;
import retrofit.MockRestAdapter;

public class BaseTest extends ActivityInstrumentationTestCase2<MainActivity> {

    @Inject MockRestAdapter             mockAdapter;
    @Inject IdlingDownloaderWrapper     downloader;
    @Inject IdlingGalleryServiceWrapper api;
    @Inject Flow                        flow;
    @Inject ActionBarPresenter          actionBar;
    @Inject DrawerPresenter             drawer;

    protected MainActivity activity;

    public BaseTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();

        activity.getMortarScope()
            .getObjectGraph()
            .plus(new TestModule())
            .inject(this);

        Espresso.registerIdlingResources(api, downloader);

        mockAdapter.setDelay(50);
        mockAdapter.setErrorPercentage(0);
        mockAdapter.setVariancePercentage(0);
    }

    protected void screenshot(String description) throws Throwable {
        Thread.sleep(100);
        Spoon.screenshot(activity, description.replaceAll("[^a-zA-Z0-9_-]", "_"));
    }


    @dagger.Module(
        complete = false,
        library = true,
        injects = {
            BaseTest.class,
            FlowTest.class
        }
    ) class TestModule {}
}
