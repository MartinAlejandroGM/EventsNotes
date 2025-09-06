package com.andro_sk.eventnotes.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

private fun String.getDifferenceDays() : Long {
    val date = this.getParsedDateOrDefaultDate()
    val currentDate = LocalDate.now()

    return ChronoUnit.DAYS.between(currentDate, date)
}

fun String.getDescriptionDaysText(): String {
    val daysLeft = this.getDifferenceDays()
    return if (daysLeft < 0) "Event Passed" else if(daysLeft == 0L) "its today!!" else "$daysLeft days left"
}

private fun String.getParsedDateOrDefaultDate(formatter: String = "dd/MM/yyyy"): LocalDate {
    val currentFormatter = DateTimeFormatter.ofPattern(formatter)
    return try {
        LocalDate.parse(this, currentFormatter)
    } catch (exception: DateTimeParseException) {
        LocalDate.parse("16/10/1995", currentFormatter)
    }
}