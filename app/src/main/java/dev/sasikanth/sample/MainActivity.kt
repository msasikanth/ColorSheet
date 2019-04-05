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

package dev.sasikanth.sample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.activity_main.colorBackground
import kotlinx.android.synthetic.main.activity_main.colorSelectedText
import kotlinx.android.synthetic.main.activity_main.colorSheet

class MainActivity : AppCompatActivity() {

    var selectedColor: Int = ColorSheet.NO_COLOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectedColor = savedInstanceState?.getInt("colorSelected") ?: ColorSheet.NO_COLOR
        setColor(selectedColor)

        val colors = resources.getIntArray(R.array.noteColors)
        colorSheet.setOnClickListener {
            ColorSheet().colorPicker(
                colors,
                selectedColor = selectedColor,
                noColorOption = true,
                colorSelected = { color ->
                    selectedColor = color
                    setColor(selectedColor)
                })
                .show(supportFragmentManager)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("colorSelected", selectedColor)
    }

    private fun setColor(color: Int) {
        if (color != ColorSheet.NO_COLOR) {
            val hexColor = "#${Integer.toHexString(color).toUpperCase()}"

            colorBackground.setBackgroundColor(color)
            colorSelectedText.text = hexColor
            colorSelectedText.setTextColor(Color.BLACK)
        } else {
            val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)

            colorBackground.setBackgroundColor(primaryColor)
            colorSelectedText.text = getString(R.string.no_color)
            colorSelectedText.setTextColor(Color.WHITE)
        }
    }

}
