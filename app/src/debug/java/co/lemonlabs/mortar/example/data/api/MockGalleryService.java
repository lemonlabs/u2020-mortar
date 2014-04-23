package co.lemonlabs.mortar.example.data.api;

import com.google.android.apps.common.testing.ui.espresso.IdlingResource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.data.api.model.Gallery;
import co.lemonlabs.mortar.example.data.api.model.Image;
import rx.Observable;

@Singleton
public class MockGalleryService implements GalleryService, IdlingResource {
    private static final Gallery BAD_REQUEST = new Gallery(200, false, null);
    private static final int     PAGE_SIZE   = 50;

    private final ServerDatabase serverDatabase;

    private final AtomicInteger idlingLock;
    private final List<ResourceCallback> callbacks;

    @Inject MockGalleryService(ServerDatabase serverDatabase) {
        this.serverDatabase = serverDatabase;
        this.idlingLock = new AtomicInteger(0);
        callbacks = new ArrayList<>();
    }

    @Override public Observable<Gallery> listGallery(Section section, Sort sort, int page) {
        idlingLock.incrementAndGet();
        // Fetch desired section.
        List<Image> images = serverDatabase.getImagesForSection(section);
        if (images == null) {
            return Observable.from(BAD_REQUEST);
        }

        // Figure out proper list subset.
        int pageStart = (page - 1) * PAGE_SIZE;
        if (pageStart >= images.size() || pageStart < 0) {
            return Observable.from(BAD_REQUEST);
        }
        int pageEnd = Math.min(pageStart + PAGE_SIZE, images.size());

        // Sort and trim images.
        SortUtil.sort(images, sort);
        images = images.subList(pageStart, pageEnd);
        idlingLock.decrementAndGet();
        notifyIdle();
        return Observable.from(new Gallery(200, true, images));
    }

    @Override public String getName() {
        return getClass().getName();
    }

    @Override public boolean isIdleNow() {
        return idlingLock.get() == 0;
    }

    @Override public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        callbacks.add(resourceCallback);
    }

    private void notifyIdle() {
        if (idlingLock.get() == 0) {
            for (ResourceCallback cb : callbacks) {
                cb.onTransitionToIdle();
            }
        }
    }
}
