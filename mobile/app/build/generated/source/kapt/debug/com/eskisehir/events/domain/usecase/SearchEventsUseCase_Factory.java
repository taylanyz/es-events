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
public final class SearchEventsUseCase_Factory implements Factory<SearchEventsUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public SearchEventsUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchEventsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SearchEventsUseCase_Factory create(Provider<EventRepository> repositoryProvider) {
    return new SearchEventsUseCase_Factory(repositoryProvider);
  }

  public static SearchEventsUseCase newInstance(EventRepository repository) {
    return new SearchEventsUseCase(repository);
  }
}
