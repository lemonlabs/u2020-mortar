package co.lemonlabs.mortar.example.core.util;

import flow.Flow;
import mortar.Blueprint;

public interface CanShowScreen<S extends Blueprint> {
  void showScreen(S screen, S oldScreen, Flow.Direction direction);
}
