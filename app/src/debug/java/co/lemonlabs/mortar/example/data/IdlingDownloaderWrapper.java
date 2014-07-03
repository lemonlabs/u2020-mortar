package co.lemonlabs.mortar.example.data;

import android.net.Uri;

import com.google.android.apps.common.testing.ui.espresso.IdlingResource;
import com.squareup.picasso.Downloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IdlingDownloaderWrapper implements Downloader, IdlingResource {

    @Inject Downloader downloader;

    private final List<ResourceCallback> callbacks;

    private final AtomicInteger counter;

    @Inject public IdlingDownloaderWrapper(Downloader downloader) {
        this.downloader = downloader;
        this.callbacks = new ArrayList<>();
        this.counter = new AtomicInteger(0);
    }

    @Override public Response load(Uri uri, boolean localCacheOnly) throws IOException {
        counter.incrementAndGet();
        try {
            Response response = downloader.load(uri, localCacheOnly);
            counter.decrementAndGet();
            notifyIdle();
            return response;
        } catch(IOException e) {
            counter.decrementAndGet();
            notifyIdle();
            throw e;
        }
    }

    @Override public String getName() {
        return this.getClass().getName() + hashCode();
    }

    @Override public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        callbacks.add(resourceCallback);
    }

    private void notifyIdle() {
        if (counter.get() == 0) {
            for (ResourceCallback cb : callbacks) {
                cb.onTransitionToIdle();
            }
        }
    }
}
