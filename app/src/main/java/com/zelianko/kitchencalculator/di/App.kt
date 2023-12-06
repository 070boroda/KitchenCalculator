package com.zelianko.kitchencalculator.di

import android.app.Application
import androidx.room.Room
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
        return Room.databaseBuilder(app, MainDb::class.java, "recipe_db").build()
    }

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
}