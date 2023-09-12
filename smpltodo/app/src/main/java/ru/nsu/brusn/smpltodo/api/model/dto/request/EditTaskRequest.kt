package ru.nsu.brusn.smpltodo.api.model.dto.request

import com.squareup.moshi.Json
import java.time.ZonedDateTime

data class EditTaskRequest (
    @field:Json(name = "name")
    var name: String? = null,

    @field:Json(name = "created")
    var created: ZonedDateTime? = null,

    @field:Json(name = "startDate")
    var startDate: ZonedDateTime? = null,

    @field:Json(name = "deadline")
    var deadline: ZonedDateTime? = null,

    @field:Json(name = "description")
    var description: String? = null,

    @field:Json(name = "important")
    var important: Boolean? = null,

    @field:Json(name = "completed")
    var completed: Boolean? = null,
)