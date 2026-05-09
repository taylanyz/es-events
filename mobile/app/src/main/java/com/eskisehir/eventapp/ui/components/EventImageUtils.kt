package com.eskisehir.eventapp.ui.components

import androidx.compose.ui.graphics.Color
import com.eskisehir.eventapp.data.model.Category

/**
 * Utility object providing category-based fallback image URLs and accent colors.
 * Every event will always have a visual — never a blank card.
 */
object EventImageUtils {

    /** Returns a high-quality Unsplash image URL for the given category. */
    fun getCategoryImageUrl(category: Category): String = when (category) {
        Category.CONCERT      -> "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=1200&auto=format&fit=crop"
        Category.THEATER      -> "https://images.unsplash.com/photo-1507924538820-ede94a04019d?q=80&w=1200&auto=format&fit=crop"
        Category.EXHIBITION   -> "https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?q=80&w=1200&auto=format&fit=crop"
        Category.FESTIVAL     -> "https://images.unsplash.com/photo-1534067783941-51c9c23ecefd?q=80&w=1200&auto=format&fit=crop"
        Category.WORKSHOP     -> "https://images.unsplash.com/photo-1565191999001-551c187427bb?q=80&w=1200&auto=format&fit=crop"
        Category.SPORTS       -> "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?q=80&w=1200&auto=format&fit=crop"
        Category.STAND_UP      -> "https://images.unsplash.com/photo-1585699324551-f6c309eedeca?q=80&w=1200&auto=format&fit=crop"
        Category.CINEMA       -> "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=1200&auto=format&fit=crop"
        Category.CONFERENCE   -> "https://images.unsplash.com/photo-1540575467063-178a50c2df87?q=80&w=1200&auto=format&fit=crop"
        Category.MUSEUM       -> "https://images.unsplash.com/photo-1554907984-15263bfd63bd?q=80&w=1200&auto=format&fit=crop"
        Category.PARK         -> "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?q=80&w=1200&auto=format&fit=crop"
        Category.WALKING_ROUTE -> "https://images.unsplash.com/photo-1506126613408-eca07ce68773?q=80&w=1200&auto=format&fit=crop"
        Category.FOOD         -> "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?q=80&w=1200&auto=format&fit=crop"
        Category.UNIVERSITY   -> "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=1200&auto=format&fit=crop"
        Category.CULTURE      -> "https://images.unsplash.com/photo-1542038784456-1ea8e935640e?q=80&w=1200&auto=format&fit=crop"
        Category.FAMILY       -> "https://images.unsplash.com/photo-1472586662442-3eec04b9dbda?q=80&w=1200&auto=format&fit=crop"
        Category.TECHNOLOGY   -> "https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=1200&auto=format&fit=crop"
        Category.OUTDOOR      -> "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?q=80&w=1200&auto=format&fit=crop"
        Category.OTHER        -> "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?q=80&w=1200&auto=format&fit=crop"
    }

    /** Returns a branded accent color per category for placeholders and tags. */
    fun getCategoryColor(category: Category): Color = when (category) {
        Category.CONCERT      -> Color(0xFFE91E63) // Pink
        Category.THEATER      -> Color(0xFF673AB7) // Deep Purple
        Category.EXHIBITION   -> Color(0xFFFF9800) // Orange
        Category.FESTIVAL     -> Color(0xFFF44336) // Red
        Category.WORKSHOP     -> Color(0xFF4CAF50) // Green
        Category.SPORTS       -> Color(0xFF2196F3) // Blue
        Category.STAND_UP      -> Color(0xFF9C27B0) // Purple
        Category.CINEMA       -> Color(0xFF607D8B) // Blue Grey
        Category.CONFERENCE   -> Color(0xFF795548) // Brown
        Category.MUSEUM       -> Color(0xFF009688) // Teal
        Category.PARK         -> Color(0xFF8BC34A) // Light Green
        Category.WALKING_ROUTE -> Color(0xFFCDDC39) // Lime
        Category.FOOD         -> Color(0xFFFF5722) // Deep Orange
        Category.UNIVERSITY   -> Color(0xFF3F51B5) // Indigo
        Category.CULTURE      -> Color(0xFFFFC107) // Amber
        Category.FAMILY       -> Color(0xFF03A9F4) // Light Blue
        Category.TECHNOLOGY   -> Color(0xFF00BCD4) // Cyan
        Category.OUTDOOR      -> Color(0xFF4CAF50) // Green
        Category.OTHER        -> Color(0xFF9E9E9E) // Grey
    }

    /** Helper to get image or fallback. */
    fun getEffectiveImageUrl(imageUrl: String?, category: Category): String {
        return if (imageUrl.isNullOrBlank()) getCategoryImageUrl(category) else imageUrl
    }
}
