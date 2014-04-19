package co.lemonlabs.mortar.example.data.api;

public enum Section {
  HOT("hot"),
  TOP("top"),
  USER("user");

  public final String value;

  Section(String value) {
    this.value = value;
  }

  @Override public String toString() {
    return value;
  }
}
