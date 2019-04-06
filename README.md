# ColorSheet

A color picker bottom sheet

<img width="300" alt="portfolio_view" src="https://github.com/msasikanth/ColorSheet/blob/master/art/screenshot.png?raw=true">

```
  TODO: Maven upload pending
```

## Usage

### Color sheet

Default color sheet, it will show grid of colors.<br>
You can pass selectedColor (ColorInt) to mark the color as selected in the sheet

```
ColorSheet().colorPicker(
    colors = colors,
    selectedColor = selectedColor,
    listener = { color ->
        // Handle Color
    })
    .show(supportFragmentManager)
````

### Color sheet with "no color" option

With this you will get a no color option at start, when user selects this it will return <br>
*ColorSheet.NO_COLOR*.

```
ColorSheet().colorPicker(
    colors = colors,
    selectedColor = selectedColor,
    noColorOption = true,
    listener = { color ->
        // Handle Color
    })
    .show(supportFragmentManager)
````

### License

```
Copyright 2019 Sasikanth Miriyampalli

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

