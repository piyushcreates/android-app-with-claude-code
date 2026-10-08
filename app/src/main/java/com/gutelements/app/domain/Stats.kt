package com.gutelements.app.domain

import com.gutelements.app.data.BowelMovement
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToInt

/** Number of whole days (including today) an insights period covers. */
enum class Period(val days: Int) { WEEK(7), MONTH(30), THREE_MONTHS(90) }

data class PeriodStats(
    val total: Int,
    val averagePerDay: Double,
    /** Share of Type 3–4 logs, 0–100, or null when there are no logs. */
    val type34Percent: Int?,
    /** Most frequently logged types; several when tied, empty when there are no logs. */
    val mostCommon: List<Int>,
    /** Count per type; index 0 is Type 1. */
    val distribution: List<Int>,
)

fun BowelMovement.localDate(zone: ZoneId): LocalDate =
    Instant.ofEpochMilli(timestamp).atZone(zone).toLocalDate()

/** Entries whose local date falls in the [days] days ending on [today]. */
fun List<BowelMovement>.inLastDays(days: Int, today: LocalDate, zone: ZoneId): List<BowelMovement> {
    val first = today.minusDays(days - 1L)
    return filter { val d = it.localDate(zone); !d.isBefore(first) && !d.isAfter(today) }
}

fun computeStats(entries: List<BowelMovement>, days: Int): PeriodStats {
    val distribution = MutableList(7) { 0 }
    entries.forEach { distribution[it.bristolType - 1]++ }
    val total = entries.size
    val max = distribution.max()
    return PeriodStats(
        total = total,
        averagePerDay = if (days > 0) total.toDouble() / days else 0.0,
        type34Percent = if (total == 0) null else ((distribution[2] + distribution[3]) * 100.0 / total).roundToInt(),
        mostCommon = if (total == 0) emptyList() else distribution.indices.filter { distribution[it] == max }.map { it + 1 },
        distribution = distribution,
    )
}
