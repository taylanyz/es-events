package com.eskisehir.events.di;

import com.eskisehir.events.data.remote.api.EventApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class NetworkModule_ProvideEventApiServiceFactory implements Factory<EventApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideEventApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public EventApiService get() {
    return provideEventApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideEventApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideEventApiServiceFactory(retrofitProvider);
  }

  public static EventApiService provideEventApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideEventApiService(retrofit));
  }
}
