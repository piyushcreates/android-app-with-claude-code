package com.gutelements.app.ui.history

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MonthCellsTest {
    @Test
    fun padsOctober2026ForMondayStart() {
        // 1 Oct 2026 is a Thursday.
        val cells = monthCells(YearMonth.of(2026, 10), DayOfWeek.MONDAY)
        assertEquals(0, cells.size % 7)
        assertNull(cells[2])
        assertEquals(LocalDate.of(2026, 10, 1), cells[3])
        assertEquals(31, cells.count { it != null })
    }

    @Test
    fun padsForSundayStart() {
        val cells = monthCells(YearMonth.of(2026, 10), DayOfWeek.SUNDAY)
        assertEquals(LocalDate.of(2026, 10, 1), cells[4])
    }
}
