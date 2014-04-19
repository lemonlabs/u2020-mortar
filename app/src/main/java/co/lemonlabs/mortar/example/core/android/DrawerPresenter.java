package co.lemonlabs.mortar.example.core.android;

import android.os.Bundle;

import mortar.MortarScope;
import mortar.Presenter;

public class DrawerPresenter extends Presenter<DrawerPresenter.View> {

    public interface View {
        MortarScope getMortarScope();
        void setDrawerIndicatorEnabled(boolean enabled);
        void setDrawerLockMode(int mode);
    }

    public static class Config {
        public final boolean indicatorEnabled;
        public final int     lockMode;

        public Config(boolean indicatorEnabled, int lockMode) {
            this.indicatorEnabled = indicatorEnabled;
            this.lockMode = lockMode;
        }
    }

    private Config config;

    @Override
    protected MortarScope extractScope(View view) {
        return view.getMortarScope();
    }

    @Override
    public void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
        if (config != null) update();
    }

    public void setConfig(Config config) {
        this.config = config;
        update();
    }

    public Config getConfig() {
        return config;
    }

    private void update() {
        View view = getView();
        if (view == null) return;
        view.setDrawerIndicatorEnabled(config.indicatorEnabled);
        view.setDrawerLockMode(config.lockMode);
    }
}

