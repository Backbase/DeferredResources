package com.backbase.deferredresources.demo

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.Px
import androidx.core.graphics.ColorUtils
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredPlurals
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.demo.databinding.ColorsBinding
import com.backbase.deferredresources.demo.databinding.DrawableListItemBinding
import com.backbase.deferredresources.demo.databinding.DrawablesBinding
import com.backbase.deferredresources.demo.databinding.PluralsBinding
import com.backbase.deferredresources.viewx.setImageDrawable
import com.backbase.deferredresources.viewx.setText
import com.google.android.material.textview.MaterialTextView
import kotlin.math.roundToInt

sealed class DeferredResourceView(context: Context) : ScrollView(context)

class DeferredColorsView(context: Context) : DeferredResourceView(context) {
    private val binding = ColorsBinding.inflate(LayoutInflater.from(context), this)

    fun display(color: DeferredColor, text: DeferredText) = with(binding.container) {
        val colorView = newColorView().apply {
            val backgroundColor = color.resolve(context)
            setBackgroundColor(backgroundColor)

            val whiteTextContrast = ColorUtils.calculateContrast(Color.WHITE, backgroundColor)
            val blackTextContrast = ColorUtils.calculateContrast(Color.BLACK, backgroundColor)
            val textColor = if (blackTextContrast > whiteTextContrast) Color.BLACK else Color.WHITE
            setTextColor(textColor)

            setText(text)
        }
        addView(colorView)
    }

    private fun newColorView() = MaterialTextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(8.dp)
        }
        gravity = Gravity.CENTER
        if (Build.VERSION.SDK_INT >= 21)
            elevation = 2f.dp
        setPadding(16.dp)
        TextViewCompat.setTextAppearance(this, R.style.TextAppearance_MaterialComponents_Headline4)
    }

    @get:Px
    private val Int.dp: Int
        get() = toFloat().dp.roundToInt()

    @get:Px
    private val Float.dp: Float
        get() = this * context.resources.displayMetrics.density
}

class DeferredPluralsView(context: Context) : DeferredResourceView(context) {
    private val binding = PluralsBinding.inflate(LayoutInflater.from(context), this)

    fun display(plurals: DeferredFormattedPlurals): Unit = with(binding) {
        zero.text = plurals.resolve(context, 0)
        one.text = plurals.resolve(context, 1)
        two.text = plurals.resolve(context, 2)
        few.text = plurals.resolve(context, 3)
        many.text = plurals.resolve(context, 14)
        other.text = plurals.resolve(context, 100)
    }

    fun display(plurals: DeferredPlurals): Unit = with(binding) {
        zero.text = plurals.resolve(context, 0)
        one.text = plurals.resolve(context, 1)
        two.text = plurals.resolve(context, 2)
        few.text = plurals.resolve(context, 3)
        many.text = plurals.resolve(context, 14)
        other.text = plurals.resolve(context, 100)
    }
}

class DeferredDrawablesView(context: Context) : DeferredResourceView(context) {
    private val binding = DrawablesBinding.inflate(LayoutInflater.from(context), this)

    fun display(deferredDrawable: DeferredDrawable) = with(binding.container) {
        val text = when (deferredDrawable) {
            is DeferredDrawable.Constant -> DeferredText.Constant("Constant")
            is DeferredDrawable.Resource -> DeferredText.Constant("Resource")
            is DeferredDrawable.Attribute -> DeferredText.Constant("Attribute")
            else -> DeferredText.Constant(deferredDrawable.javaClass.simpleName)
        }
        with(DrawableListItemBinding.inflate(LayoutInflater.from(context), this, true)) {
            drawableLabel.setText(text)
            drawableView.setImageDrawable(deferredDrawable)
        }
    }
}
