package ru.nsu.brusn.smpltodo.api.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ZonedDataTimeAdapter {
    @ToJson
    fun toJson(value: java.time.ZonedDateTime): String {
        return value.toString()
    }

    @FromJson
    fun fromJson(value: String?): java.time.ZonedDateTime? {
        return java.time.ZonedDateTime.parse(value)
    }
}