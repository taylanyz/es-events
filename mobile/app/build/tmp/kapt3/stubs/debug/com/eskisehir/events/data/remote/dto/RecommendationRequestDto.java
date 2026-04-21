package com.eskisehir.events.data.remote.dto;

/**
 * Request body for the recommendation endpoint.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B3\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0016\u001a\u00020\tH\u00c6\u0003JD\u0010\u0017\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001\u00a2\u0006\u0002\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\tH\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u0004H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011\u00a8\u0006\u001e"}, d2 = {"Lcom/eskisehir/events/data/remote/dto/RecommendationRequestDto;", "", "preferredCategories", "", "", "preferredTags", "maxPrice", "", "limit", "", "(Ljava/util/List;Ljava/util/List;Ljava/lang/Double;I)V", "getLimit", "()I", "getMaxPrice", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getPreferredCategories", "()Ljava/util/List;", "getPreferredTags", "component1", "component2", "component3", "component4", "copy", "(Ljava/util/List;Ljava/util/List;Ljava/lang/Double;I)Lcom/eskisehir/events/data/remote/dto/RecommendationRequestDto;", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class RecommendationRequestDto {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> preferredCategories = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> preferredTags = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double maxPrice = null;
    private final int limit = 0;
    
    public RecommendationRequestDto(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> preferredCategories, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> preferredTags, @org.jetbrains.annotations.Nullable()
    java.lang.Double maxPrice, int limit) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getPreferredCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getPreferredTags() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getMaxPrice() {
        return null;
    }
    
    public final int getLimit() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.remote.dto.RecommendationRequestDto copy(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> preferredCategories, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> preferredTags, @org.jetbrains.annotations.Nullable()
    java.lang.Double maxPrice, int limit) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}