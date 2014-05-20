package co.lemonlabs.mortar.example.data.api.transforms;

import java.util.List;

import co.lemonlabs.mortar.example.data.api.model.Gallery;
import co.lemonlabs.mortar.example.data.api.model.Image;
import rx.functions.Func1;

public final class GalleryToImageList implements Func1<Gallery, List<Image>> {
  @Override public List<Image> call(Gallery gallery) {
    return gallery.data;
  }
}
