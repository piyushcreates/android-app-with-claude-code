package com.gutelements.app.ui.format

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.gutelements.app.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun formatTime(context: Context, epochMillis: Long): String =
    DateFormat.getTimeFormat(context).format(Date(epochMillis))

fun LocalDateTime.toEpochMillis(zone: ZoneId = ZoneId.systemDefault()): Long =
    atZone(zone).toInstant().toEpochMilli()

fun Long.toLocalDateTime(zone: ZoneId = ZoneId.systemDefault()): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), zone)

private fun skeleton(pattern: String): DateTimeFormatter {
    val locale = Locale.getDefault()
    return DateTimeFormatter.ofPattern(DateFormat.getBestDateTimePattern(locale, pattern), locale)
}

/** "Tue, Oct 7" */
fun formatShortDate(date: LocalDate): String = skeleton("EEEMMMd").format(date)

/** "Tuesday, October 7" */
fun formatLongDate(date: LocalDate): String = skeleton("EEEEMMMMd").format(date)

/** "October 2026" */
fun formatMonth(date: LocalDate): String = skeleton("MMMMyyyy").format(date)

/** "Today", "Yesterday" or a short date. */
@Composable
fun relativeDay(date: LocalDate, today: LocalDate = LocalDate.now()): String = when (date) {
    today -> stringResource(R.string.today)
    today.minusDays(1) -> stringResource(R.string.yesterday)
    else -> formatShortDate(date)
}

/** "Today, 3:20 PM" style label for a moment. */
@Composable
fun relativeDateTime(epochMillis: Long): String {
    val context = LocalContext.current
    val time = formatTime(context, epochMillis)
    val date = epochMillis.toLocalDateTime().toLocalDate()
    val today = LocalDate.now()
    return when (date) {
        today -> stringResource(R.string.today_at, time)
        today.minusDays(1) -> stringResource(R.string.yesterday_at, time)
        else -> stringResource(R.string.date_at, formatShortDate(date), time)
    }
}
