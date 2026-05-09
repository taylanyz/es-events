package com.eskisehir.eventapp.util

import com.google.android.gms.maps.model.LatLng

/**
 * Decodes polyline encoded string from Google Routes API
 */
object PolylineDecoder {
    fun decode(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        var lat = 0
        var lng = 0

        while (index < encoded.length) {
            var result = 0
            var shift = 0
            var b: Int

            do {
                b = encoded[index++].code - 63 - 1
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)

            val dlat = if ((result and 1) > 0) (result shr 1).inv() else (result shr 1)
            lat += dlat

            result = 0
            shift = 0
            do {
                b = encoded[index++].code - 63 - 1
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)

            val dlng = if ((result and 1) > 0) (result shr 1).inv() else (result shr 1)
            lng += dlng

            poly.add(LatLng(lat / 1e5, lng / 1e5))
        }

        return poly
    }
}
