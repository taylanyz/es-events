package com.eskisehir.eventapp.data.model;

/**
 * Event data class matching the backend EventResponse DTO.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b>\b\u0086\b\u0018\u00002\u00020\u0001B\u00f5\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u0005\u0012\u0006\u0010\u000f\u001a\u00020\n\u0012\b\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u0012\u000e\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0014\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0014\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0014\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u0014\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u0014\u0012\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010 \u001a\u00020!\u00a2\u0006\u0002\u0010\"J\t\u0010A\u001a\u00020\u0003H\u00c6\u0003J\t\u0010B\u001a\u00020\nH\u00c6\u0003J\u000b\u0010C\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u0011\u0010D\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0012H\u00c6\u0003J\t\u0010E\u001a\u00020\u0014H\u00c6\u0003J\t\u0010F\u001a\u00020\u0005H\u00c6\u0003J\t\u0010G\u001a\u00020\u0005H\u00c6\u0003J\t\u0010H\u001a\u00020\u0005H\u00c6\u0003J\t\u0010I\u001a\u00020\u0005H\u00c6\u0003J\t\u0010J\u001a\u00020\u0005H\u00c6\u0003J\t\u0010K\u001a\u00020\u0014H\u00c6\u0003J\t\u0010L\u001a\u00020\u0005H\u00c6\u0003J\t\u0010M\u001a\u00020\u0014H\u00c6\u0003J\t\u0010N\u001a\u00020\u0014H\u00c6\u0003J\t\u0010O\u001a\u00020\u0014H\u00c6\u0003J\t\u0010P\u001a\u00020\u0014H\u00c6\u0003J\u000b\u0010Q\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010R\u001a\u00020!H\u00c6\u0003J\t\u0010S\u001a\u00020\u0005H\u00c6\u0003J\t\u0010T\u001a\u00020\bH\u00c6\u0003J\t\u0010U\u001a\u00020\nH\u00c6\u0003J\t\u0010V\u001a\u00020\nH\u00c6\u0003J\t\u0010W\u001a\u00020\u0005H\u00c6\u0003J\t\u0010X\u001a\u00020\u0005H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0005H\u00c6\u0003J\u008f\u0002\u0010Z\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\n2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0010\b\u0002\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010\u0017\u001a\u00020\u00052\b\b\u0002\u0010\u0018\u001a\u00020\u00052\b\b\u0002\u0010\u0019\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\u00142\b\b\u0002\u0010\u001b\u001a\u00020\u00142\b\b\u0002\u0010\u001c\u001a\u00020\u00142\b\b\u0002\u0010\u001d\u001a\u00020\u00142\b\b\u0002\u0010\u001e\u001a\u00020\u00142\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010 \u001a\u00020!H\u00c6\u0001J\u0013\u0010[\u001a\u00020\u00142\b\u0010\\\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010]\u001a\u00020!H\u00d6\u0001J\t\u0010^\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0018\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010$R\u0011\u0010\u001d\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010$R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010$R\u0011\u0010\u0016\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010$R\u0011\u0010 \u001a\u00020!\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0011\u0010\u0015\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010$R\u0011\u0010\u0017\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010$R\u0011\u0010\u001e\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\'R\u0011\u0010\u001b\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\'R\u0011\u0010\u001c\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\'R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u00105R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010$R\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\'R\u0011\u0010\u001a\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\'R\u0013\u0010\u001f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010$R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u00109R\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u00109R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010$R\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u00109R\u0011\u0010\u0019\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010$R\u0019\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010?R\u0011\u0010\f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010$\u00a8\u0006_"}, d2 = {"Lcom/eskisehir/eventapp/data/model/Event;", "", "id", "", "name", "", "description", "category", "Lcom/eskisehir/eventapp/data/model/Category;", "latitude", "", "longitude", "venue", "address", "date", "price", "imageUrl", "tags", "", "isFeatured", "", "environmentType", "difficultyLevel", "groupSizeType", "activityLevel", "socialAspect", "isWheelchairAccessible", "hasParking", "hasPublicTransport", "allowsPhotography", "hasFoodDrink", "language", "duration", "", "(JLjava/lang/String;Ljava/lang/String;Lcom/eskisehir/eventapp/data/model/Category;DDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;DLjava/lang/String;Ljava/util/List;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZZLjava/lang/String;I)V", "getActivityLevel", "()Ljava/lang/String;", "getAddress", "getAllowsPhotography", "()Z", "getCategory", "()Lcom/eskisehir/eventapp/data/model/Category;", "getDate", "getDescription", "getDifficultyLevel", "getDuration", "()I", "getEnvironmentType", "getGroupSizeType", "getHasFoodDrink", "getHasParking", "getHasPublicTransport", "getId", "()J", "getImageUrl", "getLanguage", "getLatitude", "()D", "getLongitude", "getName", "getPrice", "getSocialAspect", "getTags", "()Ljava/util/List;", "getVenue", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class Event {
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String description = null;
    @org.jetbrains.annotations.NotNull()
    private final com.eskisehir.eventapp.data.model.Category category = null;
    private final double latitude = 0.0;
    private final double longitude = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String venue = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String address = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String date = null;
    private final double price = 0.0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String imageUrl = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<java.lang.String> tags = null;
    private final boolean isFeatured = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String environmentType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String difficultyLevel = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String groupSizeType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String activityLevel = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String socialAspect = null;
    private final boolean isWheelchairAccessible = false;
    private final boolean hasParking = false;
    private final boolean hasPublicTransport = false;
    private final boolean allowsPhotography = false;
    private final boolean hasFoodDrink = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String language = null;
    private final int duration = 0;
    
    public Event(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    com.eskisehir.eventapp.data.model.Category category, double latitude, double longitude, @org.jetbrains.annotations.NotNull()
    java.lang.String venue, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String date, double price, @org.jetbrains.annotations.Nullable()
    java.lang.String imageUrl, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.String> tags, boolean isFeatured, @org.jetbrains.annotations.NotNull()
    java.lang.String environmentType, @org.jetbrains.annotations.NotNull()
    java.lang.String difficultyLevel, @org.jetbrains.annotations.NotNull()
    java.lang.String groupSizeType, @org.jetbrains.annotations.NotNull()
    java.lang.String activityLevel, @org.jetbrains.annotations.NotNull()
    java.lang.String socialAspect, boolean isWheelchairAccessible, boolean hasParking, boolean hasPublicTransport, boolean allowsPhotography, boolean hasFoodDrink, @org.jetbrains.annotations.Nullable()
    java.lang.String language, int duration) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.eventapp.data.model.Category getCategory() {
        return null;
    }
    
    public final double getLatitude() {
        return 0.0;
    }
    
    public final double getLongitude() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVenue() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAddress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDate() {
        return null;
    }
    
    public final double getPrice() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getImageUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.String> getTags() {
        return null;
    }
    
    public final boolean isFeatured() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEnvironmentType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDifficultyLevel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGroupSizeType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getActivityLevel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSocialAspect() {
        return null;
    }
    
    public final boolean isWheelchairAccessible() {
        return false;
    }
    
    public final boolean getHasParking() {
        return false;
    }
    
    public final boolean getHasPublicTransport() {
        return false;
    }
    
    public final boolean getAllowsPhotography() {
        return false;
    }
    
    public final boolean getHasFoodDrink() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLanguage() {
        return null;
    }
    
    public final int getDuration() {
        return 0;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final double component10() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.String> component12() {
        return null;
    }
    
    public final boolean component13() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component18() {
        return null;
    }
    
    public final boolean component19() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component20() {
        return false;
    }
    
    public final boolean component21() {
        return false;
    }
    
    public final boolean component22() {
        return false;
    }
    
    public final boolean component23() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component24() {
        return null;
    }
    
    public final int component25() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.eventapp.data.model.Category component4() {
        return null;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.eventapp.data.model.Event copy(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    com.eskisehir.eventapp.data.model.Category category, double latitude, double longitude, @org.jetbrains.annotations.NotNull()
    java.lang.String venue, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String date, double price, @org.jetbrains.annotations.Nullable()
    java.lang.String imageUrl, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.String> tags, boolean isFeatured, @org.jetbrains.annotations.NotNull()
    java.lang.String environmentType, @org.jetbrains.annotations.NotNull()
    java.lang.String difficultyLevel, @org.jetbrains.annotations.NotNull()
    java.lang.String groupSizeType, @org.jetbrains.annotations.NotNull()
    java.lang.String activityLevel, @org.jetbrains.annotations.NotNull()
    java.lang.String socialAspect, boolean isWheelchairAccessible, boolean hasParking, boolean hasPublicTransport, boolean allowsPhotography, boolean hasFoodDrink, @org.jetbrains.annotations.Nullable()
    java.lang.String language, int duration) {
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