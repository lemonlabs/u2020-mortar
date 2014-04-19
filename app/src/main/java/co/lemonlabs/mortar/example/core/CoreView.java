package co.lemonlabs.mortar.example.core;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;

import javax.inject.Inject;

import co.lemonlabs.mortar.example.R;
import co.lemonlabs.mortar.example.core.util.CanShowDrawer;
import co.lemonlabs.mortar.example.core.util.CanShowScreen;
import co.lemonlabs.mortar.example.core.util.ScreenConductor;
import flow.Flow;
import mortar.Blueprint;
import mortar.Mortar;

public class CoreView extends DrawerLayout implements CanShowScreen<Blueprint>, CanShowDrawer<Blueprint> {

    @Inject CorePresenter.Presenter presenter;

    private final ScreenConductor<Blueprint> screenMaestro;
    private       ActionBarDrawerToggle      drawerToggle;

    public CoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
        screenMaestro = new ScreenConductor<>(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.takeView(this);
        initNavigationDrawer();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public Flow getFlow() {
        return presenter.getFlow();
    }

    @Override
    public void showScreen(Blueprint screen, Blueprint oldScreen, Flow.Direction direction) {
        screenMaestro.showScreen(screen, oldScreen, direction);
    }

    @Override public void showDrawer(Blueprint screen) {
        screenMaestro.showDrawer(screen);
    }

    public void initNavigationDrawer() {
        drawerToggle = new ActionBarDrawerToggle(
            (Activity) getContext(),
            this,
            R.drawable.ic_drawer,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        setDrawerListener(drawerToggle);

    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

}
