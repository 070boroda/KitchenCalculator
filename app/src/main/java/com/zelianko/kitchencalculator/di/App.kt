package com.zelianko.kitchencalculator.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zelianko.kitchencalculator.data.CookingTimeRepository
import com.zelianko.kitchencalculator.data.CookingTimeRepositoryImpl
import com.zelianko.kitchencalculator.data.MainDb
import com.zelianko.kitchencalculator.data.ProductEnRepository
import com.zelianko.kitchencalculator.data.ProductEnRepositoryImp
import com.zelianko.kitchencalculator.data.RecipeRepository
import com.zelianko.kitchencalculator.data.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object App {

    @Provides
    @Singleton
    fun provideMainDb(app: Application): MainDb {
        return Room.databaseBuilder(app, MainDb::class.java, "recipe_db")
            .createFromAsset("recipe_db.db")
//            .addMigrations(MIGRATION_6_7, MIGRATION_8_9)
            .build()
    }

//    val MIGRATION_6_7 = object : Migration(6, 7) {
//        override fun migrate(db: SupportSQLiteDatabase) {
//            db.execSQL("INSERT INTO cookingtime (groupProduct, productName, boilingTime, fryTime, braiseTime)" +
//                    " VALUES (20, 'Карп', 1.5, 1, 2.5)")
//        }
//    }
//
//    val MIGRATION_8_9 = object : Migration(8, 9) {
//        override fun migrate(db: SupportSQLiteDatabase) {
//            db.execSQL("INSERT INTO cookingtime (groupProduct, productName, boilingTime, fryTime, braiseTime)" +
//                    " VALUES (10, 'Индейка', 1.5, 1, 2.5)")
//        }
//    }

    @Provides
    @Singleton
    fun provideRecipeRepo(db: MainDb): RecipeRepository {
        return RecipeRepositoryImpl(db.recipeDao)
    }

    @Provides
    @Singleton
    fun provideProductEnRepo(db: MainDb): ProductEnRepository {
        return ProductEnRepositoryImp(db.productEnDao)
    }

    @Provides
    @Singleton
    fun provideCokingTimeRepo(db: MainDb): CookingTimeRepository {
        return CookingTimeRepositoryImpl(db.cookingTimeDao)
    }
}