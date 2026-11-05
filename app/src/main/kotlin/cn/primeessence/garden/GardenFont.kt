// Garden Font
package cn.primeessence.garden

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat

val misanHan = FontFamily(
    Font(R.font.mi_san, FontWeight.Normal)
)

fun checkFontHasCodePoint(
    ctx: Context,
    fontResId: Int,
    codePoint: Int
): Boolean {
    val typeface: Typeface? = ResourcesCompat.getFont(ctx, fontResId)
    if(typeface == null) return false
    val paint = Paint().apply {
        this.typeface = typeface
        isAntiAlias = true
    }
    val glyphStr = String(intArrayOf(codePoint), 0, 1)
    return paint.hasGlyph(glyphStr)
}

