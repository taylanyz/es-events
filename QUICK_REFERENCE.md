# Maps & Roadmap Implementation - Quick Reference

## File Locations & Purposes

### Configuration Files
```
C:\Projects\es-events\local.properties
├── GOOGLE_MAPS_API_KEY=AIzaSyAAKqWhumVPP4tqShTpOVUkiuD4gawLMdc
```

### Dependency Injection (DI Layer)
```
app/src/main/java/com/eskisehir/events/di/
├── NetworkModule.kt (MODIFIED)
│   └── Provides: RoutesOkHttpClient, RoutesRetrofit, RoutesApiService
└── AppModule.kt (MODIFIED)
    └── Provides: FusedLocationProviderClient
```

### Data Layer - Remote (APIs)
```
app/src/main/java/com/eskisehir/events/data/remote/maps/
├── RoutesApiService.kt (NEW)
│   └── POST directions/v2:computeRoutes
├── dto/
│   ├── RoutesRequestDto.kt (NEW) - origin, destination, travelMode
│   └── RoutesResponseDto.kt (NEW) - routes, error
```

### Data Layer - Local (Database)
```
app/src/main/java/com/eskisehir/events/data/local/
├── entity/
│   └── RoadmapStopEntity.kt (NEW)
│       └── Table: roadmap_stops
├── dao/
│   └── RoadmapStopDao.kt (NEW)
│       └── CRUD + getAllStopsOnce() + getAllStops() Flow
└── database/
    └── AppDatabase.kt (MODIFIED)
        └── Version 2→3, added RoadmapStopEntity
```

### Data Layer - Repositories
```
app/src/main/java/com/eskisehir/events/data/repository/
├── MapsRepository.kt (NEW)
│   └── computeRoute() - calls Routes API
└── RoadmapRepository.kt (NEW)
    ├── getAllStops() Flow
    ├── addStop() / removeStop()
    ├── reorderStops()
    └── isEventInRoadmap() / clearRoadmap()
```

### Presentation Layer - ViewModels
```
app/src/main/java/com/eskisehir/events/presentation/viewmodel/
├── MapsViewModel.kt (NEW)
│   ├── State: MapsUiState (userLocation, driveRoute, walkRoute, transitRoute, permission)
│   ├── calculateRoute(lat, lng, lat, lng, mode)
│   ├── calculateAllRoutes(lat, lng, lat, lng)
│   └── getUserLocation()
└── RoadmapViewModel.kt (NEW)
    ├── State: RoadmapUiState (stops, segmentRoutes, totals)
    ├── addStop(...) / removeStop(eventId)
    ├── reorderStops(list) / clearRoadmap()
    └── isEventInRoadmap(eventId, callback)
```

### Presentation Layer - UI Components
```
app/src/main/java/com/eskisehir/events/presentation/components/
├── EventLocationMapCard.kt (NEW) - Event on map with details
├── RouteInfoCard.kt (NEW) - Duration/distance for one mode
├── TravelModeSelector.kt (NEW) - DRIVE/WALK/TRANSIT buttons
├── RoadmapMapCard.kt (NEW) - All stops + polylines on map
├── RoadmapStopCard.kt (NEW) - Single stop in list (draggable)
├── RoadmapSegmentCard.kt (NEW) - Route between two stops
└── LocationPermissionInfo.kt (NEW) - Permission status + request button
```

### Presentation Layer - Screens
```
app/src/main/java/com/eskisehir/events/presentation/screens/
├── EventDetailScreen.kt (NEW)
│   ├── Shows event location on map
│   ├── Requests location permission
│   ├── Calculates routes (DRIVE/WALK/TRANSIT)
│   ├── FAB to Add/Remove from Roadmap
│   └── Injected: MapsViewModel, RoadmapViewModel
└── RoadmapScreen.kt (NEW)
    ├── Map with all stops + polylines
    ├── Summary: totals + stop count
    ├── Stop list with remove buttons
    ├── Segment routes between stops
    └── Injected: RoadmapViewModel
```

### Navigation
```
app/src/main/java/com/eskisehir/events/presentation/navigation/
└── Screen.kt (NEW)
    ├── Home, EventDetail, Profile, Roadmap, Settings
    └── EventDetail takes eventId parameter
```

### Utilities
```
app/src/main/java/com/eskisehir/events/util/
├── PolylineDecoder.kt (NEW) - decode(String) → List<LatLng>
└── LocationUtils.kt (NEW)
    ├── formatDuration(seconds) → "1 sa 30 dk"
    ├── formatDistance(meters) → "4.3 km"
    ├── getTravelModeIcon(mode) → ImageVector
    └── getTravelModeLabel(mode) → String (Turkish)
```

---

## State Management Structure

### MapsUiState
```kotlin
data class MapsUiState(
    val userLocation: LatLng? = null,
    val driveRoute: RouteUiState = RouteUiState.Idle,
    val walkRoute: RouteUiState = RouteUiState.Idle,
    val transitRoute: RouteUiState = RouteUiState.Idle,
    val locationPermissionGranted: Boolean = false,
    val isLoadingLocation: Boolean = false,
    val locationError: String? = null
)

sealed class RouteUiState {
    object Idle : RouteUiState()
    object Loading : RouteUiState()
    data class Success(val durationSeconds: Long, val distanceMeters: Long, val encodedPolyline: String) : RouteUiState()
    data class Error(val message: String) : RouteUiState()
}
```

