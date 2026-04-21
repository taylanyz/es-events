package com.eskisehir.events.data.repository;

/**
 * Concrete implementation of EventRepository.
 * Combines remote API calls with local Room database for favorites.
 *
 * Uses Kotlin Result type for error handling, allowing the presentation
 * layer to handle errors gracefully without try-catch blocks.
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\f\u0010\rJ\"\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0010\u0010\u0011J*\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\b2\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0018H\u0096@\u00a2\u0006\u0002\u0010\u0011JP\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\b2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000f2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b!\u0010\"JD\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000f2\b\u0010%\u001a\u0004\u0018\u00010\u001e2\b\u0010&\u001a\u0004\u0018\u00010\u001eH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\'\u0010(J\u0016\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\rJ*\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\b2\u0006\u0010-\u001a\u00020\u001cH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b.\u0010/J\u0016\u00100\u001a\u0002012\u0006\u0010+\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00062"}, d2 = {"Lcom/eskisehir/events/data/repository/EventRepositoryImpl;", "Lcom/eskisehir/events/domain/repository/EventRepository;", "apiService", "Lcom/eskisehir/events/data/remote/api/EventApiService;", "favoriteDao", "Lcom/eskisehir/events/data/local/dao/FavoriteDao;", "(Lcom/eskisehir/events/data/remote/api/EventApiService;Lcom/eskisehir/events/data/local/dao/FavoriteDao;)V", "getEventById", "Lkotlin/Result;", "Lcom/eskisehir/events/domain/model/Event;", "id", "", "getEventById-gIAlu-s", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEvents", "", "getEvents-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEventsByCategory", "category", "Lcom/eskisehir/events/domain/model/Category;", "getEventsByCategory-gIAlu-s", "(Lcom/eskisehir/events/domain/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFavoriteIds", "", "getRecommendations", "categories", "tags", "", "maxPrice", "", "limit", "", "getRecommendations-yxL6bBk", "(Ljava/util/List;Ljava/util/List;Ljava/lang/Double;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRoute", "eventIds", "startLat", "startLng", "getRoute-BWLJW6A", "(Ljava/util/List;Ljava/lang/Double;Ljava/lang/Double;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isFavorite", "", "eventId", "searchEvents", "query", "searchEvents-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleFavorite", "", "app_debug"})
public final class EventRepositoryImpl implements com.eskisehir.events.domain.repository.EventRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.eskisehir.events.data.remote.api.EventApiService apiService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.eskisehir.events.data.local.dao.FavoriteDao favoriteDao = null;
    
    @javax.inject.Inject()
    public EventRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.remote.api.EventApiService apiService, @org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.dao.FavoriteDao favoriteDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getFavoriteIds(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Set<java.lang.Long>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object toggleFavorite(long eventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object isFavorite(long eventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
}