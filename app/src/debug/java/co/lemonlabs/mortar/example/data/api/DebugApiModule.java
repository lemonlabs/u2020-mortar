package co.lemonlabs.mortar.example.data.api;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import co.lemonlabs.mortar.example.data.ApiEndpoint;
import co.lemonlabs.mortar.example.data.IsMockMode;
import co.lemonlabs.mortar.example.data.prefs.StringPreference;
import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.android.AndroidMockValuePersistence;

@Module(
    complete = false,
    library = true,
    overrides = true
)
public final class DebugApiModule {

  @Provides @Singleton
  Endpoint provideEndpoint(@ApiEndpoint StringPreference apiEndpoint) {
    return Endpoints.newFixedEndpoint(apiEndpoint.get());
  }

  @Provides @Singleton
  MockRestAdapter provideMockRestAdapter(RestAdapter restAdapter, SharedPreferences preferences) {
    MockRestAdapter mockRestAdapter = MockRestAdapter.from(restAdapter);
    AndroidMockValuePersistence.install(mockRestAdapter, preferences);
    return mockRestAdapter;
  }

  @Provides @Singleton
  GalleryService provideGalleryService(IdlingGalleryServiceWrapper idlingWrapper) {
    return idlingWrapper;
  }

  @Provides @Singleton
  IdlingGalleryServiceWrapper provideIdlingGalleryServiceWrapper(Application app, RestAdapter restAdapter, MockRestAdapter mockRestAdapter,
                                                                   @IsMockMode boolean isMockMode, MockGalleryService mockService) {
    return new IdlingGalleryServiceWrapper(app, (
      isMockMode
        ? mockRestAdapter.create(GalleryService.class, mockService)
        : restAdapter.create(GalleryService.class)
    ));
  }
}