### RoadmapUiState
```kotlin
data class RoadmapUiState(
    val stops: List<RoadmapStopEntity> = emptyList(),
    val segmentRoutes: Map<String, RoadmapSegmentRoute> = emptyMap(),
    val totalDurationSeconds: Long = 0,
    val totalDistanceMeters: Long = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class RoadmapSegmentRoute(
    val fromIndex: Int, val toIndex: Int,
    val durationSeconds: Long = 0,
    val distanceMeters: Long = 0,
    val encodedPolyline: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)
```

---

## Key Integration Points

### 1. AppNavigation.kt
**TODO**: Update to include EventDetailScreen and RoadmapScreen routes

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.EventDetail.route) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toLongOrNull() ?: return@composable
            EventDetailScreen(eventId = eventId, onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Roadmap.route) { RoadmapScreen(onBackClick = { navController.popBackStack() }) }
    }
}
```

### 2. Event Model Extension
**Note**: Event model already has latitude, longitude, venue, address fields ✅

### 3. Permission Handling
Event detail screen handles permission requests via `rememberLauncherForActivityResult()`

---

## API Integration

### Google Routes API v2
- **Endpoint**: `https://routes.googleapis.com/directions/v2:computeRoutes`
- **Headers**:
  - `X-Goog-Api-Key`: API key (from BuildConfig)
  - `X-Goog-FieldMask`: `routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline`
- **Travel Modes**: DRIVE, WALK, TRANSIT
- **Request Format**: origin (LatLng), destination (LatLng), travelMode
- **Response**: routes list with duration (string "3600s"), distanceMeters, polyline

### Google Maps Android
- **Map Display**: GoogleMap Compose component
- **Markers**: Single or multiple with info windows
- **Polylines**: Decoded from Routes API encodedPolyline
- **Camera Control**: Auto-fit to bounds or manual positioning

### Location Services
- **API**: FusedLocationProviderClient
- **Methods**: lastLocation (async), locationUpdates (stream)
- **Permissions**: ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
- **Fallback**: FusedLocationProviderClient.lastLocation returns null if no permission

---

## Gradle Dependencies (Already Added)

```gradle
// Google Maps
implementation("com.google.maps.android:maps-compose:4.3.0")
implementation("com.google.android.gms:play-services-maps:18.2.0")

// Location
implementation("com.google.android.gms:play-services-location:21.0.1")

// (Others: Retrofit, Room, Compose, Hilt already present)
```

---

## Database Schema

### RoadmapStopEntity
```sql
CREATE TABLE roadmap_stops (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    eventId INTEGER NOT NULL,
    title TEXT NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    locationName TEXT NOT NULL,
    address TEXT NOT NULL,
    stopOrder INTEGER NOT NULL
);
```

---

## Testing Checklist

- [ ] Permission request/deny flows work
- [ ] Route calculation succeeds for all three modes
- [ ] Polylines render correctly on map
- [ ] Add/Remove from roadmap updates state
- [ ] Roadmap persists across app restarts
- [ ] No crashes on API errors
- [ ] Totals calculated correctly (duration + distance)
- [ ] Empty state displays when no stops
- [ ] Google Maps app deep linking works
- [ ] API key validation (reject missing/invalid keys)

---

## Common Commands

### Rebuild Project
```bash
cd C:\Projects\es-events\mobile
./gradlew clean build
```

### Run Tests
```bash
./gradlew test
```

### Run on Emulator
```bash
./gradlew installDebug
```

---

## Environment Setup

### local.properties
```properties
sdk.dir=C:\Users\tako0\AppData\Local\Android\Sdk
GOOGLE_MAPS_API_KEY=AIzaSyAAKqWhumVPP4tqShTpOVUkiuD4gawLMdc
```

### AndroidManifest.xml Permissions (Already Configured)
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="${GOOGLE_MAPS_API_KEY}" />
```

---

## Key Design Decisions

1. **Separate OkHttpClient for Routes API**
   - Independent timeout configuration
   - Isolated error handling

2. **Reactive State Management**
   - StateFlow for UI state
   - Coroutines for async operations
   - Sealed classes for type-safe states

3. **Component Composition**
   - Small, reusable Compose functions
   - Single responsibility per component
   - Material Design 3 compliance

4. **Database Normalization**
   - RoadmapStopEntity for persistence
   - Implicit relationship via eventId
   - Order tracked via stopOrder field

5. **Error Handling**
   - Result<T> for API calls
   - UiState includes error messages
   - Graceful degradation on permission denial

---

## Next Steps

1. Integrate screens into AppNavigation.kt
2. Add event detail navigation from event cards
3. Test full end-to-end flows
4. Optimize route calculation performance
5. Add drag-to-reorder functionality for roadmap stops
6. Consider caching for API responses

