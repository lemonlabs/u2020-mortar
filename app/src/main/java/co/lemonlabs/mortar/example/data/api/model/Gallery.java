package co.lemonlabs.mortar.example.data.api.model;

import java.util.List;

public final class Gallery extends ImgurResponse {
  public final List<Image> data;

  public Gallery(int status, boolean success, List<Image> data) {
    super(status, success);
    this.data = data;
  }
}
