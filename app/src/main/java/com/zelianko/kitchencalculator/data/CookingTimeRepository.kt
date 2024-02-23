package com.zelianko.kitchencalculator.data

import kotlinx.coroutines.flow.Flow

interface CookingTimeRepository {
    suspend fun insertCookingTime(item: CookingTime): Long

    suspend fun deleteCookingTime(item: CookingTime)

    fun getAllItems(): Flow<List<CookingTime>>

    suspend fun getCookingTimeById(id: Long): CookingTime

    fun getAllCookingTimeByName(name: String): Flow<List<CookingTime>>

    fun getAllCookingTimeByGroup(groupNumber: Int): Flow<List<CookingTime>>

}