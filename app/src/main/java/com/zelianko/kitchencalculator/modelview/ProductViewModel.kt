package com.zelianko.kitchencalculator.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.model.Products

private const val GRAM_EU = "Gram"
private const val GRAM_RU = "Грамм"
private const val GRAM = 1

private const val TABLE_SPOON_EU = "Table spoon"
private const val TABLE_SPOON_RU = "Столовая ложка"
private const val TABLE_SPOON = 2

private const val TEA_SPOON_EU = "Tea spoon"
private const val TEA_SPOON_RU = "Чайная ложка"
private const val TEA_SPOON = 3

private const val FACETED_GLASS_EU = "Faceted glass"
private const val FACETED_GLASS_RU = "Граненый стакан"
private const val FACETED_GLASS = 4

private const val TEA_GLASS_EU = "Tea glass"
private const val TEA_GLASS_RU = "Стакан чайный"
private const val TEA_GLASS = 5

class ProductViewModel : ViewModel() {

    private val _mapProduct = MutableLiveData(
        hashMapOf(
            "Solt" to Products(R.string.solt, "Solt", 320, 220, 30, 10),
            "Water" to Products(R.string.water, "Water", 250, 200, 18, 5)
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

        val amountFrom = getAmount(convertFrom, textQu, product)
        val amountTo = getAmount(convertTo, "1", product)
        val result: Double = amountFrom.toDouble().div(amountTo.toDouble())
        _resultCount.postValue(String.format("%.2f", result))
    }
}

/**
 * что во что переводим
 */
private fun getAmount(convertFrom: String, textQu: String, product: Products): Int {
    when (convertFrom) {
        GRAM_EU -> return textQu.toInt()
        GRAM_RU -> return textQu.toInt()

        TABLE_SPOON_EU -> return product.tableSpoon * textQu.toInt()
        TABLE_SPOON_RU -> return product.tableSpoon * textQu.toInt()

        TEA_SPOON_EU -> return product.teaSpoon * textQu.toInt()
        TEA_SPOON_RU -> return product.teaSpoon * textQu.toInt()

        FACETED_GLASS_EU -> return product.facetedGlass * textQu.toInt()
        FACETED_GLASS_RU -> return product.facetedGlass * textQu.toInt()

        TEA_GLASS_EU -> return product.teaGlass * textQu.toInt()
        TEA_GLASS_RU -> return product.teaGlass * textQu.toInt()
        else -> {
            return 0
        }
    }
}