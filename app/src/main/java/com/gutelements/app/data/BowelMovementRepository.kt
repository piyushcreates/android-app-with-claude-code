package com.gutelements.app.data

import kotlinx.coroutines.flow.Flow

class BowelMovementRepository(
    private val dao: BowelMovementDao,
    private val clock: () -> Long = System::currentTimeMillis,
) {
    /** All entries, newest first. */
    val entries: Flow<List<BowelMovement>> = dao.observeAll()

    suspend fun get(id: Long): BowelMovement? = dao.getById(id)

    suspend fun log(bristolType: Int, timestamp: Long): Long {
        require(bristolType in 1..7)
        val now = clock()
        return dao.insert(
            BowelMovement(timestamp = timestamp, bristolType = bristolType, createdAt = now, updatedAt = now)
        )
    }

    suspend fun update(id: Long, bristolType: Int, timestamp: Long) {
        require(bristolType in 1..7)
        val existing = dao.getById(id) ?: return
        dao.update(existing.copy(bristolType = bristolType, timestamp = timestamp, updatedAt = clock()))
    }

    suspend fun delete(id: Long) = dao.deleteById(id)

    suspend fun deleteAll() = dao.deleteAll()
}
