package com.zelianko.kitchencalculator.recipt_about_screen

import android.content.Context
import android.content.Intent
import androidx.core.app.ShareCompat
import com.zelianko.kitchencalculator.R

/**
 * Формирует текст рецепта и открывает системный share chooser.
 *
 * Формат:
 *
 *   <Название>
 *
 *   <Ингредиенты:>
 *   • <Имя> — <масса> <ед.>
 *   ...
 *
 *   <Отправлено из Kitchen Calculator>
 */
internal fun shareRecipe(
    context: Context,
    recipeName: String,
    description: String,
    products: List<RecipeAboutViewModel.ProductDto>,
) {
    val text = buildString {
        append(recipeName.trim())
        appendLine()
        if (description.isNotBlank()) {
            appendLine()
            appendLine(description.trim())
        }
        appendLine()
        appendLine(context.getString(R.string.ingredients) + ":")
        products.forEach { p ->
            append("• ")
            append(p.name)
            append(" — ")
            append(formatAmount(p.mass))
            if (p.measureWeight.isNotBlank()) {
                append(' ')
                append(p.measureWeight)
            }
            appendLine()
        }
        appendLine()
        append(context.getString(R.string.shared_via))
    }

    val activity = context.findActivityOrNull() ?: return
    ShareCompat.IntentBuilder(activity)
        .setType("text/plain")
        .setSubject(recipeName)
        .setText(text)
        .setChooserTitle(context.getString(R.string.share_recipe))
        .startChooser()
}

private fun formatAmount(value: Double): String =
    if (value % 1.0 == 0.0) value.toInt().toString()
    else value.toBigDecimal().stripTrailingZeros().toPlainString()

private tailrec fun Context.findActivityOrNull(): android.app.Activity? = when (this) {
    is android.app.Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivityOrNull()
    else -> null
}
