package com.eskisehir.events.data.repository;

import com.eskisehir.events.data.local.dao.FavoriteDao;
import com.eskisehir.events.data.remote.api.EventApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class EventRepositoryImpl_Factory implements Factory<EventRepositoryImpl> {
  private final Provider<EventApiService> apiServiceProvider;

  private final Provider<FavoriteDao> favoriteDaoProvider;

  public EventRepositoryImpl_Factory(Provider<EventApiService> apiServiceProvider,
      Provider<FavoriteDao> favoriteDaoProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.favoriteDaoProvider = favoriteDaoProvider;
  }

  @Override
  public EventRepositoryImpl get() {
    return newInstance(apiServiceProvider.get(), favoriteDaoProvider.get());
  }

  public static EventRepositoryImpl_Factory create(Provider<EventApiService> apiServiceProvider,
      Provider<FavoriteDao> favoriteDaoProvider) {
    return new EventRepositoryImpl_Factory(apiServiceProvider, favoriteDaoProvider);
  }

  public static EventRepositoryImpl newInstance(EventApiService apiService,
      FavoriteDao favoriteDao) {
    return new EventRepositoryImpl(apiService, favoriteDao);
  }
}
