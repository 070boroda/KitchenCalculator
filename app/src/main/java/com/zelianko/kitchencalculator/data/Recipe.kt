package com.zelianko.kitchencalculator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    val image: ByteArray?
)
