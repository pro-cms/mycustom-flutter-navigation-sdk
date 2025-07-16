/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.maps.flutter.navigation

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.StyleSpan

interface PolylineOptionsSink {
  fun setStrokeColor(color: Int)

  fun setGeodesic(geodesic: Boolean)

  fun setPoints(points: List<LatLng>)

  fun setVisible(visible: Boolean)

  fun setStrokeWidth(width: Float)

  fun setZIndex(zIndex: Float)

  fun setClickable(clickable: Boolean)

  fun setStrokeJointType(strokeJointType: Int)

  fun setStrokePattern(strokePattern: List<PatternItem>)

  fun setSpans(spans: List<StyleSpan>)
}
