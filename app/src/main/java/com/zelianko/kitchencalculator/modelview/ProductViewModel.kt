package com.zelianko.kitchencalculator.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.model.Products

private const val GRAM_EU = "Gram"
private const val GRAM_RU = "Грамм"

private const val TABLE_SPOON_EU = "Table spoon"
private const val TABLE_SPOON_RU = "Столовая ложка"

private const val TEA_SPOON_EU = "Tea spoon"
private const val TEA_SPOON_RU = "Чайная ложка"

private const val FACETED_GLASS_EU = "Faceted glass"
private const val FACETED_GLASS_RU = "Граненый стакан"

private const val TEA_GLASS_EU = "Tea glass"
private const val TEA_GLASS_RU = "Стакан чайный"
private const val OZ_VAL = 28.3495

class ProductViewModel : ViewModel() {

    private val _mapProduct = MutableLiveData(
        hashMapOf(
            "Solt" to Products(R.string.solt, "Solt", 320, 220, 30, 10),
            "Water" to Products(R.string.water, "Water", 250, 200, 18, 5),
            "Peanuts cleaned" to Products(R.string.peanuts_cleaned, "Peanuts cleaned", 175, 140, 25, 8),
            "Jam" to Products(R.string.jam, "Jam", 330, 270, 50, 17),
            "Cherry" to Products(R.string.cherry, "Cherry", 190, 150, 30, 5),
            "Split peas" to Products(R.string.split_peas, "Split peas", 230, 205, 25, 5),
            "Dried mushroom" to Products(R.string.dried_mushroom, "Dried mushroom", 100, 80, 10, 4),
            "Gelatin powder" to Products(R.string.gelatin_powder, "Gelatin powder", 0, 0, 15, 5),
            "Raisin" to Products(R.string.raisin, "Raisin", 190, 155, 25, 7),
            "Cocoa powder" to Products(R.string.cocoa_powder, "Cocoa powder", 0, 0, 12, 5),
            "Citric acid" to Products(R.string.citric_acid, "Citric acid", 0, 0, 25, 8),
            "Strawberries Fresh" to Products(R.string.strawberries_fresh, "Strawberries Fresh", 150, 120, 25, 5),
            "Cinnamon" to Products(R.string.cinnamon, "Cinnamon", 0, 0, 20, 8),
            "Ground coffee" to Products(R.string.ground_coffee, "Ground coffee", 0, 0, 20, 7),
            "Starch" to Products(R.string.starch, "starch", 180, 150, 30, 10),
            "Rolled oats" to Products(R.string.rolled_oats, "Rolled oats", 70, 50, 12, 3),
            "Buckwheat" to Products(R.string.buckwheat, "Buckwheat", 210, 165, 25, 7),
            "Semolina" to Products(R.string.semolina, "Semolina", 200, 160, 25, 8),
            "Barley" to Products(R.string.barley, "Barley", 230, 180, 25, 8),
            "Millet" to Products(R.string.millet, "Millet", 220, 170, 25, 8),
            "Rice" to Products(R.string.rice, "Rice", 240, 180, 25, 8),
            "Barley2" to Products(R.string.barley2, "Barley2", 180, 145, 20, 5),
            "Corn flour" to Products(R.string.corn_flour, "Corn flour", 160, 130, 30, 10),
            "Liqueur" to Products(R.string.liqueur, "Liqueur", 0, 0, 20, 7),
            "Mayonnaise" to Products(R.string.mayonnaise, "Mayonnaise", 250, 210, 25, 10),
            "Poppy" to Products(R.string.poppy, "Poppy", 155, 135, 18, 5),
            "Raspberries" to Products(R.string.raspberries, "Raspberries", 140, 110, 20, 5),
            "Margarine" to Products(R.string.margarine, "Margarine", 230, 180, 15, 4),
            "Butter" to Products(R.string.butter, "Butter", 240, 185, 17, 5),
            "Vegetable oil" to Products(R.string.vegetable_oil, "Vegetable oil", 230, 190, 17, 5),
            "Honey" to Products(R.string.honey, "Honey", 230, 190, 17, 5),
            "Almond" to Products(R.string.almond, "Almond", 160, 130, 30, 10),
            "Condensed milk" to Products(R.string.condensed_milk, "Condensed milk", 300, 250, 30, 12),
            "Milk powder" to Products(R.string.milk_powder, "Milk powder", 120, 100, 20, 5),
            "Flour" to Products(R.string.flour, "Flour", 160, 120, 25, 8),
            "Nut" to Products(R.string.nut, "Nut", 170, 130, 30, 10),
            "Pepper powder" to Products(R.string.pepper_powder, "Pepper powder", 0, 0, 18, 5),
            "Fruit puree" to Products(R.string.fruit_puree, "Fruit puree", 350, 290, 50, 17),
            "Sugar" to Products(R.string.sugar, "Sugar", 200, 180, 25, 8),
            "Powdered sugar" to Products(R.string.powdered_sugar, "Powdered sugar", 180, 140, 25, 10),
            "Cream" to Products(R.string.cream, "Cream", 250, 210, 25, 10),
            "Sour cream" to Products(R.string.sour_cream, "Sour cream", 250, 210, 25, 10),
            "Ground biscuits" to Products(R.string.ground_biscuits, "Ground biscuits", 125, 100, 15, 5),
            "Tomato paste" to Products(R.string.tomato_paste, "Tomato paste", 300, 250, 300, 10),
            "Vinegar" to Products(R.string.vinegar, "Vinegar", 250, 200, 15, 5),
            "Cornflake" to Products(R.string.cornflake, "Cornflake", 50, 40, 7, 2),
            "Tea" to Products(R.string.tea, "Tea", 0, 0, 3, 0),
            "Egg powder" to Products(R.string.egg_powder, "Egg powder", 100, 80, 25, 10),
        )
    )
    val mapProduct: LiveData<HashMap<String, Products>> = _mapProduct

