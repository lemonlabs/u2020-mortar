package co.lemonlabs.mortar.example;

import co.lemonlabs.mortar.example.U2020Module;
import co.lemonlabs.mortar.example.data.DebugDataModule;
import co.lemonlabs.mortar.example.ui.DebugUiModule;
import dagger.Module;

@Module(
    addsTo = U2020Module.class,
    includes = {
        DebugUiModule.class,
        DebugDataModule.class
    },
    overrides = true
)
public final class DebugU2020Module {
}
