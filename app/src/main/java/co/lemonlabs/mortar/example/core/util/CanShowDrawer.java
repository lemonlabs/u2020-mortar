package co.lemonlabs.mortar.example.core.util;

import mortar.Blueprint;

public interface CanShowDrawer<S extends Blueprint> {
    void showDrawer(S screen);
}
