package com.eskisehir.events.domain.usecase;

/**
 * Use case for getting personalized event recommendations.
 * Delegates to the backend recommendation engine.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004JT\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00072\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086B\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0013"}, d2 = {"Lcom/eskisehir/events/domain/usecase/GetRecommendedEventsUseCase;", "", "repository", "Lcom/eskisehir/events/domain/repository/EventRepository;", "(Lcom/eskisehir/events/domain/repository/EventRepository;)V", "invoke", "Lkotlin/Result;", "", "Lcom/eskisehir/events/domain/model/Event;", "categories", "Lcom/eskisehir/events/domain/model/Category;", "tags", "", "maxPrice", "", "limit", "", "invoke-yxL6bBk", "(Ljava/util/List;Ljava/util/List;Ljava/lang/Double;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class GetRecommendedEventsUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.eskisehir.events.domain.repository.EventRepository repository = null;
    
    @javax.inject.Inject()
    public GetRecommendedEventsUseCase(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.domain.repository.EventRepository repository) {
        super();
    }
}