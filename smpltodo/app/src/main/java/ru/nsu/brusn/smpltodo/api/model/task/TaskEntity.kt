package ru.nsu.brusn.smpltodo.api.model.task

import java.io.Serializable
import java.time.ZonedDateTime

class TaskEntity (
    val id: Long,
    var name: String,
    var created: ZonedDateTime,
    var startDate: ZonedDateTime?,
    var deadline: ZonedDateTime?,
    var description: String?,
    var important: Boolean,
    var completed: Boolean,
): Serializable