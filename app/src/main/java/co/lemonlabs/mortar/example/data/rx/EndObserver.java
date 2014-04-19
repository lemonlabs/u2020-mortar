package co.lemonlabs.mortar.example.data.rx;

import rx.Observer;

/** An {@link rx.Observer} that always informs when it's ended. */
public abstract class EndObserver<T> implements Observer<T> {
  @Override public void onCompleted() {
    onEnd();
  }

  @Override public void onError(Throwable throwable) {
    onEnd();
  }

  public abstract void onEnd();
}
