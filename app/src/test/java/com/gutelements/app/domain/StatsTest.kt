package com.gutelements.app.domain

import com.gutelements.app.data.BowelMovement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class StatsTest {
    private val zone = ZoneId.of("Europe/London")
    private val today = LocalDate.of(2026, 10, 7)

    private fun entry(type: Int, date: LocalDate, time: LocalTime = LocalTime.NOON): BowelMovement {
        val ts = date.atTime(time).atZone(zone).toInstant().toEpochMilli()
        return BowelMovement(timestamp = ts, bristolType = type, createdAt = ts, updatedAt = ts)
    }

    @Test
    fun emptyPeriodHasNoPercentOrMostCommon() {
        val stats = computeStats(emptyList(), 7)
        assertEquals(0, stats.total)
        assertNull(stats.type34Percent)
        assertTrue(stats.mostCommon.isEmpty())
        assertEquals(List(7) { 0 }, stats.distribution)
    }

    @Test
    fun computesDistributionShareAndAverage() {
        val entries = listOf(4, 4, 4, 3, 2, 5, 1).map { entry(it, today) }
        val stats = computeStats(entries, 7)
        assertEquals(7, stats.total)
        assertEquals(1.0, stats.averagePerDay, 0.0001)
        assertEquals(57, stats.type34Percent) // 4 of 7
        assertEquals(listOf(4), stats.mostCommon)
        assertEquals(listOf(1, 1, 1, 3, 1, 0, 0), stats.distribution)
    }

    @Test
    fun reportsAllTiedMostCommonTypes() {
        val stats = computeStats(listOf(3, 4, 3, 4).map { entry(it, today) }, 7)
        assertEquals(listOf(3, 4), stats.mostCommon)
        assertEquals(100, stats.type34Percent)
    }

    @Test
    fun lastDaysIncludesTodayAndExcludesOlderAndFutureDays() {
        val entries = listOf(
            entry(4, today, LocalTime.of(0, 1)),
            entry(4, today.minusDays(6), LocalTime.of(23, 59)),
            entry(4, today.minusDays(7)),
            entry(4, today.plusDays(1)),
        )
        assertEquals(2, entries.inLastDays(7, today, zone).size)
        assertEquals(1, entries.inLastDays(1, today, zone).size)
    }
}
