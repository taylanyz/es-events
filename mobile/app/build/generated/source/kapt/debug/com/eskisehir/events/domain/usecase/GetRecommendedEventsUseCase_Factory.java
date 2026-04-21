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
public final class GetRecommendedEventsUseCase_Factory implements Factory<GetRecommendedEventsUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public GetRecommendedEventsUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetRecommendedEventsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetRecommendedEventsUseCase_Factory create(
      Provider<EventRepository> repositoryProvider) {
    return new GetRecommendedEventsUseCase_Factory(repositoryProvider);
  }

  public static GetRecommendedEventsUseCase newInstance(EventRepository repository) {
    return new GetRecommendedEventsUseCase(repository);
  }
}
