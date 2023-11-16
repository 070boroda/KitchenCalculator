package com.zelianko.kitchencalculator.model

data class Products(
    /**
     * Название продукта
     */
    val keyName: Int,

    val nameEn:String,
    /**
     * Чайная кружка
     * 250мл
     */
    val teaGlass: Int,
    /**
     * Граненный стакан
     * 200мл.
     */
    val facetedGlass: Int,
    /**
     * Столовая ложка
     */
    val tableSpoon: Int,
    /**
     * Чайная ложка
     */
    val teaSpoon:Int

)
