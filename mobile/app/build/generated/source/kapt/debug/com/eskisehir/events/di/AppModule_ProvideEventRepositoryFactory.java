package com.eskisehir.events.di;

import com.eskisehir.events.data.repository.EventRepositoryImpl;
import com.eskisehir.events.domain.repository.EventRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AppModule_ProvideEventRepositoryFactory implements Factory<EventRepository> {
  private final Provider<EventRepositoryImpl> implProvider;

  public AppModule_ProvideEventRepositoryFactory(Provider<EventRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public EventRepository get() {
    return provideEventRepository(implProvider.get());
  }

  public static AppModule_ProvideEventRepositoryFactory create(
      Provider<EventRepositoryImpl> implProvider) {
    return new AppModule_ProvideEventRepositoryFactory(implProvider);
  }

  public static EventRepository provideEventRepository(EventRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideEventRepository(impl));
  }
}
