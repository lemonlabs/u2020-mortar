package co.lemonlabs.mortar.example.ui.screens;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.CorePresenter;
import co.lemonlabs.mortar.example.core.TransitionScreen;
import co.lemonlabs.mortar.example.core.android.ActionBarPresenter;
import co.lemonlabs.mortar.example.core.android.DrawerPresenter;
import co.lemonlabs.mortar.example.core.anim.Transition;
import co.lemonlabs.mortar.example.core.anim.Transitions;
import co.lemonlabs.mortar.example.data.GalleryDatabase;
import co.lemonlabs.mortar.example.data.api.Section;
import co.lemonlabs.mortar.example.data.api.model.Image;
import co.lemonlabs.mortar.example.data.rx.EndlessObserver;
import co.lemonlabs.mortar.example.ui.views.GalleryView;
import dagger.Provides;
import flow.Layout;
import mortar.ViewPresenter;
import rx.Subscription;

@Layout(R.layout.gallery_view)
@Transition({Transitions.NONE, Transitions.NONE, Transitions.NONE, Transitions.NONE})
public class GalleryScreen extends TransitionScreen {

    @Override
    public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
        return new Module(getViewState());
    }

    public static final Creator<GalleryScreen> CREATOR = new ScreenCreator<GalleryScreen>() {
        @Override protected GalleryScreen doCreateFromParcel(Parcel source) {
            return new GalleryScreen();
        }

        @Override public GalleryScreen[] newArray(int size) {
            return new GalleryScreen[size];
        }
    };

    @dagger.Module(
        injects = GalleryView.class,
        addsTo = CorePresenter.Module.class,
        library = true
    )

    public static class Module {

        private final SparseArray<Parcelable> viewState;

        public Module(SparseArray<Parcelable> viewState) {
            this.viewState = viewState;
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

        private final ActionBarPresenter      actionBar;
        private final DrawerPresenter         drawer;
        private final GalleryDatabase         galleryDatabase;
        private final Picasso                 picasso;
        private final Section                 section;
        private final SparseArray<Parcelable> viewState;

        private Subscription request;

        @Inject
        Presenter(ActionBarPresenter actionBar, DrawerPresenter drawer, GalleryDatabase galleryDatabase, Picasso picasso, Section section, SparseArray<Parcelable> viewState) {
            this.actionBar = actionBar;
            this.drawer = drawer;
            this.galleryDatabase = galleryDatabase;
            this.picasso = picasso;
            this.section = section;
            this.viewState = viewState;
        }

        @Override
        public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) return;

            request = galleryDatabase.loadGallery(section, new EndlessObserver<List<Image>>() {
                @Override public void onNext(List<Image> images) {
                    if (getView() != null) {
                        getView().getAdapter().replaceWith(images);
                        getView().updateChildId();
                        if (viewState != null) {
                            getView().restoreHierarchyState(viewState);
                        }
                    }
                }
            });

            actionBar.setConfig(new ActionBarPresenter.Config(true, true, "U2020", null));
            drawer.setConfig(new DrawerPresenter.Config(true, DrawerLayout.LOCK_MODE_UNLOCKED));
        }

        @Override
        public void dropView(GalleryView view) {
            request.unsubscribe();
            super.dropView(view);
        }

        public Picasso getPicasso() {
            return picasso;
        }
    }
}
