package com.backbase.deferredresources.demo

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredText

class DemoPagerAdapter : RecyclerView.Adapter<DemoPagerAdapter.DeferredResourceViewHolder>() {

    private val formattedPluralsResource = DeferredFormattedPlurals.Resource(R.plurals.horses)
    private val oval by lazy {
        GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(Color.parseColor("#00ff00"), Color.parseColor("#000fff"))
        ).apply {
            gradientType = GradientDrawable.RADIAL_GRADIENT
            cornerRadius = 0.8f
            shape = GradientDrawable.OVAL
        }
    }

    fun getPageName(position: Int): CharSequence = when (position) {
        0 -> "Colors"
        1 -> "Plurals resource"
        2 -> "Drawables"
        else -> throw IllegalArgumentException("Position $position in adapter with size $itemCount")
    }

    override fun getItemCount(): Int = ViewType.values().size

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> ViewType.COLORS.ordinal
        1 -> ViewType.PLURALS.ordinal
        2 -> ViewType.DRAWABLES.ordinal
        else -> throw IndexOutOfBoundsException("Position $position in adapter with size $itemCount")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeferredResourceViewHolder {
        val view = when (ViewType.values()[viewType]) {
            ViewType.COLORS -> DeferredColorsView(parent.context)
            ViewType.PLURALS -> DeferredPluralsView(parent.context)
            ViewType.DRAWABLES -> DeferredDrawablesView(parent.context)
        }.apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return DeferredResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeferredResourceViewHolder, position: Int) {
        when (val view = holder.root) {
            is DeferredColorsView -> {
                view.display(
                    DeferredColor.Attribute(R.attr.colorSecondary),
                    DeferredText.Constant("Secondary")
                )
                view.display(
                    DeferredColor.Resource(R.color.backbase_red),
                    DeferredText.Constant("Backbase red")
                )
                view.display(
                    DeferredColor.Constant(Color.WHITE),
                    DeferredText.Constant("White")
                )
            }
            is DeferredPluralsView -> when (position) {
                1 -> view.display(formattedPluralsResource)
            }
            is DeferredDrawablesView -> {
                view.display(DeferredDrawable.Constant(oval), DeferredText.Constant("Constant: "))
                view.display(DeferredDrawable.Resource(R.drawable.oval), DeferredText.Constant("Resource: "))
                view.display(
                    DeferredDrawable.Attribute(android.R.attr.homeAsUpIndicator),
                    DeferredText.Constant("Attribute: ")
                )
            }
        }
    }

    class DeferredResourceViewHolder(
        val root: DeferredResourceView
    ) : RecyclerView.ViewHolder(root)

    private enum class ViewType {
        COLORS, PLURALS, DRAWABLES
    }
}
