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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.sasikanth.colorsheet.utils.resolveColorAttr
import kotlinx.android.synthetic.main.color_item.view.colorSelected
import kotlinx.android.synthetic.main.color_item.view.colorSelectedCircle

internal class ColorAdapter(
    private val dialog: ColorSheet,
    private var colors: IntArray,
    private val selectedColor: Int,
    private val noColorOption: Boolean,
    private val listener: ColorPickerListener
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
            holder.bindView()
        }
    }

    inner class ColorItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindView() {
            if (noColorOption) {
                if (adapterPosition != 0) {
                    val color = colors[adapterPosition - 1]
                    bindColorView(color)
                } else {
                    bindNoColorView()
                }
            } else {
                val color = colors[adapterPosition]
                bindColorView(color)
            }
        }

        private fun bindColorView(@ColorInt color: Int) {
            if (selectedColor == color) {
                itemView.colorSelected.isVisible = true
                itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(color)
            } else {
                itemView.colorSelected.isVisible = false
                itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(color)
            }
        }

        private fun bindNoColorView() {
            if (selectedColor == ColorSheet.NO_COLOR) {
                itemView.colorSelected.isVisible = true
                itemView.colorSelectedCircle.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_circle)
                itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(
                    resolveColorAttr(itemView.context, attrRes = R.attr.dialogPrimaryVariant)
                )
            } else {
                itemView.colorSelected.isVisible = true
                itemView.colorSelected.setImageResource(R.drawable.ic_no_color)
                itemView.colorSelectedCircle.setImageResource(R.drawable.ic_circle)
                itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(
                    resolveColorAttr(itemView.context, attrRes = R.attr.dialogPrimaryVariant)
                )
            }
        }

        override fun onClick(v: View?) {
            if (noColorOption) {
                if (adapterPosition == 0) {
                    listener?.invoke(ColorSheet.NO_COLOR)
                } else {
                    listener?.invoke(colors[adapterPosition - 1])
                }
            } else {
                listener?.invoke(colors[adapterPosition])
            }
            dialog.dismiss()
        }
    }
}