package com.eskisehir.events.data.remote.api;

/**
 * Retrofit interface defining all backend API endpoints.
 * Each method maps to a REST endpoint on the Spring Boot backend.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\b\b\u0001\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\b\b\u0001\u0010\u000f\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\b2\b\b\u0001\u0010\u000f\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u0018\u0010\u0019\u001a\u00020\u001a2\b\b\u0001\u0010\u000f\u001a\u00020\u001bH\u00a7@\u00a2\u0006\u0002\u0010\u001cJ\u001e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\b\b\u0001\u0010\u001e\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u001f"}, d2 = {"Lcom/eskisehir/events/data/remote/api/EventApiService;", "", "getEventById", "Lcom/eskisehir/events/data/remote/dto/EventDto;", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEvents", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEventsByCategory", "category", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecommendations", "request", "Lcom/eskisehir/events/data/remote/dto/RecommendationRequestDto;", "(Lcom/eskisehir/events/data/remote/dto/RecommendationRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRoute", "Lcom/eskisehir/events/data/remote/dto/RouteRequestDto;", "(Lcom/eskisehir/events/data/remote/dto/RouteRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSmartRecommendations", "Lcom/eskisehir/events/data/remote/dto/RecommendationResponseDto;", "Lcom/eskisehir/events/data/remote/dto/SmartRecommendationRequestDto;", "(Lcom/eskisehir/events/data/remote/dto/SmartRecommendationRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logInteraction", "", "Lcom/eskisehir/events/data/remote/dto/InteractionRequestDto;", "(Lcom/eskisehir/events/data/remote/dto/InteractionRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchEvents", "query", "app_debug"})
public abstract interface EventApiService {
    
    /**
     * GET /api/events — Fetch all events
     */
    @retrofit2.http.GET(value = "api/events")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEvents(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.eskisehir.events.data.remote.dto.EventDto>> $completion);
    
    /**
     * GET /api/events/{id} — Fetch single event by ID
     */
    @retrofit2.http.GET(value = "api/events/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEventById(@retrofit2.http.Path(value = "id")
    long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.eskisehir.events.data.remote.dto.EventDto> $completion);
    
    /**
     * GET /api/events/category/{category} — Filter events by category
     */
    @retrofit2.http.GET(value = "api/events/category/{category}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEventsByCategory(@retrofit2.http.Path(value = "category")
    @org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.eskisehir.events.data.remote.dto.EventDto>> $completion);
    
    /**
     * GET /api/events/search?q=... — Search events
     */
    @retrofit2.http.GET(value = "api/events/search")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchEvents(@retrofit2.http.Query(value = "q")
    @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.eskisehir.events.data.remote.dto.EventDto>> $completion);
    
    /**
     * POST /api/events/recommend — Get personalized recommendations
     */
    @retrofit2.http.POST(value = "api/events/recommend")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecommendations(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.remote.dto.RecommendationRequestDto request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.eskisehir.events.data.remote.dto.EventDto>> $completion);
    
    /**
     * POST /api/events/route — Get optimized route
     */
    @retrofit2.http.POST(value = "api/events/route")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRoute(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.remote.dto.RouteRequestDto request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.eskisehir.events.data.remote.dto.EventDto>> $completion);
    
    /**
     * POST /api/interactions — Log user interaction (click)
     */
    @retrofit2.http.POST(value = "api/interactions")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object logInteraction(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.remote.dto.InteractionRequestDto request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * POST /api/smart-recommendations — Get AI-powered smart recommendations
     */
    @retrofit2.http.POST(value = "api/smart-recommendations")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSmartRecommendations(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.remote.dto.SmartRecommendationRequestDto request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.eskisehir.events.data.remote.dto.RecommendationResponseDto>> $completion);
}