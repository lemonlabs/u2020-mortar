package co.lemonlabs.mortar.example.ui.screens;

import android.content.Intent;
import android.net.Uri;
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
import co.lemonlabs.mortar.example.core.android.ActivityResultPresenter;
import co.lemonlabs.mortar.example.core.android.ActivityResultRegistrar;
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
import mortar.MortarScope;
import mortar.ViewPresenter;
import rx.Subscription;
import rx.functions.Action0;
import timber.log.Timber;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

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
    public static class Presenter extends ViewPresenter<GalleryView>
        implements ActivityResultPresenter.ActivityResultListener {

        private static final int PICK_CONTACT_REQUEST = 1;

        private final ActionBarPresenter      actionBar;
        private final DrawerPresenter         drawer;
        private final GalleryDatabase         galleryDatabase;
        private final Picasso                 picasso;
        private final Section                 section;
        private final ActivityResultRegistrar activityResultRegistrar;
        private final SparseArray<Parcelable> viewState;

        private Subscription request;

        @Inject Presenter(
            ActionBarPresenter actionBar,
            DrawerPresenter drawer,
            GalleryDatabase galleryDatabase,
            Picasso picasso,
            Section section,
            ActivityResultRegistrar activityResultRegistrar,
            SparseArray<Parcelable> viewState
        ) {
            this.actionBar = actionBar;
            this.drawer = drawer;
            this.galleryDatabase = galleryDatabase;
            this.picasso = picasso;
            this.section = section;
            this.activityResultRegistrar = activityResultRegistrar;
            this.viewState = viewState;
        }

        @Override public void onLoad(Bundle savedInstanceState) {
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

            actionBar.setConfig(new ActionBarPresenter.Config(
                true, true, "U2020",
                new ActionBarPresenter.MenuAction("Pick contact", new Action0() {
                    @Override public void call() {
                        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                        pickContactIntent.setType(Phone.CONTENT_TYPE);
                        activityResultRegistrar.startActivityForResult(PICK_CONTACT_REQUEST, pickContactIntent);
                    }
                })
            ));
            drawer.setConfig(new DrawerPresenter.Config(true, DrawerLayout.LOCK_MODE_UNLOCKED));
        }

        @Override public void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            activityResultRegistrar.register(scope, this);
        }

        @Override public void dropView(GalleryView view) {
            request.unsubscribe();
            super.dropView(view);
        }

        @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Timber.i("request code: %s, result code: %s", requestCode, resultCode);
            if (data != null) {
                Timber.i("Got data: %s", data.getData());
                // do something with data
            }
        }

        public Picasso getPicasso() {
            return picasso;
        }
    }
}