    /**
     * Выбранный продукт
     */
    private val _currentProduct = MutableLiveData<Products>()
    val currentProduct: LiveData<Products> = _currentProduct

    /**
     * Из чего конвертируем
     */
    private val _currentProductFrom = MutableLiveData<String>()
    val currentProductFrom: LiveData<String> = _currentProductFrom

    /**
     * Во что конвертируем
     */
    private val _currentProductTo = MutableLiveData<String>()
    val currentProductTo: LiveData<String> = _currentProductTo

    /**
     * Результат конверсии продукта
     */
    private val _resultCount = MutableLiveData<String>()
    val resultCount: LiveData<String> = _resultCount


    fun setCurrentProductTo(newValue: String) {
        _currentProductTo.value = newValue
    }

    fun setCurrentProductFrom(newValue: String) {
        _currentProductFrom.value = newValue
    }

    fun setCurrentProduct(newValue: Products) {
        _currentProduct.value = newValue
    }

    fun dictionary(item: String): String {
        return when (item) {
            "Соль" -> "Solt"
            "Вода" -> "Water"
            "Арахис очищенный" -> "Peanuts cleaned"
            "Варенье" -> "Jam"
            "Вишня свежая" -> "Cherry"
            "Горох лущеный" -> "Split peas"
            "Грибы сушеные" -> "Dried mushroom"
            "Желатин в порошке" -> "Gelatin powder"
            "Изюм" -> "Raisin"
            "Какао порошок" -> "Cocoa powder"
            "Кислота лимонная" -> "Citric acid"
            "Клубника свежая" -> "Strawberries Fresh"
            "Корица молотая" -> "Cinnamon"
            "Кофе молотый" -> "Ground coffee"
            "Крупа геркулес" -> "Rolled oats"
            "Крупа гречневая" -> "Buckwheat"
            "Крупа манная" -> "Semolina"
            "Крупа перловая" -> "Barley"
            "Крупа пшено" -> "Millet"
            "Крупа рисовая" -> "Rice"
            "Крупа ячневая" -> "Barley2"
            "Кукурузная мука" -> "Corn flour"
            "Ликер" -> "Liqueur"
            "Майонез" -> "Mayonnaise"
            "Мак" -> "Poppy"
            "Малина свежая" -> "Raspberries"
            "Маргарин растопленный" -> "Margarine"
            "Масло сливочное" -> "Butter"
            "Масло растительное" -> "Vegetable oil"
            "Мед" -> "Honey"
            "Миндаль" -> "Almond"
            "Молоко сгущенное" -> "Condensed milk"
            "Молоко сухое" -> "Milk powder"
            "Мука пшеничная" -> "Flour"
            "Орех фундук" -> "Nut"
            "Перец молотый" -> "Pepper powder"
            "Пюре фруктовое" -> "Fruit puree"
            "Сахарный песок" -> "Sugar"
            "Сахарная пудра" -> "Powdered sugar"
            "Сливки" -> "Cream"
            "Сметана" -> "Sour cream"
            "Сухари молотые" -> "Ground biscuits"
            "Томатная паста" -> "Tomato paste"
            "Уксус" -> "Vinegar"
            "Хлопья кукурузные" -> "Cornflake"
            "Чай сухой" -> "Tea"
            "Яичный порошок" -> "Egg powder"
            else -> { // Note the block
                ""
            }
        }
    }

    /**
     * Считаем резудьтат
     */
    fun countProductResult(
        product: Products,
        textQu: String,
        convertFrom: String,
        convertTo: String
    ) {
        var temp = textQu
        var convertFromTemp = convertFrom

        if ((convertFrom == "oz") or (convertFrom == "Унция")) {
            temp = (textQu.toDouble() * OZ_VAL).toString()
            convertFromTemp = GRAM_EU
        }
        val amountFrom = getAmount(convertFromTemp, temp, product)

        var amountTo:Double
        if ((convertTo == "oz") or (convertTo == "Унция")) {
            amountTo = getAmount(GRAM_EU, OZ_VAL.toString(), product)
        } else {
            amountTo = getAmount(convertTo, "1", product)
        }
        val result: Double = amountFrom.div(amountTo)
        _resultCount.postValue(String.format("%.2f", result))
    }
}

/**
 * что во что переводим
 */
private fun getAmount(convertFrom: String, textQu: String, product: Products): Double {
    when (convertFrom) {
        GRAM_EU -> return textQu.toDouble()
        GRAM_RU -> return textQu.toDouble()

        TABLE_SPOON_EU -> return product.tableSpoon * textQu.toDouble()
        TABLE_SPOON_RU -> return product.tableSpoon * textQu.toDouble()

        TEA_SPOON_EU -> return product.teaSpoon * textQu.toDouble()
        TEA_SPOON_RU -> return product.teaSpoon * textQu.toDouble()

        FACETED_GLASS_EU -> return product.facetedGlass * textQu.toDouble()
        FACETED_GLASS_RU -> return product.facetedGlass * textQu.toDouble()

        TEA_GLASS_EU -> return product.teaGlass * textQu.toDouble()
        TEA_GLASS_RU -> return product.teaGlass * textQu.toDouble()
        else -> {
            return Double.MAX_VALUE
        }
    }
}