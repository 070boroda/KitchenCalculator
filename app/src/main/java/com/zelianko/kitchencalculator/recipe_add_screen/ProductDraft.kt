package com.zelianko.kitchencalculator.recipe_add_screen

import com.zelianko.kitchencalculator.data.ProductEn
import java.util.UUID

/**
 * Состояние строки ингредиента в редакторе рецепта.
 *
 * uiId стабилен на стороне Compose (нужен для key() в LazyColumn), dbId — id записи в Room,
 * null если строка ещё не сохранялась.
 */
data class ProductDraft(
    val uiId: String = UUID.randomUUID().toString(),
    val dbId: Long? = null,
    val name: String = "",
    val amount: String = "",
    val unit: String = "",
) {
    fun toEntity(recipeId: Long): ProductEn = ProductEn(
        id = dbId,
        name = name,
        mass = amount.toDoubleOrNull() ?: 0.0,
        measureWeight = unit,
        recipeId = recipeId,
    )

    companion object {
        fun fromEntity(entity: ProductEn): ProductDraft = ProductDraft(
            dbId = entity.id,
            name = entity.name,
            amount = if (entity.mass == 0.0) "" else entity.mass.toBigDecimal()
                .stripTrailingZeros().toPlainString(),
            unit = entity.measureWeight,
        )

        fun empty(): ProductDraft = ProductDraft()
    }
}
