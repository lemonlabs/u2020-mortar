package co.lemonlabs.mortar.example.data.api.model;

public abstract class ImgurResponse {
  public final int status;
  public final boolean success;

  public ImgurResponse(int status, boolean success) {
    this.status = status;
    this.success = success;
  }
}
