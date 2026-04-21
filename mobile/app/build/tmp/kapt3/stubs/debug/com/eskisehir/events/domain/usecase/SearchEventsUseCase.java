package com.eskisehir.events.domain.usecase;

/**
 * Use case for searching events by a query string.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J*\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\t\u001a\u00020\nH\u0086B\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\r"}, d2 = {"Lcom/eskisehir/events/domain/usecase/SearchEventsUseCase;", "", "repository", "Lcom/eskisehir/events/domain/repository/EventRepository;", "(Lcom/eskisehir/events/domain/repository/EventRepository;)V", "invoke", "Lkotlin/Result;", "", "Lcom/eskisehir/events/domain/model/Event;", "query", "", "invoke-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class SearchEventsUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.eskisehir.events.domain.repository.EventRepository repository = null;
    
    @javax.inject.Inject()
    public SearchEventsUseCase(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.domain.repository.EventRepository repository) {
        super();
    }
}