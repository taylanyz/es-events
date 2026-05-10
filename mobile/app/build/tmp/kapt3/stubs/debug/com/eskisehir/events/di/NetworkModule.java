package com.eskisehir.events.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\b\u001a\u00020\t2\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007J\u0012\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\b\u0010\u0011\u001a\u00020\u0010H\u0007J\u0010\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\u0012\u0010\u0013\u001a\u00020\u00142\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007J\b\u0010\u0015\u001a\u00020\u0010H\u0007J\u0012\u0010\u0016\u001a\u00020\u000b2\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u0007J\u0012\u0010\u0017\u001a\u00020\u00182\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007J\b\u0010\u0019\u001a\u00020\u0010H\u0007J\u0012\u0010\u001a\u001a\u00020\u000b2\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/eskisehir/events/di/NetworkModule;", "", "()V", "BASE_URL", "", "GEMINI_BASE_URL", "ROUTES_BASE_URL", "WEATHER_BASE_URL", "provideEventApiService", "Lcom/eskisehir/events/data/remote/api/EventApiService;", "retrofit", "Lretrofit2/Retrofit;", "provideGeminiApiService", "Lcom/eskisehir/eventapp/data/remote/ai/GeminiApiService;", "provideGeminiRetrofit", "okHttpClient", "Lokhttp3/OkHttpClient;", "provideOkHttpClient", "provideRetrofit", "provideRoutesApiService", "Lcom/eskisehir/events/data/remote/maps/RoutesApiService;", "provideRoutesOkHttpClient", "provideRoutesRetrofit", "provideWeatherApiService", "Lcom/eskisehir/events/data/remote/weather/WeatherApiService;", "provideWeatherOkHttpClient", "provideWeatherRetrofit", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class NetworkModule {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String BASE_URL = "http://10.0.2.2:8081/";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String WEATHER_BASE_URL = "https://api.open-meteo.com/";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ROUTES_BASE_URL = "https://routes.googleapis.com/";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/";
    @org.jetbrains.annotations.NotNull()
    public static final com.eskisehir.events.di.NetworkModule INSTANCE = null;
    
    private NetworkModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient provideOkHttpClient() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @javax.inject.Named(value = "weatherOkHttpClient")
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient provideWeatherOkHttpClient() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @javax.inject.Named(value = "appRetrofit")
    @org.jetbrains.annotations.NotNull()
    public final retrofit2.Retrofit provideRetrofit(@org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @javax.inject.Named(value = "weatherRetrofit")
    @org.jetbrains.annotations.NotNull()
    public final retrofit2.Retrofit provideWeatherRetrofit(@javax.inject.Named(value = "weatherOkHttpClient")
    @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @javax.inject.Named(value = "geminiRetrofit")
    @org.jetbrains.annotations.NotNull()
    public final retrofit2.Retrofit provideGeminiRetrofit(@org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.remote.api.EventApiService provideEventApiService(@javax.inject.Named(value = "appRetrofit")
    @org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit retrofit) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.remote.weather.WeatherApiService provideWeatherApiService(@javax.inject.Named(value = "weatherRetrofit")
    @org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit retrofit) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.eventapp.data.remote.ai.GeminiApiService provideGeminiApiService(@javax.inject.Named(value = "geminiRetrofit")
    @org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit retrofit) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @javax.inject.Named(value = "routesOkHttpClient")
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient provideRoutesOkHttpClient() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @javax.inject.Named(value = "routesRetrofit")
    @org.jetbrains.annotations.NotNull()
    public final retrofit2.Retrofit provideRoutesRetrofit(@javax.inject.Named(value = "routesOkHttpClient")
    @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.remote.maps.RoutesApiService provideRoutesApiService(@javax.inject.Named(value = "routesRetrofit")
    @org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit retrofit) {
        return null;
    }
}