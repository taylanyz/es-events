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
public final class GetEventByIdUseCase_Factory implements Factory<GetEventByIdUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public GetEventByIdUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetEventByIdUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetEventByIdUseCase_Factory create(Provider<EventRepository> repositoryProvider) {
    return new GetEventByIdUseCase_Factory(repositoryProvider);
  }

  public static GetEventByIdUseCase newInstance(EventRepository repository) {
    return new GetEventByIdUseCase(repository);
  }
}
