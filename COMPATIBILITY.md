# Compatibility with google_maps_flutter

This document explains how to resolve conflicts between `google_navigation_flutter` and `google_maps_flutter` packages.

## Issue

When using both `google_navigation_flutter` and `google_maps_flutter` in the same project, you may encounter duplicate class errors during Android builds:

```
Duplicate class com.google.android.gms.maps.* found in modules:
- navigation-6.2.2.aar -> jetified-navigation-6.2.2-runtime
- play-services-maps-18.2.0.aar -> jetified-play-services-maps-18.2.0-runtime
```

## Root Cause

The Google Navigation SDK (used by `google_navigation_flutter`) includes its own copy of Google Maps classes, which conflicts with the separate `play-services-maps` library used by `google_maps_flutter`.

## Solution

To resolve this conflict, exclude `play-services-maps` from all dependencies and rely on the Google Navigation SDK's built-in Maps implementation.

### Android Configuration

Add the following to your app's `android/app/build.gradle` (or `build.gradle.kts`):

#### For Groovy (build.gradle):
```gradle
configurations {
    all {
        exclude group: 'com.google.android.gms', module: 'play-services-maps'
    }
}

dependencies {
    // Add required Google Play Services (excluding play-services-maps)
    implementation 'com.google.android.gms:play-services-base:18.5.0'
    implementation 'com.google.android.gms:play-services-basement:18.4.0'
    implementation 'com.google.android.gms:play-services-tasks:18.2.0'
    // ... your other dependencies
}
```

#### For Kotlin DSL (build.gradle.kts):
```kotlin
configurations {
    all {
        exclude(group = "com.google.android.gms", module = "play-services-maps")
    }
}

dependencies {
    // Add required Google Play Services (excluding play-services-maps)
    implementation("com.google.android.gms:play-services-base:18.5.0")
    implementation("com.google.android.gms:play-services-basement:18.4.0")
    implementation("com.google.android.gms:play-services-tasks:18.2.0")
    // ... your other dependencies
}
```

## Verification

After applying these changes:

1. Clean your project: `flutter clean`
2. Get dependencies: `flutter pub get`
3. Build your app: `flutter build apk --debug`

The build should complete successfully without duplicate class errors.

## iOS Configuration

For iOS, you need to resolve version conflicts between GoogleMaps dependencies. The issue occurs because:
- `google_navigation_flutter` requires `GoogleNavigation 10.0.0` → which depends on `GoogleMaps 10.0.0`
- `google_maps_flutter_ios` requires `GoogleMaps < 10.0`

### Solution 1: Force GoogleMaps Version (Recommended)

Add the following to your `ios/Podfile`:

```ruby
# Ensure minimum iOS version
platform :ios, '16.0'

# Force GoogleMaps version before target block
pod 'GoogleMaps', '10.0.0'

target 'Runner' do
  use_frameworks!
  use_modular_headers!

  flutter_install_all_ios_pods File.dirname(File.realpath(__FILE__))
end

post_install do |installer|
  installer.pods_project.targets.each do |target|
    flutter_additional_ios_build_settings(target)
  end
end
```

### Solution 2: Dependency Override

If Solution 1 doesn't work, add this to your `pubspec.yaml`:

```yaml
dependency_overrides:
  google_maps_flutter_ios:
    git:
      url: https://github.com/flutter/packages.git
      path: packages/google_maps_flutter/google_maps_flutter_ios
      ref: main
```

Then run:
```bash
flutter pub get
cd ios
pod install --repo-update
```

### Verification Steps

1. Delete `ios/Podfile.lock` and `ios/Pods/` directory
2. Run `flutter clean`
3. Run `flutter pub get`
4. Run `cd ios && pod install`
5. Build your iOS app: `flutter build ios`

## Notes

- This solution uses the Google Navigation SDK's built-in Maps implementation instead of the separate `play-services-maps` library
- The Navigation SDK includes all necessary Maps functionality, so no features are lost
- This configuration is already applied in the plugin's example app and can be used as a reference
- For iOS, both packages will use GoogleMaps 10.0.0, ensuring compatibility