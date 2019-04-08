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

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import dev.sasikanth.colorsheet.ColorSheet
import dev.sasikanth.colorsheet.utils.ColorSheetUtils
import kotlinx.android.synthetic.main.activity_main.colorBackground
import kotlinx.android.synthetic.main.activity_main.colorSelectedText
import kotlinx.android.synthetic.main.activity_main.colorSheet
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val COLOR_SELECTED = "selectedColor"
        private const val NO_COLOR_OPTION = "noColorOption"
    }

    private var selectedColor: Int = ColorSheet.NO_COLOR
    private var noColorOption = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val colors = resources.getIntArray(R.array.colors)
        selectedColor = savedInstanceState?.getInt(COLOR_SELECTED) ?: colors.first()
        setColor(selectedColor)

        noColorOption = savedInstanceState?.getBoolean(NO_COLOR_OPTION) ?: false

        colorSheet.setOnClickListener {
            ColorSheet().cornerRadius(8)
                .colorPicker(
                    colors = colors,
                    noColorOption = noColorOption,
                    selectedColor = selectedColor,
                    listener = { color ->
                        selectedColor = color
                        setColor(selectedColor)
                    })
                .show(supportFragmentManager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                menu?.findItem(R.id.dark_mode)?.isChecked = false
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                menu?.findItem(R.id.dark_mode)?.isChecked = true
            }
        }
        menu?.findItem(R.id.no_color_option)?.isChecked = noColorOption
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.dark_mode -> {
                if (item.isChecked) {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                }
            }
            R.id.no_color_option -> {
                noColorOption = !noColorOption
                item.isChecked = noColorOption
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COLOR_SELECTED, selectedColor)
        outState.putBoolean(NO_COLOR_OPTION, noColorOption)
    }

    private fun setColor(@ColorInt color: Int) {
        if (color != ColorSheet.NO_COLOR) {
            colorBackground.setBackgroundColor(color)
            colorSelectedText.text = ColorSheetUtils.colorToHex(color)
        } else {
            val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)
            colorBackground.setBackgroundColor(primaryColor)
            colorSelectedText.text = getString(R.string.no_color)
        }
    }
}
