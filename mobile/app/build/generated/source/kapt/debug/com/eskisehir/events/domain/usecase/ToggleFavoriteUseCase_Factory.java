package com.eskisehir.events.domain.usecase;

import com.eskisehir.events.domain.repository.EventRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ToggleFavoriteUseCase_Factory implements Factory<ToggleFavoriteUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public ToggleFavoriteUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ToggleFavoriteUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ToggleFavoriteUseCase_Factory create(Provider<EventRepository> repositoryProvider) {
    return new ToggleFavoriteUseCase_Factory(repositoryProvider);
  }

  public static ToggleFavoriteUseCase newInstance(EventRepository repository) {
    return new ToggleFavoriteUseCase(repository);
  }
}
