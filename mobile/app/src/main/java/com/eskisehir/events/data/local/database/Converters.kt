package com.eskisehir.events.data.local.database

import androidx.room.TypeConverter
import com.eskisehir.eventapp.data.model.SavedRouteSegment
import com.eskisehir.eventapp.data.model.SavedRouteStop
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromLongList(value: List<Long>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLongList(value: String?): List<Long>? {
        val listType = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromSavedRouteStopList(value: List<SavedRouteStop>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSavedRouteStopList(value: String?): List<SavedRouteStop>? {
        val listType = object : TypeToken<List<SavedRouteStop>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromSavedRouteSegmentList(value: List<SavedRouteSegment>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSavedRouteSegmentList(value: String?): List<SavedRouteSegment>? {
        val listType = object : TypeToken<List<SavedRouteSegment>>() {}.type
        return gson.fromJson(value, listType)
    }
}
