# MultiToolApp

A modular Android application built with Jetpack Compose, designed as a scalable container for various utility tools.

## Architecture
* **UI Framework:** Jetpack Compose (Material Design 3).
* **Structure:** Single-Activity architecture. `MainActivity` acts solely as a navigation router (`Scaffold` with `TopAppBar`). Each tool is encapsulated in its own file (e.g., `DateCalculatorScreen.kt`) for strict isolation and maintainability.
* **Minimum SDK:** 26 (Android 8.0) – required for native `java.time` API support.

## Included Modules

### 1. Date Shift Calculator
![Date Calculator Main Screen](screenshots/date_calculator_main.png)

A utility to safely calculate past or future dates by adding or subtracting days, months, or years.
* **Core:** Driven by `java.time.LocalDate` and a native `DatePickerDialog`.
* **Validation:** Implements strict input validation (limits shifts to +/- 100,000 units) to prevent `DateTimeException` crashes and UI overflow.
* **Formatting:** Era-aware formatting (BCE/CE via `Locale`) to properly handle edge cases for large historical or future shifts.
* **UI/UX:** Custom state-driven color logic applied to disabled/focused states using `colorResource`.

### 2. Date Difference Calculator
![Date Difference Main Screen](screenshots/date_difference_main.png)

A utility to calculate the exact absolute difference between two specific dates and times, breaking down the result into days, hours, minutes, and seconds.
* **Core:** Utilizes `java.time.LocalDateTime` and `java.time.Duration` for highly accurate, millisecond-level time math.
* **UI/UX:** Implements parallel `Row` structures combining `DatePickerDialog` and `TimePickerDialog`. Features a custom "Now" trailing icon button that captures the exact current system time (including seconds, bypassing standard Android UI limitations).
* **Calculation:** Employs absolute duration parsing (`abs()`) and modulo arithmetic to properly separate total time into distinct, readable units without overlap.

### 3. Number Base Converter
![Number Converter Main Screen](screenshots/number_converter_main.png)

A real-time decimal number converter demonstrating reactive state management in Jetpack Compose without explicit action buttons.
* **Core:** Utilizes Kotlin's native `toString(radix)` function to convert standard `Int32` numbers into Binary (Base2), Octal (Base8), Hexadecimal (Base16), and Alphanumeric Base36.
* **Validation:** Employs strict Regex filtering (`"^(0|-|-[1-9][0-9]{0,9}|[1-9][0-9]{0,9})$"`) directly on the input field to block leading zeros and limit string length, preventing memory overflow before computation even occurs.
* **UI/UX:** Uses `RadioButton` components inside a `selectableGroup`. The `onClick` events are elevated to the `Row` level to maximize the touch target area, adhering to mobile UX best practices.

### 4. Map Coordinates Viewer
![Map Coordinates Main Screen](screenshots/map_coordinates_main.png)

A geographic visualization tool that renders an interactive map based on user-provided coordinates.
* **Core:** Integrates the `com.google.maps.android:maps-compose` library for declarative UI map rendering. Uses `CameraPositionState` to smoothly animate the camera to the target location with a district-level zoom (15f).
* **Validation:** Implements mathematical bounding box validation (Latitude: -90 to 90, Longitude: -180 to 180) alongside Regex filtering to prevent invalid API calls and map rendering errors. Smart casting is utilized to safely unwrap nullable coordinate values.
* **Security:** The Google Maps API Key is securely injected during the build process via `local.properties` and Gradle's `manifestPlaceholders`, ensuring credentials are never exposed in the public repository.
