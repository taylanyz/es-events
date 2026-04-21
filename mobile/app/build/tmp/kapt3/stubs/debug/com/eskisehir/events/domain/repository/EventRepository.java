package com.eskisehir.events.domain.repository;

/**
 * Repository contract for the domain layer.
 * Defines what data operations are available without specifying
 * how they are implemented (API, database, cache, etc.).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0007\u0010\bJ\"\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\n0\u0003H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fJ*\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\n0\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0010\u0010\u0011J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0013H\u00a6@\u00a2\u0006\u0002\u0010\fJT\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\n0\u00032\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u000f0\n2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\n2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u001bH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001c\u0010\u001dJH\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\n0\u00032\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00060\n2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u00192\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u0019H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\"\u0010#J\u0016\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\bJ*\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\n0\u00032\u0006\u0010(\u001a\u00020\u0017H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b)\u0010*J\u0016\u0010+\u001a\u00020,2\u0006\u0010&\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\b\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006-"}, d2 = {"Lcom/eskisehir/events/domain/repository/EventRepository;", "", "getEventById", "Lkotlin/Result;", "Lcom/eskisehir/events/domain/model/Event;", "id", "", "getEventById-gIAlu-s", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEvents", "", "getEvents-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEventsByCategory", "category", "Lcom/eskisehir/events/domain/model/Category;", "getEventsByCategory-gIAlu-s", "(Lcom/eskisehir/events/domain/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFavoriteIds", "", "getRecommendations", "categories", "tags", "", "maxPrice", "", "limit", "", "getRecommendations-yxL6bBk", "(Ljava/util/List;Ljava/util/List;Ljava/lang/Double;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRoute", "eventIds", "startLat", "startLng", "getRoute-BWLJW6A", "(Ljava/util/List;Ljava/lang/Double;Ljava/lang/Double;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isFavorite", "", "eventId", "searchEvents", "query", "searchEvents-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleFavorite", "", "app_debug"})
public abstract interface EventRepository {
    
    /**
     * Gets all favorite event IDs from local storage
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getFavoriteIds(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Set<java.lang.Long>> $completion);
    
    /**
     * Toggles favorite status for an event
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object toggleFavorite(long eventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Checks if an event is favorited
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object isFavorite(long eventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    /**
     * Repository contract for the domain layer.
     * Defines what data operations are available without specifying
     * how they are implemented (API, database, cache, etc.).
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}