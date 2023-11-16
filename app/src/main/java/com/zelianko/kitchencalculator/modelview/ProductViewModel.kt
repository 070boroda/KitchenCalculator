package com.zelianko.kitchencalculator.modelview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.model.Products

class ProductViewModel : ViewModel() {

    private val _mapProduct = MutableLiveData(
        hashMapOf(
            "Solt" to Products(R.string.solt, "Solt",320, 220, 30, 10),
            "Water" to Products(R.string.water, "Water",250, 200, 18, 5)
        )
    )
    val mapProduct: LiveData<HashMap<String, Products>> = _mapProduct

    private val _currentProduct = MutableLiveData<Products>()
    val currentProduct: LiveData<Products> = _currentProduct

    fun setCurrentProduct (newValue: Products) {
        _currentProduct.value = newValue
    }

    fun dictionary(item: String): String {
        var result = ""
        when (item) {
            "Соль" -> result = "Solt"
            "Вода" -> result = "Water"
            else -> { // Note the block
                result = ""
            }
        }
        return result;
    }


}