package co.lemonlabs.mortar.example;

final class Modules {
  static Object[] list(U2020App app) {
    return new Object[] {
        new U2020Module(app)
    };
  }

  private Modules() {
    // No instances.
  }
}
