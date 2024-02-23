package com.zelianko.kitchencalculator.data

import kotlinx.coroutines.flow.Flow

class CookingTimeRepositoryImpl(
    private val dao: CookingTimeDao
) : CookingTimeRepository {
    override suspend fun insertCookingTime(item: CookingTime): Long {
        return dao.insertCookingTime(item)
    }

    override suspend fun deleteCookingTime(item: CookingTime) {
        dao.deleteCookingTime(item)
    }

    override fun getAllItems(): Flow<List<CookingTime>> {
        return dao.getAllItems()
    }

    override suspend fun getCookingTimeById(id: Long): CookingTime {
        return dao.getCookingTimeById(id)
    }

    override fun getAllCookingTimeByName(name: String): Flow<List<CookingTime>> {
        return dao.getAllCookingTimeByName(name)
    }

    override fun getAllCookingTimeByGroup(groupNumber: Int): Flow<List<CookingTime>> {
        return dao.getAllCookingTimeByGroup(groupNumber)
    }
}