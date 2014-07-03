package co.lemonlabs.mortar.example.data.api;

import android.app.Application;

import com.google.android.apps.common.testing.ui.espresso.IdlingResource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.data.api.model.Gallery;
import retrofit.http.Path;
import rx.Observable;
import rx.functions.Action1;

/**
 * GalleryService wrapper to be used when testing
 * with Espresso. See:
 * https://code.google.com/p/android-test-kit/wiki/EspressoSamples#Using_registerIdlingResource_to_synchronize_with_custom_resource
 */
@Singleton
public class IdlingGalleryServiceWrapper implements GalleryService, IdlingResource {

    @Inject GalleryService api;

    private final AtomicInteger counter;

    private final List<ResourceCallback> callbacks;

    @Inject public IdlingGalleryServiceWrapper(Application app, GalleryService api) {
        this.api = api;
        this.callbacks = new ArrayList<>();
        this.counter = new AtomicInteger(0);
    }

    @Override
    public Observable<Gallery> listGallery(@Path("section") Section section, @Path("sort") Sort sort, @Path("page") int page) {
        counter.incrementAndGet();
        return api.listGallery(section, sort, page).doOnNext(new IdlingAction<Gallery>());
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

    private class IdlingAction<T> implements Action1<T> {
        @Override public void call(T o) {
            counter.decrementAndGet();
            notifyIdle();
        }
    }
}
