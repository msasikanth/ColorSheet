/*
 *   ColorSheet
 *
 *   Copyright (c) 2019. Sasikanth Miriyampalli
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package dev.sasikanth.colorsheet

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.sasikanth.colorsheet.utils.Theme
import dev.sasikanth.colorsheet.utils.resolveColor
import kotlinx.android.synthetic.main.color_item.view.colorSelected
import kotlinx.android.synthetic.main.color_item.view.colorSelectedCircle
import kotlinx.android.synthetic.main.color_sheet.colorSheetClose
import kotlinx.android.synthetic.main.color_sheet.colorSheetList

typealias ColorSelected = ((color: Int) -> Unit)?

class ColorSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ColorSheet"
        const val NO_COLOR = -1
    }

    private var cornerRadius: Float = 0f
    private lateinit var colorAdapter: ColorAdapter

    override fun getTheme(): Int {
        return Theme.inferTheme(requireContext()).styleRes
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedInstanceState != null) dismiss()
        return inflater.inflate(R.layout.color_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog?
            val bottomSheet = dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss()
                    }
                }
            })
        }

        val gradientDrawable = GradientDrawable()

        if (cornerRadius == 0f) {
            cornerRadius = resources.getDimension(R.dimen.default_dialog_radius)
        }

        if (Theme.inferTheme(requireContext()) == Theme.LIGHT) {
            gradientDrawable.setColor(resolveColor(requireContext(), res = R.color.dialogPrimary))
        } else {
            gradientDrawable.setColor(resolveColor(requireContext(), res = R.color.dialogDarkPrimary))
        }

        gradientDrawable.cornerRadii =
            floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f)

        view.background = gradientDrawable
        if (this::colorAdapter.isInitialized) {
            colorSheetList.adapter = colorAdapter
        }

        colorSheetClose.setOnClickListener {
            dismiss()
        }
    }

    fun setCornerRadius(radius: Float): ColorSheet {
        this.cornerRadius = radius
        return this
    }

    fun colorPicker(
        colors: IntArray,
        @ColorInt selectedColor: Int = 0,
        noColorOption: Boolean = false,
        colorSelected: ColorSelected
    ): ColorSheet {
        colorAdapter = ColorAdapter(colors, selectedColor, noColorOption, colorSelected)
        return this
    }

    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, TAG)
    }

    inner class ColorAdapter(
        private var colors: IntArray,
        private val selectedColor: Int,
        private val noColorOption: Boolean,
        private val colorSelected: ColorSelected
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val color = inflater.inflate(R.layout.color_item, parent, false)
            return ColorItemViewHolder(color)
        }

        override fun getItemCount(): Int {
            return if (noColorOption) {
                colors.size + 1
            } else {
                colors.size
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ColorItemViewHolder) {
                holder.bindColor()
            }
        }

        inner class ColorItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            init {
                itemView.setOnClickListener(this)
            }

            fun bindColor() {
                if (noColorOption) {
                    if (adapterPosition != 0) {
                        val color = colors[adapterPosition - 1]
                        if (selectedColor == color) {
                            itemView.colorSelected.isVisible = true
                            itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(color)
                        } else {
                            itemView.colorSelected.isVisible = false
                            itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(color)
                        }
                    } else {
                        if (selectedColor == ColorSheet.NO_COLOR) {
                            itemView.colorSelected.isVisible = true
                            itemView.colorSelectedCircle.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_circle)
                            itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(
                                resolveColor(itemView.context, attr = R.attr.dialogPrimaryVariant)
                            )
                        } else {
                            itemView.colorSelected.isVisible = true
                            itemView.colorSelected.setImageResource(R.drawable.ic_no_color)
                            itemView.colorSelectedCircle.setImageResource(R.drawable.ic_circle)
                            itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(
                                resolveColor(itemView.context, attr = R.attr.dialogPrimaryVariant)
                            )
                        }
                    }
                } else {
                    val color = colors[adapterPosition]
                    if (selectedColor == color) {
                        itemView.colorSelected.isVisible = true
                        itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(color)
                    } else {
                        itemView.colorSelected.isVisible = false
                        itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(color)
                    }
                }
            }

            override fun onClick(v: View?) {
                if (noColorOption) {
                    if (adapterPosition == 0) {
                        colorSelected?.invoke(NO_COLOR)
                    } else {
                        colorSelected?.invoke(colors[adapterPosition - 1])
                    }
                } else {
                    colorSelected?.invoke(colors[adapterPosition])
                }
                dismiss()
            }

        }
    }

}