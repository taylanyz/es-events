package com.eskisehir.eventapp.ui.screens.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a2\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a&\u0010\u000e\u001a\u00020\u00012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\u001e\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u00a8\u0006\u0012"}, d2 = {"FeaturedEventCard", "", "event", "Lcom/eskisehir/eventapp/data/model/Event;", "onClick", "Lkotlin/Function0;", "HomeHorizontalSection", "title", "", "events", "", "onEventClick", "Lkotlin/Function1;", "", "HomeScreen", "weatherViewModel", "Lcom/eskisehir/eventapp/ui/weather/WeatherViewModel;", "SmallEventCard", "app_debug"})
public final class HomeScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEventClick, @org.jetbrains.annotations.NotNull()
    com.eskisehir.eventapp.ui.weather.WeatherViewModel weatherViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void HomeHorizontalSection(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.util.List<com.eskisehir.eventapp.data.model.Event> events, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEventClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void FeaturedEventCard(@org.jetbrains.annotations.NotNull()
    com.eskisehir.eventapp.data.model.Event event, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SmallEventCard(@org.jetbrains.annotations.NotNull()
    com.eskisehir.eventapp.data.model.Event event, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}