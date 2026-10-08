package com.gutelements.app.ui

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

/** Emits the local date and re-emits when it changes, so "today" stays correct past midnight. */
fun currentDateFlow(): Flow<LocalDate> = flow {
    while (true) {
        emit(LocalDate.now())
        delay(60_000)
    }
}.distinctUntilChanged()
