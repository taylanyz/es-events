package com.eskisehir.events.util

import com.google.android.gms.maps.model.LatLng

/**
 * Decodes Google's polyline encoding algorithm (used in Routes API responses)
 * Reference: https://developers.google.com/maps/documentation/utilities/polylinealgorithm
 */
object PolylineDecoder {

    /**
     * Decode an encoded polyline string to a list of LatLng points
     * @param encoded The encoded polyline string from Google Routes API
     * @return List of LatLng points representing the polyline
     */
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
                b = encoded[index].code - 63 - 1
                index++
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)

            val dlat = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            result = 0
            shift = 0

            do {
                b = encoded[index].code - 63 - 1
                index++
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)

            val dlng = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            poly.add(LatLng(lat / 1e5, lng / 1e5))
        }

        return poly
    }
}
