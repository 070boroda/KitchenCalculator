package com.zelianko.kitchencalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CookingTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCookingTime(item: CookingTime): Long

    @Delete
    suspend fun deleteCookingTime(item: CookingTime)

    @Query("SELECT * FROM cookingtime")
    fun getAllItems(): Flow<List<CookingTime>>

    @Query("SELECT * FROM cookingtime WHERE cookingtime.id =:id")
    suspend fun getCookingTimeById(id: Long): CookingTime

    @Query("SELECT * FROM cookingtime WHERE cookingtime.productName like :name")
    fun getAllCookingTimeByName(name: String): Flow<List<CookingTime>>

    @Query("SELECT * FROM cookingtime WHERE cookingtime.groupProduct like :groupNumber")
    fun getAllCookingTimeByGroup(groupNumber: Int): Flow<List<CookingTime>>
}