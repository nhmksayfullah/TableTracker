package app.tabletracker.util

import java.util.UUID

fun generateUniqueId(): String = UUID.randomUUID().toString()
    .split("-").last().reversed().uppercase()