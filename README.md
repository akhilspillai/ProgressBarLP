# Progressbar Material

This library provides a way to add a material themed circular progressbar into your android app.
The progressbar can be customized to swap colors for each cycle(as seen in the inbox application) by adding a color array.
Width of the progressbar stroke and the duration per cycle can also be customized.

![ProgressbarMaterial](https://raw.github.com/akhilspillai/ProgressBarLP/master/progressbar-material.gif)

### Usage 

* If you are using eclipse download and import the project as a library.
If you are using android studio use `compile 'com.trace:progressbar-material:1.1.1'` (please do not use lower versions)in your gradle.build file under `dependencies`.
* Include `com.trace.progressbar.ProgressBarMaterial` to the layout xml file wherever required.
* Please refer to the demo app for more information.

### Customization

* Use `progressStrokeWidth` attribute to control the stroke with of the progressbar.
* Use `progressSwapColors` to supply an array of colors to swap for each rotation cycle.
* Use `progressCycleDuration` to set the duration of each rotation cycle.

### Licence

> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this work except in compliance with the License.
> You may obtain a copy of the License in the LICENSE file, or at:
>
>  [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
