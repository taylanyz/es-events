package com.eskisehir.events.data.local.dao;

/**
 * Data Access Object for the favorites table.
 * Provides methods to add, remove, query, and check favorite events.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0012"}, d2 = {"Lcom/eskisehir/events/data/local/dao/FavoriteDao;", "", "addFavorite", "", "favorite", "Lcom/eskisehir/events/data/local/entity/FavoriteEntity;", "(Lcom/eskisehir/events/data/local/entity/FavoriteEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFavoriteIds", "", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFavorites", "isFavorite", "", "eventId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeFavorite", "removeFavoriteById", "app_debug"})
@androidx.room.Dao()
public abstract interface FavoriteDao {
    
    @androidx.room.Query(value = "SELECT * FROM favorites")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllFavorites(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.eskisehir.events.data.local.entity.FavoriteEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT eventId FROM favorites")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllFavoriteIds(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addFavorite(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.entity.FavoriteEntity favorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object removeFavorite(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.entity.FavoriteEntity favorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT EXISTS(SELECT 1 FROM favorites WHERE eventId = :eventId)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object isFavorite(long eventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    @androidx.room.Query(value = "DELETE FROM favorites WHERE eventId = :eventId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object removeFavoriteById(long eventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}