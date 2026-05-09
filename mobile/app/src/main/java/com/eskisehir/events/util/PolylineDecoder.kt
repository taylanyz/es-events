package com.eskisehir.events.util

import android.util.Log
import com.google.android.gms.maps.model.LatLng

/**
 * Utility to decode Google Maps encoded polyline strings
 */
object PolylineDecoder {

    /**
     * Decodes an encoded polyline string into a list of LatLng
     */
    fun decode(encoded: String): List<LatLng> {
        if (encoded.isBlank()) return emptyList()
        
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        try {
            while (index < len) {
                var b: Int
                var shift = 0
                var result = 0
                do {
                    b = encoded[index++].code - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lat += dlat

                shift = 0
                result = 0
                do {
                    b = encoded[index++].code - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lng += dlng

                val p = LatLng(
                    lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5
                )
                poly.add(p)
            }
        } catch (e: Exception) {
            Log.e("PolylineDecoder", "Error decoding polyline: ${e.message}")
        }
        return poly
    }
}
