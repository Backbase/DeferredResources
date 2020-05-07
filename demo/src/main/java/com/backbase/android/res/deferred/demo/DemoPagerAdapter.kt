package com.backbase.android.res.deferred.demo

import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.backbase.android.res.deferred.DeferredColor
import com.backbase.android.res.deferred.DeferredFormattedPlurals
import com.backbase.android.res.deferred.DeferredText

class DemoPagerAdapter : RecyclerView.Adapter<DemoPagerAdapter.DeferredResourceViewHolder>() {

    private val formattedPluralsResource = DeferredFormattedPlurals.Resource(R.plurals.horses)

    fun getPageName(position: Int): CharSequence = when (position) {
        0 -> "Colors"
        1 -> "Plurals resource"
        else -> throw IllegalArgumentException("Position $position in adapter with size $itemCount")
    }

    override fun getItemCount(): Int = 2

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> ViewType.COLORS.ordinal
        1 -> ViewType.PLURALS.ordinal
        else -> throw IndexOutOfBoundsException("Position $position in adapter with size $itemCount")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeferredResourceViewHolder {
        val view = when (ViewType.values()[viewType]) {
            ViewType.COLORS -> DeferredColorsView(parent.context)
            ViewType.PLURALS -> DeferredPluralsView(parent.context)
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
                    DeferredColor.Constant(Color.parseColor("#00A1E0")),
                    DeferredText.Constant("#00A1E0")
                )
            }
            is DeferredPluralsView -> when (position) {
                1 -> view.display(formattedPluralsResource)
            }
        }
    }

    class DeferredResourceViewHolder(
        val root: DeferredResourceView
    ) : RecyclerView.ViewHolder(root)

    private enum class ViewType {
        COLORS, PLURALS
    }
}
