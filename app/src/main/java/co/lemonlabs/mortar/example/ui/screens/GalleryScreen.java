package co.lemonlabs.mortar.example.ui.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.core.StateBlueprint;
import co.lemonlabs.mortar.example.core.android.ActionBarPresenter;
import co.lemonlabs.mortar.example.core.android.DrawerPresenter;
import co.lemonlabs.mortar.example.data.GalleryDatabase;
import co.lemonlabs.mortar.example.data.api.Section;
import co.lemonlabs.mortar.example.data.api.model.Image;
import co.lemonlabs.mortar.example.data.rx.EndlessObserver;
import co.lemonlabs.mortar.example.ui.gallery.GalleryAdapter;
import co.lemonlabs.mortar.example.ui.views.GalleryView;
import dagger.Provides;
import flow.Flow;
import flow.Layout;
import mortar.ViewPresenter;
import rx.Subscription;
import rx.util.functions.Action0;

@Layout(R.layout.gallery_view)
public class GalleryScreen implements StateBlueprint {

    private SparseArray<Parcelable> viewState;

    @Override
    public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
        return new Module(viewState);
    }

    @Override public void setViewState(SparseArray<Parcelable> viewState) {
        this.viewState = viewState;
    }

    @dagger.Module(
        injects = GalleryView.class,
        addsTo = CorePresenter.Module.class,
        library = true
    )

    public static class Module {

        private final SparseArray<Parcelable> viewState;

        public Module(SparseArray<Parcelable> viewState) {this.viewState = viewState;}

        @Provides GalleryAdapter providesGalleryAdapter(Context context, Picasso picasso) {
            return new GalleryAdapter(context, picasso);
        }

        @Provides Section providesSection() {
            return Section.HOT;
        }


        @Provides SparseArray<Parcelable> provideViewState() {
            return viewState;
        }

    }

    @Singleton
    public static class Presenter extends ViewPresenter<GalleryView> {

        private final ActionBarPresenter actionBar;
        private final DrawerPresenter    drawer;
        private final GalleryDatabase    galleryDatabase;

        private final Flow                    flow;
        private final GalleryAdapter          adapter;
        private final Section                 section;
        private final SparseArray<Parcelable> viewState;

        private Subscription request;

        @Inject
        Presenter(ActionBarPresenter actionBar, DrawerPresenter drawer, GalleryDatabase galleryDatabase, Flow flow, GalleryAdapter adapter, Section section, SparseArray<Parcelable> viewState) {
            this.actionBar = actionBar;
            this.drawer = drawer;
            this.galleryDatabase = galleryDatabase;
            this.flow = flow;
            this.adapter = adapter;
            this.section = section;
            this.viewState = viewState;
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;

            getView().setAdapter(adapter);

            request = galleryDatabase.loadGallery(section, new EndlessObserver<List<Image>>() {
                @Override public void onNext(List<Image> images) {
                    if (getView() != null) {
                        adapter.replaceWith(images);
                        getView().updateChildId();
                        if (viewState != null) {
                            getView().restoreHierarchyState(viewState);
                        }
                    }
                }
            });

            actionBar.setConfig(new ActionBarPresenter.Config(true, true, "U2020",
                new ActionBarPresenter.MenuAction("Go to Stub", new Action0() {
                    @Override public void call() {
                        flow.goTo(new StubScreen());
                    }
                })
            ));

            drawer.setConfig(new DrawerPresenter.Config(true, DrawerLayout.LOCK_MODE_UNLOCKED));
        }

        @Override
        public void dropView(GalleryView view) {
            request.unsubscribe();
            super.dropView(view);
        }
    }
}
