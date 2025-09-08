package com.andro_sk.eventnotes.ui.extension

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

fun convertMillisToDate(millis: Long?): String {
    return millis?.let {
        formatDate(Date(it))
    } ?: throw IllegalArgumentException("Millis cannot be null")
}

fun formatDate(date: Date): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
}

fun getTodaySystemDateInMillis(): Long {
    return LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun fixUtcToLocalMillis(millis: Long?): Long? {
    if (millis == null) return null

    val localDate = java.time.Instant.ofEpochMilli(millis)
        .atZone(ZoneId.of("UTC"))
        .toLocalDate()

    return localDate
        .atStartOfDay(java.time.ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}